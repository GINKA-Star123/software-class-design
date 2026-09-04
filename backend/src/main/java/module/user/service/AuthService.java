package com.example.storyworkshop.module.user.service;

import com.example.storyworkshop.common.constant.AppConstants;
import com.example.storyworkshop.common.constant.RoleConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.user.dto.LoginRequest;
import com.example.storyworkshop.module.user.dto.RegisterRequest;
import com.example.storyworkshop.module.user.entity.User;
import com.example.storyworkshop.module.user.mapper.RoleMapper;
import com.example.storyworkshop.module.user.mapper.UserMapper;
import com.example.storyworkshop.module.user.mapper.UserRoleMapper;
import com.example.storyworkshop.module.user.vo.UserVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserMapper userMapper,
            RoleMapper roleMapper,
            UserRoleMapper userRoleMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserVO register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String rawPassword = request.getPassword();
        String nickname = StringUtils.hasText(request.getNickname())
                ? request.getNickname().trim()
                : username;
        String email = StringUtils.hasText(request.getEmail())
                ? request.getEmail().trim()
                : null;

        if (userMapper.countByUsername(username) > 0) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }

        if (StringUtils.hasText(email) && userMapper.countByEmail(email) > 0) {
            throw new BusinessException(ResultCode.EMAIL_EXISTS);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setNickname(nickname);
        user.setEmail(email);
        user.setAvatarUrl(null);
        user.setStatus(1);

        userMapper.insert(user);

        userRoleMapper.insertIgnore(user.getUserId(), RoleConstants.PLAYER_ID);

        return buildUserVO(user.getUserId());
    }

    public UserVO login(LoginRequest request, HttpSession session) {
        String username = request.getUsername().trim();
        User user = userMapper.selectByUsername(username);

        if (user == null) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        if (Integer.valueOf(0).equals(user.getStatus())) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        if (!matchesPassword(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        UserVO userVO = buildUserVO(user.getUserId());
        storeLoginUser(session, userVO);
        return userVO;
    }

    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    public UserVO getCurrentUser(HttpSession session) {
        Long userId = requireLoginUserId(session);
        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (Integer.valueOf(0).equals(user.getStatus())) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        return buildUserVO(userId);
    }

    public Long requireLoginUserId(HttpSession session) {
        if (session == null) {
            throw new BusinessException(ResultCode.LOGIN_REQUIRED);
        }

        Object userIdValue = session.getAttribute(AppConstants.SESSION_USER_ID);

        if (userIdValue instanceof Long userId) {
            return userId;
        }

        if (userIdValue instanceof Number number) {
            return number.longValue();
        }

        throw new BusinessException(ResultCode.LOGIN_REQUIRED);
    }

    public List<String> getSessionRoles(HttpSession session) {
        if (session == null) {
            return List.of();
        }

        Object rolesValue = session.getAttribute(AppConstants.SESSION_ROLES);

        if (rolesValue instanceof List<?> roles) {
            return roles.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
        }

        return List.of();
    }

    public void storeLoginUser(HttpSession session, UserVO userVO) {
        if (session == null || userVO == null) {
            return;
        }

        List<String> roles = userVO.getRoles() == null ? List.of() : userVO.getRoles();

        session.setAttribute(AppConstants.SESSION_USER_ID, userVO.getUserId());
        session.setAttribute(AppConstants.SESSION_USERNAME, userVO.getUsername());
        session.setAttribute(AppConstants.SESSION_ROLES, new ArrayList<>(roles));
    }

    private UserVO buildUserVO(Long userId) {
        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        List<String> roles = roleMapper.selectRoleNamesByUserId(userId);
        return UserVO.from(user, roles);
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        if (!StringUtils.hasText(rawPassword) || !StringUtils.hasText(encodedPassword)) {
            return false;
        }

        try {
            return passwordEncoder.matches(rawPassword, encodedPassword);
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}