package com.my.models.net;

import com.google.gson.Gson;
import com.my.models.Comment;

import java.util.List;

public class WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel extends BaseGsonModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<Comment> hotComments;

        private List<Comment> comments;

        public List<Comment> getHotComments() {
            return hotComments;
        }

        public void setHotComments(List<Comment> hotComments) {
            this.hotComments = hotComments;
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "hotComments=" + hotComments +
                    ", comments=" + comments +
                    '}';
        }
    }

    public static Data getGsonModel(String response) {
        WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel gsonModel = null;
        if (response != null) {
            try {
                gsonModel = new Gson().fromJson(response, WallpaperDetailsActivity_GetWallpaperCommentByCreateTime_GsonModel.class);
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
