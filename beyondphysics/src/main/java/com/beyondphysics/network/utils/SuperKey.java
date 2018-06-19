package com.beyondphysics.network.utils;

/**
 * Created by xihuan22 on 2017/8/18.
 */

public class SuperKey {
    public static final String DEFAULT_KEY = "default_key";

    public static final String DEFAULT_TAG = "default_tag";
    /**
     * NotNull
     */
    private final String key;
    /**
     * NotNull
     */
    private final String tag;
    private final int priority;
    private final int sortType;
    private long time;
    /**
     * 排序值由priority和time组成,动态添加移除后使得LinkedHashMap重新有序,线程可直接获得并弹出栈顶数据
     */
    private double sort;

    public SuperKey(String key, String tag, int priority, int sortType, long time) {
        if (key == null) {
            key = DEFAULT_KEY;
        }
        this.key = key;
        if (tag == null) {
            tag = DEFAULT_TAG;
        }
        this.tag = tag;
        this.priority = priority;
        this.sortType = sortType;
        this.time = time;
        if (sortType == 1) {
            sort = this.priority * Math.pow(10, 19) - this.time;
        } else if (sortType == 2) {
            sort = this.priority * Math.pow(10, 19) + this.time;
        } else {
            sort = this.priority * Math.pow(10, 19);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuperKey superKey = (SuperKey) o;

        if (priority != superKey.priority) return false;
        if (sortType != superKey.sortType) return false;
        if (time != superKey.time) return false;
        if (Double.compare(superKey.sort, sort) != 0) return false;
        if (!key.equals(superKey.key)) return false;
        return tag.equals(superKey.tag);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = key.hashCode();
        result = 31 * result + tag.hashCode();
        result = 31 * result + priority;
        result = 31 * result + sortType;
        result = 31 * result + (int) (time ^ (time >>> 32));
        temp = Double.doubleToLongBits(sort);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String getKey() {
        return key;
    }

    public String getTag() {
        return tag;
    }

    public int getPriority() {
        return priority;
    }

    public int getSortType() {
        return sortType;
    }

    public long getTime() {
        synchronized (this) {
            return time;
        }
    }

    public void setTime(long time) {
        synchronized (this) {
            this.time = time;
        }
    }

    public double getSort() {
        synchronized (this) {
            return sort;
        }
    }

    public void setSort(double sort) {
        synchronized (this) {
            this.sort = sort;
        }
    }

    public void setTimeAndSort(long time) {
        synchronized (this) {
            if (sortType == 1) {
                this.time = time;
                sort = this.priority * Math.pow(10, 19) - this.time;
            } else if (sortType == 2) {
                this.time = time;
                sort = this.priority * Math.pow(10, 19) + this.time;
            } else {
                //不修改,这样可以保证执行BreakpointDownloadRequest请求存入的相同的SuperKey可以被替换
            }
        }
    }

    @Override
    public String toString() {
        return "SuperKey{" +
                "key='" + key + '\'' +
                ", tag='" + tag + '\'' +
                ", priority=" + priority +
                ", time=" + time +
                ", sort=" + sort +
                '}';
    }
}
