package com.myplas.q.common.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/12/8.
 */
public class MyViewPager extends ViewPager {
    private boolean noScroll = true;
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyViewPager(Context context) {
        super(context);
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
            return true;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
            return super.onInterceptTouchEvent(arg0);
    }
}
