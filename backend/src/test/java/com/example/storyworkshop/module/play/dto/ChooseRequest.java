package com.example.storyworkshop.module.play.dto;

public class ChooseRequest {
    private Long progressId;
    private Long choiceId;
    private Long guestStoryId;
    private Long guestCurrentNodeId;

    public Long getProgressId() {
        return progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
    }

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }

    public Long getGuestStoryId() {
        return guestStoryId;
    }

    public void setGuestStoryId(Long guestStoryId) {
        this.guestStoryId = guestStoryId;
    }

    public Long getGuestCurrentNodeId() {
        return guestCurrentNodeId;
    }

    public void setGuestCurrentNodeId(Long guestCurrentNodeId) {
        this.guestCurrentNodeId = guestCurrentNodeId;
    }

    public ChooseRequest() {
    }
}
