package com.my.models.net;


import com.google.gson.Gson;


public class WallpaperDetailsActivity_CommentWallpaper_GsonModel extends BaseGsonModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private long commentCount;
        private String tips;

        public long getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(long commentCount) {
            this.commentCount = commentCount;
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
                    "commentCount=" + commentCount +
                    ", tips='" + tips + '\'' +
                    '}';
        }
    }


    public static Data getGsonModel(String response) {
        WallpaperDetailsActivity_CommentWallpaper_GsonModel gsonModel = null;
        if (response != null) {
            try {
                gsonModel = new Gson().fromJson(response, WallpaperDetailsActivity_CommentWallpaper_GsonModel.class);
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
