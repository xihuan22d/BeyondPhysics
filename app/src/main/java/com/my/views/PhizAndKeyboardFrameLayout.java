package com.my.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beyondphysics.ui.BaseActivity;
import com.my.adapters.girdviewadapter.Normal_CommentInput_String_GirdViewAdapter;
import com.my.beyondphysicsapplication.R;
import com.my.beyondphysicsapplication.TheApplication;
import com.my.beyondphysicsapplication.uihelp.ColorHelp;
import com.my.beyondphysicsapplication.uihelp.PopupWindowHelp;

import java.util.ArrayList;
import java.util.List;

public class PhizAndKeyboardFrameLayout extends FrameLayout {

    private final String[] phizs = {"(⌒▽⌒)", "(￣▽￣)", "(=・ω・=)", "(｀・ω・´)", "(〜￣△￣)〜", "(･∀･)", "(°∀°)ﾉ", "(￣3￣)", "╮(￣▽￣)╭", "( ´_ゝ｀)", "←_←", "→_→", "(<_<)", "(>_>)", "～(￣▽￣～)", "(;¬_¬)", "(▔□▔)/", "(ﾟДﾟ≡ﾟдﾟ)!?", "Σ(ﾟдﾟ;)", "Σ( ￣□￣||)", "(´；ω；`)", "（/TДT)/", "(^・ω・^ )", "(｡･ω･｡)", "(●￣(ｴ)￣●)", "ε=ε=(ノ≧∇≦)ノ", "(´･_･`)", "(-_-#)", "(￣へ￣)", "(￣ε(#￣) Σ", "ヽ(`Д´)ﾉ", "(╯°口°)╯(┴—┴", "（#-_-)┯━┯", "_(:3」∠)_", "^_^|||", "(×_×)", "π_π", "~>__<~", "(^人^)", "O(∩_∩)O~", "╮(╯3╰)╭"};

    private final Handler handler = new Handler();

    private Activity activity;

    private int colorPrimary;
    private int mainColorDeepen;
    private int mainColorText;

    private KeyboardDetectFrameLayout keyboardDetectFrameLayout;
    private FrameLayout frameLayoutBack;
    private ImageView imageViewPhiz;
    private ImageView imageViewSend;
    private EditText editTextComment;
    private RelativeLayout relativeLayoutPostToDynamic;
    private CheckBox checkBox;
    private RelativeLayout relativeLayoutGridView;
    private GridView gridView;

    private OnClickListener onClickListener;


    private Drawable drawableNormal_comment_input_phiz;
    private Drawable drawableNormal_comment_input_keyboard;
    private Drawable drawableNormal_comment_input_send;

    private Normal_CommentInput_String_GirdViewAdapter normal_CommentInput_String_GirdViewAdapter;

    private OnPhizAndKeyboardFrameLayoutListener onPhizAndKeyboardFrameLayoutListener;
    private int showingStatus = 0;
    private boolean unHidden = false;

