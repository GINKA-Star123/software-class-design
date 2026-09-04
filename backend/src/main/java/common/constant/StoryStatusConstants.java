package com.example.storyworkshop.common.constant;

public final class StoryStatusConstants {
    private StoryStatusConstants() {
    }

    public static final int DRAFT = 0;
    public static final int PENDING = 1;
    public static final int PUBLISHED = 2;
    public static final int REJECTED = 3;
    public static final int OFFLINE = 4;

    public static String getStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }

        return switch (status) {
            case DRAFT -> "草稿";
            case PENDING -> "待审核";
            case PUBLISHED -> "已发布";
            case REJECTED -> "已驳回";
            case OFFLINE -> "已下架";
            default -> "未知";
        };
    }

    public static boolean canEdit(Integer status) {
        return status != null && (status == DRAFT || status == REJECTED);
    }

    public static boolean canSubmitAudit(Integer status) {
        return status != null && (status == DRAFT || status == REJECTED);
    }

    public static boolean canAudit(Integer status) {
        return status != null && status == PENDING;
    }

    public static boolean canPlay(Integer status) {
        return status != null && status == PUBLISHED;
    }

    public static boolean canOffline(Integer status) {
        return status != null && status == PUBLISHED;
    }

    public static boolean canRepublish(Integer status) {
        return status != null && status == OFFLINE;
    }
}