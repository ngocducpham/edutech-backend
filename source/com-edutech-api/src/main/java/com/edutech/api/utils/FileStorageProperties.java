package com.edutech.api.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private final String rootDir = ConfigurationService.getInstance().getString("file.upload-dir");
    private String uploadDir = "";
    public String getUploadDir() {
        return rootDir + uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
