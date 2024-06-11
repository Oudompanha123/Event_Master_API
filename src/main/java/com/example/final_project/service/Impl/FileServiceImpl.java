package com.example.final_project.service.Impl;

import com.example.final_project.exception.BadRequestException;
import com.example.final_project.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    //store image in images folder
    private final Path path = Paths.get("src/main/resources/image");
    @Override
    public String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        String type = file.getContentType();
        assert fileName != null;
        assert type != null;

        switch (type) {
            case "image/png":
                fileName = UUID.randomUUID() + ".png";
                break;
            case "image/jpeg":
                fileName = UUID.randomUUID() + ".jpeg";
                break;
            case "image/jpg":
                fileName = UUID.randomUUID() + ".jpg";
                break;
            case "image/webp":
                fileName = UUID.randomUUID() + ".webp";
                break;
            case "application/pdf":
                fileName = UUID.randomUUID() + ".pdf";
                break;

            default:
                throw new BadRequestException("File format not supported");

        }

        if (!Files.exists(path))
            Files.createDirectories(path);
        Files.copy(file.getInputStream(), path.resolve(fileName));
        return fileName;
    }


    @Override
    public Resource getFileByFileName(String fileName) throws IOException {
        //get file path
        Path path = Paths.get("src/main/resources/image/" + fileName);
        //read file as byte
        return new ByteArrayResource(Files.readAllBytes(path));
    }
}


