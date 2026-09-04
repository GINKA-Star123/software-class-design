package com.example.storyworkshop.module.stat.vo;

public class DashboardVO {
    private long totalUsers;
    private long totalStories;
    private long publishedStories;
    private long pendingStories;
    private long totalPlays;
    private long pendingReports;

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalStories() {
        return totalStories;
    }

    public void setTotalStories(long totalStories) {
        this.totalStories = totalStories;
    }

    public long getPublishedStories() {
        return publishedStories;
    }

    public void setPublishedStories(long publishedStories) {
        this.publishedStories = publishedStories;
    }

    public long getPendingStories() {
        return pendingStories;
    }

    public void setPendingStories(long pendingStories) {
        this.pendingStories = pendingStories;
    }

    public long getTotalPlays() {
        return totalPlays;
    }

    public void setTotalPlays(long totalPlays) {
        this.totalPlays = totalPlays;
    }

    public long getPendingReports() {
        return pendingReports;
    }

    public void setPendingReports(long pendingReports) {
        this.pendingReports = pendingReports;
    }

    public DashboardVO() {
    }
}
