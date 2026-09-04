package com.example.storyworkshop.module.achievement.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserAchievementMapper {
    int insert(@Param("userId") Long userId, @Param("achId") Long achId);
    int exists(@Param("userId") Long userId, @Param("achId") Long achId);
    List<String> selectCodesByUserId(@Param("userId") Long userId);
}
