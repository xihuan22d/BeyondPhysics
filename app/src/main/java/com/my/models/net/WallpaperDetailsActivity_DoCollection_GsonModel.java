package com.my.models.net;


import com.google.gson.Gson;
import com.my.models.Collection;


public class WallpaperDetailsActivity_DoCollection_GsonModel extends BaseGsonModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String tips;
        private long collectionCount;
        private Collection collection;

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public long getCollectionCount() {
            return collectionCount;
        }

        public void setCollectionCount(long collectionCount) {
            this.collectionCount = collectionCount;
        }

        public Collection getCollection() {
            return collection;
        }

        public void setCollection(Collection collection) {
            this.collection = collection;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "tips='" + tips + '\'' +
                    ", collectionCount=" + collectionCount +
                    ", collection=" + collection +
                    '}';
        }
    }


    public static Data getGsonModel(String response) {
        WallpaperDetailsActivity_DoCollection_GsonModel gsonModel = null;
        if (response != null) {
            try {
                gsonModel = new Gson().fromJson(response, WallpaperDetailsActivity_DoCollection_GsonModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (gsonModel == null) {
            return null;
        }
        return gsonModel.getData();
    }

}
