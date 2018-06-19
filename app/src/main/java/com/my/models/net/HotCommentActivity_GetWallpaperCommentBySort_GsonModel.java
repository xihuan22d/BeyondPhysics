package com.my.models.net;

import com.google.gson.Gson;
import com.my.models.Comment;

import java.util.List;

public class HotCommentActivity_GetWallpaperCommentBySort_GsonModel extends BaseGsonModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        private List<Comment> comments;

        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "comments=" + comments +
                    '}';
        }
    }

    public static Data getGsonModel(String response) {
        HotCommentActivity_GetWallpaperCommentBySort_GsonModel gsonModel = null;
        if (response != null) {
            try {
                gsonModel = new Gson().fromJson(response, HotCommentActivity_GetWallpaperCommentBySort_GsonModel.class);
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
