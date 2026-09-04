package com.example.storyworkshop.module.play.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.storyworkshop.common.constant.StoryStatusConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.Result;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.play.dto.ChooseRequest;
import com.example.storyworkshop.module.play.dto.StartPlayRequest;
import com.example.storyworkshop.module.play.entity.GameProgress;
import com.example.storyworkshop.module.play.mapper.ProgressMapper;
import com.example.storyworkshop.module.play.service.ProgressService;
import com.example.storyworkshop.module.play.service.StoryEngineService;
import com.example.storyworkshop.module.play.vo.PlayNodeVO;
import com.example.storyworkshop.module.play.vo.ProgressVO;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.mapper.StoryMapper;
import com.example.storyworkshop.module.user.service.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/play")
public class PlayController {
    private final StoryEngineService engineService;
    private final ProgressService progressService;
    private final AuthService authService;
    private final StoryMapper storyMapper;
    private final ProgressMapper progressMapper;

    public PlayController(StoryEngineService engineService, ProgressService progressService,
                          AuthService authService, StoryMapper storyMapper, ProgressMapper progressMapper) {
        this.engineService = engineService;
        this.progressService = progressService;
        this.authService = authService;
        this.storyMapper = storyMapper;
        this.progressMapper = progressMapper;
    }

    @PostMapping("/stories/{storyId}/start")
    public Result<PlayNodeVO> start(@PathVariable Long storyId,
                                    @RequestBody(required = false) StartPlayRequest request, HttpSession session) {
        Long userId = authService.requireLoginUserId(session);
        Integer slot = request == null ? null : request.getSlotNo();
        return Result.success(engineService.start(userId, storyId, slot));
    }

    @PostMapping("/choose")
    public Result<PlayNodeVO> choose(@RequestBody ChooseRequest request, HttpSession session) {
        Long userId = authService.requireLoginUserId(session);
        if (request.getProgressId() == null) throw new BusinessException(ResultCode.PROGRESS_NOT_FOUND);
        return Result.success(engineService.choose(userId, request.getProgressId(), request.getChoiceId()));
    }

    @GetMapping("/progress")
    public Result<List<ProgressVO>> myProgress(HttpSession session) {
        return Result.success(progressService.myProgress(authService.requireLoginUserId(session)));
    }

    @PostMapping("/progress/{progressId}/reset")
    public Result<Void> reset(@PathVariable Long progressId, HttpSession session) {
        progressService.reset(authService.requireLoginUserId(session), progressId);
        return Result.success("已重置", null);
    }

    @GetMapping("/stories/{storyId}/ending")
    public Result<Boolean> storyPlayable(@PathVariable Long storyId) {
        Story story = storyMapper.selectById(storyId);
        return Result.success(story != null && StoryStatusConstants.canPlay(story.getStatus()));
    }
}
