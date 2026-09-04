package com.example.storyworkshop.module.play.dto;

public class StartPlayRequest {
    private Long storyId;
    private Integer slotNo;

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public StartPlayRequest() {
    }
}
