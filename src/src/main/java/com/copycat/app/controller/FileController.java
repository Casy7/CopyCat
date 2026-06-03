package com.copycat.app.controller;

import com.copycat.app.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // Завантаження файлу на сервер
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.storeFile(file);
        
        // Генеруємо URL для завантаження
        String fileDownloadUri = "/api/v1/files/download/" + fileName;
        
        // Повертаємо клієнту лінк на файл та його оригінальне ім'я
        return Map.of(
            "fileUrl", fileDownloadUri,
            "originalName", file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        );
    }

    // Скачування файлу
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileService.loadFileAsResource(fileName);

        // Кажемо браузеру, що це файл, який треба скачати (або відкрити)
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}