package com.my.models;


import com.beyondphysics.network.BreakpointDownloadRequest;

public class Wallpaper extends BaseModel {
    public static final String kind_video = "video";//来自服务器的字段内容常量与服务器中命名方式保持一致
    public static final String kind_image = "image";
    public static final String kind_all = "all";

    public static final String auditStatus_unDo = "未审核";
    public static final String auditStatus_unPass = "未通过";
    public static final String auditStatus_pass = "已通过";


    public static final String SORTTYPE_VISITSORT = "visitSort";
    public static final String SORTTYPE_DOWNLOADSORT = "downloadSort";
    public static final String SORTTYPE_COLLECTIONSORT = "collectionSort";
    public static final String SORTTYPE_COMMENTSORT = "commentSort";

    public static final String UPORDOWN_UP = "up";
    public static final String UPORDOWN_DOWN = "down";


    public static final int UNDOWNLOADING = 0;
    public static final int DOWNLOADING = 1;
    public static final int DOWNLOADSUCCESS = 2;

    private String kind;
    private String name;
    private String feature;
    private String describe;
    private long needGold;
    private double previewImageWidth;
    private double previewImageHeight;
    private double previewImageScale;
    private String previewImageUrl_absolute;
    private String previewImageUrl_relative;
    private String wallpaperUrl_absolute;
    private String wallpaperUrl_relative;
    private String wallpaperMd5;
    private long size;
    private String wallpaperType_id;
    private String uploadUser_id;
    private String uploadUser_userName;
    private String uploadUser_avatarUrl_absolute;
    private String uploadUser_avatarUrl_relative;
    private long createTime;
    private long visitCount;
    private long downloadCount;
    private long collectionCount;
    private long commentCount;
    private long commentFloorCount;
    private long praiseUpCount;
    private long praiseDownCount;
    private long visitSort;
    private long downloadSort;
    private long collectionSort;
    private long commentSort;
    private long praiseUpSort;
    private long praiseDownSort;
    private String auditStatus;

    private int downloadingStatus;
    private int currentSize;
    private int totalSize;
    private BreakpointDownloadRequest<?> breakpointDownloadRequest;


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public long getNeedGold() {
        return needGold;
    }

    public void setNeedGold(long needGold) {
        this.needGold = needGold;
    }

    public double getPreviewImageWidth() {
        return previewImageWidth;
    }

    public void setPreviewImageWidth(double previewImageWidth) {
        this.previewImageWidth = previewImageWidth;
    }

    public double getPreviewImageHeight() {
        return previewImageHeight;
    }

    public void setPreviewImageHeight(double previewImageHeight) {
        this.previewImageHeight = previewImageHeight;
    }

    public double getPreviewImageScale() {
        return previewImageScale;
    }

    public void setPreviewImageScale(double previewImageScale) {
        this.previewImageScale = previewImageScale;
    }

    public String getPreviewImageUrl_absolute() {
        return previewImageUrl_absolute;
    }

    public void setPreviewImageUrl_absolute(String previewImageUrl_absolute) {
        this.previewImageUrl_absolute = previewImageUrl_absolute;
    }

    public String getPreviewImageUrl_relative() {
        return previewImageUrl_relative;
    }

    public void setPreviewImageUrl_relative(String previewImageUrl_relative) {
        this.previewImageUrl_relative = previewImageUrl_relative;
    }

    public String getWallpaperUrl_absolute() {
        return wallpaperUrl_absolute;
    }

    public void setWallpaperUrl_absolute(String wallpaperUrl_absolute) {
        this.wallpaperUrl_absolute = wallpaperUrl_absolute;
    }

    public String getWallpaperUrl_relative() {
        return wallpaperUrl_relative;
    }

    public void setWallpaperUrl_relative(String wallpaperUrl_relative) {
        this.wallpaperUrl_relative = wallpaperUrl_relative;
    }

    public String getWallpaperMd5() {
        return wallpaperMd5;
    }

