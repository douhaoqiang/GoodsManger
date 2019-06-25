package com.dhq.goodsmanger.abs;

/**
 * Created by Administrator on 2018/5/31.
 */

public interface MvpPresenter <V extends MvpView>{
    void attachView(V view);
    void detachView(boolean saveInstance);
}
