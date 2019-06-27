package com.biology.common.widget.banner;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.biology.common.R;

import java.util.ArrayList;
import java.util.List;



public class BannerDefaultIndicator<T> extends LinearLayout implements IBannerIndicator<T> {

    List<T> mDatas;
    private int mIndicatorSpace = 0;
    private int pageNormalIndicatorRes = R.drawable.banner_dot_normal;
    private int pageSelectIndicatorRes = R.drawable.banner_dot_select;
    private ArrayList<ImageView> mPointViews = new ArrayList<>();
    LinearLayout hView;
    int position = 0;
    private BannerView mBannerView;

    public BannerDefaultIndicator(@NonNull Context context) {
        this(context, null);
    }

    public BannerDefaultIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerDefaultIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        hView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.default_indicator, this, true);
        hView.setOrientation(LinearLayout.HORIZONTAL);
    }

    /**
     * 绑定BannerView
     *
     * @param bannerView
     * @return
     */
    @Override
    public void attchBannerView(BannerView bannerView) {
        this.mBannerView = bannerView;
        notifyDataChangedView();
    }


    @Override
    public IBannerIndicator setIndicatorSpace(int spaceSize) {
        this.mIndicatorSpace = spaceSize;
        return this;
    }

    /**
     * 设置默认图片
     *
     * @param pageIndicatorId
     * @return
     */
    @Override
    public BannerDefaultIndicator setNormalIndicatorRes(@DrawableRes int pageIndicatorId) {
        this.pageNormalIndicatorRes = pageIndicatorId;
        return this;
    }

    /**
     * 设置选中时图片
     *
     * @param pageIndicatorId
     * @return
     */
    @Override
    public BannerDefaultIndicator setSelectIndicatorRes(@DrawableRes int pageIndicatorId) {
        this.pageSelectIndicatorRes = pageIndicatorId;
        return this;
    }


    public void notifyDataChangedView() {
        this.position = 0;
        mPointViews.clear();
        hView.removeAllViews();
        mDatas = mBannerView.getDatas();

        for (int index = 0; index < mDatas.size(); index++) {
            // 翻页指示的点
            ImageView pointView = new ImageView(getContext());
            if (index==0){
                pointView.setPadding(0, 0, 0, 0);
            }else {
                pointView.setPadding(mIndicatorSpace, 0, 0, 0);
            }
            if (mPointViews.isEmpty()) {
                pointView.setImageResource(pageSelectIndicatorRes);
            } else {
                pointView.setImageResource(pageNormalIndicatorRes);
            }
            mPointViews.add(pointView);
            hView.addView(pointView);
        }
        onIndicatorSelected(position);
    }


    @Override
    public void onIndicatorSelected(int index) {
        // 指示器
        this.position = index;
        for (int i = 0; i < mPointViews.size(); i++) {
            mPointViews.get(i).setImageResource(pageSelectIndicatorRes);
            if (index != i) {
                mPointViews.get(i).setImageResource(pageNormalIndicatorRes);
            }
        }
    }


}
