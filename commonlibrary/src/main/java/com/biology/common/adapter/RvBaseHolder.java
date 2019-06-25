package com.biology.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * DESC RecycleView的公用ViewHolder
 * Created by douhaoqiang on 2016/9/7.
 */

public class RvBaseHolder extends RecyclerView.ViewHolder {
    /**
     * save view ids
     */
    private SparseArray<View> mSparseArray;

    private int mViewType;

    private View rootView;

    public RvBaseHolder(View itemView, int viewType) {
        super(itemView);
        this.mSparseArray = new SparseArray<>();
        this.mViewType = viewType;
        this.rootView = itemView;
    }

    /**
     * 获取跟布局
     *
     * @return
     */
    public View getRootView() {
        return rootView;
    }

    /**
     * 获取布局类型
     *
     * @return
     */
    public int getViewType() {
        return mViewType;
    }

    /**
     * 根据id获取控件
     *
     * @param resId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int resId) {
        View view = mSparseArray.get(resId);
        if (view == null) {
            view = rootView.findViewById(resId);
            mSparseArray.put(resId, view);
        }
        return (T) view;
    }

}
