package com.shodhai.contest.judge;

import com.shodhai.contest.config.JudgeConfig;
import com.shodhai.contest.exception.JudgeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class DockerExecutor {
    
    private final JudgeConfig judgeConfig;
    
    public DockerExecutor(JudgeConfig judgeConfig) {
        this.judgeConfig = judgeConfig;
        ensureTempDirectoryExists();
    }
    
    private void ensureTempDirectoryExists() {
        try {
            Path tempDir = Paths.get(judgeConfig.getTempDir());
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
            }
        } catch (IOException e) {
            throw new JudgeException("Failed to create temp directory", e);
        }
    }
    
    public ExecutionResult execute(ExecutionRequest request) {
        Path workspaceDir = null;
        try {
            // Create workspace directory
            workspaceDir = createWorkspace(request.getSubmissionId());
            
            // Write code file
            writeCodeFile(workspaceDir, request.getFileName(), request.getCode());
            
            // Write input file
            writeInputFile(workspaceDir, request.getInput());
            
            // Compile if needed
            if (request.isNeedsCompilation()) {
                ExecutionResult compileResult = compile(workspaceDir, request.getCompileCommand());
                if (!compileResult.isSuccess()) {
                    return compileResult;
                }
            }
            
            // Execute
            return run(workspaceDir, request.getRunCommand(), request.getTimeLimitMs());
            
        } catch (Exception e) {
            log.error("Execution error for submission {}", request.getSubmissionId(), e);
            return ExecutionResult.builder()
                    .success(false)
                    .verdict("SYSTEM_ERROR")
                    .errorMessage(e.getMessage())
                    .build();
        } finally {
            // Cleanup
            if (workspaceDir != null) {
                cleanup(workspaceDir);
            }
        }
    }
    
    private Path createWorkspace(Long submissionId) throws IOException {
        Path workspaceDir = Paths.get(judgeConfig.getTempDir(), "submission_" + submissionId);
        if (Files.exists(workspaceDir)) {
            deleteDirectory(workspaceDir.toFile());
        }
        Files.createDirectories(workspaceDir);
        return workspaceDir;
    }
    
    private void writeCodeFile(Path workspaceDir, String fileName, String code) throws IOException {
        Path codeFile = workspaceDir.resolve(fileName);
        Files.writeString(codeFile, code);
    }
    
    private void writeInputFile(Path workspaceDir, String input) throws IOException {
        Path inputFile = workspaceDir.resolve("input.txt");
        Files.writeString(inputFile, input != null ? input : "");
    }
    
    private ExecutionResult compile(Path workspaceDir, String compileCommand) {
        try {
            List<String> command = buildDockerCommand(workspaceDir, compileCommand, 30000);
            
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            String output = readOutput(process.getInputStream());
            
            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return ExecutionResult.builder()
                        .success(false)
                        .verdict("COMPILATION_ERROR")
                        .errorMessage("Compilation timeout")
                        .build();
            }
            
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                return ExecutionResult.builder()
                        .success(false)
                        .verdict("COMPILATION_ERROR")
                        .errorMessage(output)
                        .build();
            }
            
            return ExecutionResult.builder()
                    .success(true)
                    .build();
            
        } catch (Exception e) {
            return ExecutionResult.builder()
                    .success(false)
                    .verdict("COMPILATION_ERROR")
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
    
    private ExecutionResult run(Path workspaceDir, String runCommand, int timeLimitMs) {
        long startTime = System.currentTimeMillis();
        
        try {
            String fullCommand = runCommand + " < input.txt";
            List<String> command = buildDockerCommand(workspaceDir, fullCommand, timeLimitMs);
            
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(false);
            
            Process process = pb.start();
            
            // Read output and error in separate threads
            String output = readOutput(process.getInputStream());
            String error = readOutput(process.getErrorStream());
            
            boolean finished = process.waitFor(timeLimitMs + 1000, TimeUnit.MILLISECONDS);
            long executionTime = System.currentTimeMillis() - startTime;
            
            if (!finished) {
                process.destroyForcibly();
                return ExecutionResult.builder()
                        .success(false)
                        .verdict("TIME_LIMIT_EXCEEDED")
                        .executionTimeMs((int) executionTime)
                        .build();
            }
            
            int exitCode = process.exitValue();
            
            if (exitCode == 137) {
                return ExecutionResult.builder()
                        .success(false)
                        .verdict("MEMORY_LIMIT_EXCEEDED")
                        .executionTimeMs((int) executionTime)
                        .build();
            }
            
            if (exitCode != 0) {
                return ExecutionResult.builder()
                        .success(false)
                        .verdict("RUNTIME_ERROR")
                        .errorMessage(error)
                        .executionTimeMs((int) executionTime)
                        .build();
            }
            
            return ExecutionResult.builder()
                    .success(true)
                    .output(output)
                    .executionTimeMs((int) executionTime)
                    .build();
            
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            return ExecutionResult.builder()
                    .success(false)
                    .verdict("RUNTIME_ERROR")
                    .errorMessage(e.getMessage())
                    .executionTimeMs((int) executionTime)
                    .build();
        }
    }
    
    private List<String> buildDockerCommand(Path workspaceDir, String command, int timeLimitMs) {
        List<String> dockerCommand = new ArrayList<>();
        dockerCommand.add("docker");
        dockerCommand.add("run");
        dockerCommand.add("--rm");
        dockerCommand.add("--network=" + judgeConfig.getDocker().getNetworkMode());
        dockerCommand.add("--memory=" + judgeConfig.getDocker().getMemoryLimit());
        dockerCommand.add("--cpus=" + judgeConfig.getDocker().getCpuLimit());
        dockerCommand.add("-v");
        dockerCommand.add(workspaceDir.toAbsolutePath() + ":/workspace");
        dockerCommand.add("-w");
        dockerCommand.add("/workspace");
        dockerCommand.add(judgeConfig.getDocker().getImage());
        dockerCommand.add("sh");
        dockerCommand.add("-c");
        dockerCommand.add(command);
        
        return dockerCommand;
    }
    
    private String readOutput(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString().trim();
        }
    }
    
    private void cleanup(Path workspaceDir) {
        try {
            deleteDirectory(workspaceDir.toFile());
        } catch (Exception e) {
            log.error("Failed to cleanup workspace: {}", workspaceDir, e);
        }
    }
    
    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    public static class ExecutionRequest {
        private Long submissionId;
        private String code;
        private String fileName;
        private String input;
        private boolean needsCompilation;
        private String compileCommand;
        private String runCommand;
        private int timeLimitMs;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    public static class ExecutionResult {
        private boolean success;
        private String output;
        private String verdict;
        private String errorMessage;
        private Integer executionTimeMs;
    }
}

