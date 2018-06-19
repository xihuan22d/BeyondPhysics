package com.my.models.net;


import com.google.gson.Gson;
import com.my.models.Follow;


public class WallpaperDetailsActivity_DoFollow_GsonModel extends BaseGsonModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String tips;
        private long userFollowCount;
        private long targetUserFansCount;
        private Follow follow;

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public long getUserFollowCount() {
            return userFollowCount;
        }

        public void setUserFollowCount(long userFollowCount) {
            this.userFollowCount = userFollowCount;
        }

        public long getTargetUserFansCount() {
            return targetUserFansCount;
        }

        public void setTargetUserFansCount(long targetUserFansCount) {
            this.targetUserFansCount = targetUserFansCount;
        }

        public Follow getFollow() {
            return follow;
        }

        public void setFollow(Follow follow) {
            this.follow = follow;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "tips='" + tips + '\'' +
                    ", userFollowCount=" + userFollowCount +
                    ", targetUserFansCount=" + targetUserFansCount +
                    ", follow=" + follow +
                    '}';
        }
    }


    public static Data getGsonModel(String response) {
        WallpaperDetailsActivity_DoFollow_GsonModel gsonModel = null;
        if (response != null) {
            try {
                gsonModel = new Gson().fromJson(response, WallpaperDetailsActivity_DoFollow_GsonModel.class);
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
