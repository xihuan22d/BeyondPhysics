package com.my.models.net;


import com.google.gson.Gson;
import com.my.models.User;


public class UploadActivity_UploadWallpaper_GsonModel extends BaseGsonModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private User user;
        private String tips;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "user=" + user +
                    ", tips='" + tips + '\'' +
                    '}';
        }
    }


    public static Data getGsonModel(String response) {
        UploadActivity_UploadWallpaper_GsonModel gsonModel = null;
        if (response != null) {
            try {
                gsonModel = new Gson().fromJson(response, UploadActivity_UploadWallpaper_GsonModel.class);
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
