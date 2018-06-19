package com.my.beyondphysicsapplication;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.beyondphysics.ui.utils.PermissionHelper;


public class LoadingChooseActivity extends NewBaseActivity {

    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_choose);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setWindowType(1);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
    }

    @Override
    protected void initConfigUi() {
        permissionHelper = new PermissionHelper(LoadingChooseActivity.this, new PermissionHelper.PermissionModel[]{
                new PermissionHelper.PermissionModel("存储空间", Manifest.permission.WRITE_EXTERNAL_STORAGE, "我们需要读写存储卡权限以方便我们临时保存一些数据", PermissionHelper.WRITE_EXTERNAL_STORAGE_CODE)
        });
        permissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                goToJump();
            }
        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            goToJump();
        } else {
            if (permissionHelper.isAllRequestedPermissionGranted()) {
                goToJump();
            } else {
                permissionHelper.applyPermissions();
            }
        }
    }

    @Override
    protected void initHttp() {
    }

    @Override
    protected void initOther() {
    }

    private void goToJump() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingChooseActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
