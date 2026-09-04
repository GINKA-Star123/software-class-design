package com.example.storyworkshop.module.user.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.module.user.dto.UserProfileRequest;
import com.example.storyworkshop.module.user.service.AuthService;
import com.example.storyworkshop.module.user.service.UserService;
import com.example.storyworkshop.module.user.vo.UserVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/me")
    public Result<UserVO> me(HttpSession session) {
        UserVO userVO = authService.getCurrentUser(session);
        return Result.success(userVO);
    }

    @PutMapping("/me")
    public Result<UserVO> updateMe(
            @Valid @RequestBody UserProfileRequest request,
            HttpSession session
    ) {
        Long userId = authService.requireLoginUserId(session);
        UserVO userVO = userService.updateProfile(userId, request);
        authService.storeLoginUser(session, userVO);
        return Result.success("个人资料修改成功", userVO);
    }

    @GetMapping("/me/roles")
    public Result<List<String>> myRoles(HttpSession session) {
        Long userId = authService.requireLoginUserId(session);
        List<String> roles = userService.getRoleNames(userId);
        return Result.success(roles);
    }

    @PostMapping("/me/apply-author")
    public Result<UserVO> applyAuthor(HttpSession session) {
        Long userId = authService.requireLoginUserId(session);
        UserVO userVO = userService.applyAuthor(userId);
        authService.storeLoginUser(session, userVO);
        return Result.success("已获得作者权限", userVO);
    }

    @GetMapping("/{userId}")
    public Result<UserVO> getUser(@PathVariable @Positive(message = "用户 ID 必须为正数") Long userId) {
        UserVO userVO = userService.getUserById(userId);
        return Result.success(userVO);
    }
}