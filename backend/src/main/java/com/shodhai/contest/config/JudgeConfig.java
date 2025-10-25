package com.shodhai.contest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "judge")
@Data
public class JudgeConfig {
    private Docker docker = new Docker();
    private String tempDir = "/tmp/judge";
    private Integer workerThreads = 4;
    private Integer queueCapacity = 100;
    
    @Data
    public static class Docker {
        private String image = "judge-env:latest";
        private String memoryLimit = "256m";
        private Double cpuLimit = 1.0;
        private Integer timeoutSeconds = 5;
        private String networkMode = "none";
    }
}

