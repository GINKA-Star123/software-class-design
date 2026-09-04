package com.example.storyworkshop.module.interact.mapper;

import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.interact.entity.LikeRecord;

public interface LikeMapper {
    int insert(LikeRecord like);
    int delete(@Param("userId") Long userId, @Param("storyId") Long storyId);
    LikeRecord select(@Param("userId") Long userId, @Param("storyId") Long storyId);
}
