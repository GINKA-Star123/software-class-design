package com.example.storyworkshop.module.stat.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.story.entity.Story;

public interface StatMapper {
    List<Story> selectHot(@Param("limit") int limit);
    List<Story> selectNewest(@Param("limit") int limit);
    Map<String, Object> selectDashboard();
}
