package com.example.storyworkshop.module.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.admin.dto.AdminUserRequest;
import com.example.storyworkshop.module.admin.vo.AdminContentVO;
import com.example.storyworkshop.module.admin.vo.AdminUserVO;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.mapper.StoryMapper;
import com.example.storyworkshop.module.user.entity.User;
import com.example.storyworkshop.module.user.mapper.RoleMapper;
import com.example.storyworkshop.module.user.mapper.UserMapper;
import com.example.storyworkshop.module.user.mapper.UserRoleMapper;

@Service
public class AdminService {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final StoryMapper storyMapper;

    public AdminService(UserMapper userMapper, RoleMapper roleMapper, UserRoleMapper userRoleMapper, StoryMapper storyMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.storyMapper = storyMapper;
    }

    public List<AdminUserVO> users(AdminUserRequest request) {
        int page = request.getPage() == null || request.getPage() < 1 ? 1 : request.getPage();
        int size = request.getPageSize() == null || request.getPageSize() < 1 ? 10 : Math.min(request.getPageSize(), 100);
        List<AdminUserVO> result = new ArrayList<>();
        for (User u : userMapper.selectAdminPage(request.getKeyword(), request.getStatus(), (page - 1) * size, size)) {
            result.add(toUserVO(u));
        }
        return result;
    }

    public long countUsers(AdminUserRequest request) {
        return userMapper.countAdminPage(request.getKeyword(), request.getStatus());
    }

    @Transactional
    public void updateStatus(Long userId, Integer status) {
        if (userMapper.selectById(userId) == null) throw new BusinessException(ResultCode.USER_NOT_FOUND);
        userMapper.updateStatus(userId, status == null ? 1 : status);
    }

    @Transactional
    public void updateRoles(Long userId, List<Short> roleIds) {
        if (userMapper.selectById(userId) == null) throw new BusinessException(ResultCode.USER_NOT_FOUND);
        userRoleMapper.deleteByUserId(userId);
        if (roleIds != null) {
            for (Short roleId : roleIds) {
                if (roleMapper.selectById(roleId) != null) userRoleMapper.insertIgnore(userId, roleId);
            }
        }
    }

    public List<AdminContentVO> contents(Integer status) {
        List<AdminContentVO> result = new ArrayList<>();
        for (Story s : storyMapper.selectByStatus(status, 500)) {
            AdminContentVO vo = new AdminContentVO();
            vo.setStoryId(s.getStoryId());
            vo.setTitle(s.getTitle());
            vo.setAuthorName(s.getAuthorName());
            vo.setStatus(s.getStatus());
            vo.setPlayCount(s.getPlayCount());
            vo.setCreateTime(s.getCreateTime());
            vo.setUpdateTime(s.getUpdateTime());
            result.add(vo);
        }
        return result;
    }

    private AdminUserVO toUserVO(User u) {
        AdminUserVO vo = new AdminUserVO();
        vo.setUserId(u.getUserId());
        vo.setUsername(u.getUsername());
        vo.setNickname(u.getNickname());
        vo.setEmail(u.getEmail());
        vo.setStatus(u.getStatus());
        vo.setRoles(roleMapper.selectRoleNamesByUserId(u.getUserId()));
        vo.setCreateTime(u.getCreateTime());
        return vo;
    }
}
