package com.example.storyworkshop.module.story.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.story.entity.Story;

public interface StoryMapper {
    Story selectById(@Param("storyId") Long storyId);
    List<Story> selectByAuthor(@Param("authorId") Long authorId);
    List<Story> selectPublished(@Param("keyword") String keyword, @Param("category") String category,
            @Param("sort") String sort, @Param("offset") int offset, @Param("pageSize") int pageSize);
    long countPublished(@Param("keyword") String keyword, @Param("category") String category);
    List<Story> selectByStatus(@Param("status") Integer status, @Param("limit") int limit);
    List<Story> selectPendingBefore(@Param("limit") int limit);
    int insert(Story story);
    int updateBasic(Story story);
    int updateStatus(@Param("storyId") Long storyId, @Param("status") Integer status);
    int updateAudit(@Param("storyId") Long storyId, @Param("status") Integer status,
            @Param("rejectReason") String rejectReason, @Param("auditUserId") Long auditUserId);
    int deleteById(@Param("storyId") Long storyId);
    int changeCount(@Param("storyId") Long storyId, @Param("field") String field, @Param("delta") int delta);
}
