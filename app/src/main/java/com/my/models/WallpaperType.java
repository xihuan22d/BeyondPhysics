package com.my.models;


import java.util.List;

public class WallpaperType extends BaseModel {

    private String name;
    private String url_absolute;
    private String url_relative;
    private long childCount;
    private long childVideoCount;
    private long childImageCount;
    private long sort;

    private List<Wallpaper> recommendWallpapers;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_absolute() {
        return url_absolute;
    }

    public void setUrl_absolute(String url_absolute) {
        this.url_absolute = url_absolute;
    }

    public String getUrl_relative() {
        return url_relative;
    }

    public void setUrl_relative(String url_relative) {
        this.url_relative = url_relative;
    }

    public long getChildCount() {
        return childCount;
    }

    public void setChildCount(long childCount) {
        this.childCount = childCount;
    }

    public long getChildVideoCount() {
        return childVideoCount;
    }

    public void setChildVideoCount(long childVideoCount) {
        this.childVideoCount = childVideoCount;
    }

    public long getChildImageCount() {
        return childImageCount;
    }

    public void setChildImageCount(long childImageCount) {
        this.childImageCount = childImageCount;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public List<Wallpaper> getRecommendWallpapers() {
        return recommendWallpapers;
    }

    public void setRecommendWallpapers(List<Wallpaper> recommendWallpapers) {
        this.recommendWallpapers = recommendWallpapers;
    }

    @Override
    public String toString() {
        return "WallpaperType{" +
                "name='" + name + '\'' +
                ", url_absolute='" + url_absolute + '\'' +
                ", url_relative='" + url_relative + '\'' +
                ", childCount=" + childCount +
                ", childVideoCount=" + childVideoCount +
                ", childImageCount=" + childImageCount +
                ", sort=" + sort +
                ", recommendWallpapers=" + recommendWallpapers +
                '}';
    }
}