    public PhizAndKeyboardFrameLayout(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PhizAndKeyboardFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PhizAndKeyboardFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @SuppressWarnings("ResourceType")
    private void getTypeArray() {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary, R.attr.mainColorDeepen, R.attr.mainColorText});
        try {
            colorPrimary = typedArray.getColor(0, 0xffffffff);
            mainColorDeepen = typedArray.getColor(1, 0xffffffff);
            mainColorText = typedArray.getColor(2, 0xffffffff);
        } finally {
            typedArray.recycle();
        }
    }

    private void initView(Context context) {
        activity = BaseActivity.getActivityByContext(context);
        getTypeArray();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.normal_comment_input, this, true);
        keyboardDetectFrameLayout = (KeyboardDetectFrameLayout) view.findViewById(R.id.keyboardDetectFrameLayout);
        frameLayoutBack = (FrameLayout) view.findViewById(R.id.frameLayoutBack);
        imageViewPhiz = (ImageView) view.findViewById(R.id.imageViewPhiz);
        imageViewSend = (ImageView) view.findViewById(R.id.imageViewSend);
        editTextComment = (EditText) view.findViewById(R.id.editTextComment);
        relativeLayoutPostToDynamic = (RelativeLayout) view.findViewById(R.id.relativeLayoutPostToDynamic);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        relativeLayoutGridView = (RelativeLayout) view.findViewById(R.id.relativeLayoutGridView);
        gridView = (GridView) view.findViewById(R.id.gridView);
        keyboardDetectFrameLayout.setOnKeyboardDetectFrameLayoutListener(new KeyboardDetectFrameLayout.OnKeyboardDetectFrameLayoutListener() {
            @Override
            public void onKeyboardStateChanged(boolean isActive, int keyboardHeight) {
                if (isActive) {
                    setLayoutParams(gridView, keyboardHeight);
                    showKeyboard();
                } else {
                    if (!unHidden) {
                        hidden();
                    }
                }
            }
        });
        editTextComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    showSoftInput(false, editTextComment);
                    hidden();
                    if (onPhizAndKeyboardFrameLayoutListener != null) {
                        onPhizAndKeyboardFrameLayoutListener.onSendComment(imageViewSend, editTextComment);
                    }
                    return true;
                }
                return false;
            }
        });
        onClickListener();
        drawableNormal_comment_input_phiz = activity.getResources().getDrawable(R.mipmap.normal_comment_input_phiz);
        drawableNormal_comment_input_keyboard = activity.getResources().getDrawable(R.mipmap.normal_comment_input_keyboard);
        drawableNormal_comment_input_send = activity.getResources().getDrawable(R.mipmap.normal_comment_input_send);
        ColorHelp.setImageViewDrawableTint(imageViewPhiz, drawableNormal_comment_input_phiz, mainColorText);
        ColorHelp.setImageViewDrawableTint(imageViewSend, drawableNormal_comment_input_send, mainColorText);
    }

    private void onClickListener() {
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.frameLayoutBack:
                        showSoftInput(false, editTextComment);
                        hidden();
                        break;
                    case R.id.imageViewPhiz:
                        if (showingStatus == 0) {
                            showPhiz();
                        } else if (showingStatus == 1) {
                            showSoftInput(true, editTextComment);
                            showKeyboard();
                        } else if (showingStatus == 2) {
                            imageViewPhiz.setEnabled(false);
                            unHidden = true;
                            showSoftInput(false, editTextComment);
                            showPhiz();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    unHidden = false;
                                    imageViewPhiz.setEnabled(true);
                                }
                            }, 500);
                        }
                        break;
                    case R.id.imageViewSend:
                        showSoftInput(false, editTextComment);
                        hidden();
                        if (onPhizAndKeyboardFrameLayoutListener != null) {
                            onPhizAndKeyboardFrameLayoutListener.onSendComment(imageViewSend, editTextComment);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        frameLayoutBack.setOnClickListener(onClickListener);
        imageViewPhiz.setOnClickListener(onClickListener);
        imageViewSend.setOnClickListener(onClickListener);
    }


    private void showSoftInput(boolean isOpen, EditText editText) {
        if (editText.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(PopupWindowHelp.WINDOWTOKENISNULL);
        } else {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                if (isOpen) {
                    editText.requestFocus();
                    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                } else {
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            }
        }
    }

    private void showPhiz() {
        if (showingStatus != 1) {
            frameLayoutBack.setVisibility(View.VISIBLE);
            relativeLayoutPostToDynamic.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.VISIBLE);
            ColorHelp.setImageViewDrawableTint(imageViewPhiz, drawableNormal_comment_input_phiz, colorPrimary);
            ColorHelp.setImageViewDrawableTint(imageViewSend, drawableNormal_comment_input_send, colorPrimary);
            if (normal_CommentInput_String_GirdViewAdapter == null) {
                final List<String> strings = new ArrayList<String>();
                for (int i = 0; i < phizs.length; i++) {
                    strings.add(phizs[i]);
                }
                normal_CommentInput_String_GirdViewAdapter = new Normal_CommentInput_String_GirdViewAdapter(activity, strings, gridView, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        editTextComment.getText().append(strings.get(position));
                        TheApplication.setCursorToLast(editTextComment);
                    }
                });
                gridView.setAdapter(normal_CommentInput_String_GirdViewAdapter);
            }
            showingStatus = 1;
        }
    }

    private void showKeyboard() {
        if (!isHorizontalScreen() && showingStatus != 2) {
            frameLayoutBack.setVisibility(View.VISIBLE);
            relativeLayoutPostToDynamic.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.INVISIBLE);
            ColorHelp.setImageViewDrawableTint(imageViewPhiz, drawableNormal_comment_input_keyboard, colorPrimary);
            ColorHelp.setImageViewDrawableTint(imageViewSend, drawableNormal_comment_input_send, colorPrimary);
            showingStatus = 2;
        }
    }

    private void hidden() {
        if (showingStatus != 0) {
            frameLayoutBack.setVisibility(View.INVISIBLE);
            relativeLayoutPostToDynamic.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            ColorHelp.setImageViewDrawableTint(imageViewPhiz, drawableNormal_comment_input_phiz, mainColorText);
            ColorHelp.setImageViewDrawableTint(imageViewSend, drawableNormal_comment_input_send, mainColorText);
            showingStatus = 0;
        }
    }

    private void setLayoutParams(View view, int height) {
        if (!isHorizontalScreen()) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    private boolean isHorizontalScreen() {
        boolean result = false;
        Configuration configuration = getContext().getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            result = true;
        }
        return result;
    }

    public KeyboardDetectFrameLayout getKeyboardDetectFrameLayout() {
        return keyboardDetectFrameLayout;
    }

    public FrameLayout getFrameLayoutBack() {
        return frameLayoutBack;
    }

    public ImageView getImageViewPhiz() {
        return imageViewPhiz;
    }

    public ImageView getImageViewSend() {
        return imageViewSend;
    }

    public EditText getEditTextComment() {
        return editTextComment;
    }

    public RelativeLayout getRelativeLayoutPostToDynamic() {
        return relativeLayoutPostToDynamic;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public RelativeLayout getRelativeLayoutGridView() {
        return relativeLayoutGridView;
    }

    public GridView getGridView() {
        return gridView;
    }

    public OnPhizAndKeyboardFrameLayoutListener getOnPhizAndKeyboardFrameLayoutListener() {
        return onPhizAndKeyboardFrameLayoutListener;
    }

    public void setOnPhizAndKeyboardFrameLayoutListener(OnPhizAndKeyboardFrameLayoutListener onPhizAndKeyboardFrameLayoutListener) {
        this.onPhizAndKeyboardFrameLayoutListener = onPhizAndKeyboardFrameLayoutListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    public interface OnPhizAndKeyboardFrameLayoutListener {
        void onSendComment(View view, EditText editTextComment);
    }

}
