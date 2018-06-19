package com.beyondphysics.network;

import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.TimeTool;

/**
 * Created by xihuan22 on 2017/9/20.
 */
public class RequestStatusItem {
    public static final int STATUS_PAUSE = 1;
    public static final int STATUS_CANCELING = 2;//主要用在cancelBitmapRequest的wait方法里面阻止BaseBitmapNetworkThread在相同url的情况下移除相似请求并加入到BaseDiskCacheThread,非wait方法意义不大

    public static final int KIND_NORMAL_REQUEST = 1;
    public static final int KIND_BITMAP_REQUEST = 2;
    public static final int KIND_ALL_REQUEST = 3;

    public static final int TYPE_SUPERKEY = 1;
    public static final int TYPE_TAG = 2;
    public static final int TYPE_ALL = 3;

    private final int status;
    private final int kind;
    private final int type;
    private final SuperKey superKey;
    private final String tag;
    private final long time;

    public RequestStatusItem(int status, int kind, int type, SuperKey superKey, String tag) {
        this.status = status;
        this.kind = kind;
        this.type = type;
        this.superKey = superKey;
        this.tag = tag;
        time = TimeTool.getOnlyTimeWithoutSleep();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestStatusItem that = (RequestStatusItem) o;

        if (status != that.status) return false;
        if (kind != that.kind) return false;
        if (type != that.type) return false;
        if (time != that.time) return false;
        if (superKey != null ? !superKey.equals(that.superKey) : that.superKey != null)
            return false;
        return tag != null ? tag.equals(that.tag) : that.tag == null;
    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + kind;
        result = 31 * result + type;
        result = 31 * result + (superKey != null ? superKey.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }

    public int getStatus() {
        return status;
    }

    public int getKind() {
        return kind;
    }

    public int getType() {
        return type;
    }

    public SuperKey getSuperKey() {
        return superKey;
    }

    public String getTag() {
        return tag;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "RequestStatusItem{" +
                "status=" + status +
                ", kind=" + kind +
                ", type=" + type +
                ", superKey=" + superKey +
                ", tag='" + tag + '\'' +
                ", time=" + time +
                '}';
    }
}
