package com.example.storyworkshop.module.admin.dto;

import java.util.ArrayList;
import java.util.List;

public class RoleUpdateRequest {
    private java.util.List<Short> roleIds;

    public java.util.List<Short> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(java.util.List<Short> roleIds) {
        this.roleIds = roleIds;
    }

    public RoleUpdateRequest() {
        this.roleIds = new ArrayList<>();
    }
}
