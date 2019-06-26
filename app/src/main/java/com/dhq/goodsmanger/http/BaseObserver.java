package com.dhq.goodsmanger.http;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.biology.common.base.BaseDialogFragment;
import com.biology.common.base.CommDialog;
import com.biology.common.base.ViewConvertListener;
import com.biology.common.base.ViewHolder;
import com.biology.common.util.ToastUtils;
import com.dhq.goodsmanger.R;
import com.dhq.goodsmanger.entity.BaseResponse;
import com.dhq.goodsmanger.view.ProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * DESC
 * Created by douhaoqiang on 2017/2/14.
 */
public abstract class BaseObserver<T> implements Observer<BaseResponse> {
    private static final String TAG = "BaseObserver";
    private Gson gson = new Gson();
    private Context mContext;
    private Disposable mDisposable;
    private ProgressDialog myDialog;
    private boolean mLastShow=true; //是否显示错误弹框

    private BaseResponse mResponse;

    /**
     * 显示弹框(请求前后都显示)
     *
     * @param context
     */
    public BaseObserver(Context context) {
        mContext = context;
        myDialog = ProgressDialog.getInstance(mContext);
//        myDialog.setCancelable(false);
//        myDialog.setCanceledOnTouchOutside(false);

//        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                //弹框消失，取消网络请求
//                cancle();
//            }
//        });

    }

    /**
     * 显示弹框
     *
     * @param context
     * @param lastShow 后弹框
     */
    public BaseObserver(Context context,boolean lastShow) {
        mContext = context;
        mLastShow = lastShow;
        myDialog = ProgressDialog.getInstance(mContext);

//        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                //弹框消失，取消网络请求
//                cancle();
//            }
//        });

    }

    /**
     * 不显示弹框
     */
    public BaseObserver() {

    }


    @Override
    public void onSubscribe(Disposable d) {
        this.mDisposable = d;

//        if (!NetWorkUtils.isNetworkConnected()) {
//            ToastUtils.showToastLong( mContext,"当前网络不可用，请检查网络情况");
//
//            fail("");
//            cancle();
//            return;
//        }

        showWaitingDialog();
    }

    @Override
    public void onNext(BaseResponse response) {

        mResponse=response;


    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, e.toString());
        hintWaitingDialog();
        showError("网络请求失败！");

    }

    @Override
    public void onComplete() {
        hintWaitingDialog();
//        if (responseCallback != null) {
//            responseCallback.onComplete();
//        }

        if (mResponse == null) {
            showError("请求数据错误");
            return;
        }
        if (!"ok".equals(mResponse.getStatus())) {
            showError(mResponse.getMsg());
            return;
        }

        try {
//            Class<T> entityClass = getEntityClass();
            ParameterizedType parameterizedType = (ParameterizedType) this.getClass()
                    .getGenericSuperclass();
            Type type = parameterizedType.getActualTypeArguments()[0];
            if (type != null) {
                T result = gson.fromJson(mResponse.getData(), type);
                success(result);

            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            showError("解析数据失败！");
        }


    }

    private void showError(final String msg){
//        CrashReport.postCatchedException(new Exception(msg));//上报错误信息
        if (mContext!=null && mLastShow){
            //显示错误弹框
//            Toasts.showToastShort(msg);
            CommDialog.init()
                    .setLayoutId(R.layout.dialog_error_lay)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        public void convertView(ViewHolder holder, final BaseDialogFragment dialog) {
                            TextView tvMsg = holder.getView(R.id.tv_msg);
                            TextView tvComfirm = holder.getView(R.id.tv_comfirm);
                            tvComfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            tvMsg.setText(msg);
                        }
                    }).show(((FragmentActivity)mContext).getSupportFragmentManager());
        }

        fail(msg);
    }


    /**
     * 解除网络请求绑定
     */
    public void cancle() {
        mDisposable.dispose();
    }


    /**
     * 显示网络请求等待框
     */
    private void showWaitingDialog() {
        if (myDialog != null) {
            myDialog.show();
        }
    }

    /**
     * 取消等待框
     */
    private void hintWaitingDialog() {
        if (myDialog != null) {
            myDialog.dismiss();
        }
    }


    /**
     * 获取泛型T的Class
     *
     * @return
     */
    public Class<T> getEntityClass() {


        Type t = getClass().getGenericSuperclass();
        Class<T> entityClass = null;
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            entityClass = (Class<T>) p[0];
        }
        return entityClass;
    }


    /**
     * 请求成功
     *
     * @param result 请求数据
     */
    public abstract void success(T result);

    /**
     * 请求失败
     *
     * @param msg 失败信息
     */
    public abstract void fail(String msg);


}
