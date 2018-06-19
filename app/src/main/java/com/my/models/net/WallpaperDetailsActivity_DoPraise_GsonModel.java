package com.my.models.net;


import com.google.gson.Gson;
import com.my.models.Praise;


public class WallpaperDetailsActivity_DoPraise_GsonModel extends BaseGsonModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String tips;
        private long praiseUpCount;
        private long praiseDownCount;
        private Praise praise;

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public long getPraiseUpCount() {
            return praiseUpCount;
        }

        public void setPraiseUpCount(long praiseUpCount) {
            this.praiseUpCount = praiseUpCount;
        }

        public long getPraiseDownCount() {
            return praiseDownCount;
        }

        public void setPraiseDownCount(long praiseDownCount) {
            this.praiseDownCount = praiseDownCount;
        }

        public Praise getPraise() {
            return praise;
        }

        public void setPraise(Praise praise) {
            this.praise = praise;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "tips='" + tips + '\'' +
                    ", praiseUpCount=" + praiseUpCount +
                    ", praiseDownCount=" + praiseDownCount +
                    ", praise=" + praise +
                    '}';
        }
    }


    public static Data getGsonModel(String response) {
        WallpaperDetailsActivity_DoPraise_GsonModel gsonModel = null;
        if (response != null) {
            try {
                gsonModel = new Gson().fromJson(response, WallpaperDetailsActivity_DoPraise_GsonModel.class);
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
