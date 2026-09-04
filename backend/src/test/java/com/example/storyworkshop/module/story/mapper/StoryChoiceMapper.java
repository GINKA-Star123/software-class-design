package com.example.storyworkshop.module.story.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.story.entity.StoryChoice;

public interface StoryChoiceMapper {
    StoryChoice selectById(@Param("choiceId") Long choiceId);
    List<StoryChoice> selectByFromNode(@Param("fromNodeId") Long fromNodeId);
    List<StoryChoice> selectByStory(@Param("storyId") Long storyId);
    int insert(StoryChoice choice);
    int update(StoryChoice choice);
    int deleteById(@Param("choiceId") Long choiceId);
    int deleteByNode(@Param("nodeId") Long nodeId);
}
