package com.example.storyworkshop.module.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.storyworkshop.common.constant.RoleConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.admin.dto.AdminUserRequest;
import com.example.storyworkshop.module.admin.dto.RoleUpdateRequest;
import com.example.storyworkshop.module.admin.service.AdminService;
import com.example.storyworkshop.module.admin.vo.AdminContentVO;
import com.example.storyworkshop.module.admin.vo.AdminUserVO;
import com.example.storyworkshop.module.user.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final AuthService authService;

    public AdminController(AdminService adminService, AuthService authService) {
        this.adminService = adminService;
        this.authService = authService;
    }

    private long requireAdmin(HttpSession session) {
        if (!RoleConstants.canManage(authService.getSessionRoles(session))) throw new BusinessException(ResultCode.FORBIDDEN);
        return authService.requireLoginUserId(session);
    }

    @GetMapping("/users")
    public Result<Map<String, Object>> users(AdminUserRequest request, HttpSession session) {
        requireAdmin(session);
        return Result.success(Map.of("list", adminService.users(request), "total", adminService.countUsers(request)));
    }

    @PutMapping("/users/{userId}/status")
    public Result<Void> updateStatus(@PathVariable Long userId, @RequestParam(defaultValue = "1") Integer status, HttpSession session) {
        requireAdmin(session);
        adminService.updateStatus(userId, status);
        return Result.success("已更新", null);
    }

    @PutMapping("/users/{userId}/roles")
    public Result<Void> updateRoles(@PathVariable Long userId, @Valid @RequestBody RoleUpdateRequest request, HttpSession session) {
        requireAdmin(session);
        adminService.updateRoles(userId, request.getRoleIds());
        return Result.success("角色已更新", null);
    }

    @GetMapping("/stories")
    public Result<List<AdminContentVO>> contents(@RequestParam(required = false) Integer status, HttpSession session) {
        requireAdmin(session);
        return Result.success(adminService.contents(status));
    }
}
