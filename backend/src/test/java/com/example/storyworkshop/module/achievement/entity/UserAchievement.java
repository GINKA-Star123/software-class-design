package com.example.storyworkshop.module.achievement.entity;

import java.time.LocalDateTime;

public class UserAchievement {
    private Long uaId;
    private Long userId;
    private Long achId;
    private java.time.LocalDateTime achieveTime;

    public Long getUaId() {
        return uaId;
    }

    public void setUaId(Long uaId) {
        this.uaId = uaId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAchId() {
        return achId;
    }

    public void setAchId(Long achId) {
        this.achId = achId;
    }

    public java.time.LocalDateTime getAchieveTime() {
        return achieveTime;
    }

    public void setAchieveTime(java.time.LocalDateTime achieveTime) {
        this.achieveTime = achieveTime;
    }

    public UserAchievement() {
    }
}
