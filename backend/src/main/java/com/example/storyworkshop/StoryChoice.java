package com.example.storyworkshop.module.story.entity;

import java.time.LocalDateTime;

public class StoryChoice {
    private Long choiceId;
    private Long fromNodeId;
    private Long toNodeId;
    private String choiceText;
    private String conditionExpr;
    private Integer sortOrder;
    private Long toNodeStoryId;
    private String toNodeText;
    private Integer toIsEnding;
    private Integer toIsStart;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }

    public Long getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(Long fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public Long getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(Long toNodeId) {
        this.toNodeId = toNodeId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public String getConditionExpr() {
        return conditionExpr;
    }

    public void setConditionExpr(String conditionExpr) {
        this.conditionExpr = conditionExpr;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getToNodeStoryId() {
        return toNodeStoryId;
    }

    public void setToNodeStoryId(Long toNodeStoryId) {
        this.toNodeStoryId = toNodeStoryId;
    }

    public String getToNodeText() {
        return toNodeText;
    }

    public void setToNodeText(String toNodeText) {
        this.toNodeText = toNodeText;
    }

    public Integer getToIsEnding() {
        return toIsEnding;
    }

    public void setToIsEnding(Integer toIsEnding) {
        this.toIsEnding = toIsEnding;
    }

    public Integer getToIsStart() {
        return toIsStart;
    }

    public void setToIsStart(Integer toIsStart) {
        this.toIsStart = toIsStart;
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

    public StoryChoice() {
    }
}
