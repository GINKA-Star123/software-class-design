package com.example.storyworkshop.module.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.storyworkshop.module.user.entity.Role;

public interface RoleMapper {
    Role selectById(@Param("roleId") Short roleId);

    Role selectByName(@Param("roleName") String roleName);

    List<Role> selectAll();

    List<Role> selectByUserId(@Param("userId") Long userId);

    List<String> selectRoleNamesByUserId(@Param("userId") Long userId);
}