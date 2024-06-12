package com.example.final_project.controller;

import com.example.final_project.model.dto.response.APIResponse;
import com.example.final_project.model.dto.response.FileResponse;
import com.example.final_project.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/file")
@AllArgsConstructor

public class FileController {
    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String fileName = fileService.saveFile(file);

        String fileUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/file/getFile__Query__fileName=" + fileName)
                .toUriString().replace("__Query__", "?");

        APIResponse<FileResponse> response = new APIResponse<>(
                "Uploaded file successfully",
                new FileResponse(fileName, fileUrl, file.getContentType(), file.getSize()),
                HttpStatus.OK,
                LocalDateTime.now()

        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getFile")
    public ResponseEntity<?> getFile(@RequestParam String fileName) throws IOException {
        Resource resource = fileService.getFileByFileName(fileName);
        MediaType mediaType;
        if (fileName.endsWith(".pdf")){mediaType = MediaType.APPLICATION_PDF;}
        else if(fileName.endsWith(".jpg") ||
                fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") ||
                fileName.endsWith(".gif") ||
                fileName.endsWith(".webp")
        ){
            mediaType = MediaType.IMAGE_PNG;}
        else {mediaType = MediaType.APPLICATION_OCTET_STREAM;}
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(mediaType).body(resource);
    }
}

