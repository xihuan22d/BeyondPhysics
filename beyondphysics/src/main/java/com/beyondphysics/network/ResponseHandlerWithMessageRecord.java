package com.beyondphysics.network;


import android.os.Looper;
import android.os.Message;

import com.beyondphysics.network.utils.SuperKey;
import com.beyondphysics.network.utils.TimeTool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ResponseHandler的改进版
 * 记录当前Handler所有的信息,并且支持未执行消息的移除
 * Created by xihuan22 on 2017/9/5.
 */
public class ResponseHandlerWithMessageRecord extends ResponseHandler {
    public final String MESSAGEKEY = "messageKey";

    public final LinkedHashMap<MessageKey, MessageObject> linkedHashMapMessageObjects = new LinkedHashMap<MessageKey, MessageObject>();

    public ResponseHandlerWithMessageRecord() {
        super();
    }

    public ResponseHandlerWithMessageRecord(Callback callback) {
        super(callback);
    }

    public ResponseHandlerWithMessageRecord(Looper looper) {
        super(looper);
    }

    public ResponseHandlerWithMessageRecord(Looper looper, Callback callback) {
        super(looper, callback);
    }

    public void putMessageObject(MessageKey messageKey, MessageObject messageObject) {
        if (messageKey == null || messageObject == null) {
            return;
        }
        synchronized (linkedHashMapMessageObjects) {
            linkedHashMapMessageObjects.put(messageKey, messageObject);
        }
    }

    public void removeMessageObject(MessageKey messageKey) {
        if (messageKey == null) {
            return;
        }
        synchronized (linkedHashMapMessageObjects) {
            if (linkedHashMapMessageObjects.containsKey(messageKey)) {
                linkedHashMapMessageObjects.remove(messageKey);
            }
        }
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        MessageObject messageObject = (MessageObject) message.obj;
        if (messageObject != null) {
            MessageKey messageKey = (MessageKey) messageObject.getObject(MESSAGEKEY);
            removeMessageObject(messageKey);
        }
    }

    /**
     * 参数message,superKey,messageObject都是NotNull的
     */
    @Override
    protected void doSendMessage(Message message, SuperKey superKey, MessageObject messageObject) {
        MessageKey messageKey = new MessageKey(superKey, messageObject.getWhat(), superKey.getTag());
        messageObject.putObject(MESSAGEKEY, messageKey);
        synchronized (linkedHashMapMessageObjects) {
            putMessageObject(messageKey, messageObject);
            sendMessage(message);
        }
    }

    /**
     * linkedHashMapMessageObjects托管了那些发送给主线程,但主线程还未执行的请求
     * 该方法可以解决RequestManager.cancelRequestWithRequest(Request<?> request,boolean removeListener)中遇到的请求已发送且主线程还未执行消息,但队列里面用superKey已经搜索不到的问题
     */
    @Override
    public void removeResponseMessageBySuperKey(SuperKey superKey, boolean removeListener) {
        if (superKey == null) {
            return;
        }
        synchronized (linkedHashMapMessageObjects) {
            List<MessageObject> messageObjects = findMessageWithSuperKey(superKey);
            removeMessageObjects(messageObjects, removeListener);
        }
    }


    @Override
    public void removeResponseMessageByTag(String tag, boolean removeListener) {
        if (tag == null) {
            tag = SuperKey.DEFAULT_TAG;
        }
        synchronized (linkedHashMapMessageObjects) {//需要对putMessage(messageKey, message) sendMessage(message)加锁,以保证removeMessages操作时候不会因为中间添加了新消息而导致把新添加的消息通过tag移除了,又无法触发handleMessage,因为之后发现更加好的解决方案,用removeMessages(messageObject.getWhat(), messageObject),messageObject是新new的对象是唯一的,所以就不会再出现上面的问题了
            List<MessageObject> messageObjects = findMessageWithTag(tag);
            removeMessageObjects(messageObjects, removeListener);
        }
    }

    @Override
    public void removeAllResponseMessage(boolean removeListener) {
        synchronized (linkedHashMapMessageObjects) {
            List<MessageObject> messageObjects = getAllMessage();
            removeMessageObjects(messageObjects, removeListener);
        }
    }

