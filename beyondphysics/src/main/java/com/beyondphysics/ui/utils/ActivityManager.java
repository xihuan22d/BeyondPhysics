package com.beyondphysics.ui.utils;


import com.beyondphysics.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xihuan22 on 2016/1/10.
 */
public class ActivityManager {
    /**
     * 传入的baseActivity的类和时间组成的唯一的key的分隔符
     */
    public static final String ACTIVITYKEYSEPARATOR = "&&";

    private static ActivityManager activityManager;

    private final HashMap<String, BaseActivity> hashMapBaseActivitys = new HashMap<String, BaseActivity>();

    private ActivityManager() {//只允许单列方式创建
    }

    /**
     * 已加方法锁####
     * 方法锁保证只会被初始化一次
     */
    public static synchronized ActivityManager getInstance() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    /**
     * 存入对应唯一key的baseActivity
     */
    public void addBaseActivity(String activityKey, BaseActivity baseActivity) {
        if (activityKey == null || baseActivity == null) {
            return;
        }
        synchronized (this) {
            hashMapBaseActivitys.put(activityKey, baseActivity);
        }
    }

    /**
     * 移除对应唯一key的baseActivity
     */
    public void removeBaseActivity(String activityKey) {
        if (activityKey == null) {
            return;
        }
        synchronized (this) {
            if (hashMapBaseActivitys.containsKey(activityKey)) {
                hashMapBaseActivitys.remove(activityKey);
            }
        }
    }

    /**
     * 获得对应唯一key的baseActivity
     */
    public BaseActivity getBaseActivity(String activityKey) {
        if (activityKey == null) {
            return null;
        }
        synchronized (this) {
            if (hashMapBaseActivitys.containsKey(activityKey)) {
                return hashMapBaseActivitys.get(activityKey);
            } else {
                return null;
            }
        }
    }

    /**
     * 获得所有baseActivity
     */
    public ArrayList<BaseActivity> getAllBaseActivitys() {
        synchronized (this) {
            ArrayList<BaseActivity> arrayListBaseActivitys = new ArrayList<BaseActivity>();
            Iterator<Map.Entry<String, BaseActivity>> iterator = hashMapBaseActivitys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseActivity> entry = iterator.next();
                BaseActivity baseActivity = entry.getValue();
                arrayListBaseActivitys.add(baseActivity);
            }
            return arrayListBaseActivitys;
        }
    }

    /**
     * 获得所有的包含className的Activity数据,arraylist里面数组一定是长度为3不为null的数组
     * 从hashMap获取的ArrayList数据在声明时候也写成ArrayList,而不要写成List,这样可以和一般的服务器获取的数据区分开来,更加直观得知道它来自hashMap还是服务器
     */
    public ArrayList<Object[]> getTheActivitysByClassName(String className) {
        if (className == null) {
            return null;
        }
        synchronized (this) {
            ArrayList<Object[]> arrayListBaseActivitys = new ArrayList<Object[]>();
            Iterator<Map.Entry<String, BaseActivity>> iterator = hashMapBaseActivitys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseActivity> entry = iterator.next();//entry是不可能为null的
                String key = entry.getKey();
                BaseActivity baseActivity = entry.getValue();
                String[] ss = key.split(ACTIVITYKEYSEPARATOR);//add方法保证key和value不为null,外界写入key只能通过public方法是不允许直接操作hashMap的
                if (className.equals(ss[0])) {
                    Object[] object = new Object[3];
                    object[0] = key;
                    object[1] = baseActivity;
                    if (ss.length >= 2) {
                        object[2] = ss[1];//时间
                    } else {
                        object[2] = "-1";//时间
                    }
                    arrayListBaseActivitys.add(object);
                }
            }
            return arrayListBaseActivitys;
        }
    }

    /**
     * 关闭所有的activity
     **/
    public void killAllActivity() {
        synchronized (this) {
            Iterator<Map.Entry<String, BaseActivity>> iterator = hashMapBaseActivitys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseActivity> entry = iterator.next();
                BaseActivity baseActivity = entry.getValue();
                baseActivity.finish();//onDestory需要在本方法执行完后才会执行所以iterator.hasNext()不会出错,若把移除写在finish内就会出错,如果想在baseActivity的finish方法里面也加移除,就需要在iterator.hasNext()外面加个arrayList存放,然后再finish
            }
        }
    }

    /**
     * 关闭除指定activity以外的所有activity
     **/
    public void killAllActivityWithOutMe(String activityKey) {
        if (activityKey == null) {
            return;
        }
        synchronized (this) {
            Iterator<Map.Entry<String, BaseActivity>> iterator = hashMapBaseActivitys.entrySet().iterator();
            //     ArrayList<String> arrayListRemoves = new ArrayList<String>();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseActivity> entry = iterator.next();
                String key = entry.getKey();
                BaseActivity baseActivity = entry.getValue();
                if (!activityKey.equals(key)) {
                    baseActivity.finish();
                    //arrayListRemoves.add(key);
                }
            }
//        for (int i = 0; i < arrayListRemoves.size(); i++) {//OnDestory里面会移除
//            hashMapActivitys.remove(arrayListRemoves.get(i));
//        }
        }
    }

    /**
     * 关闭除指定activity们以外的所有activity
     **/
    public void killAllActivityWithOutWe(String[] activityKeys) {
        if (activityKeys == null) {
            return;
        }
        synchronized (this) {
            Iterator<Map.Entry<String, BaseActivity>> iterator = hashMapBaseActivitys.entrySet().iterator();
            //     ArrayList<String> arrayListRemoves = new ArrayList<String>();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseActivity> entry = iterator.next();
                String key = entry.getKey();
                BaseActivity baseActivity = entry.getValue();
                boolean notFinish = false;
                for (int i = 0; i < activityKeys.length; i++) {
                    if (activityKeys[i] != null) {
                        if (activityKeys[i].equals(key)) {
                            notFinish = true;
                            break;
                        }
                    }
                }
                if (!notFinish) {
                    baseActivity.finish();
                    //arrayListRemoves.add(key);
                }
            }
//        for (int i = 0; i < arrayListRemoves.size(); i++) {
//            hashMapActivitys.remove(arrayListRemoves.get(i));
//        }
        }
    }
}
