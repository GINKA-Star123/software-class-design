package com.example.storyworkshop.module.audit.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.audit.entity.StoryDeleteRequest;

public interface StoryDeleteRequestMapper {
    int insert(StoryDeleteRequest request);
    StoryDeleteRequest selectById(@Param("reqId") Long reqId);
    int countPending(@Param("storyId") Long storyId);
    List<StoryDeleteRequest> selectPending(@Param("limit") int limit);
    int updateHandle(StoryDeleteRequest request);
}
