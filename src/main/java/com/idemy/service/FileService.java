package com.idemy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    // Videoların yadda saxlanılacağı qovluq yolu
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) {
        try {
            // Qovluq yoxdursa yarat
            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            // Faylın adını unikal et (UUID ilə)
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            // Faylı kopyala
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName; // Bazada saxlamalı olduğumuz adı qaytarır
        } catch (IOException e) {
            throw new RuntimeException("Fayl yüklənərkən xəta baş verdi: " + e.getMessage());
        }
    }
}