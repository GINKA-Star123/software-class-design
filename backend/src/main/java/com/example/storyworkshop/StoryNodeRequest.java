package com.example.storyworkshop.module.story.dto;

public class StoryNodeRequest {
    private String nodeText;
    private Integer isEnding;
    private String endingTitle;
    private Integer sortOrder;
    private Integer isStart;

    public String getNodeText() {
        return nodeText;
    }

    public void setNodeText(String nodeText) {
        this.nodeText = nodeText;
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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getIsStart() {
        return isStart;
    }

    public void setIsStart(Integer isStart) {
        this.isStart = isStart;
    }

    public StoryNodeRequest() {
    }
}
