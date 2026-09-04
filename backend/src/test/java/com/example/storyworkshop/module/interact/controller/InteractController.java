package com.example.storyworkshop.module.interact.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.module.interact.dto.CommentRequest;
import com.example.storyworkshop.module.interact.dto.ReportRequest;
import com.example.storyworkshop.module.interact.entity.Comment;
import com.example.storyworkshop.module.interact.service.InteractService;
import com.example.storyworkshop.module.interact.vo.CommentVO;
import com.example.storyworkshop.module.interact.vo.InteractStatusVO;
import com.example.storyworkshop.module.user.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/stories/{storyId}")
public class InteractController {
    private final InteractService interactService;
    private final AuthService authService;

    public InteractController(InteractService interactService, AuthService authService) {
        this.interactService = interactService;
        this.authService = authService;
    }

    @GetMapping("/interact/status")
    public Result<InteractStatusVO> status(@PathVariable Long storyId, HttpSession session) {
        Long userId = safeUserId(session);
        return Result.success(interactService.status(userId, storyId));
    }

    @PostMapping("/like")
    public Result<Map<String, Object>> like(@PathVariable Long storyId, HttpSession session) {
        boolean liked = interactService.toggleLike(authService.requireLoginUserId(session), storyId);
        return Result.success(liked ? "已点赞" : "已取消点赞", Map.of("liked", liked));
    }

    @PostMapping("/favorite")
    public Result<Map<String, Object>> favorite(@PathVariable Long storyId, HttpSession session) {
        boolean fav = interactService.toggleFavorite(authService.requireLoginUserId(session), storyId);
        return Result.success(fav ? "已收藏" : "已取消收藏", Map.of("favorited", fav));
    }

    @GetMapping("/comments")
    public Result<List<CommentVO>> comments(@PathVariable Long storyId,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(interactService.comments(storyId, page, pageSize));
    }

    @PostMapping("/comments")
    public Result<Comment> addComment(@PathVariable Long storyId, @Valid @RequestBody CommentRequest request, HttpSession session) {
        Comment comment = interactService.addComment(authService.requireLoginUserId(session), storyId, request);
        return Result.success("评论成功", comment);
    }

    @PostMapping("/report")
    public Result<Void> report(@PathVariable Long storyId, @Valid @RequestBody ReportRequest request, HttpSession session) {
        interactService.report(authService.requireLoginUserId(session), storyId, request);
        return Result.success("举报已提交", null);
    }

    private Long safeUserId(HttpSession session) {
        try {
            return authService.requireLoginUserId(session);
        } catch (Exception e) {
            return null;
        }
    }
}
