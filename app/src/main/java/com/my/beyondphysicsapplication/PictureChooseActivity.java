
package com.my.beyondphysicsapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.beyondphysics.ui.imagechooselibrary.ChooseImageActivity;
import com.my.adapters.girdviewadapter.PictureChooseActivity_GirdViewAdapter;

import java.util.ArrayList;

public class PictureChooseActivity extends NewBaseActivity {
    public static final int CHOOSECOUNT = 9;
    private final int ADD_REQUEST = 1;
    private final int CHOOSE_REQUEST = 2;
    private final ArrayList<String> paths = new ArrayList<String>();
    private ImageView imageViewBack;
    private GridView gridView;

    private View.OnClickListener onClickListener;
    private PictureChooseActivity_GirdViewAdapter pictureChooseActivity_GirdViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_choose);
        initAll();
    }

    @Override
    protected void initHandler() {

    }

    @Override
    protected void initUi() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        gridView = (GridView) findViewById(R.id.gridView);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        pictureChooseActivity_GirdViewAdapter = new PictureChooseActivity_GirdViewAdapter(PictureChooseActivity.this, paths, new PictureChooseActivity_GirdViewAdapter.OnPictureChooseListener() {
            @Override
            public void addClick() {
                Intent intent = new Intent(PictureChooseActivity.this,
                        NewChooseImageActivity.class);
                intent.putExtra(ChooseImageActivity.CHOOSECOUNT_KEY, CHOOSECOUNT - paths.size());
                startActivityForResult(intent, ADD_REQUEST);
            }

            @Override
            public void chooseClick(String url) {
                Intent intent = new Intent(PictureChooseActivity.this,
                        NewChooseImageActivity.class);
                intent.putExtra(ChooseImageActivity.CHOOSECOUNT_KEY, CHOOSECOUNT);
                intent.putStringArrayListExtra(ChooseImageActivity.SELECTIMAGEPATHS_KEY, paths);
                startActivityForResult(intent, CHOOSE_REQUEST);
            }
        });
        gridView.setAdapter(pictureChooseActivity_GirdViewAdapter);
    }

    @Override
    protected void initHttp() {
    }

    @Override
    protected void initOther() {
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    default:
                        break;
                }
            }
        };

        imageViewBack.setOnClickListener(onClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ADD_REQUEST) {
                ArrayList<String> paths = data.getStringArrayListExtra(ChooseImageActivity.SELECTIMAGEPATHS_KEY);
                if (paths != null) {
                    this.paths.addAll(paths);
                }
                if (pictureChooseActivity_GirdViewAdapter != null) {
                    pictureChooseActivity_GirdViewAdapter.notifyDataSetChanged();
                }
            } else if (requestCode == CHOOSE_REQUEST) {
                ArrayList<String> paths = data.getStringArrayListExtra(ChooseImageActivity.SELECTIMAGEPATHS_KEY);
                if (paths != null) {
                    this.paths.clear();
                    this.paths.addAll(paths);
                }
                if (pictureChooseActivity_GirdViewAdapter != null) {
                    pictureChooseActivity_GirdViewAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
