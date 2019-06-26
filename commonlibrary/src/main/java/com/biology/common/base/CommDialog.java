package com.biology.common.base;

import android.support.annotation.LayoutRes;

/**
 * 通用Dialogfragment
 */
public class CommDialog extends BaseDialogFragment {
    private ViewConvertListener convertListener;

    public static CommDialog init() {
        return new CommDialog();
    }

    @Override
    public int intLayoutId() {
        return layoutId;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialogFragment dialog) {
        if (convertListener != null) {
            convertListener.convertView(holder, dialog);
        }
    }


    public CommDialog setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public CommDialog setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }
}
