package com.my.beyondphysicsapplication.uihelp;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ColorHelp {


    public static void setImageViewDrawableTint(ImageView imageView, Drawable drawable, int tint) {
        if (imageView != null && drawable != null) {
            Drawable theDrawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTint(theDrawable, tint);
            imageView.setImageDrawable(theDrawable);
        }
    }

    public static String colorConvert(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        return "#" + Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue);
    }

    public static List<Integer> getRandomColorList(int size, List<Integer> colors) {
        List<Integer> resultColors = new ArrayList<Integer>();
        if (colors == null) {
            return resultColors;
        }

        int count = 0;
        List<Integer> theColors = new ArrayList<Integer>();
        theColors.addAll(colors);
        for (int i = 0; i < size; i++) {
            int index = (int) (Math.random() * theColors.size());
            resultColors.add(theColors.get(index));
            count = count + 1;
            if (count >= colors.size()) {
                theColors.clear();
                theColors.addAll(colors);
                count = 0;
            } else {
                theColors.remove(index);
            }
        }
        return resultColors;
    }
}
