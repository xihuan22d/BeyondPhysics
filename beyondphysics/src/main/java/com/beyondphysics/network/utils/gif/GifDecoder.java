package com.beyondphysics.network.utils.gif;


import android.graphics.Bitmap;
import android.os.Build;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xihuan22 on 2018/6/2.
 */
public class GifDecoder {
    private static final int MIN_FRAME_DELAY = 2;

    private static final int DEFAULT_FRAME_DELAY = 10;

    public static final int MAX_STACK_SIZE = 4096;

    public static final int NULL_CODE = -1;

    private final int MAX_WORK_BUFFER_SIZE = 16384;


    private final GifHeader gifHeader = new GifHeader();
    private byte[] workBuffer;
    private final byte[] block = new byte[256];
    private byte[] decodeBlock;

    private int blockSize = 0;

    private int reqWidth;
    private int reqHeight;
    private Bitmap.Config config;
    private int zoom = 1;
    private int zoomWidth;
    private int zoomHeight;
    private int workBufferSize = 0;
    private int workBufferPosition = 0;
    private byte[] mainPixels;
    private int[] mainScratch;
    private boolean isFirstFrameTransparent;

    private short[] prefix;
    private byte[] suffix;
    private byte[] pixelStack;


    public List<GifBitmap> decode(byte[] data, int reqWidth, int reqHeight, Bitmap.Config config) {
        if (data == null) {
            throw new NullPointerException("data为null");
        }
        synchronized (this) {
            this.reqWidth = reqWidth;
            this.reqHeight = reqHeight;
            this.config = config;
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            byteBuffer = byteBuffer.asReadOnlyBuffer();
            byteBuffer.position(0);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            String id = "";
            for (int i = 0; i < 6; i++) {
                id += (char) read(byteBuffer);
            }
            gifHeader.setId(id);
            readLSD(byteBuffer);
            if (gifHeader.isGctFlag()) {
                gifHeader.setGct(readColorTable(byteBuffer, gifHeader.getGctSize()));
                gifHeader.setBgColor(gifHeader.getGct()[gifHeader.getBgIndex()]);
            }
            readContents(byteBuffer);
            zoom = getZoom(gifHeader.getWidth(), gifHeader.getHeight());
            zoomWidth = gifHeader.getWidth() / zoom;
            zoomHeight = gifHeader.getHeight() / zoom;
            mainPixels = new byte[gifHeader.getWidth() * gifHeader.getHeight()];
            mainScratch = new int[zoomWidth * zoomHeight];
            List<GifBitmap> gifBitmaps = new ArrayList<GifBitmap>();
            List<GifFrame> gifFrames = gifHeader.getGifFrames();
            for (int i = 0; i < gifFrames.size(); i++) {
                GifFrame gifFrame = gifFrames.get(i);
                GifBitmap gifBitmap = new GifBitmap();
                gifBitmap.setWidth(zoomWidth);
                gifBitmap.setHeight(zoomHeight);
                gifBitmap.setDelay(gifFrame.getDelay());
                gifBitmap.setBitmap(getBitmap(byteBuffer, gifFrame, i));
                gifBitmaps.add(gifBitmap);
            }
            clear();
            return gifBitmaps;
        }
    }

    private void clear() {
        gifHeader.reset();
        workBuffer = null;
        decodeBlock = null;
        mainPixels = null;
        mainScratch = null;
        isFirstFrameTransparent = false;
        prefix = null;
        suffix = null;
        pixelStack = null;
    }

    private void readLSD(ByteBuffer byteBuffer) {
        gifHeader.setWidth(readShort(byteBuffer));
        gifHeader.setHeight(readShort(byteBuffer));
        int packed = read(byteBuffer);
        gifHeader.setGctFlag((packed & 0x80) != 0);
        gifHeader.setGctSize(2 << (packed & 7));
        gifHeader.setBgIndex(read(byteBuffer));
        gifHeader.setPixelAspect(read(byteBuffer));
    }

