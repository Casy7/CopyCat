package com.copycat.app.service;

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

@Service
public class FileService {

    // Папка, де будуть лежати файли
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileService() {
        try {
            // Створюємо папку при старті сервера, якщо її ще немає
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Не вдалося створити директорію для файлів.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            // Генеруємо унікальне ім'я: 123e4567-e89b-12d3..._myphoto.jpg
            String originalName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalName;

            // Копіюємо файл на диск
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName; // Повертаємо нове ім'я файлу
        } catch (IOException ex) {
            throw new RuntimeException("Не вдалося зберегти файл", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Файл не знайдено: " + fileName);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Файл не знайдено: " + fileName, ex);
        }
    }
}