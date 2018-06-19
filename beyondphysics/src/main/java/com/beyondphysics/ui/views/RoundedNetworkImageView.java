package com.beyondphysics.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.beyondphysics.R;
import com.beyondphysics.network.BitmapConfig;


/**
 * Created by xihuan22 on 2017/9/16.
 */
public class RoundedNetworkImageView extends NetworkImageView {
    private Context context;
    private int roundedType = 1;
    private float circleBorderWidth = 3.0f;
    private int circleBorderColor = 0xffffffff;
    private float cornerDegree = 6.0f;

    public RoundedNetworkImageView(Context context) {
        super(context);
        this.context = context;
    }

    public RoundedNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(attrs);
    }

    public RoundedNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        float scale = context.getResources().getDisplayMetrics().density;

        int roundedNetworkImageView_default_circleBorderWidth = (int) (1 * scale);
        int roundedNetworkImageView_default_cornerDegree = (int) (2 * scale);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.roundedNetworkImageView);
        try {
            roundedType = typedArray.getInt(R.styleable.roundedNetworkImageView_roundedType, 1);
            circleBorderWidth = typedArray.getDimension(R.styleable.roundedNetworkImageView_circleBorderWidth, roundedNetworkImageView_default_circleBorderWidth);
            circleBorderColor = typedArray.getColor(R.styleable.roundedNetworkImageView_circleBorderColor, 0xffffffff);
            cornerDegree = typedArray.getDimension(R.styleable.roundedNetworkImageView_cornerDegree, roundedNetworkImageView_default_cornerDegree);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public BitmapConfig getBitmapConfig() {
        return new BitmapConfig(Bitmap.Config.ARGB_8888, roundedType, circleBorderWidth, circleBorderColor, cornerDegree);
    }
}
