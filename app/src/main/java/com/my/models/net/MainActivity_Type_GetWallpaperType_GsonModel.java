package com.my.models.net;

import com.google.gson.Gson;
import com.my.models.WallpaperType;

import java.util.List;

public class MainActivity_Type_GetWallpaperType_GsonModel extends BaseGsonModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<WallpaperType> wallpaperTypes;

        public List<WallpaperType> getWallpaperTypes() {
            return wallpaperTypes;
        }

        public void setWallpaperTypes(List<WallpaperType> wallpaperTypes) {
            this.wallpaperTypes = wallpaperTypes;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "wallpaperTypes=" + wallpaperTypes +
                    '}';
        }
    }


    public static Data getGsonModel(String response) {
        MainActivity_Type_GetWallpaperType_GsonModel gsonModel = null;
        if (response != null) {
            try {
                gsonModel = new Gson().fromJson(response, MainActivity_Type_GetWallpaperType_GsonModel.class);
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