    private int getZoom(int width, int height) {
        int zoom = 1;
        if (reqWidth <= 0 && reqHeight <= 0) {
            zoom = 1;
        } else if (reqWidth > 0 && reqHeight <= 0) {
            int zoomWidth = width / reqWidth;
            zoom = zoomWidth;
        } else if (reqWidth <= 0 && reqHeight > 0) {
            int zoomHeight = height / reqHeight;
            zoom = zoomHeight;
        } else if (reqWidth > 0 && reqHeight > 0) {
            int zoomWidth = width / reqWidth;
            int zoomHeight = height / reqHeight;
            if (zoomWidth < zoomHeight) {
                zoom = zoomWidth;
            } else {
                zoom = zoomHeight;
            }
        }
        if (zoom <= 0) {
            zoom = 1;
        }
        return zoom;
    }


    private int read(ByteBuffer byteBuffer) {
        int result = 0;
        try {
            result = byteBuffer.get() & 0xFF;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private int readShort(ByteBuffer byteBuffer) {
        int result = 0;
        try {
            result = byteBuffer.getShort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private void readContents(ByteBuffer byteBuffer) {
        boolean done = false;
        while (!done) {
            int code = read(byteBuffer);
            switch (code) {
                case 0x2c:
                    if (gifHeader.getCurrentGifFrame() == null) {
                        gifHeader.setCurrentGifFrame(new GifFrame());
                    }
                    readBitmap(byteBuffer);
                    break;
                case 0x21:
                    code = read(byteBuffer);
                    switch (code) {
                        case 0xf9:
                            gifHeader.setCurrentGifFrame(new GifFrame());
                            readGraphicControlExt(byteBuffer);
                            break;
                        case 0xff:
                            readBlock(byteBuffer);
                            String app = "";
                            for (int i = 0; i < 11; i++) {
                                app = app + (char) block[i];
                            }
                            if (app.equals("NETSCAPE2.0")) {
                                readNetscapeExt(byteBuffer);
                            } else {
                                skip(byteBuffer);
                            }
                            break;
                        case 0xfe:
                            skip(byteBuffer);
                            break;
                        case 0x01:
                            skip(byteBuffer);
                            break;
                        default:
                            skip(byteBuffer);
                            break;
                    }
                    break;
                case 0x3b:
                    done = true;
                    break;
                case 0x00:
                default:
                    break;
            }
        }
    }

    private void skip(ByteBuffer byteBuffer) {
        try {
            int blockSize;
            do {
                blockSize = read(byteBuffer);
                byteBuffer.position(byteBuffer.position() + blockSize);
            } while (blockSize > 0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    private void readBitmap(ByteBuffer byteBuffer) {
        gifHeader.getCurrentGifFrame().setIx(readShort(byteBuffer));
        gifHeader.getCurrentGifFrame().setIy(readShort(byteBuffer));
        gifHeader.getCurrentGifFrame().setIw(readShort(byteBuffer));
        gifHeader.getCurrentGifFrame().setIh(readShort(byteBuffer));
        int packed = read(byteBuffer);
        boolean lctFlag = (packed & 0x80) != 0;
        int lctSize = (int) Math.pow(2, (packed & 0x07) + 1);
        gifHeader.getCurrentGifFrame().setInterlace((packed & 0x40) != 0);
        if (lctFlag) {
            gifHeader.getCurrentGifFrame().setLct(readColorTable(byteBuffer, lctSize));
        } else {
            gifHeader.getCurrentGifFrame().setLct(null);
        }
        gifHeader.getCurrentGifFrame().setBufferFrameStart(byteBuffer.position());
        skipImageData(byteBuffer);
        gifHeader.setGifFrameCount(gifHeader.getGifFrameCount() + 1);
        gifHeader.getGifFrames().add(gifHeader.getCurrentGifFrame());
    }


    private int[] readColorTable(ByteBuffer byteBuffer, int colors) {
        int byteCount = 3 * colors;
        int[] tab = null;
        byte[] bytes = new byte[byteCount];
        try {
            byteBuffer.get(bytes);
            tab = new int[256];
            int i = 0;
            int j = 0;
            while (i < colors) {
                int r = ((int) bytes[j++]) & 0xff;
                int g = ((int) bytes[j++]) & 0xff;
                int b = ((int) bytes[j++]) & 0xff;
                tab[i++] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        } catch (BufferUnderflowException e) {
            e.printStackTrace();
        }
        return tab;
    }


    private void skipImageData(ByteBuffer byteBuffer) {
        read(byteBuffer);
        skip(byteBuffer);
    }


    private void readGraphicControlExt(ByteBuffer byteBuffer) {
        read(byteBuffer);
        int packed = read(byteBuffer);
        gifHeader.getCurrentGifFrame().setDispose((packed & 0x1c) >> 2);
        if (gifHeader.getCurrentGifFrame().getDispose() == 0) {
            gifHeader.getCurrentGifFrame().setDispose(1);
        }
        gifHeader.getCurrentGifFrame().setTransparency((packed & 1) != 0);
        int delayInHundredthsOfASecond = readShort(byteBuffer);
        if (delayInHundredthsOfASecond < MIN_FRAME_DELAY) {
            delayInHundredthsOfASecond = DEFAULT_FRAME_DELAY;
        }
        gifHeader.getCurrentGifFrame().setDelay(delayInHundredthsOfASecond * 10);
        gifHeader.getCurrentGifFrame().setTransIndex(read(byteBuffer));
        read(byteBuffer);
    }

    private void readNetscapeExt(ByteBuffer byteBuffer) {
        do {
            readBlock(byteBuffer);
            if (block[0] == 1) {
                int b1 = ((int) block[1]) & 0xff;
                int b2 = ((int) block[2]) & 0xff;
                gifHeader.setLoopCount((b2 << 8) | b1);
                if (gifHeader.getLoopCount() == 0) {
                    gifHeader.setLoopCount(-1);
                }
            }
        } while (blockSize > 0);
    }

    private int readBlock(ByteBuffer byteBuffer) {
        blockSize = read(byteBuffer);
        int n = 0;
        if (blockSize > 0) {
            int count = 0;
            try {
                while (n < blockSize) {
                    count = blockSize - n;
                    byteBuffer.get(block, n, count);
                    n = n + count;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return n;
    }


    private Bitmap getBitmap(ByteBuffer byteBuffer, GifFrame currentGifFrame, int position) {
        int[] act = currentGifFrame.getLct() != null ? currentGifFrame.getLct() : gifHeader.getGct();
        if (act == null) {
            return null;
        }
        if (currentGifFrame.isTransparency()) {
            int[] pct = new int[256];
            System.arraycopy(act, 0, pct, 0, act.length);
            act = pct;
            act[currentGifFrame.getTransIndex()] = 0;
        }
        final int[] dest = mainScratch;
        decodeBitmapData(byteBuffer, currentGifFrame);
        int ih = currentGifFrame.getIh() / zoom;
        int iy = currentGifFrame.getIy() / zoom;
        int iw = currentGifFrame.getIw() / zoom;
        int ix = currentGifFrame.getIx() / zoom;
        int pass = 1;
        int inc = 8;
        int iline = 0;
        boolean isFirstFrame = false;
        if (position == 0) {
            isFirstFrame = true;
        }
        for (int i = 0; i < ih; i++) {
            int line = i;
            if (currentGifFrame.isInterlace()) {
                if (iline >= ih) {
                    pass++;
                    switch (pass) {
                        case 2:
                            iline = 4;
                            break;
                        case 3:
                            iline = 2;
                            inc = 4;
                            break;
                        case 4:
                            iline = 1;
                            inc = 2;
                            break;
                        default:
                            break;
                    }
                }
                line = iline;
                iline += inc;
            }
            line += iy;
            if (line < zoomHeight) {
                int k = line * zoomWidth;
                int dx = k + ix;
                int dlim = dx + iw;
                if (k + zoomWidth < dlim) {
                    dlim = k + zoomWidth;
                }
                int sx = i * zoom * currentGifFrame.getIw();
                int maxPositionInSource = sx + ((dlim - dx) * zoom);
                while (dx < dlim) {
                    int averageColor;
                    if (zoom == 1) {
                        int currentColorIndex = ((int) mainPixels[sx]) & 0x000000ff;
                        averageColor = act[currentColorIndex];
                    } else {
                        averageColor = averageColorsNear(act, sx, maxPositionInSource, currentGifFrame.getIw());
                    }
                    if (averageColor != 0) {
                        dest[dx] = averageColor;
                    } else if (!isFirstFrameTransparent && isFirstFrame) {
                        isFirstFrameTransparent = true;
                    }
                    sx += zoom;
                    dx++;
                }
            }
        }
        Bitmap bitmap = createBitmap();
        bitmap.setPixels(dest, 0, zoomWidth, 0, 0, zoomWidth, zoomHeight);
        return bitmap;
    }

    private Bitmap createBitmap() {
        Bitmap.Config theConfig = config;
        if (theConfig == null) {
            theConfig = isFirstFrameTransparent ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        }
        Bitmap bitmap = Bitmap.createBitmap(zoomWidth, zoomHeight, theConfig);
        if (Build.VERSION.SDK_INT >= 12) {
            bitmap.setHasAlpha(true);
        }
        return bitmap;
    }


    private int averageColorsNear(int[] act, int positionInMainPixels, int maxPositionInMainPixels,
                                  int currentFrameIw) {
        int alphaSum = 0;
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int totalAdded = 0;
        for (int i = positionInMainPixels; i < positionInMainPixels + zoom && i < mainPixels.length && i < maxPositionInMainPixels; i++) {
            int currentColorIndex = ((int) mainPixels[i]) & 0xff;
            int currentColor = act[currentColorIndex];
            if (currentColor != 0) {
                alphaSum += currentColor >> 24 & 0x000000ff;
                redSum += currentColor >> 16 & 0x000000ff;
                greenSum += currentColor >> 8 & 0x000000ff;
                blueSum += currentColor & 0x000000ff;
                totalAdded++;
            }
        }
        for (int i = positionInMainPixels + currentFrameIw; i < positionInMainPixels + currentFrameIw + zoom && i < mainPixels.length && i < maxPositionInMainPixels; i++) {
            int currentColorIndex = ((int) mainPixels[i]) & 0xff;
            int currentColor = act[currentColorIndex];
            if (currentColor != 0) {
                alphaSum += currentColor >> 24 & 0x000000ff;
                redSum += currentColor >> 16 & 0x000000ff;
                greenSum += currentColor >> 8 & 0x000000ff;
                blueSum += currentColor & 0x000000ff;
                totalAdded++;
            }
        }
        if (totalAdded == 0) {
            return 0;
        } else {
            return ((alphaSum / totalAdded) << 24)
                    | ((redSum / totalAdded) << 16)
                    | ((greenSum / totalAdded) << 8)
                    | (blueSum / totalAdded);
        }
    }

    private void decodeBitmapData(ByteBuffer byteBuffer, GifFrame gifFrame) {
        workBufferSize = 0;
        workBufferPosition = 0;
        if (gifFrame != null) {
            byteBuffer.position(gifFrame.getBufferFrameStart());
        }

        int npix = (gifFrame == null) ? gifHeader.getWidth() * gifHeader.getHeight() : gifFrame.getIw() * gifFrame.getIh();
        int available, clear, codeMask, codeSize, endOfInformation, inCode, oldCode, bits, code, count, i, datum, dataSize, first, top, bi, pi;

        if (mainPixels == null || mainPixels.length < npix) {
            mainPixels = new byte[npix];
        }
        if (prefix == null) {
            prefix = new short[MAX_STACK_SIZE];
        }
        if (suffix == null) {
            suffix = new byte[MAX_STACK_SIZE];
        }
        if (pixelStack == null) {
            pixelStack = new byte[MAX_STACK_SIZE + 1];
        }

        dataSize = readByte(byteBuffer);
        clear = 1 << dataSize;
        endOfInformation = clear + 1;
        available = clear + 2;
        oldCode = NULL_CODE;
        codeSize = dataSize + 1;
        codeMask = (1 << codeSize) - 1;
        for (code = 0; code < clear; code++) {
            prefix[code] = 0;
            suffix[code] = (byte) code;
        }

        datum = bits = count = first = top = pi = bi = 0;
        for (i = 0; i < npix; ) {
            if (count == 0) {
                count = readDecodeBlock(byteBuffer);
                if (count <= 0) {
                    break;
                }
                bi = 0;
            }
            datum += (((int) decodeBlock[bi]) & 0xff) << bits;
            bits += 8;
            bi++;
            count--;
            while (bits >= codeSize) {
                code = datum & codeMask;
                datum >>= codeSize;
                bits -= codeSize;
                if (code == clear) {
                    codeSize = dataSize + 1;
                    codeMask = (1 << codeSize) - 1;
                    available = clear + 2;
                    oldCode = NULL_CODE;
                    continue;
                }
                if (code > available) {
                    break;
                }
                if (code == endOfInformation) {
                    break;
                }
                if (oldCode == NULL_CODE) {
                    pixelStack[top++] = suffix[code];
                    oldCode = code;
                    first = code;
                    continue;
                }
                inCode = code;
                if (code >= available) {
                    pixelStack[top++] = (byte) first;
                    code = oldCode;
                }
                while (code >= clear) {
                    pixelStack[top++] = suffix[code];
                    code = prefix[code];
                }
                first = ((int) suffix[code]) & 0xff;
                pixelStack[top++] = (byte) first;

                if (available < MAX_STACK_SIZE) {
                    prefix[available] = (short) oldCode;
                    suffix[available] = (byte) first;
                    available++;
                    if (((available & codeMask) == 0) && (available < MAX_STACK_SIZE)) {
                        codeSize++;
                        codeMask += available;
                    }
                }
                oldCode = inCode;
                while (top > 0) {
                    mainPixels[pi++] = pixelStack[--top];
                    i++;
                }
            }
        }
        for (i = pi; i < npix; i++) {
            mainPixels[i] = 0;
        }
    }

    private void readChunkIfNeeded(ByteBuffer byteBuffer) {
        if (workBufferSize > workBufferPosition) {
            return;
        }
        workBufferPosition = 0;
        workBufferSize = Math.min(byteBuffer.remaining(), MAX_WORK_BUFFER_SIZE);
        if (workBuffer == null) {
            workBuffer = new byte[workBufferSize];
        }
        byteBuffer.get(workBuffer, 0, workBufferSize);
    }


    private int readByte(ByteBuffer byteBuffer) {
        try {
            readChunkIfNeeded(byteBuffer);
            return workBuffer[workBufferPosition++] & 0xFF;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    private int readDecodeBlock(ByteBuffer byteBuffer) {
        int blockSize = readByte(byteBuffer);
        if (blockSize > 0) {
            try {
                decodeBlock = new byte[255];
                final int remaining = workBufferSize - workBufferPosition;
                if (remaining >= blockSize) {
                    System.arraycopy(workBuffer, workBufferPosition, decodeBlock, 0, blockSize);
                    workBufferPosition += blockSize;
                } else if (byteBuffer.remaining() + remaining >= blockSize) {
                    System.arraycopy(workBuffer, workBufferPosition, decodeBlock, 0, remaining);
                    workBufferPosition = workBufferSize;
                    readChunkIfNeeded(byteBuffer);
                    final int secondHalfRemaining = blockSize - remaining;
                    System.arraycopy(workBuffer, 0, decodeBlock, remaining, secondHalfRemaining);
                    workBufferPosition += secondHalfRemaining;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return blockSize;
    }

}
