package com.example.storyworkshop.module.audit.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.storyworkshop.common.constant.RoleConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.audit.dto.OfflineRequest;
import com.example.storyworkshop.module.audit.dto.RejectRequest;
import com.example.storyworkshop.module.audit.service.AuditService;
import com.example.storyworkshop.module.audit.vo.AuditStoryVO;
import com.example.storyworkshop.module.audit.vo.ReportHandleVO;
import com.example.storyworkshop.module.user.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/audit")
public class AuditController {
    private final AuditService auditService;
    private final AuthService authService;

    public AuditController(AuditService auditService, AuthService authService) {
        this.auditService = auditService;
        this.authService = authService;
    }

    private long operator(HttpSession session) {
        if (!RoleConstants.canAudit(authService.getSessionRoles(session))) throw new BusinessException(ResultCode.FORBIDDEN);
        return authService.requireLoginUserId(session);
    }

    @GetMapping("/stories")
    public Result<List<AuditStoryVO>> pendingStories(HttpSession session) {
        operator(session);
        return Result.success(auditService.pendingStories());
    }

    @PostMapping("/stories/{storyId}/approve")
    public Result<Void> approve(@PathVariable Long storyId, HttpSession session) {
        auditService.approve(operator(session), storyId);
        return Result.success("已通过并发布", null);
    }

    @PostMapping("/stories/{storyId}/reject")
    public Result<Void> reject(@PathVariable Long storyId, @Valid @RequestBody RejectRequest request, HttpSession session) {
        auditService.reject(operator(session), storyId, request.getReason());
        return Result.success("已驳回", null);
    }

    @PostMapping("/stories/{storyId}/offline")
    public Result<Void> offline(@PathVariable Long storyId, @RequestBody(required = false) OfflineRequest request, HttpSession session) {
        auditService.offline(operator(session), storyId, request == null ? "" : request.getReason());
        return Result.success("已下架", null);
    }

    @GetMapping("/reports")
    public Result<List<ReportHandleVO>> reports(HttpSession session) {
        operator(session);
        return Result.success(auditService.pendingReports());
    }

    @PostMapping("/reports/{reportId}/handle")
    public Result<Void> handle(@PathVariable Long reportId, @RequestParam(defaultValue = "resolve") String action, HttpSession session) {
        auditService.handleReport(operator(session), reportId, action);
        return Result.success("处理完成", null);
    }
}
