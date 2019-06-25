package com.dhq.goodsmanger.entity;

import com.google.gson.JsonElement;

import java.io.Serializable;

/**
 * DESC
 * Created by douhaoqiang on 2016/11/9.
 */
public class BaseResponse implements Serializable{

    //  判断标示
    private String status;
    //    提示信息
    private String msg;
    //显示数据（用户需要关心的数据）
    private JsonElement data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
