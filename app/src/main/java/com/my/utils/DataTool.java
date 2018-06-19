package com.my.utils;


public class DataTool {
    private static final int seconds_of_1minute = 60;

    private static final int seconds_of_30minutes = 30 * 60;

    private static final int seconds_of_1hour = 60 * 60;

    private static final int seconds_of_1day = 24 * 60 * 60;

    private static final int seconds_of_15days = seconds_of_1day * 15;

    private static final int seconds_of_30days = seconds_of_1day * 30;

    private static final int seconds_of_6months = seconds_of_30days * 6;

    private static final int seconds_of_1year = seconds_of_30days * 12;


    public static String getTimeRange(long myTime) {
        long between = (System.currentTimeMillis() - myTime) / 1000;
        if (between < seconds_of_1minute) {
            return "刚刚";
        }
        if (between < seconds_of_30minutes) {
            return between / seconds_of_1minute + "分钟前";
        }
        if (between < seconds_of_1hour) {
            return "半小时前";
        }
        if (between < seconds_of_1day) {
            return between / seconds_of_1hour + "小时前";
        }
        if (between < seconds_of_15days) {
            return between / seconds_of_1day + "天前";
        }
        if (between < seconds_of_30days) {
            return "半个月前";
        }
        if (between < seconds_of_6months) {
            return between / seconds_of_30days + "月前";
        }
        if (between < seconds_of_1year) {
            return "半年前";
        }
        if (between >= seconds_of_1year) {
            return between / seconds_of_1year + "年前";
        }
        return "";
    }

}
