package com.biology.common.widget.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.biology.common.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * android banner图
 */
public class BannerView<T extends Serializable> extends FrameLayout implements OnPageChangeListener {


    private static final int Gravity_bottomLeft = 0;
    private static final int Gravity_bottomRight = 1;
    private static final int Gravity_bottomCenter = 2;
    private static final int Gravity_topLeft = 3;
    private static final int Gravity_topRight = 4;
    private static final int Gravity_topCenter = 5;

    private Context mContext;
    private LayoutInflater mInflater;

    private BannerCallBack mItemCallBack;
    private CustomBannerCallBack mCustomItemCallBack;

    private ViewPager mViewPager;
    //    private LinearLayout mDotLl;
    private List<T> mUrlList = new ArrayList<>();

    private List<ImageView> dotList = null;
    private MyAdapter mAdapter = null;
    private Handler mHandler = null;
    private AutoRollRunnable mAutoRollRunnable = null;

    //    private int prePosition = 0;
    private TextView title, desc;
    private int pointMarginTopOrBottom;
    private int pointMarginLeftOrRight;
    private int pointSeparation;
    private int pointNormalImg;
    private int pointSelectImg;
    private int pointGravity;
    private int bannerLayout;
    private boolean isAutoScoll;//是否自动滚动
    private boolean isCycleScoll;//是否循环滚动

