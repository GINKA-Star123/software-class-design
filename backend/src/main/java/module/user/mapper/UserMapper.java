package com.example.storyworkshop.module.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.storyworkshop.module.user.entity.User;

public interface UserMapper {
    User selectById(@Param("userId") Long userId);

    User selectByUsername(@Param("username") String username);

    User selectByEmail(@Param("email") String email);

    int countByUsername(@Param("username") String username);

    int countByEmail(@Param("email") String email);

    int insert(User user);

    int updateProfile(User user);

    int updateStatus(@Param("userId") Long userId, @Param("status") Integer status);

    List<User> selectAdminPage(
            @Param("keyword") String keyword,
            @Param("status") Integer status,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    long countAdminPage(
            @Param("keyword") String keyword,
            @Param("status") Integer status
    );
}