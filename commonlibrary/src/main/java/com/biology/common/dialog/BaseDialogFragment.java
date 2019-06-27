package com.biology.common.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.biology.common.R;

import java.lang.reflect.Method;


/**
 * 基础dialogfragment
 */
public abstract class BaseDialogFragment extends DialogFragment {
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DIM = "dim_amount";
    private static final String BOTTOM = "show_bottom";
    private static final String CANCEL = "out_cancel";
    private static final String LAYOUT = "layout_id";

    public static final int GRAVITY_LEFT = 0;
    public static final int GRAVITY_TOP = 1;
    public static final int GRAVITY_BOTTOM = 2;
    public static final int GRAVITY_RIGHT = 3;
    public static final int GRAVITY_CENTER = 4;

    @IntDef({GRAVITY_LEFT, GRAVITY_TOP, GRAVITY_BOTTOM, GRAVITY_RIGHT, GRAVITY_CENTER})
    public @interface GravityType {
        //对齐方式
    }

    private int width;//宽度
    private int height;//高度
    private float dimAmount = 0.5f;//灰度深浅
    private boolean outCancel = true;//是否点击外部取消
    @StyleRes
    private int animStyle;
    @LayoutRes
    protected int layoutId;

    private View mContentView;

    @GravityType
    private int mGravityType = GRAVITY_CENTER;


    public abstract int intLayoutId();

    public abstract void convertView(ViewHolder holder, BaseDialogFragment dialog);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.NiceDialog);
        layoutId = intLayoutId();

//        //恢复保存的数据
//        if (savedInstanceState != null) {
//            width = savedInstanceState.getInt(WIDTH);
//            height = savedInstanceState.getInt(HEIGHT);
//            dimAmount = savedInstanceState.getFloat(DIM);
//            showBottom = savedInstanceState.getBoolean(BOTTOM);
//            outCancel = savedInstanceState.getBoolean(CANCEL);
//            layoutId = savedInstanceState.getInt(LAYOUT);
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.dialog_base_lay, container, false);
        mContentView = inflater.inflate(layoutId, view, false);
        view.addView(mContentView);
        convertView(ViewHolder.create(view), this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

//    /**
//     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
//     *
//     * @param outState
//     */
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt(WIDTH, width);
//        outState.putInt(HEIGHT, height);
//        outState.putFloat(DIM, dimAmount);
//        outState.putBoolean(BOTTOM, showBottom);
//        outState.putBoolean(CANCEL, outCancel);
//        outState.putInt(LAYOUT, layoutId);
//    }

    private void initParams() {

        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.dimAmount = 0;//window背景为透明颜色，颜色透明度利用本布局颜色
            window.setAttributes(lp);
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mContentView.getLayoutParams();
        //是否在底部显示
        if (mGravityType == GRAVITY_BOTTOM) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            //从下向上飘入动画（）
//            TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                    1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//            mHiddenAction.setDuration(500);
//            mContentView.startAnimation(mHiddenAction);
        } else if (mGravityType == GRAVITY_RIGHT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else if (mGravityType == GRAVITY_LEFT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (mGravityType == GRAVITY_TOP) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        }

        //设置dialog背景的透明度(from=0,to=255)
        int alpha = (int) (dimAmount * 255);
        getView().getBackground().setAlpha(alpha);

        mContentView.setLayoutParams(layoutParams);

        setCancelable(outCancel);
        if (outCancel) {
            getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            mContentView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置透明状态栏,这样才能让 ContentView 向上
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (checkDeviceHasNavigationBar(getActivity())) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }

        }


    }


    public BaseDialogFragment setDimAmount(float dimAmount) {
        if (dimAmount >= 0 && dimAmount <= 1) {
            this.dimAmount = dimAmount;
        }
        return this;
    }

    public BaseDialogFragment setShowBottom(boolean showBottom) {
        this.mGravityType = GRAVITY_BOTTOM;
        return this;
    }

    public BaseDialogFragment setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public BaseDialogFragment setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public BaseDialogFragment setGravityMode(@GravityType int gravityType) {
        this.mGravityType = gravityType;
        return this;
    }


    public BaseDialogFragment show(FragmentManager manager) {
        super.show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    private int getStatusBarHeight() {
        // 获得状态栏高度
        int resourceId = getActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getActivity().getResources().getDimensionPixelSize(resourceId);
    }

    //判断是否有虚拟按键
    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }


}
