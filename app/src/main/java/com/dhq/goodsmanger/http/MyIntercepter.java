package com.dhq.goodsmanger.http;

import android.util.Log;

import com.biology.common.BuildConfig;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

import static okhttp3.internal.Util.UTF_8;

/**
 * DESC 请求拦截类
 * Created by douhaoqiang on 2016/11/9.
 */
public class MyIntercepter implements Interceptor {
    private static final String TAG = "MyIntercepter";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oriRequest = chain.request();
        Response response = chain.proceed(getNewRequest(oriRequest));
        if (BuildConfig.DEBUG) {
            responseModify(response);
        }
        return response;
    }

    /**
     * 在请求中添加公用信息
     *
     * @param oriRequest 原始请求信息
     * @return
     */
    private Request getNewRequest(Request oriRequest) {
        Request newRequest = oriRequest.newBuilder()
                .header("token", "oneself_token")
                .build();

//        printParams(newRequest.body());

        return newRequest;
    }


    /**
     * 修改公用返回信息
     *
     * @param response
     */
    private void responseModify(Response response) {

        //这里不能直接使用response.body().string()的方式输出日志
        // 因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        // 个新的response给应用层处理

        try {
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            Log.d("infe", "响应头：" + response.code());
            Log.d("infe", "返回数据：" + responseBody.string());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 打印请求参数
     *
     * @param body
     */
    private void printParams(RequestBody body) {

        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF_8);
            }
            if ("multipart".equals(contentType.type())) {
                //表示是文件上传
                return;
            }
            String params = buffer.readString(charset);
            Log.d(TAG, "请求参数： | " + params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
