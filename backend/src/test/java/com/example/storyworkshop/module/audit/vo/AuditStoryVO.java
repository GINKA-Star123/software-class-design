package com.example.storyworkshop.module.audit.vo;

import java.time.LocalDateTime;

public class AuditStoryVO {
    private Long storyId;
    private String title;
    private String intro;
    private String authorName;
    private Integer status;
    private Integer nodeCount;
    private Integer endingCount;
    private String rejectReason;
    private java.time.LocalDateTime submitTime;

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    public Integer getEndingCount() {
        return endingCount;
    }

    public void setEndingCount(Integer endingCount) {
        this.endingCount = endingCount;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public java.time.LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(java.time.LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public AuditStoryVO() {
    }
}
