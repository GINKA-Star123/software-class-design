package com.example.storyworkshop.module.interact.mapper;

import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.interact.entity.Favorite;

public interface FavoriteMapper {
    int insert(Favorite favorite);
    int delete(@Param("userId") Long userId, @Param("storyId") Long storyId);
    Favorite select(@Param("userId") Long userId, @Param("storyId") Long storyId);
}
