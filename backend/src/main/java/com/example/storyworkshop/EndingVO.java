package com.example.storyworkshop.module.play.vo;

public class EndingVO {
    private String endingTitle;
    private String endingText;
    private Integer endingCount;
    private boolean newEnding;

    public String getEndingTitle() {
        return endingTitle;
    }

    public void setEndingTitle(String endingTitle) {
        this.endingTitle = endingTitle;
    }

    public String getEndingText() {
        return endingText;
    }

    public void setEndingText(String endingText) {
        this.endingText = endingText;
    }

    public Integer getEndingCount() {
        return endingCount;
    }

    public void setEndingCount(Integer endingCount) {
        this.endingCount = endingCount;
    }

    public boolean getNewEnding() {
        return newEnding;
    }

    public void setNewEnding(boolean newEnding) {
        this.newEnding = newEnding;
    }

    public EndingVO() {
    }
}
