package com.example.storyworkshop.module.story.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storyworkshop.common.constant.RoleConstants;
import com.example.storyworkshop.common.constant.StoryStatusConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.story.dto.StoryCreateRequest;
import com.example.storyworkshop.module.story.dto.StoryQueryRequest;
import com.example.storyworkshop.module.story.dto.StoryUpdateRequest;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.mapper.StoryMapper;
import com.example.storyworkshop.module.story.mapper.StoryNodeMapper;
import com.example.storyworkshop.module.story.vo.StoryCardVO;
import com.example.storyworkshop.module.story.vo.StoryDetailVO;
import com.example.storyworkshop.module.story.vo.StoryValidationVO;

@Service
public class StoryService {
    private final StoryMapper storyMapper;
    private final StoryNodeMapper nodeMapper;
    private final StoryValidator validator;

    public StoryService(StoryMapper storyMapper, StoryNodeMapper nodeMapper, StoryValidator validator) {
        this.storyMapper = storyMapper;
        this.nodeMapper = nodeMapper;
        this.validator = validator;
    }

    public Story requireStory(Long storyId) {
        Story story = storyMapper.selectById(storyId);
        if (story == null) throw new BusinessException(ResultCode.STORY_NOT_FOUND);
        return story;
    }

    public List<StoryCardVO> page(StoryQueryRequest query) {
        int page = query.getPage() == null || query.getPage() < 1 ? 1 : query.getPage();
        int size = query.getPageSize() == null || query.getPageSize() < 1 ? 10 : Math.min(query.getPageSize(), 100);
        List<Story> rows = storyMapper.selectPublished(query.getKeyword(), query.getCategory(), query.getSort(), (page - 1) * size, size);
        List<StoryCardVO> result = new ArrayList<>();
        for (Story s : rows) result.add(toCard(s));
        return result;
    }

    public StoryDetailVO detail(Long storyId, List<String> roles, Long viewerId) {
        Story story = requireStory(storyId);
        boolean published = StoryStatusConstants.canPlay(story.getStatus());
        boolean owner = viewerId != null && viewerId.equals(story.getAuthorId());
        boolean privileged = roles != null && (roles.contains(RoleConstants.AUDITOR) || roles.contains(RoleConstants.ADMIN));
        if (!published && !owner && !privileged) throw new BusinessException(ResultCode.STORY_NOT_PUBLISHED);
        StoryDetailVO vo = new StoryDetailVO();
        copyDetail(story, vo);
        vo.setNodeCount(nodeMapper.countByStory(storyId));
        vo.setEndingCount(nodeMapper.countEndingByStory(storyId));
        return vo;
    }

    public List<StoryCardVO> mine(Long authorId) {
        List<StoryCardVO> result = new ArrayList<>();
        for (Story s : storyMapper.selectByAuthor(authorId)) result.add(toCard(s));
        return result;
    }

    @Transactional
    public Story create(Long authorId, StoryCreateRequest request) {
        Story story = new Story();
        story.setAuthorId(authorId);
        story.setTitle(request.getTitle());
        story.setIntro(request.getIntro());
        story.setCategory(request.getCategory());
        story.setCoverUrl(request.getCoverUrl());
        story.setStatus(StoryStatusConstants.DRAFT);
        storyMapper.insert(story);
        return story;
    }

    @Transactional
    public void updateBasic(Long operatorId, Long storyId, StoryUpdateRequest request) {
        Story story = requireOwnedEditable(operatorId, storyId);
        story.setTitle(request.getTitle());
        story.setIntro(request.getIntro());
        story.setCategory(request.getCategory());
        story.setCoverUrl(request.getCoverUrl());
        storyMapper.updateBasic(story);
    }

    @Transactional
    public void deleteStory(Long operatorId, Long storyId) {
        Story story = requireOwned(operatorId, storyId);
        if (!StoryStatusConstants.canEdit(story.getStatus())) throw new BusinessException(ResultCode.STORY_STATUS_ERROR);
        storyMapper.deleteById(storyId);
    }

    @Transactional
    public void submitAudit(Long operatorId, Long storyId) {
        Story story = requireOwned(operatorId, storyId);
        if (!StoryStatusConstants.canSubmitAudit(story.getStatus())) throw new BusinessException(ResultCode.STORY_STATUS_ERROR);
        StoryValidationVO result = validator.validate(storyId);
        if (!result.getValid()) throw new BusinessException(ResultCode.STORY_VALIDATION_FAILED, "校验未通过：" + String.join("；", result.getIssues()));
        storyMapper.updateStatus(storyId, StoryStatusConstants.PENDING);
    }

    public StoryValidationVO validate(Long storyId) {
        return validator.validate(storyId);
    }

    public Story requireOwned(Long operatorId, Long storyId) {
        Story story = requireStory(storyId);
        if (!story.getAuthorId().equals(operatorId)) throw new BusinessException(ResultCode.FORBIDDEN);
        return story;
    }

    public Story requireOwnedEditable(Long operatorId, Long storyId) {
        Story story = requireOwned(operatorId, storyId);
        if (!StoryStatusConstants.canEdit(story.getStatus())) throw new BusinessException(ResultCode.STORY_STATUS_ERROR);
        return story;
    }

    public StoryCardVO toCard(Story s) {
        StoryCardVO vo = new StoryCardVO();
        vo.setStoryId(s.getStoryId());
        vo.setTitle(s.getTitle());
        vo.setIntro(s.getIntro());
        vo.setCategory(s.getCategory());
        vo.setCoverUrl(s.getCoverUrl());
        vo.setAuthorName(s.getAuthorName());
        vo.setStatus(s.getStatus());
        vo.setPlayCount(s.getPlayCount());
        vo.setLikeCount(s.getLikeCount());
        vo.setFavoriteCount(s.getFavoriteCount());
        vo.setCommentCount(s.getCommentCount());
        vo.setCreateTime(s.getCreateTime());
        return vo;
    }

    private void copyDetail(Story s, StoryDetailVO vo) {
        vo.setStoryId(s.getStoryId());
        vo.setTitle(s.getTitle());
        vo.setIntro(s.getIntro());
        vo.setCategory(s.getCategory());
        vo.setCoverUrl(s.getCoverUrl());
        vo.setAuthorName(s.getAuthorName());
        vo.setStatus(s.getStatus());
        vo.setStatusName(StoryStatusConstants.getStatusName(s.getStatus()));
        vo.setRejectReason(s.getRejectReason());
        vo.setPlayCount(s.getPlayCount());
        vo.setLikeCount(s.getLikeCount());
        vo.setFavoriteCount(s.getFavoriteCount());
        vo.setCommentCount(s.getCommentCount());
        vo.setPublishTime(s.getPublishTime());
        vo.setCreateTime(s.getCreateTime());
    }
}
