package com.example.storyworkshop.module.achievement.entity;

import java.time.LocalDateTime;

public class Achievement {
    private Long achId;
    private String achCode;
    private String achName;
    private String description;
    private Integer achType;
    private Long storyId;
    private Integer status;
    private java.time.LocalDateTime createTime;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Achievement() {
    }
}
