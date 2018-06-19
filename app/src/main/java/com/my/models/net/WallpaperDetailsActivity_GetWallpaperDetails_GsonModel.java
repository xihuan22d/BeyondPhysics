package com.my.models.net;


import com.google.gson.Gson;
import com.my.models.Collection;
import com.my.models.Follow;
import com.my.models.Praise;
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
        private Collection collection;
        private Praise praise;
        private Follow follow;
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

        public Collection getCollection() {
            return collection;
        }

        public void setCollection(Collection collection) {
            this.collection = collection;
        }

        public Praise getPraise() {
            return praise;
        }

        public void setPraise(Praise praise) {
            this.praise = praise;
        }

        public Follow getFollow() {
            return follow;
        }

        public void setFollow(Follow follow) {
            this.follow = follow;
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
                    ", collection=" + collection +
                    ", praise=" + praise +
                    ", follow=" + follow +
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