    public void setWallpaperMd5(String wallpaperMd5) {
        this.wallpaperMd5 = wallpaperMd5;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getWallpaperType_id() {
        return wallpaperType_id;
    }

    public void setWallpaperType_id(String wallpaperType_id) {
        this.wallpaperType_id = wallpaperType_id;
    }

    public String getUploadUser_id() {
        return uploadUser_id;
    }

    public void setUploadUser_id(String uploadUser_id) {
        this.uploadUser_id = uploadUser_id;
    }

    public String getUploadUser_userName() {
        return uploadUser_userName;
    }

    public void setUploadUser_userName(String uploadUser_userName) {
        this.uploadUser_userName = uploadUser_userName;
    }

    public String getUploadUser_avatarUrl_absolute() {
        return uploadUser_avatarUrl_absolute;
    }

    public void setUploadUser_avatarUrl_absolute(String uploadUser_avatarUrl_absolute) {
        this.uploadUser_avatarUrl_absolute = uploadUser_avatarUrl_absolute;
    }

    public String getUploadUser_avatarUrl_relative() {
        return uploadUser_avatarUrl_relative;
    }

    public void setUploadUser_avatarUrl_relative(String uploadUser_avatarUrl_relative) {
        this.uploadUser_avatarUrl_relative = uploadUser_avatarUrl_relative;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(long visitCount) {
        this.visitCount = visitCount;
    }

    public long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public long getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(long collectionCount) {
        this.collectionCount = collectionCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public long getCommentFloorCount() {
        return commentFloorCount;
    }

    public void setCommentFloorCount(long commentFloorCount) {
        this.commentFloorCount = commentFloorCount;
    }

    public long getPraiseUpCount() {
        return praiseUpCount;
    }

    public void setPraiseUpCount(long praiseUpCount) {
        this.praiseUpCount = praiseUpCount;
    }

    public long getPraiseDownCount() {
        return praiseDownCount;
    }

    public void setPraiseDownCount(long praiseDownCount) {
        this.praiseDownCount = praiseDownCount;
    }

    public long getVisitSort() {
        return visitSort;
    }

    public void setVisitSort(long visitSort) {
        this.visitSort = visitSort;
    }

    public long getDownloadSort() {
        return downloadSort;
    }

    public void setDownloadSort(long downloadSort) {
        this.downloadSort = downloadSort;
    }

    public long getCollectionSort() {
        return collectionSort;
    }

    public void setCollectionSort(long collectionSort) {
        this.collectionSort = collectionSort;
    }

    public long getCommentSort() {
        return commentSort;
    }

    public void setCommentSort(long commentSort) {
        this.commentSort = commentSort;
    }

    public long getPraiseUpSort() {
        return praiseUpSort;
    }

    public void setPraiseUpSort(long praiseUpSort) {
        this.praiseUpSort = praiseUpSort;
    }

    public long getPraiseDownSort() {
        return praiseDownSort;
    }

    public void setPraiseDownSort(long praiseDownSort) {
        this.praiseDownSort = praiseDownSort;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public int getDownloadingStatus() {
        return downloadingStatus;
    }

    public void setDownloadingStatus(int downloadingStatus) {
        this.downloadingStatus = downloadingStatus;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public BreakpointDownloadRequest<?> getBreakpointDownloadRequest() {
        return breakpointDownloadRequest;
    }

    public void setBreakpointDownloadRequest(BreakpointDownloadRequest<?> breakpointDownloadRequest) {
        this.breakpointDownloadRequest = breakpointDownloadRequest;
    }

    @Override
    public String toString() {
        return "Wallpaper{" +
                "kind='" + kind + '\'' +
                ", name='" + name + '\'' +
                ", feature='" + feature + '\'' +
                ", describe='" + describe + '\'' +
                ", needGold=" + needGold +
                ", previewImageWidth=" + previewImageWidth +
                ", previewImageHeight=" + previewImageHeight +
                ", previewImageScale=" + previewImageScale +
                ", previewImageUrl_absolute='" + previewImageUrl_absolute + '\'' +
                ", previewImageUrl_relative='" + previewImageUrl_relative + '\'' +
                ", wallpaperUrl_absolute='" + wallpaperUrl_absolute + '\'' +
                ", wallpaperUrl_relative='" + wallpaperUrl_relative + '\'' +
                ", wallpaperMd5='" + wallpaperMd5 + '\'' +
                ", size=" + size +
                ", wallpaperType_id='" + wallpaperType_id + '\'' +
                ", uploadUser_id='" + uploadUser_id + '\'' +
                ", uploadUser_userName='" + uploadUser_userName + '\'' +
                ", uploadUser_avatarUrl_absolute='" + uploadUser_avatarUrl_absolute + '\'' +
                ", uploadUser_avatarUrl_relative='" + uploadUser_avatarUrl_relative + '\'' +
                ", createTime=" + createTime +
                ", visitCount=" + visitCount +
                ", downloadCount=" + downloadCount +
                ", collectionCount=" + collectionCount +
                ", commentCount=" + commentCount +
                ", commentFloorCount=" + commentFloorCount +
                ", praiseUpCount=" + praiseUpCount +
                ", praiseDownCount=" + praiseDownCount +
                ", visitSort=" + visitSort +
                ", downloadSort=" + downloadSort +
                ", collectionSort=" + collectionSort +
                ", commentSort=" + commentSort +
                ", praiseUpSort=" + praiseUpSort +
                ", praiseDownSort=" + praiseDownSort +
                ", auditStatus='" + auditStatus + '\'' +
                ", downloadingStatus=" + downloadingStatus +
                ", currentSize=" + currentSize +
                ", totalSize=" + totalSize +
                ", breakpointDownloadRequest=" + breakpointDownloadRequest +
                '}';
    }
}
