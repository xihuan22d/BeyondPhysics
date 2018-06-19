package com.my.models;


public class Praise extends BaseModel {
    public static final String type_wallpaper = "wallpaper";
    public static final String type_comment = "comment";


    public static final String status_unPraise = "未赞";
    public static final String status_praiseUp = "赞";
    public static final String status_praiseDown = "倒赞";

    public static final String PRAISEUPORDOWN_PRAISEUP = "praiseUp";
    public static final String PRAISEUPORDOWN_PRAISEDOWN = "praiseDown";

    private String type;
    private String targetObject_id;
    private String user_id;
    private String status;
    private long createTime;
    private long lastUpdateTime;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetObject_id() {
        return targetObject_id;
    }

    public void setTargetObject_id(String targetObject_id) {
        this.targetObject_id = targetObject_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "Praise{" +
                "type='" + type + '\'' +
                ", targetObject_id='" + targetObject_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
