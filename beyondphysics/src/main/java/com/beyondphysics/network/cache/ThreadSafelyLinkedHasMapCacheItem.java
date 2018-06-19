package com.beyondphysics.network.cache;


import com.beyondphysics.network.Request;
import com.beyondphysics.network.RequestManager;
import com.beyondphysics.network.utils.FileNameLockManager;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.network.utils.JsonTool;
import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.ThreadSafelyLinkedHasMap;
import com.beyondphysics.network.utils.TimeTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 该类的部分方法如putRequest,removeRequest会操作FileNameLockManager,当执行这类方法时候,需要注意外部不能包含FileNameLockManager的getFileNameLock方法
 * 该类和其他几个线程安全类的主要区别在于,尽量不要在主线程操作本类,因为CacheItem的数量往往会很大,而且每次被获取后会触发排序,数据多了以后排序和文件的存入读取都会导致占用this锁的时间很多,如果主线程直接操作会因为this锁被占用导致卡顿
 * 建议磁盘缓存的大小不要设置过大,否则会导致CacheItem过于庞大
 * Created by xihuan22 on 2017/8/17.
 */

public class ThreadSafelyLinkedHasMapCacheItem extends ThreadSafelyLinkedHasMap<CacheItem> {
    /**
     * NotNull
     */
    private final BitmapMemoryCache bitmapMemoryCache;
    /**
     * NotNull
     */
    private final String fileCache;
    /**
     * NotNull
     */
    private final String fileCache_infoFile;
    /**
     * NotNull
     */
    private final String fileCache_textFolder;
    /**
     * NotNull
     */
    private final String fileCache_imageFolder;

    private final long maxDiskCacheSize;

    private long totalDiskCacheSize = 0;//long的取值和赋值非原子操作

    private boolean init = false;

    /**
     * 为了维护linkedHashMapRequests操作的性能,不把文件的写入操作包含在this锁内
     * 可能在写入的过程中进行了put操作,但是getAllRequests到的是put之前的数据,这可能导致漏存的情况,所以建议缓存线程隔段时间再存入一次数据
     */
    private boolean needWrite = false;


    public ThreadSafelyLinkedHasMapCacheItem(BitmapMemoryCache bitmapMemoryCache, String fileCache, long maxDiskCacheSize) {
        if (bitmapMemoryCache == null) {
            throw new NullPointerException("bitmapMemoryCache为null");
        } else {
            this.bitmapMemoryCache = bitmapMemoryCache;
        }
        if (fileCache == null) {
            throw new NullPointerException("fileCache为null");
        } else {
            this.fileCache = fileCache.toLowerCase();
            fileCache_infoFile = (this.fileCache + File.separator + "cacheinfo.txt").toLowerCase();
            fileCache_textFolder = (this.fileCache + File.separator + "text").toLowerCase();
            fileCache_imageFolder = (this.fileCache + File.separator + "image").toLowerCase();
            FileTool.mkdirs(this.fileCache);
            FileTool.mkdirs(fileCache_textFolder);
            FileTool.mkdirs(fileCache_imageFolder);
        }
        this.maxDiskCacheSize = maxDiskCacheSize;
    }

