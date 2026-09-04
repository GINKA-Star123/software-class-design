package com.example.storyworkshop.module.interact.service;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CommentFilterService {
    private static final List<String> SENSITIVE = List.of("赌博", "博彩", "代刷", "枪支", "违禁品");

    public String filter(String content) {
        String result = content;
        for (String word : SENSITIVE) result = result.replace(word, "***");
        return result;
    }
}
