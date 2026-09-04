package com.example.storyworkshop.module.audit.dto;

public class AuditRequest {
    private String comment;
    private Integer status;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public AuditRequest() {
    }
}
