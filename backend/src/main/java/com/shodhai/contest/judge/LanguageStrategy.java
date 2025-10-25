package com.shodhai.contest.judge;

import com.shodhai.contest.model.Submission.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LanguageStrategy {
    
    private final Map<Language, LanguageConfig> configs = new HashMap<>();
    
    public LanguageStrategy() {
        initializeConfigs();
    }
    
    private void initializeConfigs() {
        // Java Configuration
        configs.put(Language.JAVA, LanguageConfig.builder()
                .fileExtension(".java")
                .fileName("Main.java")
                .compileCommand("javac Main.java")
                .runCommand("java Main")
                .needsCompilation(true)
                .build());
        
        // Python Configuration
        configs.put(Language.PYTHON, LanguageConfig.builder()
                .fileExtension(".py")
                .fileName("main.py")
                .compileCommand(null)
                .runCommand("python3 main.py")
                .needsCompilation(false)
                .build());
        
        // C++ Configuration
        configs.put(Language.CPP, LanguageConfig.builder()
                .fileExtension(".cpp")
                .fileName("main.cpp")
                .compileCommand("g++ -o main main.cpp")
                .runCommand("./main")
                .needsCompilation(true)
                .build());
        
        // JavaScript Configuration
        configs.put(Language.JAVASCRIPT, LanguageConfig.builder()
                .fileExtension(".js")
                .fileName("main.js")
                .compileCommand(null)
                .runCommand("node main.js")
                .needsCompilation(false)
                .build());
    }
    
    public LanguageConfig getConfig(Language language) {
        LanguageConfig config = configs.get(language);
        if (config == null) {
            throw new IllegalArgumentException("Unsupported language: " + language);
        }
        return config;
    }
    
    @Data
    @AllArgsConstructor
    @lombok.Builder
    public static class LanguageConfig {
        private String fileExtension;
        private String fileName;
        private String compileCommand;
        private String runCommand;
        private boolean needsCompilation;
    }
}

