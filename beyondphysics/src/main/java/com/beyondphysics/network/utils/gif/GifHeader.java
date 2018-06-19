package com.beyondphysics.network.utils.gif;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2018/6/2.
 */
public class GifHeader {
    private String id = null;
    private int[] gct = null;
    private int gifFrameCount = 0;
    private GifFrame currentGifFrame = null;
    private final List<GifFrame> gifFrames = new ArrayList<GifFrame>();
    private int width = 0;
    private int height = 0;
    private boolean gctFlag = false;
    private int gctSize = 0;
    private int bgIndex = 0;
    private int pixelAspect = 0;
    private int bgColor = 0;
    private int loopCount = 0;

    public void reset() {
        id = null;
        gct = null;
        gifFrameCount = 0;
        currentGifFrame = null;
        gifFrames.clear();
        width = 0;
        height = 0;
        gctFlag = false;
        gctSize = 0;
        bgIndex = 0;
        pixelAspect = 0;
        bgColor = 0;
        loopCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int[] getGct() {
        return gct;
    }

    public void setGct(int[] gct) {
        this.gct = gct;
    }

    public int getGifFrameCount() {
        return gifFrameCount;
    }

    public void setGifFrameCount(int gifFrameCount) {
        this.gifFrameCount = gifFrameCount;
    }

    public GifFrame getCurrentGifFrame() {
        return currentGifFrame;
    }

    public void setCurrentGifFrame(GifFrame currentGifFrame) {
        this.currentGifFrame = currentGifFrame;
    }

    public List<GifFrame> getGifFrames() {
        return gifFrames;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isGctFlag() {
        return gctFlag;
    }

    public void setGctFlag(boolean gctFlag) {
        this.gctFlag = gctFlag;
    }

    public int getGctSize() {
        return gctSize;
    }

    public void setGctSize(int gctSize) {
        this.gctSize = gctSize;
    }

    public int getBgIndex() {
        return bgIndex;
    }

    public void setBgIndex(int bgIndex) {
        this.bgIndex = bgIndex;
    }

    public int getPixelAspect() {
        return pixelAspect;
    }

    public void setPixelAspect(int pixelAspect) {
        this.pixelAspect = pixelAspect;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }
}
