package com.example.storyworkshop.module.play.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.play.entity.GameProgress;

public interface ProgressMapper {
    GameProgress selectById(@Param("progressId") Long progressId);
    List<GameProgress> selectByUserStory(@Param("userId") Long userId, @Param("storyId") Long storyId);
    GameProgress selectOngoing(@Param("userId") Long userId, @Param("storyId") Long storyId);
    List<GameProgress> selectByUser(@Param("userId") Long userId);
    int insert(GameProgress progress);
    int update(GameProgress progress);
    int deleteById(@Param("progressId") Long progressId);
}
