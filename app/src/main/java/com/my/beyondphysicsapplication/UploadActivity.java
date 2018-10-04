package com.my.beyondphysicsapplication;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyondphysics.network.BitmapConfig;
import com.beyondphysics.network.Request;
import com.beyondphysics.network.UploadRequest;
import com.beyondphysics.network.utils.FileTool;
import com.beyondphysics.ui.BaseActivity;
import com.beyondphysics.ui.utils.BeyondPhysicsManager;
import com.beyondphysics.ui.utils.GetPathFromUriTool;
import com.my.beyondphysicsapplication.uihelp.PopupWindowHelp;
import com.my.modelhttpfunctions.MainActivity_Type_HttpFunction;
import com.my.modelhttpfunctions.UploadActivityHttpFunction;
import com.my.models.Wallpaper;
import com.my.models.WallpaperType;
import com.my.models.local.KeyValueItem;
import com.my.models.net.BaseGsonModel;
import com.my.models.net.MainActivity_Type_GetWallpaperType_GsonModel;
import com.my.models.net.UploadActivity_UploadWallpaper_GsonModel;
import com.my.utils.BitmapChangeSizeTool;
import com.my.utils.EncryptionTool;

import java.util.ArrayList;
import java.util.List;


public class UploadActivity extends NewBaseActivity {
    private final int PREVIEW_INTENT_REQUEST = 1;
    private final int WALLPAPER_VIDEO_INTENT_REQUEST = 2;
    private final int WALLPAPER_IMAGE_INTENT_REQUEST = 3;
    private int mainColorText;
    private int editText_normal_hint;

    private int activity_upload_imageViewPreview_widthOrHeight;

    private RelativeLayout relativeLayoutRoot;//最外层的view,提供给popupwindow居中用
    private ImageView imageViewBack;
    private RelativeLayout relativeLayoutKind;
    private TextView textViewKindContent;
    private ImageView imageViewKindOk;
    private RelativeLayout relativeLayoutType;
    private TextView textViewTypeContent;
    private ImageView imageViewTypeOk;
    private EditText editTextNameContent;
    private ImageView imageViewNameOk;
    private EditText editTextFeatureContent;
    private ImageView imageViewFeatureOk;
    private EditText editTextDescribeContent;
    private ImageView imageViewDescribeOk;
    private EditText editTextNeedGoldContent;
    private ImageView imageViewNeedGoldOk;
    private RelativeLayout relativeLayoutPreview;
    private ImageView imageViewPreview;
    private TextView textViewPreviewContent;
    private RelativeLayout relativeLayoutWallpaper;
    private ImageView imageViewWallpaperOk;
    private TextView textViewWallpaperContent;
    private TextView textViewToolbarUpload;
    private View.OnClickListener onClickListener;

    private final boolean[] validate = new boolean[8];

