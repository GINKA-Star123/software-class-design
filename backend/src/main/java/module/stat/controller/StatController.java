package com.example.storyworkshop.module.stat.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.storyworkshop.common.constant.RoleConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.stat.service.StatService;
import com.example.storyworkshop.module.stat.vo.DashboardVO;
import com.example.storyworkshop.module.stat.vo.HomeRecommendVO;
import com.example.storyworkshop.module.stat.vo.RankStoryVO;
import com.example.storyworkshop.module.user.service.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping
public class StatController {
    private final StatService statService;
    private final AuthService authService;

    public StatController(StatService statService, AuthService authService) {
        this.statService = statService;
        this.authService = authService;
    }

    @GetMapping("/home")
    public Result<HomeRecommendVO> home() {
        return Result.success(statService.home());
    }

    @GetMapping("/stats/rank")
    public Result<List<RankStoryVO>> rank(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(statService.rank(limit));
    }

    @GetMapping("/stats/dashboard")
    public Result<DashboardVO> dashboard(HttpSession session) {
        if (!RoleConstants.canManage(authService.getSessionRoles(session))) throw new BusinessException(ResultCode.FORBIDDEN);
        return Result.success(statService.dashboard());
    }
}
