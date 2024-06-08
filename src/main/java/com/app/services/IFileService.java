package com.app.services;

import com.app.controllers.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface IFileService {
    FileDTO uploadFile(Long taskId, Long userId, MultipartFile file);
    Path getFilePath(Long fileId);
}
