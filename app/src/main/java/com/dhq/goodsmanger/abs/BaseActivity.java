package com.dhq.goodsmanger.abs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.dhq.goodsmanger.R;
import com.dhq.goodsmanger.util.HeaderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 *
 * @author Administrator
 * @date 2017/10/26
 */
public abstract class BaseActivity extends AppCompatActivity implements MvpView {
    protected static String TAG = "BaseActivity";

    private HeaderUtil headerUtil;
    private Unbinder unbinder;

    public static final int REQUEST_CODE_CALLBACK = 0x1000;
    public static final String EXTRA_ACTIVITY_NAME = "_extra_activity_name";
    public static final String EXTRA_START_CALLBACK = "_extra_start_callback";


    private List<MvpPresenterIml> mvpPresenterImls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        startTransition();
        TAG = getClass().getSimpleName();
        super.onCreate(savedInstanceState);
        View contentView = getLayoutInflater().inflate(R.layout.activity_base, null);
        setContentView(contentView);

        setView();
        headerUtil = new HeaderUtil(this, contentView);
        unbinder = ButterKnife.bind(this);
        initStatusBar();
        initializeData();
    }


    protected void initStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置透明状态栏,这样才能让 ContentView 向上
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void addPresenter(MvpPresenterIml presenterIml) {
        if (mvpPresenterImls == null) {
            mvpPresenterImls = new ArrayList<>();
        }
        mvpPresenterImls.add(presenterIml);
    }

    public void cleanPresenter() {
        if (mvpPresenterImls != null && mvpPresenterImls.size() > 0) {
            int size = mvpPresenterImls.size();
            for (int i = 0; i < size; i++) {
                MvpPresenterIml mvpPresenterIml = mvpPresenterImls.get(i);
                if (mvpPresenterIml != null) {

                }
                mvpPresenterIml.detachView(false);
            }
        }
    }

    protected void startTransition() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    protected void stopTransition() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
        cleanPresenter();
    }

    @Override
    public void finish() {
        super.finish();
        stopTransition();
    }


    /**
     * 设置Activity的内容布局，取代setContentView（）方法
     */
    public void setView() {
        LinearLayout content_linear = this.findViewById(R.id.content_view);
        content_linear.addView(View.inflate(this, getLayout(), null),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }


    /**
     * 启动一个插入型Activity，自带转场
     * 例如，当你在A中启动B，如果你想在B中启动C，同时关闭A
     * 则在A中使用startCallbackActivity启动B
     * 在B中使用startResponseActivity启动C即可
     *
     * @param intent
     */
    public void startCallbackActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE_CALLBACK);
    }

    /**
     * 启动一个Activity，自带转场，同时关闭自己
     * 例如，当你在A中启动B，如果你想在B中启动C，同时关闭B
     * 则在A中使用startCallbackActivity启动B
     * 在B中使用startResponseActivity，关闭B并启动C即可
     *
     * @param intent
     */
    public void startResponseActivity(Intent intent) {
//        Logger.i(TAG, "startResponseActivity：" + "   intent is null： " + (intent == null));
        setResult(Activity.RESULT_FIRST_USER, intent);
        finish();
    }

    /**
     * @param clazz
     */
    public void finishToActivity(Class<? extends Activity> clazz) {
//        Logger.i(TAG, "finishToActivity clazz:" + clazz.getName());
        if (!getClass().getCanonicalName().equals(clazz.getCanonicalName())) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_FIRST_USER, intent);
            intent.putExtra(EXTRA_ACTIVITY_NAME, clazz.getCanonicalName());
            finish();
        }
    }

    /**
     * 关闭从[自己到目标clazz]之间的所有Activity，并启动一个新的Activity
     * 例如，当你在A中启动B，B中启动C->D->E->F。此时想启动Z，同时关闭之前除A以外所有Activity
     * 则在从A到Y启动Activity都使用startCallbackActivity方法。而在Y->Z使用
     * startResponseActivityFromAssignedActivity，第一个参数选Z的intent，
     * 第二个参数选用想要关闭的最前面一个Activity也就是B。
     * <p>
     * 如果仅仅是想关闭从Z到A的所有Activity而不开新的Activity，则此处传入一个不带class和Action的干净的 Intent 即可
     *
     * @param clazz  打开新Activity时所要关闭的最后一个Activity
     * @param intent
     */
    public void startResponseActivityFromAssignedActivity(Intent intent, Class<? extends Activity> clazz) {
//        Logger.i(TAG, "startResponseActivityFromAssignedActivity  intent:" + intent + " clazz:" + clazz.getName());
        if (getClass().getCanonicalName().equals(clazz.getCanonicalName())) {
            startResponseActivity(intent);
        } else {
            setResult(Activity.RESULT_FIRST_USER, intent);
            intent.putExtra(EXTRA_ACTIVITY_NAME, clazz.getCanonicalName());
            finish();
        }
    }


    /**
     * 隐藏软键盘
     */
    protected void hideKeyBoard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    public HeaderUtil getHeaderUtil() {
        return headerUtil;
    }

    protected abstract int getLayout();

    protected abstract void initializeData();

}
