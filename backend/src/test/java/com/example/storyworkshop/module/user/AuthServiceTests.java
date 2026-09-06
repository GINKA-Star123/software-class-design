package com.example.storyworkshop.module.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.storyworkshop.common.constant.AppConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.module.user.dto.LoginRequest;
import com.example.storyworkshop.module.user.dto.RegisterRequest;
import com.example.storyworkshop.module.user.entity.User;
import com.example.storyworkshop.module.user.mapper.RoleMapper;
import com.example.storyworkshop.module.user.mapper.UserMapper;
import com.example.storyworkshop.module.user.mapper.UserRoleMapper;
import com.example.storyworkshop.module.user.service.AuthService;
import com.example.storyworkshop.module.user.vo.UserVO;

import jakarta.servlet.http.HttpSession;

class AuthServiceTests {

    private UserMapper userMapper;
    private RoleMapper roleMapper;
    private UserRoleMapper userRoleMapper;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        roleMapper = mock(RoleMapper.class);
        userRoleMapper = mock(UserRoleMapper.class);
        authService = new AuthService(userMapper, roleMapper, userRoleMapper, new BCryptPasswordEncoder());
    }

    private User user(Long id) {
        User u = new User();
        u.setUserId(id);
        u.setUsername("tester");
        u.setPassword("$2a$10$abcdefghijklmnopqrstuv");
        u.setNickname("测试");
        u.setEmail("t@example.com");
        u.setStatus(1);
        return u;
    }

    @Test
    void registerSuccess() {
        when(userMapper.countByUsername("tester")).thenReturn(0);
        when(userMapper.countByEmail("t@example.com")).thenReturn(0);
        doAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setUserId(1L);
            return 1;
        }).when(userMapper).insert(any(User.class));
        when(userMapper.selectById(1L)).thenReturn(user(1L));
        when(roleMapper.selectRoleNamesByUserId(1L)).thenReturn(List.of("PLAYER"));

        RegisterRequest req = new RegisterRequest();
        req.setUsername("tester");
        req.setPassword("12345678");
        req.setNickname("测试");
        req.setEmail("t@example.com");
        UserVO vo = authService.register(req);
        assertEquals("tester", vo.getUsername());
        verify(userRoleMapper).insertIgnore(1L, (short) 1);
    }

    @Test
    void registerDuplicateUsername() {
        when(userMapper.countByUsername("tester")).thenReturn(1);
        RegisterRequest req = new RegisterRequest();
        req.setUsername("tester");
        req.setPassword("12345678");
        assertThrows(BusinessException.class, () -> authService.register(req));
    }

    @Test
    void loginWrongPasswordRejected() {
        User u = user(1L);
        u.setPassword("$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5e6vLhG9x5u5Q4R8qV5pY1bZ1k1W2");
        when(userMapper.selectByUsername("tester")).thenReturn(u);
        LoginRequest req = new LoginRequest();
        req.setUsername("tester");
        req.setPassword("wrong-password");
        assertThrows(BusinessException.class, () -> authService.login(req, mock(HttpSession.class)));
    }

    @Test
    void loginSuccessStoresSession() {
        User u = user(1L);
        u.setPassword(new BCryptPasswordEncoder().encode("12345678"));
        when(userMapper.selectByUsername("tester")).thenReturn(u);
        when(userMapper.selectById(1L)).thenReturn(u);
        when(roleMapper.selectRoleNamesByUserId(1L)).thenReturn(List.of("PLAYER"));
        HttpSession session = mock(HttpSession.class);
        LoginRequest req = new LoginRequest();
        req.setUsername("tester");
        req.setPassword("12345678");
        UserVO vo = authService.login(req, session);
        assertEquals("tester", vo.getUsername());
        verify(session).setAttribute(AppConstants.SESSION_USER_ID, 1L);
    }
}
