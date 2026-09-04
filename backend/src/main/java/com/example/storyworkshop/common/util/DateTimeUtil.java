package com.example.storyworkshop.common.util;

import com.example.storyworkshop.common.constant.AppConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(AppConstants.DATE_TIME_PATTERN);

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(AppConstants.DATE_PATTERN);

    private DateTimeUtil() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }

        return date.format(DATE_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        return LocalDateTime.parse(text, DATE_TIME_FORMATTER);
    }

    public static LocalDate parseDate(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        return LocalDate.parse(text, DATE_FORMATTER);
    }

    public static LocalDateTime startOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }

        return LocalDateTime.of(date, LocalTime.MIN);
    }

    public static LocalDateTime endOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }

        return LocalDateTime.of(date, LocalTime.MAX);
    }
}