package com.edutech.api.service;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.exception.FileNotFoundException;
import com.edutech.api.exception.FileStorageException;
import com.edutech.api.utils.FileStorageProperties;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@NoArgsConstructor
public class FileStorageService {
    private Path fileStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            fileName = RandomStringUtils.random(20, true, true) + "_" + fileName;

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    public String studentStoreFile(MultipartFile file, Long studentId) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileName = studentId.toString()+"_"+fileName;
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(EduTechConstant.ROOT_DIRECTORY + fileName).toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found");
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found", ex);
        }
    }

    public void deleteFile(String fileName){
        try {
            Path fileToDeletePath = Paths.get(EduTechConstant.ROOT_DIRECTORY + fileName).toAbsolutePath().normalize();
            Resource resource = new UrlResource(fileToDeletePath.toUri());
            if(resource.exists()) {
                Files.delete(fileToDeletePath);
            } else {
                throw new FileNotFoundException("File not found");
            }
        } catch (IOException e) {
            throw new FileNotFoundException("File not found", e);
        }
    }

}
