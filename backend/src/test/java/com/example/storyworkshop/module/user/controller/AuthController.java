package com.example.storyworkshop.module.user.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.module.user.dto.LoginRequest;
import com.example.storyworkshop.module.user.dto.RegisterRequest;
import com.example.storyworkshop.module.user.service.AuthService;
import com.example.storyworkshop.module.user.vo.UserVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterRequest request) {
        UserVO userVO = authService.register(request);
        return Result.success("注册成功", userVO);
    }

    @PostMapping("/login")
    public Result<UserVO> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        UserVO userVO = authService.login(request, session);
        return Result.success("登录成功", userVO);
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        authService.logout(session);
        return Result.success("注销成功", null);
    }

    @GetMapping("/current")
    public Result<UserVO> current(HttpSession session) {
        UserVO userVO = authService.getCurrentUser(session);
        return Result.success(userVO);
    }
}