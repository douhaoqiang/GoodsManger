package com.biology.common.util;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biology.common.R;


/**
 * DESC
 * Created by douhaoqiang on 2016/12/2.
 */
public class HeaderUtil {
    private static final String TAG = "HeaderUtil";
    private RelativeLayout id_header_bg;

    private View rootView;
    private Activity activity;

    private ImageView LeftIv;
    private TextView left_tv;
    private TextView title_tv;
    private ImageView right_iv;
    private TextView right_tv;

    public HeaderUtil(Activity activity, View rootView) {
        this.rootView = rootView;
        this.activity = activity;
        initView();
    }

    private void initView() {
        id_header_bg = rootView.findViewById(R.id.id_header);
        if (id_header_bg == null) {
            return;
        }
        LeftIv = rootView.findViewById(R.id.iv_header_left);
        title_tv = rootView.findViewById(R.id.tv_header_title);
        left_tv = rootView.findViewById(R.id.tv_header_left);

        right_iv = rootView.findViewById(R.id.iv_header_right);
        right_tv = rootView.findViewById(R.id.tv_header_right);

        if (rootView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout relativeLayout = rootView.findViewById(R.id.id_header_relative_layout);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
            layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);
            relativeLayout.setLayoutParams(layoutParams);
        }

        setLeftBack();
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    private int getStatusBarHeight() {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return activity.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 显示左侧返回按钮
     *
     * @return
     */
    public HeaderUtil setLeftBack() {
        return setLeftBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }
    /**
     * 显示左侧返回按钮
     *
     * @return
     */
    public HeaderUtil setLeftBack(View.OnClickListener clickListener) {
        return setLeftBack(0,clickListener);
    }


    /**
     * 显示左侧返回按钮
     *
     * @return
     */
    public HeaderUtil setLeftBack(int serid,View.OnClickListener clickListener) {
        left_tv.setVisibility(View.GONE);
        LeftIv.setVisibility(View.VISIBLE);
        if (serid!=0) {
            LeftIv.setImageResource(serid);
        }
        LeftIv.setOnClickListener(clickListener);
        return this;
    }


    /**
     * 设置中间标题
     *
     * @param title 文字标题
     * @return
     */
    public HeaderUtil setHeaderTitle(String title) {
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(title);
        return this;
    }

    /**
     * 设置中间标题字体
     *
     * @param textSize 文字字体
     * @return
     */
    public HeaderUtil setHeaderTitleSize(@DimenRes int textSize) {
        title_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.getResources().getDimensionPixelSize(textSize));
        return this;
    }

    /**
     * 设置中间标题字体
     *
     * @param title 文字字体
     * @return
     */
    public HeaderUtil setHeaderTitleColor(int title) {
        title_tv.setTextColor(title);
        return this;
    }

    /**
     * 设置中间标题
     *
     * @param resId 资源id
     * @return
     */
    public HeaderUtil setHeaderTitle(int resId) {
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(resId);
        return this;
    }


    /**
     * 设置左侧图片
     *
     * @param resId           资源id
     * @param onClickListener 点击事件
     * @return
     */
    public HeaderUtil setHeaderLeftImage(int resId, View.OnClickListener onClickListener) {
        left_tv.setVisibility(View.GONE);
        LeftIv.setVisibility(View.VISIBLE);
        LeftIv.setImageResource(resId);
        if (onClickListener != null) {
            LeftIv.setOnClickListener(onClickListener);
        }
        return this;
    }

    /**
     * 设置右侧图片
     *
     * @param resId           资源id
     * @param onClickListener 点击事件
     * @return
     */
    public HeaderUtil setHeaderRightImage(int resId, View.OnClickListener onClickListener) {
        right_tv.setVisibility(View.GONE);
        right_iv.setVisibility(View.VISIBLE);
        right_iv.setImageResource(resId);
        if (onClickListener != null) {
            right_iv.setOnClickListener(onClickListener);
        }
        return this;
    }

    /**
     * 设置左侧文字
     *
     * @param str             文字
     * @param onClickListener 点击事件
     * @return
     */
    public HeaderUtil setHeaderLeftText(String str, View.OnClickListener onClickListener) {
        LeftIv.setVisibility(View.GONE);
        left_tv.setVisibility(View.VISIBLE);
        left_tv.setText(str);
        if (onClickListener != null) {
            left_tv.setOnClickListener(onClickListener);
        }
        return this;
    }

    /**
     * 设置左侧文字大小
     *
     * @param strSizeId 文字大小
     * @return
     */
    public HeaderUtil setHeaderLeftTextSize(@DimenRes int strSizeId) {
        int textSize = activity.getResources().getDimensionPixelSize(strSizeId);
        left_tv.setTextSize(textSize);
        return this;
    }

    /**
     * 设置左侧文字颜色
     *
     * @param colorId 文字颜色
     * @return
     */
    public HeaderUtil setHeaderLeftTextColor(@ColorRes int colorId) {
        int color = ContextCompat.getColor(activity, colorId);
        left_tv.setTextColor(color);
        return this;
    }

    /**
     * 设置右侧文字大小
     *
     * @param strSizeId 文字大小
     * @return
     */
    public HeaderUtil setHeaderRightTextSize(@DimenRes int strSizeId) {
        int textSize = activity.getResources().getDimensionPixelSize(strSizeId);
        right_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        return this;
    }

    /**
     * 设置左侧文字颜色
     *
     * @param colorId 文字颜色
     * @return
     */
    public HeaderUtil setHeaderRightTextColor(@ColorRes int colorId) {
        int color = ContextCompat.getColor(activity, colorId);
        right_tv.setTextColor(color);
        return this;
    }


    /**
     * 设置右侧文字
     *
     * @param str             文字
     * @param onClickListener 点击事件
     * @return
     */
    public HeaderUtil setHeaderRightText(String str, View.OnClickListener onClickListener) {
        right_iv.setVisibility(View.GONE);
        right_tv.setVisibility(View.VISIBLE);
        right_tv.setText(str);
        if (onClickListener != null) {
            right_tv.setOnClickListener(onClickListener);
        }
        return this;
    }

    /**
     * 设置右侧文字
     *
     * @param resId           资源id
     * @param onClickListener 点击事件
     * @return
     */
    public HeaderUtil setHeaderRightText(@StringRes int resId, View.OnClickListener onClickListener) {

        right_iv.setVisibility(View.GONE);
        right_tv.setVisibility(View.VISIBLE);
        right_tv.setText(resId);
        if (onClickListener != null) {
            right_tv.setOnClickListener(onClickListener);
        }

        return this;
    }

    /**
     * 设置右侧文字
     *
     * @param str 文字
     * @return
     */
    public HeaderUtil setHeaderRightText(String str) {
        right_iv.setVisibility(View.GONE);
        right_tv.setVisibility(View.VISIBLE);
        right_tv.setText(str);
        return this;
    }

    public HeaderUtil setHeaderBg(int serid) {
        id_header_bg.setBackgroundResource(serid);
        return this;
    }

    public HeaderUtil setHeaderHint() {
        id_header_bg.setVisibility(View.GONE);
        return this;
    }
}
