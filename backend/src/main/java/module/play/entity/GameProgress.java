package com.example.storyworkshop.module.play.entity;

import java.time.LocalDateTime;

public class GameProgress {
    private Long progressId;
    private Long userId;
    private Long storyId;
    private String storyTitle;
    private Long currentNodeId;
    private String currentNodeText;
    private String currentEndingTitle;

    private Integer slotNo;
    private Integer status;
    private String history;
    private Integer endingCount;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;

    public Long getProgressId() {
        return progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public Long getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(Long currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Integer getEndingCount() {
        return endingCount;
    }

    public void setEndingCount(Integer endingCount) {
        this.endingCount = endingCount;
    }

    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public java.time.LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.time.LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public GameProgress() {
    }

    public String getCurrentNodeText() { return currentNodeText; }
    public void setCurrentNodeText(String currentNodeText) { this.currentNodeText = currentNodeText; }
    public String getCurrentEndingTitle() { return currentEndingTitle; }
    public void setCurrentEndingTitle(String currentEndingTitle) { this.currentEndingTitle = currentEndingTitle; }
}
