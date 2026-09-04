package com.example.storyworkshop.module.achievement.vo;

import java.time.LocalDateTime;

public class UserAchievementVO {
    private Long achId;
    private String achCode;
    private String achName;
    private String description;
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

    public java.time.LocalDateTime getAchieveTime() {
        return achieveTime;
    }

    public void setAchieveTime(java.time.LocalDateTime achieveTime) {
        this.achieveTime = achieveTime;
    }

    public UserAchievementVO() {
    }
}
