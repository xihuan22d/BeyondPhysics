package com.beyondphysics.network.utils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xihuan22 on 2017/8/17.
 */

public class ThreadSafelyLinkedHasMap<T> {
    /**
     * 等待执行的请求,不要在外界直接操作,否则可能导致线程不安全
     */
    public final LinkedHashMap<SuperKey, T> linkedHashMapRequests = new LinkedHashMap<SuperKey, T>();

    public ThreadSafelyLinkedHasMap() {

    }

    /**
     * put方法保证linkedHashMapRequests内的superKey和object不为null
     */
    public void putRequest(SuperKey superKey, T object) {
        if (superKey == null || object == null) {
            return;
        }
        synchronized (this) {
            if (putEventBefore(superKey, object)) {
                linkedHashMapRequests.put(superKey, object);
                putEventAfter(superKey, object);
            }
        }
    }

    public T getRequest(SuperKey superKey) {
        if (superKey == null) {
            return null;
        }
        synchronized (this) {
            if (linkedHashMapRequests.containsKey(superKey)) {
                return linkedHashMapRequests.get(superKey);
            } else {
                return null;
            }
        }
    }

    /**
     * return List<T> NotNull , T NotNull
     */
    public List<T> getAllRequest() {
        synchronized (this) {
            List<T> objects = new ArrayList<T>();
            Iterator<Map.Entry<SuperKey, T>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, T> entry = iterator.next();
                T object = entry.getValue();
                objects.add(object);
            }
            return objects;
        }
    }

    public void removeRequest(SuperKey superKey) {
        if (superKey == null) {
            return;
        }
        synchronized (this) {
            if (linkedHashMapRequests.containsKey(superKey)) {
                T object = linkedHashMapRequests.get(superKey);
                removeEventBefore(superKey, object);
                linkedHashMapRequests.remove(superKey);
                removeEventAfter(superKey, object);
            }
        }
    }

    public void removeAllRequest() {
        synchronized (this) {
            List<SuperKey> superKeys = new ArrayList<SuperKey>();
            List<T> objects = new ArrayList<T>();
            Iterator<Map.Entry<SuperKey, T>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, T> entry = iterator.next();
                SuperKey superKey = entry.getKey();
                T object = entry.getValue();
                superKeys.add(superKey);
                objects.add(object);
            }
            for (int i = 0; i < superKeys.size(); i++) {
                SuperKey superKey = superKeys.get(i);
                T object = objects.get(i);
                removeEventBefore(superKey, object);
                linkedHashMapRequests.remove(superKey);
                removeEventAfter(superKey, object);
            }
        }
    }


    /**
     * put方法保证linkedHashMapRequests内的superKey和object不为null
     * sortType为1表示降序排序,其他时候为升序
     */
    public void putAndSortRequest(SuperKey superKey, T object, int sortType) {
        if (superKey == null || object == null) {
            return;
        }
        synchronized (this) {
            if (putEventBefore(superKey, object)) {
                linkedHashMapRequests.put(superKey, object);
                if (sortType == 1) {
                    sortRequestDown();
                } else {
                    sortRequestUp();
                }
                putEventAfter(superKey, object);
            }
        }
    }

    public void removeAndSortRequest(SuperKey superKey, int sortType) {
        if (superKey == null) {
            return;
        }
        synchronized (this) {
            if (linkedHashMapRequests.containsKey(superKey)) {
                T object = linkedHashMapRequests.get(superKey);
                removeEventBefore(superKey, object);
                linkedHashMapRequests.remove(superKey);
                if (sortType == 1) {
                    sortRequestDown();
                } else {
                    sortRequestUp();
                }
                removeEventAfter(superKey, object);
            }
        }
    }

    /**
     * 该方法位于锁内,返回true表示可以进行put,false表示不可以进行put
     * superKey NotNull
     * object NotNull
     */
    public boolean putEventBefore(SuperKey superKey, T object) {
        return true;
    }

    /**
     * 该方法位于锁内
     * superKey NotNull
     * object NotNull
     */
    public void putEventAfter(SuperKey superKey, T object) {
    }

    /**
     * 该方法位于锁内
     * superKey NotNull
     * object NotNull
     */
    public void removeEventBefore(SuperKey superKey, T object) {
    }

    /**
     * 该方法位于锁内
     * superKey NotNull
     * object NotNull
     */
    public void removeEventAfter(SuperKey superKey, T object) {
    }


    /**
     * 根据sort值降序排序(冒泡排序)
     */
    public void sortRequestDown() {
        synchronized (this) {
            List<SuperKey> superKeys = new ArrayList<SuperKey>();
            List<T> objects = new ArrayList<T>();
            Iterator<Map.Entry<SuperKey, T>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, T> entry = iterator.next();
                SuperKey superKey = entry.getKey();
                T object = entry.getValue();
                superKeys.add(superKey);
                objects.add(object);
            }
            // put方法保证linkedHashMapRequests内的superKey和object不为null
            for (int i = 0; i < superKeys.size() - 1; i++) {//降序
                for (int j = 0; j < superKeys.size() - 1 - i; j++) {
                    if (superKeys.get(j).getSort() < superKeys.get(j + 1).getSort()) {
                        SuperKey superKey = superKeys.get(j);
                        superKeys.set(j, superKeys.get(j + 1));
                        superKeys.set(j + 1, superKey);

                        T object = objects.get(j);
                        objects.set(j, objects.get(j + 1));
                        objects.set(j + 1, object);
                    }
                }
            }
            linkedHashMapRequests.clear();
            for (int i = 0; i < superKeys.size(); i++) {
                linkedHashMapRequests.put(superKeys.get(i), objects.get(i));
            }
        }
    }

    /**
     * 根据sort值升序排序(冒泡排序)
     */
    public void sortRequestUp() {
        synchronized (this) {
            List<SuperKey> superKeys = new ArrayList<SuperKey>();
            List<T> objects = new ArrayList<T>();
            Iterator<Map.Entry<SuperKey, T>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, T> entry = iterator.next();
                SuperKey superKey = entry.getKey();
                T object = entry.getValue();
                superKeys.add(superKey);
                objects.add(object);
            }
            for (int i = 0; i < superKeys.size() - 1; i++) {//升序
                for (int j = 0; j < superKeys.size() - 1 - i; j++) {
                    if (superKeys.get(j).getSort() > superKeys.get(j + 1).getSort()) {
                        SuperKey superKey = superKeys.get(j);
                        superKeys.set(j, superKeys.get(j + 1));
                        superKeys.set(j + 1, superKey);

                        T object = objects.get(j);
                        objects.set(j, objects.get(j + 1));
                        objects.set(j + 1, object);
                    }
                }
            }
            linkedHashMapRequests.clear();
            for (int i = 0; i < superKeys.size(); i++) {
                linkedHashMapRequests.put(superKeys.get(i), objects.get(i));
            }
        }
    }


}
