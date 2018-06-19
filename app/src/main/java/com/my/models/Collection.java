package com.my.models;


public class Collection extends BaseModel {
    public static final String type_wallpaper = "wallpaper";
    public static final String type_comment = "comment";

    public static final String status_unCollection = "未收藏";
    public static final String status_collection = "已收藏";

    private String type;
    private String targetObject_id;
    private String user_id;
    private String status;
    private long createTime;
    private long lastUpdateTime;

    private Wallpaper wallpaper;


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

    public Wallpaper getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(Wallpaper wallpaper) {
        this.wallpaper = wallpaper;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "type='" + type + '\'' +
                ", targetObject_id='" + targetObject_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", wallpaper=" + wallpaper +
                '}';
    }
}
