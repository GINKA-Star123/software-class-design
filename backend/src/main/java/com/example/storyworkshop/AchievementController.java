package com.example.storyworkshop.module.achievement.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.module.achievement.service.AchievementService;
import com.example.storyworkshop.module.achievement.vo.AchievementVO;
import com.example.storyworkshop.module.user.service.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/achievements")
public class AchievementController {
    private final AchievementService achievementService;
    private final AuthService authService;

    public AchievementController(AchievementService achievementService, AuthService authService) {
        this.achievementService = achievementService;
        this.authService = authService;
    }

    @GetMapping("/mine")
    public Result<List<AchievementVO>> mine(HttpSession session) {
        return Result.success(achievementService.mine(authService.requireLoginUserId(session)));
    }
}
