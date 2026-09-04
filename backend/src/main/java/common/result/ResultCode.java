package com.example.storyworkshop.common.result;

public enum ResultCode {
    SUCCESS(200, "操作成功"),
    CREATED(201, "创建成功"),

    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "请先登录"),
    FORBIDDEN(403, "没有操作权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    VALIDATION_ERROR(422, "参数校验失败"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),

    INTERNAL_ERROR(500, "系统内部错误"),
    JSON_PROCESS_ERROR(5001, "JSON 处理失败"),

    BUSINESS_ERROR(1000, "业务处理失败"),

    USER_NOT_FOUND(2001, "用户不存在"),
    USER_DISABLED(2002, "用户已被禁用"),
    USERNAME_EXISTS(2003, "用户名已存在"),
    EMAIL_EXISTS(2004, "邮箱已存在"),
    PASSWORD_ERROR(2005, "用户名或密码错误"),
    LOGIN_REQUIRED(2006, "请先登录"),

    STORY_NOT_FOUND(3001, "故事不存在"),
    STORY_NOT_PUBLISHED(3002, "故事未发布"),
    STORY_STATUS_ERROR(3003, "故事状态不允许当前操作"),
    STORY_VALIDATION_FAILED(3004, "故事完整性校验失败"),
    STORY_NODE_NOT_FOUND(3005, "故事节点不存在"),
    STORY_CHOICE_NOT_FOUND(3006, "故事选项不存在"),

    PROGRESS_NOT_FOUND(4001, "游戏进度不存在"),
    PROGRESS_LIMIT_EXCEEDED(4002, "同一故事最多保存 3 条进度"),
    CHOICE_CONDITION_NOT_MATCH(4003, "当前选项条件不满足"),

    COMMENT_NOT_FOUND(5001, "评论不存在"),
    REPORT_NOT_FOUND(5002, "举报记录不存在"),
    DUPLICATE_OPERATION(5003, "请勿重复操作"),

    ACHIEVEMENT_NOT_FOUND(6001, "成就不存在");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultCode fromCode(Integer code) {
        if (code == null) {
            return BUSINESS_ERROR;
        }

        for (ResultCode resultCode : values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }

        return BUSINESS_ERROR;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}