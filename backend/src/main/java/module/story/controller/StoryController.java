package com.example.storyworkshop.module.story.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storyworkshop.common.constant.RoleConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.story.dto.StoryCreateRequest;
import com.example.storyworkshop.module.story.dto.StoryQueryRequest;
import com.example.storyworkshop.module.story.dto.StoryUpdateRequest;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.service.StoryService;
import com.example.storyworkshop.module.story.vo.StoryCardVO;
import com.example.storyworkshop.module.story.vo.StoryDetailVO;
import com.example.storyworkshop.module.story.vo.StoryValidationVO;
import com.example.storyworkshop.module.user.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/stories")
@Validated
public class StoryController {
    private final StoryService storyService;
    private final AuthService authService;

    public StoryController(StoryService storyService, AuthService authService) {
        this.storyService = storyService;
        this.authService = authService;
    }

    @GetMapping
    public Result<List<StoryCardVO>> list(StoryQueryRequest query) {
        return Result.success("查询成功", storyService.page(query));
    }

    @GetMapping("/{storyId}")
    public Result<StoryDetailVO> detail(@PathVariable Long storyId, HttpSession session) {
        List<String> roles = authService.getSessionRoles(session);
        Long viewer = safeUserId(session);
        return Result.success(storyService.detail(storyId, roles, viewer));
    }

    @PostMapping
    public Result<Long> create(@Valid @RequestBody StoryCreateRequest request, HttpSession session) {
        List<String> roles = authService.getSessionRoles(session);
        if (!RoleConstants.canCreateStory(roles)) throw new BusinessException(ResultCode.FORBIDDEN);
        Story story = storyService.create(authService.requireLoginUserId(session), request);
        return Result.success("创建成功", story.getStoryId());
    }

    @GetMapping("/mine")
    public Result<List<StoryCardVO>> mine(HttpSession session) {
        List<String> roles = authService.getSessionRoles(session);
        if (!RoleConstants.canCreateStory(roles)) throw new BusinessException(ResultCode.FORBIDDEN);
        return Result.success(storyService.mine(authService.requireLoginUserId(session)));
    }

    @PutMapping("/{storyId}")
    public Result<Void> update(@PathVariable Long storyId, @Valid @RequestBody StoryUpdateRequest request, HttpSession session) {
        storyService.updateBasic(authService.requireLoginUserId(session), storyId, request);
        return Result.success("保存成功", null);
    }

    @DeleteMapping("/{storyId}")
    public Result<Void> delete(@PathVariable Long storyId, HttpSession session) {
        storyService.deleteStory(authService.requireLoginUserId(session), storyId);
        return Result.success("删除成功", null);
    }

    @PostMapping("/{storyId}/submit")
    public Result<Void> submit(@PathVariable Long storyId, HttpSession session) {
        storyService.submitAudit(authService.requireLoginUserId(session), storyId);
        return Result.success("已提交审核", null);
    }

    @PostMapping("/{storyId}/delete-request")
    public Result<Void> deleteRequest(@PathVariable Long storyId,
                                      @RequestBody(required = false) java.util.Map<String, String> body,
                                      HttpSession session) {
        storyService.createDeleteRequest(authService.requireLoginUserId(session), storyId,
                body == null ? null : body.get("reason"));
        return Result.success("删除申请已提交，等待审核", null);
    }

    @PostMapping("/{storyId}/validate")
    public Result<StoryValidationVO> validate(@PathVariable Long storyId, HttpSession session) {
        storyService.requireOwned(authService.requireLoginUserId(session), storyId);
        return Result.success(storyService.validate(storyId));
    }

    private Long safeUserId(HttpSession session) {
        try {
            return authService.requireLoginUserId(session);
        } catch (Exception e) {
            return null;
        }
    }
}
