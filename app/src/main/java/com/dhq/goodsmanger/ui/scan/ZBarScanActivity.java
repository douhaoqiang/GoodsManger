package com.dhq.goodsmanger.ui.scan;

import android.os.Vibrator;
import android.util.Log;

import com.biology.common.base.BaseActivity;
import com.dhq.goodsmanger.R;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.BarcodeFormat;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * DESC
 *
 * @author douhaoqiang
 * @date 2019/6/26.
 */
public class ZBarScanActivity extends BaseActivity implements QRCodeView.Delegate{

    private ZBarView mZBarView;

    @Override
    protected int getLayout() {
        return R.layout.activity_bar_scan_lay;
    }

    @Override
    protected void initializeData() {
        mZBarView = findViewById(R.id.zbarview);
        mZBarView.setDelegate(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZBarView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    /**
     * 开启或关闭闪光灯
     */
    private void openCloseflashlight(boolean isOpen){
        if (isOpen){
            mZBarView.openFlashlight(); // 打开闪光灯
        }else {
            mZBarView.closeFlashlight(); // 关闭闪光灯
        }
    }

    private void startSpot(){

        mZBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式

        List<BarcodeFormat> formatList = new ArrayList<>();
        formatList.add(BarcodeFormat.QRCODE);
        formatList.add(BarcodeFormat.ISBN13);
        formatList.add(BarcodeFormat.UPCA);
        formatList.add(BarcodeFormat.EAN13);
        formatList.add(BarcodeFormat.CODE128);
        mZBarView.setType(BarcodeType.CUSTOM, formatList); // 自定义识别的类型

        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        vibrate();
        mZBarView.stopSpot();//停止识别
        mZBarView.startSpot(); // 开始识别
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZBarView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZBarView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZBarView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }



}
