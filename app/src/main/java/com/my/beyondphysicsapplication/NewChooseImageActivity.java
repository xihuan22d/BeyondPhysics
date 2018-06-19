package com.my.beyondphysicsapplication;

import android.content.Intent;

import com.beyondphysics.ui.imagechooselibrary.ChooseImageActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManagerParams;

/**
 * 如果继承重写了BaseActivity的getBeyondPhysicsManagerParams方法,如果需要用到相册的话,建议把ChooseImageActivity里面的getBeyondPhysicsManagerParams方法也改成一样的
 */
public class NewChooseImageActivity extends ChooseImageActivity {


    public Intent getPreviewActivityIntent() {
        Intent intent = new Intent(NewChooseImageActivity.this,
                NewPreviewActivity.class);
        return intent;
    }

    @Override
    public BeyondPhysicsManagerParams getBeyondPhysicsManagerParams() {
        return NewBaseActivity.getBeyondPhysicsManagerParams(this);
    }
}
