package com.dhq.goodsmanger.http.http;

import android.util.Log;

import com.dhq.goodsmanger.http.BaseObserver;
import com.dhq.goodsmanger.http.MyIntercepter;
import com.dhq.goodsmanger.http.util.DataUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * DESC
 * Created by douhaoqiang on 2017/3/28.
 */

public class HttpUtil {

    private static String TAG = HttpUtil.class.getSimpleName();

    private static HttpUtil mHttpUtil;

    private static final MediaType mediaTypeJson = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType mediaTypeForm = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");


    private static final int CONNECT_TIMEOUT = 15;
    private static final int READ_TIMEOUT = 30;
    private static final String baseUrl = "";

    private Retrofit retrofit;
    private ApiService mApiService;

    private HttpUtil() {
        retrofitBuild();
    }

    public static HttpUtil getInstance() {
        if (mHttpUtil == null) {
            synchronized (HttpUtil.class) {
                if (mHttpUtil == null) {
                    mHttpUtil = new HttpUtil();
                }
            }
        }
        return mHttpUtil;
    }


    /**
     * 初始化网络请求
     */
    private void retrofitBuild() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new MyIntercepter())
                .build();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(new GsonBuilder().registerTypeAdapterFactory(new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                Class<T> rawType = (Class<T>) type.getRawType();
                if (rawType != String.class) {
                    return null;
                }
                return (TypeAdapter<T>) new StringNullAdapter();
            }
        }).create());


        if (mApiService==null){
            retrofit = new Retrofit.Builder()
                    .client(httpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
            mApiService = retrofit.create(ApiService.class);
        }

    }

    /**
     * get请求 添加参数
     *
     * @param url
     * @param paramMaps
     * @return
     */
    public void getHttpRequest(String url, HashMap<String, String> paramMaps, Observer observer) {

        mApiService.getHttpRequest(url, paramMaps).compose(new RxTransformer()).subscribe(observer);
    }

    /**
     * post请求 添加表单参数
     *
     * @param url
     * @param paramMaps
     * @param observer
     */
    public void postFormHttpRequest(String url, HashMap<String, Object> paramMaps, BaseObserver observer) {

        Log.d("请求参数", DataUtils.mapToJson(paramMaps));
        mApiService.postFormHttpRequest(baseUrl + url, paramMaps).compose(new RxTransformer()).subscribe(observer);

    }

    /**
     * 上传文件请求
     *
     * @param url
     * @return
     */
    public void uploadFileReq(String url, List<File> files, HashMap<String, String> paramMaps, BaseObserver observer) {

        MultipartBody.Builder builder = new MultipartBody.Builder();

        if (files == null) {
            Log.d(TAG, "必须添加一个文件");
            return;
        }

        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.setType(MultipartBody.FORM)
                    .addFormDataPart("pic" + i, file.getName(), imageBody);
        }
        if (paramMaps != null) {
            Iterator<Map.Entry<String, String>> iterator = paramMaps.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        mApiService.uploadFileReq(baseUrl + url, parts).compose(new RxTransformer()).subscribe(observer);
    }


    /**
     * 上传文件请求
     *
     * @param url
     * @return
     */
    public void uploadFileReq(String url, HashMap<String, File> fileMaps, HashMap<String, String> paramMaps, BaseObserver observer) {

        MultipartBody.Builder builder = new MultipartBody.Builder();

        if (fileMaps == null || fileMaps.size()==0) {
            Log.d(TAG, "必须添加一个文件");
            return;
        }

        for (Map.Entry<String, File> entry : fileMaps.entrySet()) {
            File file = entry.getValue();
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.setType(MultipartBody.FORM)
                    .addFormDataPart(entry.getKey(), file.getName(), imageBody);
        }

        if (paramMaps != null) {
            Iterator<Map.Entry<String, String>> iterator = paramMaps.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        mApiService.uploadFileReq(baseUrl + url, parts).compose(new RxTransformer()).subscribe(observer);
    }

    /**
     * 上传文件请求
     *
     * @param url
     * @return
     */
    public void uploadFileReq(String url, File file, HashMap<String, String> paramMaps, BaseObserver observer) {

        MultipartBody.Builder builder = new MultipartBody.Builder();

        if (file == null) {
            Log.d(TAG, "必须添加一个文件");
            return;
        }

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("media", file.getName(), imageBody);
        if (paramMaps != null) {
            Iterator<Map.Entry<String, String>> iterator = paramMaps.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
//        builder.addFormDataPart("phone", "phone");
        List<MultipartBody.Part> parts = builder.build().parts();
        mApiService.uploadFileReq(baseUrl + url, parts).compose(new RxTransformer()).subscribe(observer);
    }

    /**
     * post请求 添加表json参数
     *
     * @param url
     * @param paramMaps
     * @return
     */
    public void postJsonHttpRequest(String url, HashMap<String, String> paramMaps, Observer observer) {

        String jsonParam = DataUtils.mapToJson(paramMaps);
        RequestBody body = RequestBody.create(mediaTypeJson, jsonParam);

//        Observable<BaseResponse> observable = mApiService.postJsonHttpRequest(url, body).map(new HttpResultFunc());
//        toSubscribe(observable, observer);

        mApiService.postJsonHttpRequest(url, body).compose(new RxTransformer<>()).subscribe(observer);

    }

    /**
     * post请求 添加表json参数
     *
     * @param url
     * @param paramMaps
     * @return
     */
    public void postJsonHttpRequest(String url, Object paramMaps, Observer observer) {

//        String jsonParam = DataUtils.gsonObjectToJson(paramMaps);
//        RequestBody body = RequestBody.create(mediaTypeJson, jsonParam);
//
//        Observable<BaseResponse> observable = mApiService.postJsonHttpRequest(url, body).map(new HttpResultFunc());
//        toSubscribe(observable, observer);
    }


    public <T> void toSubscribe(Observable<T> observable, Observer<T> subscriber) {
        observable
                /*
                订阅关系发生在IO线程中
                 */
                .subscribeOn(Schedulers.io())
                /*
                解除订阅关系也发生在IO线程中
                 */
//                .unsubscribeOn(Schedulers.io())
                /*
                指定subscriber (观察者)的回调在主线程中，
                observeOn的作用是指定subscriber（观察者）将会在哪个Scheduler观察这个Observable,
                由于subscriber已经能取到界面所关心的数据了，所以设定指定subscriber的回调在主线程中
                 */
                .observeOn(AndroidSchedulers.mainThread())
                /*
                订阅观察者，subscribe就相当于setOnclickListener()
                 */
                .subscribe(subscriber);
        //subscribeOn影响的是它调用之前的代码（也就是observable），observeOn影响的是它调用之后的代码（也就是subscribe()）
    }

    public class RxTransformer<T> implements ObservableTransformer<T, T> {

        @Override
        public ObservableSource apply(Observable<T> observable) {
            Observable<T> tObservable = observable
                    /*
                    订阅关系发生在IO线程中
                     */
                    .subscribeOn(Schedulers.io())
//                    /*
//                    解除订阅关系也发生在IO线程中
//                     */
//                    .unsubscribeOn(Schedulers.io())
                    /*
                    指定subscriber (观察者)的回调在主线程中，
                    observeOn的作用是指定subscriber（观察者）将会在哪个Scheduler观察这个Observable,
                    由于subscriber已经能取到界面所关心的数据了，所以设定指定subscriber的回调在主线程中
                     */
                    .observeOn(AndroidSchedulers.mainThread());
            return tObservable;
        }
    }

}
