package com.dhq.goodsmanger.ui.scan;


import com.biology.common.base.BaseActivity;
import com.dhq.goodsmanger.R;
import com.dhq.goodsmanger.ui.scan.ocr.OCRUtils;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * DESC
 *
 * @author douhaoqiang
 * @date 2019/6/26.
 */
public class TextScanActivity extends BaseActivity {


    @Override
    protected int getLayout() {
        return R.layout.activity_text_scan_lay;
    }

    @Override
    protected void initializeData() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                OCRUtils.OCRParseText();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });
    }

}
