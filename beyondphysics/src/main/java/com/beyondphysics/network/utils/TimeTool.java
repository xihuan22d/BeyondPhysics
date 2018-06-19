package com.beyondphysics.network.utils;


/**
 * Created by xihuan22 on 2017/8/1.
 */
public class TimeTool {

    private static long oldTime = -1;

    private static int count = 0;

    /**
     * 获得时间戳,唯一
     */
    public static synchronized long getOnlyTime() {
        long time = System.currentTimeMillis();
        if (time == oldTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time = System.currentTimeMillis();
        }
        oldTime = time;
        return time;
    }

    /**
     * 获得一个包含了时间戳的唯一值,时间戳会被放大100000倍,复用view内建议使用该方法获取唯一值以减少睡眠时间过大导致的卡顿
     * long是19位,时间戳是13位,所以最大可以附加5个0
     */
    public static synchronized long getOnlyTimeWithoutSleep() {
        int ratio = 100000;
        if (count < ratio - 1) {
            count = count + 1;
        } else {
            count = 0;
            try {
                Thread.sleep(1);//其实即使设置为1,在非主线程中可能会消耗10ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long time = System.currentTimeMillis();
        time = time * ratio + count;
        return time;
    }
}
