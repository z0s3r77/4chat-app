package com.fourchat.infrastructure.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        // Guardar la imagen en el servidor
        String fileName = file.getOriginalFilename();
        File storeFile = new File(fileStorageLocation + "/" + fileName);
        file.transferTo(storeFile);

        // Construir la URL de la imagen
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/")
                .path(fileName)
                .toUriString();

        return ResponseEntity.ok().body(fileDownloadUri);
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) throws MalformedURLException {
        // Cargar la imagen desde el servidor
        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .body(resource);
    }
}
