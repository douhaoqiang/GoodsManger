package com.dhq.goodsmanger.http.http;


import android.support.annotation.NonNull;


import com.dhq.goodsmanger.entity.BaseResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * DESC
 * Created by douhaoqiang on 2017/3/28.
 */

public interface ApiService {


    /**
     * get请求 添加参数
     * @return
     */
    @GET()
    Observable<BaseResponse> getHttpRequest(@Url String url, @QueryMap HashMap<String, String> paramMaps);


    /**
     * post请求 添加表单参数
     * @return
     */
    @POST()
    @FormUrlEncoded
    Observable<BaseResponse> postFormHttpRequest(@Url String url, @FieldMap(encoded = true) HashMap<String, Object> paramMaps);


    /**
     * post请求 添加表json参数
     * @param url
     * @param jsonBody
     * @return
     */
    @POST()
    Observable<BaseResponse> postJsonHttpRequest(@Url String url, @Body RequestBody jsonBody);



    /**
     * post请求上传文件
     *
     * @return
     */
    @Multipart
    @POST()
    Observable<BaseResponse> uploadFileReq(@NonNull @Url String url, @Part List<MultipartBody.Part> fileList);


    @Streaming
    @GET
    Observable<ResponseBody> downLoadFileReq(@NonNull @Url String url, @QueryMap HashMap<String, Object> paramMaps);




}
