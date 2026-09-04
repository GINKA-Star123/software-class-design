package com.example.storyworkshop.module.story.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.story.entity.StoryNode;

public interface StoryNodeMapper {
    StoryNode selectById(@Param("nodeId") Long nodeId);
    StoryNode selectStartByStory(@Param("storyId") Long storyId);
    List<StoryNode> selectByStory(@Param("storyId") Long storyId);
    int countByStory(@Param("storyId") Long storyId);
    int countEndingByStory(@Param("storyId") Long storyId);
    int insert(StoryNode node);
    int update(StoryNode node);
    int deleteById(@Param("nodeId") Long nodeId);
}
