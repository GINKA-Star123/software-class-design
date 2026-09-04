package com.example.storyworkshop.module.achievement.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.achievement.entity.Achievement;

public interface AchievementMapper {
    Achievement selectById(@Param("achId") Long achId);
    Achievement selectByCode(@Param("code") String code);
    List<Achievement> selectAll();
    List<Achievement> selectByStory(@Param("storyId") Long storyId);
}