    private BannerDefaultIndicator mBannerIndicator;


    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttrs(attrs);
        this.mContext = context;
        initView();
        initData();
        initListener();
    }


    private void parseAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView);

        //指示器间隔
        pointSeparation = arr.getDimensionPixelSize(R.styleable.BannerView_pointSeparation, getContext().getResources().getDimensionPixelSize(R.dimen.dp720_10));
        //指示器未选中图标
        pointNormalImg = arr.getResourceId(R.styleable.BannerView_pointNormalImg, R.drawable.banner_dot_normal);
        //指示器选中图标
        pointSelectImg = arr.getResourceId(R.styleable.BannerView_pointSelectImg, R.drawable.banner_dot_select);
        //指示器位置
        pointGravity = arr.getInt(R.styleable.BannerView_pointGravity, Gravity_bottomCenter);
        //自定义布局
        bannerLayout = arr.getResourceId(R.styleable.BannerView_itemLayout, 0);
        isAutoScoll = arr.getBoolean(R.styleable.BannerView_isAutoScoll, true);
        isCycleScoll = arr.getBoolean(R.styleable.BannerView_isCycleScoll, true);
        pointMarginTopOrBottom = arr.getDimensionPixelSize(R.styleable.BannerView_pointMarginTopOrBottom, getContext().getResources().getDimensionPixelSize(R.dimen.dp720_30));
        pointMarginLeftOrRight = arr.getDimensionPixelSize(R.styleable.BannerView_pointMarginLeftOrRight, getContext().getResources().getDimensionPixelSize(R.dimen.dp720_30));

        arr.recycle();
    }


    //初始化view
    private void initView() {
        mInflater = LayoutInflater.from(mContext);
        View.inflate(mContext, R.layout.banner_view, this);
        mViewPager = (ViewPager) findViewById(R.id.banner_view_vp);
//        mDotLl = (LinearLayout) findViewById(R.id.banner_view_dot);
        mBannerIndicator = (BannerDefaultIndicator) findViewById(R.id.banner_view_dot);
        title = (TextView) findViewById(R.id.banner_view_item_title);
        desc = (TextView) findViewById(R.id.banner_view_item_desc);
        FrameLayout.LayoutParams layoutParams = (LayoutParams) mBannerIndicator.getLayoutParams();
        if (pointGravity == Gravity_bottomLeft) {
            layoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
            layoutParams.leftMargin = pointMarginLeftOrRight;
            layoutParams.bottomMargin = pointMarginTopOrBottom;
        } else if (pointGravity == Gravity_bottomRight) {
            layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            layoutParams.rightMargin = pointMarginLeftOrRight;
            layoutParams.bottomMargin = pointMarginTopOrBottom;
        } else if (pointGravity == Gravity_bottomCenter) {
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            layoutParams.bottomMargin = pointMarginTopOrBottom;
        } else if (pointGravity == Gravity_topLeft) {
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.leftMargin = pointMarginLeftOrRight;
            layoutParams.topMargin = pointMarginTopOrBottom;
        } else if (pointGravity == Gravity_topRight) {
            layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
            layoutParams.rightMargin = pointMarginLeftOrRight;
            layoutParams.topMargin = pointMarginTopOrBottom;
        } else if (pointGravity == Gravity_topCenter) {
            layoutParams.gravity = Gravity.TOP | Gravity.CENTER;
            layoutParams.topMargin = pointMarginLeftOrRight;
        }
        mBannerIndicator.setLayoutParams(layoutParams);
        mBannerIndicator.setNormalIndicatorRes(pointNormalImg)
                .setSelectIndicatorRes(pointSelectImg)
                .setIndicatorSpace(pointSeparation)
                .attchBannerView(this);
    }

    //初始化数据
    private void initData() {
        dotList = new ArrayList<ImageView>();
        mAutoRollRunnable = new AutoRollRunnable();
        mHandler = new Handler();
        mAdapter = new MyAdapter();
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(this);
    }


    public void setItemCallBack(BannerCallBack itemCallBack) {
        this.mItemCallBack = itemCallBack;
    }

    public void setCustomItemCallBack(CustomBannerCallBack itemCallBack) {
        this.mCustomItemCallBack = itemCallBack;
    }

    /**
     * 设置数据
     *
     * @param urlList
     */
    public void setImgUrlData(List<T> urlList) {

        if (urlList==null||urlList.size()==0){
            return;
        }
        this.mUrlList.clear();
        this.mUrlList.addAll(urlList);

        mAdapter = new MyAdapter();
        mViewPager.setAdapter(mAdapter);
        mBannerIndicator.notifyDataChangedView();
        //设置viewpager初始位置
        mViewPager.setCurrentItem(0);
        if (urlList.size() > 0 && isAutoScoll) {
            startRoll();
        }
    }


    /**
     * 获取BannerView的数据列表
     */
    public List<T> getDatas() {
        return mUrlList;
    }

    //开始轮播
    public void startRoll() {
        mAutoRollRunnable.start();
    }

    // 停止轮播
    public void stopRoll() {
        mAutoRollRunnable.stop();
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }


    private class AutoRollRunnable implements Runnable {

        //是否在轮播的标志
        boolean isRunning = false;

        public void start() {
            if (!isRunning) {
                isRunning = true;
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, 6000);
            }
        }

        public void stop() {
            if (isRunning) {
                mHandler.removeCallbacks(this);
                isRunning = false;
            }
        }

        @Override
        public void run() {
            if (isRunning) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                mHandler.postDelayed(this, 6000);
            }
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //无限滑动
            if (isCycleScoll) {
                if (mUrlList.size() == 1) {
                    return mUrlList.size();
                }
                return Integer.MAX_VALUE;
            } else {
                return mUrlList.size();
            }
        }

        @Override
        public int getItemPosition(Object object) {
            //解决刷新无效的问题
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {


            View view = null;

            view = LayoutInflater.from(getContext()).inflate(bannerLayout == 0 ? R.layout.fragment_defaut_banner_lay : bannerLayout, null);
            if (view instanceof ImageView) {
                ((ImageView) view).setScaleType(ScaleType.FIT_CENTER);
                view.setOnTouchListener(new OnTouchListener() {
                    private int downX = 0;
                    private long downTime = 0;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mAutoRollRunnable.stop();
                                //获取按下的x坐标
                                downX = (int) v.getX();
                                downTime = System.currentTimeMillis();
                                break;
                            case MotionEvent.ACTION_UP:
                                mAutoRollRunnable.start();
                                int moveX = (int) v.getX();
                                long moveTime = System.currentTimeMillis();
                                if (downX == moveX && (moveTime - downTime < 500)) {//点击的条件
                                    if (mItemCallBack != null) {
                                        //轮播图回调点击事件
                                        mItemCallBack.itemClick(mUrlList.get(position % mUrlList.size()));
                                    }
                                }
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                mAutoRollRunnable.start();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
            }

            if (mCustomItemCallBack != null) {
                //自定义View的回调
                mCustomItemCallBack.drawView(view, mUrlList.get(position % mUrlList.size()));
            } else if (mItemCallBack != null) {
                //轮播图默认回调
                mItemCallBack.getView((ImageView) view, mUrlList.get(position % mUrlList.size()));
            }

            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object != null) {
                container.removeView((View) object);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

        int realPosition = position % mUrlList.size();
        mBannerIndicator.onIndicatorSelected(realPosition);

        if (mCustomItemCallBack == null && mItemCallBack != null) {
            mItemCallBack.showItem(title, desc, mUrlList.get(position % mUrlList.size()));
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }


    //停止轮播
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRoll();
    }

    public interface BannerCallBack<T> {

        void getView(ImageView imageView, T data);

        void showItem(TextView title, TextView desc, T data);

        void itemClick(T data);

    }


    /**
     * 自定义view回调
     *
     * @param <T>
     */
    public interface CustomBannerCallBack<T> {
        void drawView(View view, T data);
    }


}
