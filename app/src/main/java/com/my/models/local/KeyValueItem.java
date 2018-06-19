package com.my.models.local;

public class KeyValueItem {
    private String key;
    private Object value;

    public KeyValueItem(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "KeyValueItem{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}

