package com.biology.common.widget.banner;

import android.support.annotation.DrawableRes;


public interface IBannerIndicator<T> {


    /**
     * 设置指示器之间的间隔
     * @param spaceSize
     * @return
     */
    IBannerIndicator setIndicatorSpace(int spaceSize);

    /**
     * 设置指示器的默认图片
     * @param pageIndicatorId
     * @return
     */
    IBannerIndicator setNormalIndicatorRes(@DrawableRes int pageIndicatorId);

    /**
     * 设置指示器的选中图片
     * @param pageIndicatorId
     * @return
     */
    IBannerIndicator setSelectIndicatorRes(@DrawableRes int pageIndicatorId);

    /**
     * 绑定BannerView
     * @param bannerView
     */
    void attchBannerView(BannerView bannerView);

    /**
     * 设置指示器的选中项
     * @param index
     * @return
     */
    void onIndicatorSelected(int index);

}
