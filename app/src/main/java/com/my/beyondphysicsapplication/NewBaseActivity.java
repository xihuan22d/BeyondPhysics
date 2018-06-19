package com.my.beyondphysicsapplication;

import android.content.Context;
import android.os.Bundle;

import com.beyondphysics.network.ResponseHandler;
import com.beyondphysics.network.http.HttpAgreement_Default;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManagerParams;

import java.io.File;


public class NewBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelType(CANCELREQUESTWITHTAG);
    }

    /**
     * BeyondPhysics初始化参数
     */
    @Override
    public BeyondPhysicsManagerParams getBeyondPhysicsManagerParams() {
        return getBeyondPhysicsManagerParams(this);
    }


    public static BeyondPhysicsManagerParams getBeyondPhysicsManagerParams(Context context) {
        //String rootPath= TheApplication.getFilesDirRootPath(context) + File.separator + FileTool.DEFAULT_ROOT_PATH;
        String rootPath = FileTool.getSdcardRootPath(TheApplication.DEFAULT_ROOT_PATH) + File.separator + FileTool.DEFAULT_ROOT_PATH;
        return new BeyondPhysicsManagerParams(context, 4, 2, new HttpAgreement_Default(), null, null, 2, rootPath, 52428800, ResponseHandler.RESPONSEHANDLERWITHMESSAGERECORD, true, 2);
    }


}
