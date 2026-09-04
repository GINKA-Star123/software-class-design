package com.example.storyworkshop.module.play.vo;

import java.util.ArrayList;
import java.util.List;

public class PlayNodeVO {
    private Long progressId;
    private Long storyId;
    private String storyTitle;
    private Long nodeId;
    private String nodeText;
    private Integer isStart;
    private Integer isEnding;
    private String endingTitle;
    private boolean justEnded;
    private Integer endingCount;
    private Integer slotNo;
    private java.util.List<com.example.storyworkshop.module.play.vo.PlayChoiceVO> choices;

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

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeText() {
        return nodeText;
    }

    public void setNodeText(String nodeText) {
        this.nodeText = nodeText;
    }

    public Integer getIsStart() {
        return isStart;
    }

    public void setIsStart(Integer isStart) {
        this.isStart = isStart;
    }

    public Integer getIsEnding() {
        return isEnding;
    }

    public void setIsEnding(Integer isEnding) {
        this.isEnding = isEnding;
    }

    public String getEndingTitle() {
        return endingTitle;
    }

    public void setEndingTitle(String endingTitle) {
        this.endingTitle = endingTitle;
    }

    public boolean getJustEnded() {
        return justEnded;
    }

    public void setJustEnded(boolean justEnded) {
        this.justEnded = justEnded;
    }

    public Integer getEndingCount() {
        return endingCount;
    }

    public void setEndingCount(Integer endingCount) {
        this.endingCount = endingCount;
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public java.util.List<com.example.storyworkshop.module.play.vo.PlayChoiceVO> getChoices() {
        return choices;
    }

    public void setChoices(java.util.List<com.example.storyworkshop.module.play.vo.PlayChoiceVO> choices) {
        this.choices = choices;
    }

    public PlayNodeVO() {
        this.choices = new ArrayList<>();
    }
}
