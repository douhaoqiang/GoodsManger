package com.biology.common.divider;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * DESC 分割线工具
 * Created by douhaoqiang on 2017/11/17.
 */

public abstract class DividerFactory extends RecyclerView.ItemDecoration {


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawDivider(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        calculateItemOffsets(outRect, view, parent, state);
    }

    /**
     * 绘制分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    public abstract void drawDivider(Canvas c, RecyclerView parent, RecyclerView.State state);

    /**
     * 计算偏移量
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    public abstract void calculateItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state);

    public static Builder builder(@Nullable Context context) {
        if (context == null) {
            throw new NullPointerException("Context is null");
        }
        return new Builder(context);
    }

    /**
     * 添加分割线
     *
     * @param recyclerView target recyclerView
     */
    public void addTo(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(this);
    }

    public static class Builder {

        private final Context context;
        private Drawable drawable;
        private int columnSpace = 0;
        private int rowSpace = 0;
        private boolean hideLastDivider = true;

        private Builder(Context context) {
            this.context = context;
        }

        public LinearDivider buildLinearDivider() {
            return new LinearDivider(this);
        }

        public GridDivider buildGridDivider() {
            return new GridDivider(this);
        }

        public Builder setSpaceColor(@ColorRes int id, @DimenRes int strokeWidth) {
            this.drawable = new ColorDrawable(ContextCompat.getColor(context, id));
            this.columnSpace = this.rowSpace = context.getResources().getDimensionPixelSize(strokeWidth);
            return this;
        }

        public Builder setSpaceColor(@ColorRes int id) {
            this.drawable = new ColorDrawable(ContextCompat.getColor(context, id));
            return this;
        }

        public Builder setColumnSpace(@DimenRes int strokeWidth) {
            this.columnSpace = context.getResources().getDimensionPixelSize(strokeWidth);
            return this;
        }

        public Builder setRowSpace(@DimenRes int strokeWidth) {
            this.rowSpace = context.getResources().getDimensionPixelSize(strokeWidth);
            return this;
        }

        public Builder setSpace(@DimenRes int strokeWidth) {
            this.columnSpace = this.rowSpace = context.getResources().getDimensionPixelSize(strokeWidth);
            return this;
        }


        public Context getContext() {
            return context;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public Builder setDrawable(@DrawableRes int id) {
            this.drawable = ResourcesCompat.getDrawable(context.getResources(), id, context.getTheme());
            return this;
        }

        public int getColumnSpace() {
            return columnSpace;
        }

        public int getRowSpace() {
            return rowSpace;
        }

        public boolean isHideLastDivider() {
            return hideLastDivider;
        }

        public Builder setHideLastDivider(boolean hideLastDivider) {
            this.hideLastDivider = hideLastDivider;
            return this;
        }
    }

}
