package com.example.storyworkshop.module.interact.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storyworkshop.common.constant.StoryStatusConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.interact.dto.CommentRequest;
import com.example.storyworkshop.module.interact.dto.ReportRequest;
import com.example.storyworkshop.module.interact.entity.Comment;
import com.example.storyworkshop.module.interact.entity.Favorite;
import com.example.storyworkshop.module.interact.entity.LikeRecord;
import com.example.storyworkshop.module.interact.entity.Report;
import com.example.storyworkshop.module.interact.mapper.CommentMapper;
import com.example.storyworkshop.module.interact.mapper.FavoriteMapper;
import com.example.storyworkshop.module.interact.mapper.LikeMapper;
import com.example.storyworkshop.module.interact.mapper.ReportMapper;
import com.example.storyworkshop.module.interact.vo.CommentVO;
import com.example.storyworkshop.module.interact.vo.InteractStatusVO;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.mapper.StoryMapper;

@Service
public class InteractService {
    private final StoryMapper storyMapper;
    private final CommentMapper commentMapper;
    private final LikeMapper likeMapper;
    private final FavoriteMapper favoriteMapper;
    private final ReportMapper reportMapper;
    private final CommentFilterService filterService;

    public InteractService(StoryMapper storyMapper, CommentMapper commentMapper, LikeMapper likeMapper,
                           FavoriteMapper favoriteMapper, ReportMapper reportMapper, CommentFilterService filterService) {
        this.storyMapper = storyMapper;
        this.commentMapper = commentMapper;
        this.likeMapper = likeMapper;
        this.favoriteMapper = favoriteMapper;
        this.reportMapper = reportMapper;
        this.filterService = filterService;
    }

    private Story requirePublished(Long storyId) {
        Story story = storyMapper.selectById(storyId);
        if (story == null) throw new BusinessException(ResultCode.STORY_NOT_FOUND);
        if (!StoryStatusConstants.canPlay(story.getStatus())) throw new BusinessException(ResultCode.STORY_NOT_PUBLISHED);
        return story;
    }

    @Transactional
    public boolean toggleLike(Long userId, Long storyId) {
        requirePublished(storyId);
        boolean liked;
        if (likeMapper.select(userId, storyId) == null) {
            LikeRecord like = new LikeRecord();
            like.setUserId(userId);
            like.setStoryId(storyId);
            likeMapper.insert(like);
            storyMapper.changeCount(storyId, "like_count", 1);
            liked = true;
        } else {
            likeMapper.delete(userId, storyId);
            storyMapper.changeCount(storyId, "like_count", -1);
            liked = false;
        }
        return liked;
    }

    @Transactional
    public boolean toggleFavorite(Long userId, Long storyId) {
        requirePublished(storyId);
        boolean fav;
        if (favoriteMapper.select(userId, storyId) == null) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setStoryId(storyId);
            favoriteMapper.insert(favorite);
            storyMapper.changeCount(storyId, "favorite_count", 1);
            fav = true;
        } else {
            favoriteMapper.delete(userId, storyId);
            storyMapper.changeCount(storyId, "favorite_count", -1);
            fav = false;
        }
        return fav;
    }

    public InteractStatusVO status(Long userId, Long storyId) {
        Story story = requirePublished(storyId);
        InteractStatusVO vo = new InteractStatusVO();
        vo.setLiked(userId != null && likeMapper.select(userId, storyId) != null);
        vo.setFavorited(userId != null && favoriteMapper.select(userId, storyId) != null);
        vo.setLikeCount(story.getLikeCount());
        vo.setFavoriteCount(story.getFavoriteCount());
        return vo;
    }

    @Transactional
    public Comment addComment(Long userId, Long storyId, CommentRequest request) {
        requirePublished(storyId);
        if (request.getParentId() != null && commentMapper.selectById(request.getParentId()) == null) {
            throw new BusinessException(ResultCode.COMMENT_NOT_FOUND);
        }
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setStoryId(storyId);
        comment.setContent(filterService.filter(request.getContent()));
        comment.setParentId(request.getParentId());
        commentMapper.insert(comment);
        storyMapper.changeCount(storyId, "comment_count", 1);
        return commentMapper.selectById(comment.getCommentId());
    }

    public List<CommentVO> comments(Long storyId, int page, int pageSize) {
        int p = page < 1 ? 1 : page;
        int s = pageSize < 1 ? 10 : Math.min(pageSize, 100);
        List<CommentVO> result = new ArrayList<>();
        for (Comment c : commentMapper.selectPageByStory(storyId, (p - 1) * s, s)) {
            CommentVO vo = new CommentVO();
            vo.setCommentId(c.getCommentId());
            vo.setUserId(c.getUserId());
            vo.setNickname(c.getNickname());
            vo.setAvatarUrl(c.getAvatarUrl());
            vo.setContent(c.getContent());
            vo.setParentId(c.getParentId());
            vo.setCreateTime(c.getCreateTime());
            result.add(vo);
        }
        return result;
    }

    @Transactional
    public void report(Long userId, Long storyId, ReportRequest request) {
        Story story = storyMapper.selectById(storyId);
        if (story == null) throw new BusinessException(ResultCode.STORY_NOT_FOUND);
        Report report = new Report();
        report.setUserId(userId);
        report.setStoryId(storyId);
        report.setCommentId(request.getCommentId());
        report.setReason(request.getReason());
        reportMapper.insert(report);
    }
}
