package com.example.storyworkshop.module.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.storyworkshop.module.user.entity.UserRole;

public interface UserRoleMapper {
    int insert(UserRole userRole);

    int insertIgnore(@Param("userId") Long userId, @Param("roleId") Short roleId);

    int deleteByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Short roleId);

    int deleteByUserId(@Param("userId") Long userId);

    List<UserRole> selectByUserId(@Param("userId") Long userId);
}