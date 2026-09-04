package com.example.storyworkshop.module.interact.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.interact.entity.Comment;

public interface CommentMapper {
    int insert(Comment comment);
    Comment selectById(@Param("commentId") Long commentId);
    List<Comment> selectPageByStory(@Param("storyId") Long storyId, @Param("offset") int offset, @Param("pageSize") int pageSize);
    long countByStory(@Param("storyId") Long storyId);
    int deleteById(@Param("commentId") Long commentId);
}
