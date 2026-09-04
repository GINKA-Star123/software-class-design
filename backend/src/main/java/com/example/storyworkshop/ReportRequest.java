package com.example.storyworkshop.module.interact.dto;

public class ReportRequest {
    private Long commentId;
    private String reason;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReportRequest() {
    }
}
