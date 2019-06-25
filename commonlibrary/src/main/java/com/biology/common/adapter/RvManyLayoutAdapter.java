package com.biology.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DESC RecycleView 的公用adapter
 * Created by douhaoqiang on 2016/9/6.
 */

public abstract class RvManyLayoutAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<T> datas = new ArrayList<>();

    /**
     * 设置列表数据
     *
     * @param datas 列表数据
     */
    public void setDatas(List<T> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加单个数据
     *
     * @param data 列表数据
     */
    public void addData(T data) {
        this.datas.add(data);
        notifyDataSetChanged();
    }

    /**
     * 添加单个数据到一个位置
     *
     * @param data 列表数据
     */
    public void addData(T data, int position) {
        this.datas.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * 添加多个数据
     *
     * @param datas 要添加的多个数据
     */
    public void addDatas(ArrayList<T> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加多个数据
     *
     * @param datas 要添加的多个数据
     */
    public void addDatas(int position,ArrayList<T> datas) {
        this.datas.addAll(position,datas);
        notifyDataSetChanged();
    }

    /**
     * 移除单个item
     *
     * @param position 要移除的item下标
     */
    public void removeData(int position) {
        this.datas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 移除指定坐标以后的多条数据（）
     *
     * @param startPosition 要移除的item开始下标
     */
    public void removeMoreData(int startPosition) {
        int length=this.datas.size();
        int removeCount=length-startPosition;
        if (removeCount<0){
            return;
        }
        for (int i = length-1; i >= startPosition; i--) {
            this.datas.remove(i);
        }
        notifyItemRangeRemoved(startPosition,removeCount);
    }

    public List<T> getDatas() {
        return this.datas;
    }

    @Override
    public int getItemViewType(int position) {
        return getViewResId(this.datas.get(position));
    }


    /**
     * 拖拽交换数据
     *
     * @param dragPosition   拖拽的item下标
     * @param targetPosition 要交换的item的下标
     */
    public void changeItem(int dragPosition, int targetPosition) {
        Collections.swap(datas, dragPosition, targetPosition);
        notifyItemMoved(dragPosition, targetPosition);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        return new RvBaseHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            convert((RvBaseHolder) holder, datas.get(position), getItemViewType(position), position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    /**
     * 根据类型获取布局id
     *
     * @param item
     * @return
     */
    public abstract int getViewResId(T item);

    public abstract void convert(RvBaseHolder holder, T item, int viewtype, int position);

}
