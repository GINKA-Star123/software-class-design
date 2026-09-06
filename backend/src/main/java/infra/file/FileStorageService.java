package com.example.storyworkshop.infra.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.storyworkshop.common.constant.AppConstants;
import com.example.storyworkshop.common.exception.BusinessException;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private final Path uploadRoot;

    public FileStorageService(@Value("${app.upload.root-path:../uploads}") String root) {
        this.uploadRoot = Paths.get(root).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file, String category) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        if (!"covers".equals(category) && !"avatars".equals(category)) {
            category = AppConstants.COVER_DIR;
        }
        if (file.getSize() > AppConstants.MAX_IMAGE_SIZE) {
            throw new BusinessException("图片不能超过5MB");
        }
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        int dot = original.lastIndexOf('.');
        String ext = dot >= 0 ? original.substring(dot + 1).toLowerCase(Locale.ROOT) : "";
        if (!ALLOWED_EXT.contains(ext)) {
            throw new BusinessException("仅支持 jpg/jpeg/png/gif/webp 图片");
        }
        try {
            Path dir = uploadRoot.resolve(category);
            Files.createDirectories(dir);
            String name = UUID.randomUUID().toString().replace("-", "") + "." + ext;
            file.transferTo(dir.resolve(name));
            return "/api" + AppConstants.UPLOAD_URL_PREFIX + "/" + category + "/" + name;
        } catch (IOException e) {
            throw new BusinessException("文件保存失败，请稍后重试");
        }
    }
}
