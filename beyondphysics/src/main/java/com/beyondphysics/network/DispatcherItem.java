package com.beyondphysics.network;

import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.TimeTool;

/**
 * Created by xihuan22 on 2017/9/20.
 */
public class DispatcherItem implements Comparable<DispatcherItem> {

    public static final String CANCELREQUESTWITHSUPERKEYTODISPATCHER_TAG = "cancelRequestWithSuperKeyToDispatcher_tag";

    public static final String CANCELREQUESTWITHTAGTODISPATCHER_TAG = "cancelRequestWithTagToDispatcher_tag";

    public static final String CANCELALLREQUESTTODISPATCHER_TAG = "cancelAllRequestToDispatcher_tag";


    public static final int WAITING = 1;

    public static final int RUNNING = 2;

    public static final int FINISH = 3;


    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_ADD_WITHSORT = 2;
    public static final int BITMAPREQUEST_ADD_DISKCACHE = 3;
    public static final int BITMAPREQUEST_ADD_DISKCACHE_WITHSORT = 4;
    public static final int BITMAPREQUEST_ADD_NETWORK = 5;
    public static final int BITMAPREQUEST_ADD_NETWORK_WITHSORT = 6;
    public static final int REQUEST_CANCEL = 7;
    public static final int BITMAPREQUEST_CANCEL = 8;
    public static final int REQUEST_CANCELWITHTAG = 9;
    public static final int REQUEST_CANCELALL = 10;
    public static final int DOCACHEITEMS = 11;

    /**
     * 用于框架内控制,外界只可读取不该修改
     */
    private int runningStatus = WAITING;


    private final int type;
    /**
     * NotNull
     */
    private final SuperKey superKey;
    private final Object object;
    private final long time;

    private final int priority;

    public DispatcherItem(int type, SuperKey superKey, Object object) {
        this.type = type;
        if (superKey == null) {
            throw new NullPointerException("superKey为null");
        } else {
            this.superKey = superKey;
        }
        this.object = object;
        time = TimeTool.getOnlyTimeWithoutSleep();
        priority = superKey.getPriority();
    }

    public DispatcherItem(int type, SuperKey superKey, Object object, int priority) {
        this.type = type;
        if (superKey == null) {
            throw new NullPointerException("superKey为null");
        } else {
            this.superKey = superKey;
        }
        this.object = object;
        time = TimeTool.getOnlyTimeWithoutSleep();
        this.priority = priority;
    }

    @Override
    public int compareTo(DispatcherItem dispatcherItem) {
        int result = 0;
        if (dispatcherItem == null) {
            return result;
        }
        if (this.priority == dispatcherItem.priority) {
            if (this.time != dispatcherItem.time) {
                result = this.time > dispatcherItem.time ? 1 : -1;
            }
        } else {
            result = this.priority > dispatcherItem.priority ? -1 : 1;
        }
        return result;
    }


    public int getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(int runningStatus) {
        if (this.runningStatus == FINISH) {
            FileTool.needShowAndWriteLogToSdcard(RequestManager.openDebug, RequestManager.logFileName, "DispatcherItem_setRunningStatus:runningStatus为FINISH后不允许修改", null, 1);
            throw new NullPointerException("runningStatus为FINISH后不允许修改");
        }
        this.runningStatus = runningStatus;
    }

    public int getType() {
        return type;
    }

    public SuperKey getSuperKey() {
        return superKey;
    }

    public Object getObject() {
        return object;
    }

    public long getTime() {
        return time;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "DispatcherItem{" +
                "runningStatus=" + runningStatus +
                ", type=" + type +
                ", superKey=" + superKey +
                ", object=" + object +
                ", time=" + time +
                ", priority=" + priority +
                '}';
    }
}
