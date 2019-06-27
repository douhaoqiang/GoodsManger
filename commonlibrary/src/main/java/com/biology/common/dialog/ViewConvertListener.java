package com.biology.common.dialog;

import java.io.Serializable;

public interface ViewConvertListener extends Serializable {
    long serialVersionUID = System.currentTimeMillis();

    void convertView(ViewHolder holder, BaseDialogFragment dialog);
}
