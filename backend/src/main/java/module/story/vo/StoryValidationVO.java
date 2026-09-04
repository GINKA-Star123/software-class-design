package com.example.storyworkshop.module.story.vo;

import java.util.ArrayList;
import java.util.List;

public class StoryValidationVO {
    private boolean valid;
    private int nodeCount;
    private int endingCount;
    private java.util.List<String> issues;

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public int getEndingCount() {
        return endingCount;
    }

    public void setEndingCount(int endingCount) {
        this.endingCount = endingCount;
    }

    public java.util.List<String> getIssues() {
        return issues;
    }

    public void setIssues(java.util.List<String> issues) {
        this.issues = issues;
    }

    public StoryValidationVO() {
        this.issues = new ArrayList<>();
    }
}
