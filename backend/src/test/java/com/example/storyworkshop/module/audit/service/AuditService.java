package com.example.storyworkshop.module.audit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storyworkshop.common.constant.StoryStatusConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.audit.vo.AuditStoryVO;
import com.example.storyworkshop.module.audit.vo.ReportHandleVO;
import com.example.storyworkshop.module.interact.entity.Report;
import com.example.storyworkshop.module.interact.mapper.ReportMapper;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.mapper.StoryMapper;
import com.example.storyworkshop.module.story.mapper.StoryNodeMapper;

@Service
public class AuditService {
    private final StoryMapper storyMapper;
    private final StoryNodeMapper nodeMapper;
    private final ReportMapper reportMapper;

    public AuditService(StoryMapper storyMapper, StoryNodeMapper nodeMapper, ReportMapper reportMapper) {
        this.storyMapper = storyMapper;
        this.nodeMapper = nodeMapper;
        this.reportMapper = reportMapper;
    }

    public List<AuditStoryVO> pendingStories() {
        List<AuditStoryVO> result = new ArrayList<>();
        for (Story s : storyMapper.selectByStatus(StoryStatusConstants.PENDING, 200)) {
            AuditStoryVO vo = new AuditStoryVO();
            vo.setStoryId(s.getStoryId());
            vo.setTitle(s.getTitle());
            vo.setIntro(s.getIntro());
            vo.setAuthorName(s.getAuthorName());
            vo.setStatus(s.getStatus());
            vo.setNodeCount(nodeMapper.countByStory(s.getStoryId()));
            vo.setEndingCount(nodeMapper.countEndingByStory(s.getStoryId()));
            vo.setRejectReason(s.getRejectReason());
            vo.setSubmitTime(s.getUpdateTime());
            result.add(vo);
        }
        return result;
    }

    @Transactional
    public void approve(Long operatorId, Long storyId) {
        Story story = requirePending(storyId);
        storyMapper.updateAudit(storyId, StoryStatusConstants.PUBLISHED, null, operatorId);
    }

    @Transactional
    public void reject(Long operatorId, Long storyId, String reason) {
        requirePending(storyId);
        storyMapper.updateAudit(storyId, StoryStatusConstants.REJECTED, reason == null ? "" : reason, operatorId);
    }

    @Transactional
    public void offline(Long operatorId, Long storyId, String reason) {
        Story story = storyMapper.selectById(storyId);
        if (story == null) throw new BusinessException(ResultCode.STORY_NOT_FOUND);
        if (!StoryStatusConstants.canOffline(story.getStatus())) throw new BusinessException(ResultCode.STORY_STATUS_ERROR);
        storyMapper.updateAudit(storyId, StoryStatusConstants.OFFLINE, reason == null ? "" : reason, operatorId);
    }

    public List<ReportHandleVO> pendingReports() {
        List<ReportHandleVO> result = new ArrayList<>();
        for (Report r : reportMapper.selectPending(200)) {
            ReportHandleVO vo = new ReportHandleVO();
            vo.setReportId(r.getReportId());
            vo.setStoryId(r.getStoryId());
            vo.setStoryTitle(r.getStoryTitle());
            vo.setReason(r.getReason());
            vo.setReporterName(r.getReporterName());
            vo.setStatus(r.getStatus());
            vo.setCreateTime(r.getCreateTime());
            result.add(vo);
        }
        return result;
    }

    @Transactional
    public void handleReport(Long operatorId, Long reportId, String action) {
        Report report = reportMapper.selectById(reportId);
        if (report == null) throw new BusinessException(ResultCode.REPORT_NOT_FOUND);
        String handle = action == null ? "resolve" : action;
        if (handle.equals("offline")) {
            Story story = storyMapper.selectById(report.getStoryId());
            if (story != null && StoryStatusConstants.canOffline(story.getStatus())) {
                storyMapper.updateAudit(report.getStoryId(), StoryStatusConstants.OFFLINE, "举报核实：" + report.getReason(), operatorId);
            }
            report.setStatus(1);
            report.setHandleResult("已下架并处理");
        } else if (handle.equals("ignore")) {
            report.setStatus(2);
            report.setHandleResult("举报已忽略");
        } else {
            report.setStatus(1);
            report.setHandleResult("已处理");
        }
        report.setHandleUserId(operatorId);
        reportMapper.updateHandle(report);
    }

    private Story requirePending(Long storyId) {
        Story story = storyMapper.selectById(storyId);
        if (story == null) throw new BusinessException(ResultCode.STORY_NOT_FOUND);
        if (!StoryStatusConstants.canAudit(story.getStatus())) throw new BusinessException(ResultCode.STORY_STATUS_ERROR);
        return story;
    }
}
