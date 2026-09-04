package com.example.storyworkshop.common.constant;

import java.util.Collection;
import java.util.Objects;

public final class RoleConstants {
    private RoleConstants() {
    }

    public static final short PLAYER_ID = 1;
    public static final short AUTHOR_ID = 2;
    public static final short AUDITOR_ID = 3;
    public static final short ADMIN_ID = 4;

    public static final String PLAYER = "PLAYER";
    public static final String AUTHOR = "AUTHOR";
    public static final String AUDITOR = "AUDITOR";
    public static final String ADMIN = "ADMIN";

    public static boolean isPlayer(String roleName) {
        return Objects.equals(PLAYER, roleName);
    }

    public static boolean isAuthor(String roleName) {
        return Objects.equals(AUTHOR, roleName);
    }

    public static boolean isAuditor(String roleName) {
        return Objects.equals(AUDITOR, roleName);
    }

    public static boolean isAdmin(String roleName) {
        return Objects.equals(ADMIN, roleName);
    }

    public static boolean canAudit(Collection<String> roles) {
        return roles != null && (roles.contains(AUDITOR) || roles.contains(ADMIN));
    }

    public static boolean canManage(Collection<String> roles) {
        return roles != null && roles.contains(ADMIN);
    }

    public static boolean canCreateStory(Collection<String> roles) {
        return roles != null && (roles.contains(AUTHOR) || roles.contains(ADMIN));
    }
}