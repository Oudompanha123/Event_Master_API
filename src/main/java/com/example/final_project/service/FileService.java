package com.example.final_project.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {
    String saveFile(MultipartFile file) throws IOException;

    Resource getFileByFileName(String fileName) throws IOException;
}

