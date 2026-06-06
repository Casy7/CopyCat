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

    // Upload file to the server
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED) // Змінюємо на 201
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.storeFile(file);

        // Generate download URL
        String fileDownloadUri = "/api/v1/files/download/" + fileName;

        // Return the file link and its original name to the client
        return Map.of(
                "fileUrl", fileDownloadUri,
                "originalName", file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
    }

    // File download
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileService.loadFileAsResource(fileName);

        // Tell the browser that this is a file that needs to be downloaded (or opened)
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}