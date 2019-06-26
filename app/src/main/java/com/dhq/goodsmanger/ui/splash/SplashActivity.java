package com.dhq.goodsmanger.ui.splash;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.biology.common.util.SysUtils;
import com.dhq.goodsmanger.R;
import com.dhq.goodsmanger.abs.BaseActivity;
import com.dhq.goodsmanger.ui.login.LoginActivity;
import com.dhq.goodsmanger.ui.scan.ZBarScanActivity;


/**
 * Created by Administrator on 2018/5/31.
 */

public class SplashActivity extends BaseActivity {


    private TextView tvTime;

    private boolean isReqSuccess = false;//判断是否可以进入应用
    private boolean isTimeFinish = false;//判断是否可以进入应用

    //
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isReqSuccess = true;
            startApp();
        }

    };

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initializeData() {

        getHeaderUtil().setHeaderHint();
        tvTime = findViewById(R.id.tv_time);
        startTime();
        startPage();
    }


    public void startTime() {

        /** 倒计时60秒，一次1秒 */
        new CountDownTimer(4 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                tvTime.setText("0");
                isTimeFinish = true;
                startApp();
            }
        }.start();


    }

    /**
     * 开启应用
     */
    private void startApp() {

        if (isReqSuccess && isTimeFinish) {
            //时间到了 并且请求成功 则可以跳转进入应用
            startPage();
        }

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        SysUtils.exitApp(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void startPage() {
//        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        Intent intent = new Intent(SplashActivity.this, ZBarScanActivity.class);
        startActivity(intent);
        finish();
    }


}
