package com.my.models;


import java.util.List;

public class Comment extends BaseModel {
    public static final String type_wallpaper = "type_wallpaper";


    public static final String SORTTYPE_CHILDCOMMENTSORT = "childCommentSort";
    public static final String SORTTYPE_PRAISEUPSORT = "praiseUpSort";
    public static final String SORTTYPE_PRAISEDOWNSORT = "praiseDownSort";

    public static final String UPORDOWN_UP = "up";
    public static final String UPORDOWN_DOWN = "down";

    public static final String PARENTORCHILD_CHILD = "child";
    public static final String PARENTORCHILD_PARENT = "parent";

    private String type;
    private String targetObject_id;
    private String content;
    private String targetUser_id;
    private String user_id;
    private String parent_id;
    private String rootParent_id;
    private long floor;
    private long depth;
    private long createTime;
    private long childCommentCount;
    private long praiseUpCount;
    private long praiseDownCount;
    private long childCommentFloorCount;
    private long childCommentSort;
    private long praiseUpSort;
    private long praiseDownSort;

    private String userName;
    private String avatarUrl_absolute;
    private String avatarUrl_relative;
    private long userLevel;
    private String targetUserName;
    private String targetAvatarUrl_absolute;
    private String targetAvatarUrl_relative;
    private long targetUserLevel;
    private Praise praise;
    private Follow follow;
    private List<Comment> childComments;

    private boolean showChildComments;
    private boolean showMoreTips;
    private String moreTips;
    private boolean showHotMore;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getRootParent_id() {
        return rootParent_id;
    }

    public void setRootParent_id(String rootParent_id) {
        this.rootParent_id = rootParent_id;
    }

    public long getFloor() {
        return floor;
    }

    public void setFloor(long floor) {
        this.floor = floor;
    }

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getChildCommentCount() {
        return childCommentCount;
    }

    public void setChildCommentCount(long childCommentCount) {
        this.childCommentCount = childCommentCount;
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

    public long getChildCommentFloorCount() {
        return childCommentFloorCount;
    }

    public void setChildCommentFloorCount(long childCommentFloorCount) {
        this.childCommentFloorCount = childCommentFloorCount;
    }

    public long getChildCommentSort() {
        return childCommentSort;
    }

    public void setChildCommentSort(long childCommentSort) {
        this.childCommentSort = childCommentSort;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl_absolute() {
        return avatarUrl_absolute;
    }

    public void setAvatarUrl_absolute(String avatarUrl_absolute) {
        this.avatarUrl_absolute = avatarUrl_absolute;
    }

    public String getAvatarUrl_relative() {
        return avatarUrl_relative;
    }

    public void setAvatarUrl_relative(String avatarUrl_relative) {
        this.avatarUrl_relative = avatarUrl_relative;
    }

    public long getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(long userLevel) {
        this.userLevel = userLevel;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getTargetAvatarUrl_absolute() {
        return targetAvatarUrl_absolute;
    }

    public void setTargetAvatarUrl_absolute(String targetAvatarUrl_absolute) {
        this.targetAvatarUrl_absolute = targetAvatarUrl_absolute;
    }

    public String getTargetAvatarUrl_relative() {
        return targetAvatarUrl_relative;
    }

    public void setTargetAvatarUrl_relative(String targetAvatarUrl_relative) {
        this.targetAvatarUrl_relative = targetAvatarUrl_relative;
    }

    public long getTargetUserLevel() {
        return targetUserLevel;
    }

    public void setTargetUserLevel(long targetUserLevel) {
        this.targetUserLevel = targetUserLevel;
    }

    public Praise getPraise() {
        return praise;
    }

    public void setPraise(Praise praise) {
        this.praise = praise;
    }

    public Follow getFollow() {
        return follow;
    }

    public void setFollow(Follow follow) {
        this.follow = follow;
    }

    public List<Comment> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<Comment> childComments) {
        this.childComments = childComments;
    }

    public boolean isShowChildComments() {
        return showChildComments;
    }

    public void setShowChildComments(boolean showChildComments) {
        this.showChildComments = showChildComments;
    }

    public boolean isShowMoreTips() {
        return showMoreTips;
    }

    public void setShowMoreTips(boolean showMoreTips) {
        this.showMoreTips = showMoreTips;
    }

    public String getMoreTips() {
        return moreTips;
    }

    public void setMoreTips(String moreTips) {
        this.moreTips = moreTips;
    }

    public boolean isShowHotMore() {
        return showHotMore;
    }

    public void setShowHotMore(boolean showHotMore) {
        this.showHotMore = showHotMore;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "type='" + type + '\'' +
                ", targetObject_id='" + targetObject_id + '\'' +
                ", content='" + content + '\'' +
                ", targetUser_id='" + targetUser_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", rootParent_id='" + rootParent_id + '\'' +
                ", floor=" + floor +
                ", depth=" + depth +
                ", createTime=" + createTime +
                ", childCommentCount=" + childCommentCount +
                ", praiseUpCount=" + praiseUpCount +
                ", praiseDownCount=" + praiseDownCount +
                ", childCommentFloorCount=" + childCommentFloorCount +
                ", childCommentSort=" + childCommentSort +
                ", praiseUpSort=" + praiseUpSort +
                ", praiseDownSort=" + praiseDownSort +
                ", userName='" + userName + '\'' +
                ", avatarUrl_absolute='" + avatarUrl_absolute + '\'' +
                ", avatarUrl_relative='" + avatarUrl_relative + '\'' +
                ", userLevel=" + userLevel +
                ", targetUserName='" + targetUserName + '\'' +
                ", targetAvatarUrl_absolute='" + targetAvatarUrl_absolute + '\'' +
                ", targetAvatarUrl_relative='" + targetAvatarUrl_relative + '\'' +
                ", targetUserLevel=" + targetUserLevel +
                ", praise=" + praise +
                ", follow=" + follow +
                ", childComments=" + childComments +
                ", showChildComments=" + showChildComments +
                ", showMoreTips=" + showMoreTips +
                ", moreTips='" + moreTips + '\'' +
                ", showHotMore=" + showHotMore +
                '}';
    }
}
