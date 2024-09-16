package com.krnal.products.scoutinghub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@Service
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    private final String uploadDir;

    public FileStorageService(@Value("${file.upload.dir}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String storeFile(MultipartFile file) throws IOException {
        String c = "FileStorageService";
        String m = "storeFile";
        logger.info(createLogMessage(c, m, "Start"));

        createDirIfNotExist(uploadDir);

        // Generate a unique file name
        String fileName = UUID.randomUUID().toString();

        // Copy file to the target location
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        logger.info(createLogMessage(c, m, "Success"));
        // Return the file path
        return fileName;
    }

    private void createDirIfNotExist(String uploadDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("File not found " + fileName, e);
        }
    }
}
