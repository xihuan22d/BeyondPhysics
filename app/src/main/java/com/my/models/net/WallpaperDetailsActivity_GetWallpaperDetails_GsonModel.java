package com.my.models.net;


import com.google.gson.Gson;
import com.my.models.User;
import com.my.models.Wallpaper;

import java.util.List;

public class WallpaperDetailsActivity_GetWallpaperDetails_GsonModel extends BaseGsonModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private Wallpaper wallpaper;
        private User uploadUser;
        private List<Wallpaper> recommendWallpapers;

        public Wallpaper getWallpaper() {
            return wallpaper;
        }

        public void setWallpaper(Wallpaper wallpaper) {
            this.wallpaper = wallpaper;
        }

        public User getUploadUser() {
            return uploadUser;
        }

        public void setUploadUser(User uploadUser) {
            this.uploadUser = uploadUser;
        }


        public List<Wallpaper> getRecommendWallpapers() {
            return recommendWallpapers;
        }

        public void setRecommendWallpapers(List<Wallpaper> recommendWallpapers) {
            this.recommendWallpapers = recommendWallpapers;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "wallpaper=" + wallpaper +
                    ", uploadUser=" + uploadUser +
                    ", recommendWallpapers=" + recommendWallpapers +
                    '}';
        }
    }


    public static Data getGsonModel(String response) {
        WallpaperDetailsActivity_GetWallpaperDetails_GsonModel gsonModel = null;
        if (response != null) {
            try {
                gsonModel = new Gson().fromJson(response, WallpaperDetailsActivity_GetWallpaperDetails_GsonModel.class);
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
