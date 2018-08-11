package com.my.models;

public class User extends BaseModel {
    public static final String sex_unKnow = "sex_unKnow";
    public static final String sex_girl = "sex_girl";
    public static final String sex_boy = "sex_boy";

    public static final String UNKNOW_TIPS = "未知";
    public static final String GIRL_TIPS = "女";
    public static final String BOY_TIPS = "男";

    public static final String UPDATETYPE_USERNAME = "userName";
    public static final String UPDATETYPE_PASSWORD = "password";
    public static final String UPDATETYPE_SEX = "sex";
    public static final String UPDATETYPE_BIRTHDAY = "birthday";
    public static final String UPDATETYPE_SIGNATURE = "signature";

    private String userName;
    private String phoneNumber;
    private String email;
    private String password;
    private String sex;
    private String birthday;
    private String signature;
    private long gold;
    private long userLevel;
    private long userExp;
    private String avatarUrl_absolute;
    private String avatarUrl_relative;
    private long registTime;
    private String registMachineInformation;
    private String token;
    private long lastLoginTime;
    private String lastLoginIp;
    private String lastLoginMachineInformation;
    private long lastUploadTime;
    private long followCount;
    private long fansCount;
    private long followSort;
    private long fansSort;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public long getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(long userLevel) {
        this.userLevel = userLevel;
    }

    public long getUserExp() {
        return userExp;
    }

    public void setUserExp(long userExp) {
        this.userExp = userExp;
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

    public long getRegistTime() {
        return registTime;
    }

    public void setRegistTime(long registTime) {
        this.registTime = registTime;
    }

    public String getRegistMachineInformation() {
        return registMachineInformation;
    }

    public void setRegistMachineInformation(String registMachineInformation) {
        this.registMachineInformation = registMachineInformation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getLastLoginMachineInformation() {
        return lastLoginMachineInformation;
    }

    public void setLastLoginMachineInformation(String lastLoginMachineInformation) {
        this.lastLoginMachineInformation = lastLoginMachineInformation;
    }

    public long getLastUploadTime() {
        return lastUploadTime;
    }

    public void setLastUploadTime(long lastUploadTime) {
        this.lastUploadTime = lastUploadTime;
    }

    public long getFollowCount() {
        return followCount;
    }

    public void setFollowCount(long followCount) {
        this.followCount = followCount;
    }

    public long getFansCount() {
        return fansCount;
    }

    public void setFansCount(long fansCount) {
        this.fansCount = fansCount;
    }

    public long getFollowSort() {
        return followSort;
    }

    public void setFollowSort(long followSort) {
        this.followSort = followSort;
    }

    public long getFansSort() {
        return fansSort;
    }

    public void setFansSort(long fansSort) {
        this.fansSort = fansSort;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", signature='" + signature + '\'' +
                ", gold=" + gold +
                ", userLevel=" + userLevel +
                ", userExp=" + userExp +
                ", avatarUrl_absolute='" + avatarUrl_absolute + '\'' +
                ", avatarUrl_relative='" + avatarUrl_relative + '\'' +
                ", registTime=" + registTime +
                ", registMachineInformation='" + registMachineInformation + '\'' +
                ", token='" + token + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", lastLoginMachineInformation='" + lastLoginMachineInformation + '\'' +
                ", lastUploadTime=" + lastUploadTime +
                ", followCount=" + followCount +
                ", fansCount=" + fansCount +
                ", followSort=" + followSort +
                ", fansSort=" + fansSort +
                '}';
    }
}

