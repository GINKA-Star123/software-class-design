package com.example.storyworkshop.infra.file;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.module.user.service.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final AuthService authService;

    public FileUploadController(FileStorageService fileStorageService, AuthService authService) {
        this.fileStorageService = fileStorageService;
        this.authService = authService;
    }

    @PostMapping
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file,
                                              @RequestParam(value = "type", defaultValue = "covers") String type,
                                              HttpSession session) {
        authService.requireLoginUserId(session);
        String url = fileStorageService.store(file, type);
        return Result.success("上传成功", Map.of("url", url));
    }
}
