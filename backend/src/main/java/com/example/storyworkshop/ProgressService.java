package com.example.storyworkshop.module.play.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.play.entity.GameProgress;
import com.example.storyworkshop.module.play.mapper.ProgressMapper;
import com.example.storyworkshop.module.play.vo.ProgressVO;

@Service
public class ProgressService {
    private final ProgressMapper progressMapper;

    public ProgressService(ProgressMapper progressMapper) {
        this.progressMapper = progressMapper;
    }

    public List<ProgressVO> myProgress(Long userId) {
        List<ProgressVO> result = new ArrayList<>();
        for (GameProgress p : progressMapper.selectByUser(userId)) {
            ProgressVO vo = new ProgressVO();
            vo.setProgressId(p.getProgressId());
            vo.setStoryId(p.getStoryId());
            vo.setStoryTitle(p.getStoryTitle());
            vo.setSlotNo(p.getSlotNo());
            vo.setStatus(p.getStatus());
            vo.setCurrentNodeText(p.getCurrentNodeText());
            vo.setCurrentEndingTitle(p.getCurrentEndingTitle());
            vo.setEndingCount(p.getEndingCount());
            vo.setUpdateTime(p.getUpdateTime());
            result.add(vo);
        }
        return result;
    }

    public GameProgress requireOwned(Long userId, Long progressId) {
        GameProgress progress = progressMapper.selectById(progressId);
        if (progress == null || !progress.getUserId().equals(userId)) throw new BusinessException(ResultCode.PROGRESS_NOT_FOUND);
        return progress;
    }

    public void reset(Long userId, Long progressId) {
        requireOwned(userId, progressId);
        progressMapper.deleteById(progressId);
    }
}
