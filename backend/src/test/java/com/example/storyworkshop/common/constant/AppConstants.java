package com.example.storyworkshop.common.constant;

import java.util.Set;

public final class AppConstants {
    private AppConstants() {
    }

    public static final String SESSION_USER_ID = "LOGIN_USER_ID";
    public static final String SESSION_USERNAME = "LOGIN_USERNAME";
    public static final String SESSION_ROLES = "LOGIN_ROLES";

    public static final int DEFAULT_PAGE_NO = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    public static final int MAX_PROGRESS_SLOT_COUNT = 3;

    public static final String UPLOAD_URL_PREFIX = "/uploads";
    public static final String COVER_DIR = "covers";
    public static final String AVATAR_DIR = "avatars";

    public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024L;

    public static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    public static final String EMPTY_JSON_ARRAY = "[]";

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
}