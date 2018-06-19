package com.my.models;


public class Follow extends BaseModel {
    public static final String status_unFollow = "未关注";
    public static final String status_follow = "已关注";

    public static final String UNFOLLOW_TIPS = "+关注";
    public static final String FOLLOW_TIPS = "已关注";

    private String targetUser_id;
    private String user_id;
    private String status;
    private long createTime;
    private long lastUpdateTime;


    public String getTargetUser_id() {
        return targetUser_id;
    }

    public void setTargetUser_id(String targetUser_id) {
        this.targetUser_id = targetUser_id;
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
        return "Follow{" +
                "targetUser_id='" + targetUser_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
