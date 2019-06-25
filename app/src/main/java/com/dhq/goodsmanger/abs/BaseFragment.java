package com.dhq.goodsmanger.abs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dhq.goodsmanger.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 * @author Administrator
 * @date 2018/5/31
 */

public class BaseFragment extends Fragment {
    protected String mPageName = "BaseFragment";
    private Unbinder unbinder;
    public View root;

    //    private LoadingView mLoadingView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPageName = getClass().getSimpleName();
        root = inflater.inflate(R.layout.fragment_base, null);
        return root;
    }



    public void addPresenter(MvpPresenterIml presenterIml) {
//        Logger.d(mPageName, "addPresenter");
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).addPresenter(presenterIml);
        }
    }

    public void setView(int layoutResID) {
        LinearLayout content_linear = (LinearLayout) findViewById(R.id.fragment_content_view);
        content_linear.addView(View.inflate(getActivity(), layoutResID, null), new LinearLayout.LayoutParams(-1, -1));
        unbinder = ButterKnife.bind(this, root);
    }

    public View findViewById(int viewID) {
        return root.findViewById(viewID);
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
