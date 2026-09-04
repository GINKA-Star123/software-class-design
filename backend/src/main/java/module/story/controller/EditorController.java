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
import com.example.storyworkshop.module.story.dto.StoryChoiceRequest;
import com.example.storyworkshop.module.story.dto.StoryNodeRequest;
import com.example.storyworkshop.module.story.entity.StoryChoice;
import com.example.storyworkshop.module.story.entity.StoryNode;
import com.example.storyworkshop.module.story.mapper.StoryChoiceMapper;
import com.example.storyworkshop.module.story.mapper.StoryNodeMapper;
import com.example.storyworkshop.module.story.service.StoryChoiceService;
import com.example.storyworkshop.module.story.service.StoryNodeService;
import com.example.storyworkshop.module.story.service.StoryService;
import com.example.storyworkshop.module.user.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/editor")
@Validated
public class EditorController {
    private final StoryNodeService nodeService;
    private final StoryChoiceService choiceService;
    private final StoryService storyService;
    private final StoryNodeMapper nodeMapper;
    private final StoryChoiceMapper choiceMapper;
    private final AuthService authService;

    public EditorController(StoryNodeService nodeService, StoryChoiceService choiceService,
                            StoryService storyService, StoryNodeMapper nodeMapper,
                            StoryChoiceMapper choiceMapper, AuthService authService) {
        this.nodeService = nodeService;
        this.choiceService = choiceService;
        this.storyService = storyService;
        this.nodeMapper = nodeMapper;
        this.choiceMapper = choiceMapper;
        this.authService = authService;
    }

    private long userId(HttpSession session) {
        return authService.requireLoginUserId(session);
    }

    private void requireEditor(HttpSession session) {
        if (!RoleConstants.canCreateStory(authService.getSessionRoles(session))) throw new BusinessException(ResultCode.FORBIDDEN);
    }

    @GetMapping("/stories/{storyId}/nodes")
    public Result<List<StoryNode>> nodes(@PathVariable Long storyId, HttpSession session) {
        requireEditor(session);
        storyService.requireOwned(userId(session), storyId);
        return Result.success(nodeMapper.selectByStory(storyId));
    }

    @GetMapping("/nodes/{nodeId}/choices")
    public Result<List<StoryChoice>> choices(@PathVariable Long nodeId, HttpSession session) {
        requireEditor(session);
        nodeService.requireEditable(userId(session), nodeId);
        return Result.success(choiceMapper.selectByFromNode(nodeId));
    }

    @PostMapping("/stories/{storyId}/nodes")
    public Result<Long> addNode(@PathVariable Long storyId, @Valid @RequestBody StoryNodeRequest request, HttpSession session) {
        requireEditor(session);
        return Result.success("创建成功", nodeService.add(userId(session), storyId, request).getNodeId());
    }

    @PutMapping("/nodes/{nodeId}")
    public Result<Void> updateNode(@PathVariable Long nodeId, @Valid @RequestBody StoryNodeRequest request, HttpSession session) {
        requireEditor(session);
        nodeService.update(userId(session), nodeId, request);
        return Result.success("节点已保存", null);
    }

    @DeleteMapping("/nodes/{nodeId}")
    public Result<Void> deleteNode(@PathVariable Long nodeId, HttpSession session) {
        requireEditor(session);
        nodeService.delete(userId(session), nodeId);
        return Result.success("节点已删除", null);
    }

    @PostMapping("/nodes/{nodeId}/choices")
    public Result<Long> addChoice(@PathVariable Long nodeId, @Valid @RequestBody StoryChoiceRequest request, HttpSession session) {
        requireEditor(session);
        return Result.success("创建成功", choiceService.add(userId(session), nodeId, request).getChoiceId());
    }

    @PutMapping("/choices/{choiceId}")
    public Result<Void> updateChoice(@PathVariable Long choiceId, @Valid @RequestBody StoryChoiceRequest request, HttpSession session) {
        requireEditor(session);
        choiceService.update(userId(session), choiceId, request);
        return Result.success("选项已保存", null);
    }

    @DeleteMapping("/choices/{choiceId}")
    public Result<Void> deleteChoice(@PathVariable Long choiceId, HttpSession session) {
        requireEditor(session);
        choiceService.delete(userId(session), choiceId);
        return Result.success("选项已删除", null);
    }
}