    /**
     * 该方法需要在 synchronized (linkedHashMapMessages)锁内操作
     * 不建议使用bundle传递消息,bundle有容量限制,而且序列化速度慢
     */
    private void removeMessageObjects(List<MessageObject> messageObjects, boolean removeListener) {
        if (messageObjects == null) {
            return;
        }
        for (int i = 0; i < messageObjects.size(); i++) {
            MessageObject messageObject = messageObjects.get(i);
            MessageKey messageKey = (MessageKey) messageObject.getObject(MESSAGEKEY);
            Request<?> request = (Request<?>) messageObject.getObject(REQUEST);
            if (request != null) {
                request.cancelRequest();
                Request.removeListener(request, removeListener);
            }
            CacheItemRequest cacheItemRequest = (CacheItemRequest) messageObject.getObject(CACHEITEMREQUEST);
            if (cacheItemRequest != null) {
                cacheItemRequest.cancelRequest();
                CacheItemRequest.removeCacheItemListener(cacheItemRequest, removeListener);
            }
            removeMessages(messageObject.getWhat(), messageObject);
            removeMessageObject(messageKey);
        }
    }

    public List<MessageObject> findMessageWithSuperKey(SuperKey superKey) {
        synchronized (linkedHashMapMessageObjects) {
            List<MessageObject> messageObjects = new ArrayList<MessageObject>();
            if (superKey != null) {
                Iterator<Map.Entry<MessageKey, MessageObject>> iterator = linkedHashMapMessageObjects.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<MessageKey, MessageObject> entry = iterator.next();
                    MessageKey messageKey = entry.getKey();
                    MessageObject messageObject = entry.getValue();
                    if (superKey.equals(messageKey.getSuperKey())) {
                        messageObjects.add(messageObject);
                    }
                }
            }
            return messageObjects;
        }
    }

    public List<MessageObject> findMessageWithTag(String tag) {
        synchronized (linkedHashMapMessageObjects) {
            List<MessageObject> messageObjects = new ArrayList<MessageObject>();
            if (tag != null) {
                Iterator<Map.Entry<MessageKey, MessageObject>> iterator = linkedHashMapMessageObjects.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<MessageKey, MessageObject> entry = iterator.next();
                    MessageKey messageKey = entry.getKey();
                    MessageObject messageObject = entry.getValue();
                    if (tag.equals(messageKey.getTag())) {
                        messageObjects.add(messageObject);
                    }
                }
            }
            return messageObjects;
        }
    }

    public List<MessageObject> getAllMessage() {
        synchronized (linkedHashMapMessageObjects) {
            List<MessageObject> messageObjects = new ArrayList<MessageObject>();
            Iterator<Map.Entry<MessageKey, MessageObject>> iterator = linkedHashMapMessageObjects.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<MessageKey, MessageObject> entry = iterator.next();
                MessageObject messageObject = entry.getValue();
                messageObjects.add(messageObject);
            }
            return messageObjects;
        }
    }


    public class MessageKey implements Serializable {

        private final SuperKey superKey;
        private final int what;
        private final String tag;
        private final long time;

        public MessageKey(SuperKey superKey, int what, String tag) {
            this.superKey = superKey;
            this.what = what;
            this.tag = tag;
            time = TimeTool.getOnlyTimeWithoutSleep();
        }

        public SuperKey getSuperKey() {
            return superKey;
        }

        public int getWhat() {
            return what;
        }

        public String getTag() {
            return tag;
        }

        public long getTime() {
            return time;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MessageKey that = (MessageKey) o;

            if (what != that.what) return false;
            if (time != that.time) return false;
            if (superKey != null ? !superKey.equals(that.superKey) : that.superKey != null)
                return false;
            return !(tag != null ? !tag.equals(that.tag) : that.tag != null);

        }

        @Override
        public int hashCode() {
            int result = superKey != null ? superKey.hashCode() : 0;
            result = 31 * result + what;
            result = 31 * result + (tag != null ? tag.hashCode() : 0);
            result = 31 * result + (int) (time ^ (time >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return "MessageKey{" +
                    "superKey=" + superKey +
                    ", what=" + what +
                    ", tag='" + tag + '\'' +
                    ", time=" + time +
                    '}';
        }
    }

}
