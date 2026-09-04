package com.example.storyworkshop.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class PasswordUtil {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private PasswordUtil() {
    }

    public static String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        return PASSWORD_ENCODER.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            return false;
        }

        if (encodedPassword == null || encodedPassword.isBlank()) {
            return false;
        }

        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }
}