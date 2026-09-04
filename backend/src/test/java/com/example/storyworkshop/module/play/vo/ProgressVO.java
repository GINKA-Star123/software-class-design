package com.example.storyworkshop.module.play.vo;

import java.time.LocalDateTime;

public class ProgressVO {
    private Long progressId;
    private Long storyId;
    private String storyTitle;
    private Integer slotNo;
    private Integer status;
    private String currentNodeText;
    private String currentEndingTitle;
    private Integer endingCount;
    private java.time.LocalDateTime updateTime;

    public Long getProgressId() {
        return progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
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

    public String getCurrentNodeText() {
        return currentNodeText;
    }

    public void setCurrentNodeText(String currentNodeText) {
        this.currentNodeText = currentNodeText;
    }

    public String getCurrentEndingTitle() {
        return currentEndingTitle;
    }

    public void setCurrentEndingTitle(String currentEndingTitle) {
        this.currentEndingTitle = currentEndingTitle;
    }

    public Integer getEndingCount() {
        return endingCount;
    }

    public void setEndingCount(Integer endingCount) {
        this.endingCount = endingCount;
    }

    public java.time.LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.time.LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public ProgressVO() {
    }
}