    private int kindPosition = -1;
    private int wallpaperTypePosition = -1;
    private String kind;
    private String wallpaperType_id;
    private String uploadPreviewPath;
    private String uploadWallpaperVideoPath;
    private String uploadWallpaperImagePath;
    private long oldCurrentSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initAll();
    }

    @SuppressWarnings("ResourceType")
    private void getTypeArrayColor() {
        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.mainColorText});
        try {
            mainColorText = typedArray.getColor(0, 0xffffffff);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void initHandler() {

    }

    @Override
    protected void initUi() {
        getTypeArrayColor();
        editText_normal_hint = ContextCompat.getColor(UploadActivity.this, R.color.editText_normal_hint);

        activity_upload_imageViewPreview_widthOrHeight = getResources().getDimensionPixelSize(R.dimen.activity_upload_imageViewPreview_widthOrHeight);

        relativeLayoutRoot = (RelativeLayout) findViewById(R.id.relativeLayoutRoot);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        relativeLayoutKind = (RelativeLayout) findViewById(R.id.relativeLayoutKind);
        textViewKindContent = (TextView) findViewById(R.id.textViewKindContent);
        imageViewKindOk = (ImageView) findViewById(R.id.imageViewKindOk);
        relativeLayoutType = (RelativeLayout) findViewById(R.id.relativeLayoutType);
        textViewTypeContent = (TextView) findViewById(R.id.textViewTypeContent);
        imageViewTypeOk = (ImageView) findViewById(R.id.imageViewTypeOk);
        editTextNameContent = ((EditText) findViewById(R.id.editTextNameContent));
        imageViewNameOk = (ImageView) findViewById(R.id.imageViewNameOk);
        editTextFeatureContent = ((EditText) findViewById(R.id.editTextFeatureContent));
        imageViewFeatureOk = (ImageView) findViewById(R.id.imageViewFeatureOk);
        editTextDescribeContent = ((EditText) findViewById(R.id.editTextDescribeContent));
        imageViewDescribeOk = (ImageView) findViewById(R.id.imageViewDescribeOk);
        editTextNeedGoldContent = ((EditText) findViewById(R.id.editTextNeedGoldContent));
        imageViewNeedGoldOk = (ImageView) findViewById(R.id.imageViewNeedGoldOk);
        relativeLayoutPreview = (RelativeLayout) findViewById(R.id.relativeLayoutPreview);
        imageViewPreview = (ImageView) findViewById(R.id.imageViewPreview);
        textViewPreviewContent = (TextView) findViewById(R.id.textViewPreviewContent);
        relativeLayoutWallpaper = (RelativeLayout) findViewById(R.id.relativeLayoutWallpaper);
        imageViewWallpaperOk = (ImageView) findViewById(R.id.imageViewWallpaperOk);
        textViewWallpaperContent = (TextView) findViewById(R.id.textViewWallpaperContent);
        textViewToolbarUpload = (TextView) findViewById(R.id.textViewToolbarUpload);
        onTextChangedListener();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        editTextNeedGoldContent.setText("0");
        validate[5] = true;
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {
    }

    private void onTextChangedListener() {
        editTextNameContent.addTextChangedListener(new MyTextWatcher(editTextNameContent, "name"));
        editTextFeatureContent.addTextChangedListener(new MyTextWatcher(editTextFeatureContent, "feature"));
        editTextDescribeContent.addTextChangedListener(new MyTextWatcher(editTextDescribeContent, "describe"));
        editTextNeedGoldContent.addTextChangedListener(new MyTextWatcher(editTextNeedGoldContent, "needGold"));
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.relativeLayoutKind:
                        final List<KeyValueItem> keyValueItems = new ArrayList<KeyValueItem>();
                        keyValueItems.add(new KeyValueItem("图片壁纸", Wallpaper.kind_image));
                        keyValueItems.add(new KeyValueItem("视频壁纸", Wallpaper.kind_video));
                        PopupWindowHelp.showPopupWindowNormalListViewString(UploadActivity.this, relativeLayoutRoot, relativeLayoutType, "上传类型", keyValueItems, kindPosition, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                KeyValueItem keyValueItem = keyValueItems.get(position);
                                if (keyValueItem != null) {
                                    textViewKindContent.setText(keyValueItem.getKey());
                                    textViewKindContent.setTextColor(mainColorText);
                                    imageViewKindOk.setVisibility(View.VISIBLE);
                                    kind = (String) keyValueItem.getValue();
                                    validate[0] = true;
                                    resetSelect();
                                    kindPosition = position;
                                }
                            }
                        });
                        break;
                    case R.id.relativeLayoutType:
                        if (kind == null) {
                            BaseActivity.showShortToast(UploadActivity.this, "请先选择上传类型");
                        } else {
                            relativeLayoutType.setEnabled(false);
                            MainActivity_Type_HttpFunction.mainActivity_type_getWallpaperType(UploadActivity.this, new Request.OnResponseListener<String>() {
                                @Override
                                public void onSuccessResponse(String response) {
                                }

                                @Override
                                public void onErrorResponse(String error) {
                                    relativeLayoutType.setEnabled(true);
                                }
                            }, new BaseGsonModel.OnBaseGsonModelListener<MainActivity_Type_GetWallpaperType_GsonModel.Data>() {
                                @Override
                                public void error(String error) {
                                    BaseActivity.showShortToast(UploadActivity.this, error);
                                    relativeLayoutType.setEnabled(true);
                                }

                                @Override
                                public void successByTips(String tips) {
                                    BaseActivity.showShortToast(UploadActivity.this, tips);
                                    relativeLayoutType.setEnabled(true);
                                }

                                @Override
                                public void success(MainActivity_Type_GetWallpaperType_GsonModel.Data data) {
                                    if (data == null || data.getWallpaperTypes() == null) {
                                        BaseActivity.showShortToast(UploadActivity.this, TheApplication.SERVERERROR);
                                        relativeLayoutType.setEnabled(true);
                                    } else {
                                        final List<KeyValueItem> keyValueItems = new ArrayList<KeyValueItem>();
                                        final List<WallpaperType> wallpaperTypes = data.getWallpaperTypes();
                                        for (int i = 0; i < wallpaperTypes.size(); i++) {
                                            WallpaperType wallpaperType = wallpaperTypes.get(i);
                                            if (wallpaperType == null) {
                                                keyValueItems.add(null);
                                            } else {
                                                keyValueItems.add(new KeyValueItem(wallpaperType.getName(), wallpaperType.get_id()));
                                            }
                                        }
                                        PopupWindowHelp.showPopupWindowNormalListViewString(UploadActivity.this, relativeLayoutRoot, relativeLayoutType, "壁纸分类", keyValueItems, wallpaperTypePosition, new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                WallpaperType wallpaperType = wallpaperTypes.get(position);
                                                if (wallpaperType != null) {
                                                    wallpaperType_id = wallpaperType.get_id();
                                                    String name = wallpaperType.getName();
                                                    textViewTypeContent.setText(name);
                                                    textViewTypeContent.setTextColor(mainColorText);
                                                    imageViewTypeOk.setVisibility(View.VISIBLE);
                                                    editTextNameContent.setText(name);
                                                    editTextNameContent.requestFocus();
                                                    Spannable spannable = editTextNameContent.getText();
                                                    if (spannable != null) {
                                                        Selection.setSelection(spannable, spannable.length());
                                                    }
                                                    editTextFeatureContent.setText(name);
                                                    editTextDescribeContent.setText(name + "壁纸");
                                                    validate[1] = true;
                                                    isCanUpload();
                                                    wallpaperTypePosition = position;
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        break;
                    case R.id.relativeLayoutPreview:
                        Intent intentPreview = new Intent();
                        intentPreview.setType("image/*");
                        intentPreview.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intentPreview, PREVIEW_INTENT_REQUEST);
                        break;
                    case R.id.relativeLayoutWallpaper:
                        if (kind == null) {
                            BaseActivity.showShortToast(UploadActivity.this, "请先选择上传类型");
                        } else {
                            if (kind.equals(Wallpaper.kind_video)) {
                                Intent intentMovie = new Intent();
                                intentMovie.setType("video/*");
                                intentMovie.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentMovie, WALLPAPER_VIDEO_INTENT_REQUEST);
                            } else {
                                Intent intentMovie = new Intent();
                                intentMovie.setType("image/*");
                                intentMovie.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentMovie, WALLPAPER_IMAGE_INTENT_REQUEST);
                            }
                        }
                        break;
                    case R.id.textViewToolbarUpload:
                        if (kind == null || kind.equals(Wallpaper.kind_video)) {
                            BaseActivity.showShortToast(UploadActivity.this, "请先选择上传类型且不允许上传视频进行测试,敬请谅解");
                        } else {
                            String name = editTextNameContent.getText().toString().trim();
                            String feature = editTextFeatureContent.getText().toString().trim();
                            String describe = editTextDescribeContent.getText().toString();
                            String needGold = editTextNeedGoldContent.getText().toString();
                            if (name.equals("") || feature.equals("")) {
                                BaseActivity.showShortToast(UploadActivity.this, "名称和特征不能为空");
                            } else {
                                int gold = 0;
                                try {
                                    gold = Integer.parseInt(needGold);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (gold > 10) {
                                    BaseActivity.showShortToast(UploadActivity.this, "售价不能超过10");
                                } else {
                                    int[] res = FileTool.getBitmapWidthAndHeight(uploadWallpaperImagePath);
                                    final String[] names = {"kind", "wallpaperType_id", "name", "feature", "describe", "needGold", "previewImageWidth", "previewImageHeight"};
                                    final String[] values = {EncryptionTool.stringToDecode(kind), EncryptionTool.stringToDecode(wallpaperType_id), EncryptionTool.stringToDecode(name), EncryptionTool.stringToDecode(feature), EncryptionTool.stringToDecode(describe), EncryptionTool.stringToDecode(needGold), EncryptionTool.stringToDecode(String.valueOf(res[0])), EncryptionTool.stringToDecode(String.valueOf(res[1]))};
                                    final String[] fileNames = {"filePreview", "fileWallpaper"};
                                    String uploadWallpaperPath = uploadWallpaperImagePath;
                                    final String[] filePaths = {uploadPreviewPath, uploadWallpaperPath};
                                    PopupWindowHelp.showPopupWindowNormalProgress(UploadActivity.this, relativeLayoutRoot, textViewToolbarUpload, "正在上传", new PopupWindowHelp.OnShowPopupwindowNormalProgressListener() {
                                        @Override
                                        public void init(ProgressBar progressBar, TextView textViewProgress, PopupWindow popupWindow, View popupWindowView) {
                                            UploadRequest<?> uploadRequest = uploadWallpaper(names, values, fileNames, filePaths, progressBar, textViewProgress, popupWindow);
                                            if (uploadRequest != null) {
                                                popupWindowView.setTag(uploadRequest);
                                            }
                                        }

                                        @Override
                                        public void cancel(PopupWindow popupWindow, View popupWindowView) {

                                        }

                                        @Override
                                        public void popupWindowDismiss(PopupWindow popupWindow, View popupWindowView) {
                                            if (popupWindowView.getTag() != null) {
                                                BeyondPhysicsManager.getInstance(UploadActivity.this).cancelRequestWithRequest((UploadRequest<?>) popupWindowView.getTag(), false);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        relativeLayoutKind.setOnClickListener(onClickListener);
        relativeLayoutType.setOnClickListener(onClickListener);
        relativeLayoutPreview.setOnClickListener(onClickListener);
        relativeLayoutWallpaper.setOnClickListener(onClickListener);
        textViewToolbarUpload.setOnClickListener(onClickListener);
    }


    private void isCanUpload() {
        boolean pass = true;
        for (int i = 0; i < validate.length; i++) {
            if (!validate[i]) {
                pass = false;
                break;
            }
        }
        if (pass) {
            textViewToolbarUpload.setVisibility(View.VISIBLE);
        } else {
            textViewToolbarUpload.setVisibility(View.INVISIBLE);
        }
    }


    private UploadRequest<?> uploadWallpaper(String[] names, String[] values, String[] fileNames, String[] filePaths, final ProgressBar progressBar, final TextView textViewProgress, final PopupWindow popupWindow) {
        UploadRequest<?> uploadRequest = UploadActivityHttpFunction.uploadActivity_uploadWallpaper(UploadActivity.this, names, values, fileNames, filePaths, new Request.OnResponseListener<String>() {
            @Override
            public void onSuccessResponse(String response) {
            }

            @Override
            public void onErrorResponse(String error) {
                BaseActivity.showShortToast(UploadActivity.this, error);
                popupWindow.dismiss();
            }
        }, new BaseGsonModel.OnBaseGsonModelListener<UploadActivity_UploadWallpaper_GsonModel.Data>() {
            @Override
            public void error(String error) {
                BaseActivity.showShortToast(UploadActivity.this, error);
                popupWindow.dismiss();
            }

            @Override
            public void successByTips(String tips) {
                BaseActivity.showShortToast(UploadActivity.this, tips);
                popupWindow.dismiss();
            }

            @Override
            public void success(UploadActivity_UploadWallpaper_GsonModel.Data data) {
                if (data == null) {
                    BaseActivity.showShortToast(UploadActivity.this, TheApplication.SERVERERROR);
                    popupWindow.dismiss();
                } else {
                    popupWindow.dismiss();
                    Intent intent = new Intent(UploadActivity.this,
                            WaterfallFlowActivity.class);
                    intent.putExtra(WaterfallFlowActivity.FROM_KEY, WaterfallFlowActivity.FROM_UPLOADACTIVITY);
                    startActivity(intent);
                    BaseActivity.showShortToast(UploadActivity.this, data.getTips());
                }
            }
        }, new UploadRequest.OnUploadProgressListener() {
            @Override
            public void maxProgress(UploadRequest<?> uploadRequest, long totalSize) {
                progressBar.setMax(100);
            }

            @Override
            public void updateProgress(UploadRequest<?> uploadRequest, long currentSize, long totalSize) {
                if (Math.abs(currentSize - oldCurrentSize) >= 10240 || currentSize == totalSize) {
                    int x = (int) (currentSize * 100 / totalSize);
                    progressBar.setProgress(x);
                    textViewProgress.setText(x + "%");
                    oldCurrentSize = currentSize;
                }
            }
        });
        return uploadRequest;
    }

    public void resetSelect() {
        validate[1] = false;
        imageViewTypeOk.setVisibility(View.INVISIBLE);
        textViewTypeContent.setText(R.string.activity_upload_textViewTypeContent_text);
        textViewTypeContent.setTextColor(editText_normal_hint);
        validate[2] = false;
        editTextNameContent.setText("");
        imageViewNameOk.setVisibility(View.INVISIBLE);
        validate[3] = false;
        editTextFeatureContent.setText("");
        imageViewFeatureOk.setVisibility(View.INVISIBLE);
        validate[4] = false;
        editTextDescribeContent.setText("");
        imageViewDescribeOk.setVisibility(View.INVISIBLE);
        validate[5] = true;
        editTextNeedGoldContent.setText("0");
        imageViewNeedGoldOk.setVisibility(View.VISIBLE);
        validate[6] = false;
        textViewPreviewContent.setText(R.string.activity_upload_textViewPreviewContent_text);
        textViewPreviewContent.setTextColor(editText_normal_hint);
        imageViewPreview.setImageBitmap(null);
        validate[7] = false;
        textViewWallpaperContent.setText(R.string.activity_upload_textViewWallpaperContent_text);
        textViewWallpaperContent.setTextColor(editText_normal_hint);
        imageViewWallpaperOk.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PREVIEW_INTENT_REQUEST) {
                String theUploadPreviewPath = GetPathFromUriTool.getPath(UploadActivity.this, data.getData());
                if (theUploadPreviewPath == null) {
                    BaseActivity.showShortToast(UploadActivity.this, "获取该文件异常");
                } else {
                    Bitmap bitmap = FileTool.getCompressBitmapFromFile(theUploadPreviewPath, activity_upload_imageViewPreview_widthOrHeight, activity_upload_imageViewPreview_widthOrHeight, ImageView.ScaleType.CENTER, new BitmapConfig(Bitmap.Config.RGB_565));
                    if (bitmap == null) {
                        BaseActivity.showShortToast(UploadActivity.this, "该图片解析后为空请从新选择");
                    } else {
                        imageViewPreview.setImageBitmap(bitmap);
                        int[] widthAndHeight = FileTool.getBitmapWidthAndHeight(theUploadPreviewPath);
                        if (widthAndHeight[0] < 720) {
                            uploadPreviewPath = theUploadPreviewPath;
                            textViewPreviewContent.setText(uploadPreviewPath);
                            textViewPreviewContent.setTextColor(mainColorText);
                            validate[6] = true;
                        } else {
                            String path = BitmapChangeSizeTool.getResizeBitmapAndSaveWithJPG(theUploadPreviewPath, "uploadPreview.jpg", TheApplication.getUploadCachePath(UploadActivity.this), 540);
                            if (path == null) {
                                BaseActivity.showShortToast(UploadActivity.this, "图片压缩成预览图时异常");
                            } else {
                                uploadPreviewPath = path;
                                textViewPreviewContent.setText(uploadPreviewPath);
                                textViewPreviewContent.setTextColor(mainColorText);
                                validate[6] = true;
                            }
                        }
                    }
                }
            } else if (requestCode == WALLPAPER_IMAGE_INTENT_REQUEST) {
                String theUploadWallpaperImagePath = GetPathFromUriTool.getPath(UploadActivity.this, data.getData());
                if (theUploadWallpaperImagePath == null) {
                    BaseActivity.showShortToast(UploadActivity.this, "获取该文件异常");
                } else {
                    uploadWallpaperImagePath = theUploadWallpaperImagePath;
                    textViewWallpaperContent.setText(uploadWallpaperImagePath);
                    textViewWallpaperContent.setTextColor(mainColorText);
                    validate[7] = true;
                    imageViewWallpaperOk.setVisibility(View.VISIBLE);
                }
            }else if (requestCode == WALLPAPER_VIDEO_INTENT_REQUEST) {
                String theUploadWallpaperVideoPath = GetPathFromUriTool.getPath(UploadActivity.this, data.getData());
                if (theUploadWallpaperVideoPath == null) {
                    BaseActivity.showShortToast(UploadActivity.this, "获取该文件异常");
                } else {
                    uploadWallpaperVideoPath = theUploadWallpaperVideoPath;
                    textViewWallpaperContent.setText(uploadWallpaperVideoPath);
                    textViewWallpaperContent.setTextColor(mainColorText);
                    validate[7] = true;
                    imageViewWallpaperOk.setVisibility(View.VISIBLE);
                }
            }
            isCanUpload();
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }


    public class MyTextWatcher implements TextWatcher {
        private EditText editText;
        private String type;

        public MyTextWatcher(EditText editText, String type) {
            this.editText = editText;
            this.type = type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String string = editText.getText().toString();
            if (type != null) {
                if (type.equals("name")) {
                    if (string.equals("")) {
                        validate[2] = false;
                        imageViewNameOk.setVisibility(View.INVISIBLE);
                    } else {
                        validate[2] = true;
                        imageViewNameOk.setVisibility(View.VISIBLE);
                    }
                } else if (type.equals("feature")) {
                    if (string.equals("")) {
                        validate[3] = false;
                        imageViewFeatureOk.setVisibility(View.INVISIBLE);
                    } else {
                        validate[3] = true;
                        imageViewFeatureOk.setVisibility(View.VISIBLE);
                    }
                } else if (type.equals("describe")) {
                    if (string.equals("")) {
                        validate[4] = false;
                        imageViewDescribeOk.setVisibility(View.INVISIBLE);
                    } else {
                        validate[4] = true;
                        imageViewDescribeOk.setVisibility(View.VISIBLE);
                    }
                } else if (type.equals("needGold")) {
                    if (string.equals("")) {
                        validate[5] = false;
                        imageViewNeedGoldOk.setVisibility(View.INVISIBLE);
                    } else {
                        if (!string.equals("0")) {
                            BaseActivity.showShortToast(UploadActivity.this, "上传免费壁纸可获得双倍奖励");
                        }
                        validate[5] = true;
                        imageViewNeedGoldOk.setVisibility(View.VISIBLE);
                    }
                }
                isCanUpload();
            }
        }
    }

}
