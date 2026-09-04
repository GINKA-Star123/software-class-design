package com.example.storyworkshop.module.interact.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.example.storyworkshop.module.interact.entity.Report;

public interface ReportMapper {
    int insert(Report report);
    Report selectById(@Param("reportId") Long reportId);
    List<Report> selectPending(@Param("limit") int limit);
    int updateHandle(Report report);
}
