package com.my.models.net;

import com.google.gson.Gson;
import com.my.models.Wallpaper;

import java.util.List;

public class MainActivity_Home_GetWallpaper_Default_GsonModel extends BaseGsonModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<Wallpaper> wallpapers;

        public List<Wallpaper> getWallpapers() {
            return wallpapers;
        }

        public void setWallpapers(List<Wallpaper> wallpapers) {
            this.wallpapers = wallpapers;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "wallpapers=" + wallpapers +
                    '}';
        }
    }

    public static Data getGsonModel(String response) {
        MainActivity_Home_GetWallpaper_Default_GsonModel gsonModel = null;
        if (response != null) {
            try {
                gsonModel = new Gson().fromJson(response, MainActivity_Home_GetWallpaper_Default_GsonModel.class);
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
