package com.example.storyworkshop.module.user.vo;

import com.example.storyworkshop.module.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class UserVO {
    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String avatarUrl;
    private Integer status;
    private String statusName;
    private List<String> roles;
    private LocalDateTime createTime;

    public static UserVO from(User user, List<String> roles) {
        if (user == null) {
            return null;
        }

        UserVO userVO = new UserVO();
        userVO.setUserId(user.getUserId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setEmail(user.getEmail());
        userVO.setAvatarUrl(user.getAvatarUrl());
        userVO.setStatus(user.getStatus());
        userVO.setStatusName(resolveStatusName(user.getStatus()));
        userVO.setRoles(roles == null ? List.of() : roles);
        userVO.setCreateTime(user.getCreateTime());
        return userVO;
    }

    private static String resolveStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }

        return status == 1 ? "正常" : "禁用";
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
