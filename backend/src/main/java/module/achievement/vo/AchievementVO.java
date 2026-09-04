package com.example.storyworkshop.module.achievement.vo;

import java.time.LocalDateTime;

public class AchievementVO {
    private Long achId;
    private String achCode;
    private String achName;
    private String description;
    private Integer achType;
    private Long storyId;
    private boolean achieved;
    private java.time.LocalDateTime achieveTime;

    public Long getAchId() {
        return achId;
    }

    public void setAchId(Long achId) {
        this.achId = achId;
    }

    public String getAchCode() {
        return achCode;
    }

    public void setAchCode(String achCode) {
        this.achCode = achCode;
    }

    public String getAchName() {
        return achName;
    }

    public void setAchName(String achName) {
        this.achName = achName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAchType() {
        return achType;
    }

    public void setAchType(Integer achType) {
        this.achType = achType;
    }

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public boolean getAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public java.time.LocalDateTime getAchieveTime() {
        return achieveTime;
    }

    public void setAchieveTime(java.time.LocalDateTime achieveTime) {
        this.achieveTime = achieveTime;
    }

    public AchievementVO() {
    }
}
