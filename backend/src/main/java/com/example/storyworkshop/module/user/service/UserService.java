package com.example.storyworkshop.module.user.service;

import com.example.storyworkshop.common.constant.RoleConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.user.dto.UserProfileRequest;
import com.example.storyworkshop.module.user.entity.Role;
import com.example.storyworkshop.module.user.entity.User;
import com.example.storyworkshop.module.user.mapper.RoleMapper;
import com.example.storyworkshop.module.user.mapper.UserMapper;
import com.example.storyworkshop.module.user.mapper.UserRoleMapper;
import com.example.storyworkshop.module.user.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    public UserService(
            UserMapper userMapper,
            RoleMapper roleMapper,
            UserRoleMapper userRoleMapper
    ) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }

    public UserVO getUserById(Long userId) {
        User user = requireUserEntity(userId);
        List<String> roles = getRoleNames(userId);
        return UserVO.from(user, roles);
    }

    @Transactional
    public UserVO updateProfile(Long userId, UserProfileRequest request) {
        User user = requireUserEntity(userId);
        assertEnabled(user);

        if (StringUtils.hasText(request.getNickname())) {
            user.setNickname(request.getNickname().trim());
        }

        if (request.getEmail() != null) {
            String email = StringUtils.hasText(request.getEmail())
                    ? request.getEmail().trim()
                    : null;

            if (StringUtils.hasText(email)) {
                User sameEmailUser = userMapper.selectByEmail(email);
                if (sameEmailUser != null && !Objects.equals(sameEmailUser.getUserId(), userId)) {
                    throw new BusinessException(ResultCode.EMAIL_EXISTS);
                }
            }

            user.setEmail(email);
        }

        if (request.getAvatarUrl() != null) {
            String avatarUrl = StringUtils.hasText(request.getAvatarUrl())
                    ? request.getAvatarUrl().trim()
                    : null;
            user.setAvatarUrl(avatarUrl);
        }

        userMapper.updateProfile(user);
        return getUserById(userId);
    }

    @Transactional
    public UserVO applyAuthor(Long userId) {
        User user = requireUserEntity(userId);
        assertEnabled(user);

        userRoleMapper.insertIgnore(userId, RoleConstants.AUTHOR_ID);
        return getUserById(userId);
    }

    public List<String> getRoleNames(Long userId) {
        if (userId == null) {
            return List.of();
        }

        List<String> roleNames = roleMapper.selectRoleNamesByUserId(userId);
        return roleNames == null ? List.of() : roleNames;
    }

    public boolean hasRole(Long userId, String roleName) {
        if (userId == null || !StringUtils.hasText(roleName)) {
            return false;
        }

        return getRoleNames(userId).contains(roleName);
    }

    @Transactional
    public void assignRole(Long userId, Short roleId) {
        User user = requireUserEntity(userId);
        assertEnabled(user);

        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "角色不存在");
        }

        userRoleMapper.insertIgnore(userId, roleId);
    }

    @Transactional
    public void removeRole(Long userId, Short roleId) {
        requireUserEntity(userId);
        userRoleMapper.deleteByUserIdAndRoleId(userId, roleId);
    }

    private User requireUserEntity(Long userId) {
        if (userId == null) {
            throw new BusinessException(ResultCode.LOGIN_REQUIRED);
        }

        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        return user;
    }

    private void assertEnabled(User user) {
        if (user != null && Integer.valueOf(0).equals(user.getStatus())) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
    }
}