    @Override
    public void putRequest(SuperKey superKey, CacheItem object) {
        if (superKey == null || object == null) {
            return;
        }
        synchronized (this) {
            Iterator<Map.Entry<SuperKey, CacheItem>> iterator = linkedHashMapRequests.entrySet().iterator();
            SuperKey needRemoveSuperKey = null;
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, CacheItem> entry = iterator.next();
                SuperKey theSuperKey = entry.getKey();
                if (superKey.getKey().equals(theSuperKey.getKey()) && superKey.getTag().equals(theSuperKey.getTag())) {
                    needRemoveSuperKey = theSuperKey;
                    break;
                }
            }
            if (needRemoveSuperKey != null) {
                linkedHashMapRequests.remove(needRemoveSuperKey);
            }
            super.putRequest(superKey, object);
        }
    }

    @Override
    public void putAndSortRequest(SuperKey superKey, CacheItem object, int sortType) {
        if (superKey == null || object == null) {
            return;
        }
        synchronized (this) {
            Iterator<Map.Entry<SuperKey, CacheItem>> iterator = linkedHashMapRequests.entrySet().iterator();
            SuperKey needRemoveSuperKey = null;
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, CacheItem> entry = iterator.next();
                SuperKey theSuperKey = entry.getKey();
                if (superKey.getKey().equals(theSuperKey.getKey()) && superKey.getTag().equals(theSuperKey.getTag())) {
                    needRemoveSuperKey = theSuperKey;
                    break;
                }
            }
            if (needRemoveSuperKey != null) {
                linkedHashMapRequests.remove(needRemoveSuperKey);
            }
            super.putAndSortRequest(superKey, object, sortType);
        }
    }

    @Override
    public boolean putEventBefore(SuperKey superKey, CacheItem cacheItem) {
        long laveDiskCacheSize = maxDiskCacheSize - totalDiskCacheSize;
        long cacheSize = cacheItem.getCacheSize();
        if (laveDiskCacheSize >= cacheSize) {
            totalDiskCacheSize = totalDiskCacheSize + cacheSize;
            return true;
        } else {
            boolean success = false;
            List<SuperKey> superKeys = new ArrayList<SuperKey>();
            Iterator<Map.Entry<SuperKey, CacheItem>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, CacheItem> entry = iterator.next();
                SuperKey theSuperKey = entry.getKey();
                CacheItem theCacheItem = entry.getValue();
                long theCacheSize = theCacheItem.getCacheSize();
                superKeys.add(theSuperKey);
                laveDiskCacheSize = laveDiskCacheSize + theCacheSize;
                if (laveDiskCacheSize >= cacheSize) {
                    totalDiskCacheSize = totalDiskCacheSize + cacheSize;
                    success = true;
                    break;
                }
            }
            if (success) {
                for (int i = 0; i < superKeys.size(); i++) {
                    SuperKey theSuperKey = superKeys.get(i);
                    removeRequest(theSuperKey);
                }
            }
            return success;
        }
    }

    @Override
    public void putEventAfter(SuperKey superKey, CacheItem cacheItem) {
        needWrite = true;
    }

    @Override
    public void removeEventBefore(SuperKey superKey, CacheItem cacheItem) {
        FileNameLockManager.getInstance().deleteFile(cacheItem.getCachePath());
        totalDiskCacheSize = totalDiskCacheSize - cacheItem.getCacheSize();
    }

    @Override
    public void removeEventAfter(SuperKey superKey, CacheItem cacheItem) {
        needWrite = true;
    }


    public BitmapMemoryCache getBitmapMemoryCache() {
        return bitmapMemoryCache;
    }

    public String getFileCache() {
        return fileCache;
    }

    public String getFileCache_infoFile() {
        return fileCache_infoFile;
    }

    public String getFileCache_textFolder() {
        return fileCache_textFolder;
    }

    public String getFileCache_imageFolder() {
        return fileCache_imageFolder;
    }

    public long getMaxDiskCacheSize() {
        return maxDiskCacheSize;
    }

    public long getTotalDiskCacheSize() {
        synchronized (this) {
            return totalDiskCacheSize;
        }
    }

    public boolean isNeedWrite() {
        return needWrite;
    }

    public void setNeedWrite(boolean needWrite) {
        this.needWrite = needWrite;
    }


    public void clearMemory(String key) {
        bitmapMemoryCache.clearMemory(key);
    }

    public void clearAllMemory() {
        bitmapMemoryCache.clearAllMemory();
    }

    /**
     * 建议不要在主线程中初始化,否则文件大了可能导致主线程停顿
     */
    public void init() {
        synchronized (this) {
            if (!init) {
                readCacheInfo();
                init = true;
            }
        }
    }

    public void putRequestWithInit(SuperKey superKey, CacheItem cacheItem) {
        synchronized (this) {
            init();
            putRequest(superKey, cacheItem);
        }
    }

    public CacheItem getRequestWithInit(SuperKey superKey) {
        synchronized (this) {
            init();
            return getRequest(superKey);
        }
    }

    public List<CacheItem> getAllRequestWithInit() {
        synchronized (this) {
            init();
            return getAllRequest();
        }
    }

    public void removeRequestWithInit(SuperKey superKey) {
        synchronized (this) {
            init();
            removeRequest(superKey);
        }
    }

    public void removeAllRequestWithInit() {
        synchronized (this) {
            init();
            removeAllRequest();
        }
    }

    public void putAndSortRequestWithInit(SuperKey superKey, CacheItem cacheItem, int sortType) {
        synchronized (this) {
            init();
            putAndSortRequest(superKey, cacheItem, sortType);
        }
    }

    public void removeAndSortRequestWithInit(SuperKey superKey, int sortType) {
        synchronized (this) {
            init();
            removeAndSortRequest(superKey, sortType);
        }
    }


    /**
     * 获取成功将写入最新一次获取的时间并从新排序
     */
    public CacheItem getCacheItemByKey(String key, String tag) {
        if (key == null) {
            return null;
        }
        synchronized (this) {
            init();
            Iterator<Map.Entry<SuperKey, CacheItem>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, CacheItem> entry = iterator.next();
                SuperKey superKey = entry.getKey();
                CacheItem cacheItem = entry.getValue();
                if (tag == null) {
                    if (key.equals(superKey.getKey())) {
                        superKey.setTimeAndSort(TimeTool.getOnlyTimeWithoutSleep());
                        sortRequestUp();
                        needWrite = true;
                        return cacheItem;
                    }
                } else {
                    if (key.equals(superKey.getKey()) && tag.equals(superKey.getTag())) {
                        superKey.setTimeAndSort(TimeTool.getOnlyTimeWithoutSleep());
                        sortRequestUp();
                        needWrite = true;
                        return cacheItem;
                    }
                }
            }
        }
        return null;
    }


    /**
     * 查找tag对应的缓存,若tag为null,表示在队列内全局查找,tag一般为Request.TAG,BitmapRequest.TAG,BreakpointDownloadRequest.TAG,一般情况下队列内是不会存在相同key的,因为BitmapRequest的key,没有存请求类型,所以不会和BreakpointDownloadRequest重合,但是Request使用GET方法还是可能会导致和BreakpointDownloadRequest重合的,所以tag的存在可以确保获得绝对正确的值
     * return List<CacheItem> NotNull
     */
    public List<CacheItem> getCacheItemsByKeys(List<String> keys, String tag) {
        List<CacheItem> resultCacheItems = new ArrayList<CacheItem>();
        if (keys == null) {
            return resultCacheItems;
        }
        synchronized (this) {
            init();
            List<SuperKey> superKeys = new ArrayList<SuperKey>();
            List<CacheItem> cacheItems = new ArrayList<CacheItem>();
            Iterator<Map.Entry<SuperKey, CacheItem>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, CacheItem> entry = iterator.next();
                SuperKey superKey = entry.getKey();
                CacheItem cacheItem = entry.getValue();
                superKeys.add(superKey);
                cacheItems.add(cacheItem);
            }
            for (int i = 0; i < keys.size(); i++) {
                boolean isHave = false;
                String key = keys.get(i);
                if (key != null) {
                    for (int j = 0; j < superKeys.size(); j++) {
                        SuperKey superKey = superKeys.get(j);
                        CacheItem cacheItem = cacheItems.get(j);
                        if (tag == null) {
                            if (key.equals(superKey.getKey())) {
                                resultCacheItems.add(cacheItem);
                                isHave = true;
                                break;
                            }
                        } else {
                            if (key.equals(superKey.getKey()) && tag.equals(superKey.getTag())) {
                                resultCacheItems.add(cacheItem);
                                isHave = true;
                                break;
                            }
                        }
                    }
                }
                if (isHave == false) {
                    resultCacheItems.add(null);
                }
            }

        }
        return resultCacheItems;
    }

    public void clearCacheItem(String key, String tag) {
        if (key == null) {
            return;
        }
        synchronized (this) {
            init();
            SuperKey needRemoveSuperKey = null;
            Iterator<Map.Entry<SuperKey, CacheItem>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, CacheItem> entry = iterator.next();
                SuperKey superKey = entry.getKey();
                if (tag == null) {
                    if (key.equals(superKey.getKey())) {
                        needRemoveSuperKey = superKey;
                    }
                } else {
                    if (key.equals(superKey.getKey()) && tag.equals(superKey.getTag())) {
                        needRemoveSuperKey = superKey;
                    }
                }
            }
            if (needRemoveSuperKey != null) {
                removeRequest(needRemoveSuperKey);
            }
        }
    }

    public void clearCacheItems(List<String> keys, String tag) {
        if (keys == null) {
            return;
        }
        synchronized (this) {
            init();
            List<SuperKey> needRemoveSuperKeys = new ArrayList<SuperKey>();
            List<SuperKey> superKeys = new ArrayList<SuperKey>();
            List<CacheItem> cacheItems = new ArrayList<CacheItem>();
            Iterator<Map.Entry<SuperKey, CacheItem>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, CacheItem> entry = iterator.next();
                SuperKey superKey = entry.getKey();
                CacheItem cacheItem = entry.getValue();
                superKeys.add(superKey);
                cacheItems.add(cacheItem);
            }
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                if (key != null) {
                    for (int j = 0; j < superKeys.size(); j++) {
                        SuperKey superKey = superKeys.get(j);
                        if (tag == null) {
                            if (key.equals(superKey.getKey())) {
                                needRemoveSuperKeys.add(superKey);
                                break;
                            }
                        } else {
                            if (key.equals(superKey.getKey()) && tag.equals(superKey.getTag())) {
                                needRemoveSuperKeys.add(superKey);
                                break;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < needRemoveSuperKeys.size(); i++) {
                removeRequest(needRemoveSuperKeys.get(i));
            }
        }
    }

    /**
     * 存入时候io线程因周期问题未即时写入关系不大,但是移除时候还是及时写入的好
     */
    public void clearAllCacheItem() {
        synchronized (this) {
            init();
            List<SuperKey> superKeys = new ArrayList<SuperKey>();
            Iterator<Map.Entry<SuperKey, CacheItem>> iterator = linkedHashMapRequests.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SuperKey, CacheItem> entry = iterator.next();
                SuperKey theSuperKey = entry.getKey();
                superKeys.add(theSuperKey);
            }
            for (int i = 0; i < superKeys.size(); i++) {
                removeRequest(superKeys.get(i));
            }
        }
        writeCacheInfo();
    }

    public void readCacheInfo() {
        removeAllRequest();
        List<SuperKey> superKeys = new ArrayList<SuperKey>();
        List<CacheItem> cacheItems = new ArrayList<CacheItem>();
        String cacheInfo = FileTool.readContentWithLock(fileCache_infoFile);
        if (cacheInfo != null) {
            try {
                JSONObject jsonObject = new JSONObject(cacheInfo);
                JSONArray jsonArrayCacheItems = jsonObject.getJSONArray("cacheItems");
                if (jsonArrayCacheItems != null) {
                    for (int i = 0; i < jsonArrayCacheItems.length(); i++) {
                        JSONObject theJsonObject = jsonArrayCacheItems.getJSONObject(i);
                        if (theJsonObject != null) {
                            String urlString = JsonTool.getStringFromJsonObject(theJsonObject, "urlString");
                            int modelType = JsonTool.getIntFromJsonObject(theJsonObject, "modelType");
                            int requestType = JsonTool.getIntFromJsonObject(theJsonObject, "requestType");
                            String cachePath = restoreString(JsonTool.getStringFromJsonObject(theJsonObject, "cachePath"));
                            long cacheSize = JsonTool.getLongFromJsonObject(theJsonObject, "cacheSize");
                            String tag = JsonTool.getStringFromJsonObject(theJsonObject, "tag");
                            int priority = JsonTool.getIntFromJsonObject(theJsonObject, "priority");
                            long visitTime = JsonTool.getLongFromJsonObject(theJsonObject, "visitTime");
                            String contentType = JsonTool.getStringFromJsonObject(theJsonObject, "contentType");
                            String contentEncoding = restoreString(JsonTool.getStringFromJsonObject(theJsonObject, "contentEncoding"));
                            int contentLength = JsonTool.getIntFromJsonObject(theJsonObject, "contentLength");
                            SuperKey superKeyCacheItem = null;
                            if (modelType == Request.NORMAL_REQUEST) {
                                superKeyCacheItem = new SuperKey(RequestManager.getRequestKey(urlString, requestType), tag, priority, 2, visitTime);//priority和visitTime越大,越不容易被移除,越小的排在越前面,越先移除
                            } else if (modelType == Request.BITMAP_REQUEST) {
                                superKeyCacheItem = new SuperKey(urlString, tag, priority, 2, visitTime);//这种做法可以保证图片请求key和断点下载请求key的不同,即使他们指向相同的图片也没事
                            } else if (modelType == Request.BREAKPOINT_DOWNLOAD_REQUEST) {
                                superKeyCacheItem = new SuperKey(RequestManager.getRequestKey(urlString, requestType), tag, priority, 2, visitTime);
                            }
                            if (superKeyCacheItem != null) {
                                CacheItem cacheItem = new CacheItem(urlString, modelType, requestType, cachePath, cacheSize, superKeyCacheItem, contentType, contentEncoding, contentLength);
                                superKeys.add(superKeyCacheItem);
                                cacheItems.add(cacheItem);
                            }
                        }
                    }
                    for (int i = 0; i < superKeys.size(); i++) {//升序
                        for (int j = 0; j < superKeys.size() - 1; j++) {
                            if (superKeys.get(j).getSort() > superKeys.get(j + 1).getSort()) {
                                SuperKey superKey = superKeys.get(j);
                                superKeys.set(j, superKeys.get(j + 1));
                                superKeys.set(j + 1, superKey);

                                CacheItem cacheItem = cacheItems.get(j);
                                cacheItems.set(j, cacheItems.get(j + 1));
                                cacheItems.set(j + 1, cacheItem);
                            }
                        }
                    }
                    synchronized (this) {
                        for (int i = 0; i < superKeys.size(); i++) {
                            putRequest(superKeys.get(i), cacheItems.get(i));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void writeCacheInfo() {
        List<CacheItem> cacheItems = getAllRequest();
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArrayCacheItems = new JSONArray();
            for (int i = 0; i < cacheItems.size(); i++) {
                CacheItem cacheItem = cacheItems.get(i);
                JSONObject theJsonObject = new JSONObject();
                theJsonObject.put("urlString", cacheItem.getUrlString());
                theJsonObject.put("modelType", cacheItem.getModelType());
                theJsonObject.put("requestType", cacheItem.getRequestType());
                theJsonObject.put("cachePath", checkAndChangeString(cacheItem.getCachePath()));//因为比如断点下载就没有cachePath,所以出现cachePath为null是正常现象,所以加上一个验证,以免捕获到不必要的异常
                theJsonObject.put("cacheSize", cacheItem.getCacheSize());
                SuperKey superKeyCacheItem = cacheItem.getSuperKey();
                if (superKeyCacheItem != null) {
                    theJsonObject.put("tag", superKeyCacheItem.getTag());
                    theJsonObject.put("priority", superKeyCacheItem.getPriority());
                    theJsonObject.put("visitTime", superKeyCacheItem.getTime());
                }
                theJsonObject.put("contentType", cacheItem.getContentType());
                theJsonObject.put("contentEncoding", checkAndChangeString(cacheItem.getContentEncoding()));
                theJsonObject.put("contentLength", cacheItem.getContentLength());
                jsonArrayCacheItems.put(theJsonObject);
            }
            jsonObject.put("cacheItems", jsonArrayCacheItems);
            FileTool.writeContentWithLock(fileCache_infoFile, jsonObject.toString(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        needWrite = false;
    }


    private String checkAndChangeString(String string) {
        if (string == null) {
            string = "error";
        }
        return string;
    }

    private String restoreString(String string) {
        if (string != null && string.equals("error")) {
            string = null;
        }
        return string;
    }
}
