package com.myplas.q.common.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myplas.q.R;


/**
 * 下拉刷新控件
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 12/13/14.
 */
public class RefreshableView extends LinearLayout {
	private static final String TAG = RefreshableView.class.getSimpleName();
	private ProgressBar progressBar;
	private TextView tv;
	public RefreshableView(Context context) {
		super(context);
		init(context);
	}

	public RefreshableView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.refreshableView);
		for (int i = 0, len = a.length(); i < len; i++) {
			int attrIndex = a.getIndex(i);
			switch (attrIndex) {
				case R.styleable.refreshableView_interceptAllMoveEvents:
					interceptAllMoveEvents = a.getBoolean(i, false);
					break;
			}
		}
		a.recycle();

		init(context);
	}

	public RefreshableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 刷新状态
	 */
	public static final int STATE_REFRESH_NORMAL = 0x21;
	public static final int STATE_REFRESH_NOT_ARRIVED = 0x22;
	public static final int STATE_REFRESH_ARRIVED = 0x23;
	public static final int STATE_REFRESHING = 0x24;
	private int refreshState;
	private Context context;
	/**
	 * 刷新的view
	 */
	private View refreshHeaderView;
	/**
	 * 刷新的view的真实高度
	 */
	private int originRefreshHeight;
	/**
	 * 有效下拉刷新需要达到的高度
	 */
	private int refreshArrivedStateHeight;
	/**
	 * 刷新时显示的高度
	 */
	private int refreshingHeight;
	/**
	 * 正常未刷新高度
	 */
	private int refreshNormalHeight;


	/**
	 * 默认不允许拦截（即，往子view传递事件），该属性只有在interceptAllMoveEvents为false的时候才有效
	 */
	private boolean disallowIntercept = true;
	/**
	 * xml中可设置它的值为false，表示不把移动的事件传递给子控件
	 */
	private boolean interceptAllMoveEvents;

	private void init(Context context) {
		this.context = context;
		this.setOrientation(VERTICAL);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		refreshHeaderView= LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
		this.removeView(refreshHeaderView);
		this.addView(refreshHeaderView, 0);

		// 计算refreshHeadView尺寸
		int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		refreshHeaderView.measure(width, expandSpec);

		Log.e(TAG, "[onSizeChanged]w: " + w + ", h: " + h);
		Log.e(TAG, "[onSizeChanged]oldw: " + oldw + ", oldh: " + oldh);
		Log.e(TAG, "[onSizeChanged]child counts: " + this.getChildCount());
		originRefreshHeight = refreshHeaderView.getMeasuredHeight();

		boolean isUseDefault = true;

		// 初始化各个高度
		if (isUseDefault) {
			refreshArrivedStateHeight = originRefreshHeight;
			refreshingHeight = originRefreshHeight;
			refreshNormalHeight = 0;
		}
		Log.d(TAG, "[onSizeChanged]refreshHeaderView origin height: " + originRefreshHeight);
		changeViewHeight(refreshHeaderView, refreshNormalHeight);

		// 初始化为正常状态
		setRefreshState(STATE_REFRESH_NORMAL);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.d(TAG, "[dispatchTouchEvent]ev action: " + ev.getAction());
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.d(TAG, "[onInterceptTouchEvent]ev action: " + ev.getAction());
			if (!interceptAllMoveEvents) {
                return !disallowIntercept;
            }
			// 如果设置了拦截所有move事件，即interceptAllMoveEvents为true
			if (MotionEvent.ACTION_MOVE == ev.getAction()) {
				return true;
			}
		return false;
	}

	@Override
	public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
		if (this.disallowIntercept == disallowIntercept) {
			return;
		}
		this.disallowIntercept = disallowIntercept;
		super.requestDisallowInterceptTouchEvent(disallowIntercept);
	}

	private float downY = Float.MAX_VALUE;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG, "[onTouchEvent]ev action: " + event.getAction());
		refreshHeaderView.setVisibility(VISIBLE);
			switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = event.getY();
                    Log.d(TAG, "Down --> downY: " + downY);
                    requestDisallowInterceptTouchEvent(true); // 保证事件可往下传递
                    break;
                case MotionEvent.ACTION_MOVE:
                    float curY = event.getY();
                    float deltaY = curY - downY;
                    // 是否是有效的往下拖动事件（则需要显示加载header）
                    boolean isDropDownValidate = Float.MAX_VALUE != downY;
                    /**
                     * 修改拦截设置
                     * 如果是有效往下拖动事件，则事件需要在本ViewGroup中处理，所以需要拦截不往子控件传递，即不允许拦截设为false
                     * 如果不是有效往下拖动事件，则事件传递给子控件处理，所以不需要拦截，并往子控件传递，即不允许拦截设为true
                     */
                    requestDisallowInterceptTouchEvent(true);

                    downY = curY;
                    Log.d(TAG, "Move --> deltaY(curY - downY): " + deltaY);
                    int curHeight = refreshHeaderView.getMeasuredHeight();
                    int exceptHeight = curHeight + (int) (deltaY / 2);
					changeViewHeight(refreshHeaderView, Math.max(curHeight, exceptHeight));
                    // 如果当前没有处在正在刷新状态，则更新刷新状态
                    if (STATE_REFRESHING != refreshState) {
                        if (curHeight >= refreshArrivedStateHeight) { // 达到可刷新状态
                            setRefreshState(STATE_REFRESH_ARRIVED);
                        } else { // 未达到可刷新状态
                            setRefreshState(STATE_REFRESH_NOT_ARRIVED);
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    downY = Float.MAX_VALUE;
                    Log.d(TAG, "Up --> downY: " + downY);
                    requestDisallowInterceptTouchEvent(true); // 保证事件可往下传递
                    // 如果是达到刷新状态，则设置正在刷新状态的高度
                    if (STATE_REFRESH_ARRIVED == refreshState) { // 达到了刷新的状态
                        startHeightAnimation(refreshHeaderView, refreshHeaderView.getMeasuredHeight(), refreshingHeight);
                        setRefreshState(STATE_REFRESHING);
                    } else if (STATE_REFRESHING == refreshState) { // 正在刷新的状态
                        startHeightAnimation(refreshHeaderView, refreshHeaderView.getMeasuredHeight(), refreshingHeight);
                    } else {
                        // 执行动画后回归正常状态
                        startHeightAnimation(refreshHeaderView, refreshHeaderView.getMeasuredHeight(), refreshNormalHeight, normalAnimatorListener);
                    }

                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.d(TAG, "cancel");
                    break;
            }
		return true;
	}

	/**
	 * 刷新完毕后调用此方法
	 */
	public void onCompleteRefresh() {
		if (STATE_REFRESHING == refreshState) {
            progressBar.setVisibility(INVISIBLE);
            setRefreshState(STATE_REFRESH_NORMAL);
			startHeightAnimation(refreshHeaderView, refreshHeaderView.getMeasuredHeight(), refreshNormalHeight);
		}
	}

	/**
	 * 修改当前的刷新状态
	 *
	 * @param expectRefreshState
	 */
	private void setRefreshState(int expectRefreshState) {
		if (expectRefreshState != refreshState) {
			refreshState = expectRefreshState;
			tv = (TextView) refreshHeaderView.findViewById(R.id.xlistview_header_hint_textview);
			progressBar= (ProgressBar) refreshHeaderView.findViewById(R.id.xlistview_header_progressbar);
			switch (refreshState) {
				case RefreshableView.STATE_REFRESH_NORMAL:
					tv.setText("");
					break;
				case RefreshableView.STATE_REFRESH_NOT_ARRIVED:
					tv.setText("松开刷新");
					break;
				case RefreshableView.STATE_REFRESH_ARRIVED:
					tv.setText("松开刷新");
                    progressBar.setVisibility(INVISIBLE);
                    break;
				case RefreshableView.STATE_REFRESHING:
					tv.setText("正在刷新");
					progressBar.setVisibility(VISIBLE);
					onreListener.refresh();
					break;

			}
		}
	}


	/**
	 * 改变某控件的高度
	 *
	 * @param view
	 * @param height
	 */
	private void changeViewHeight(View view, int height) {
		Log.d(TAG, "[changeViewHeight]change Height: " + height);
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		lp.height = height;
		view.setLayoutParams(lp);
	}

	/**
	 * 改变某控件的高度动画
	 *
	 * @param view
	 * @param fromHeight
	 * @param toHeight
	 */
	private void startHeightAnimation(final View view, int fromHeight, int toHeight) {
		startHeightAnimation(view, fromHeight, toHeight, null);
	}

	private void startHeightAnimation(final View view, int fromHeight, int toHeight, Animator.AnimatorListener animatorListener) {
		if (toHeight == view.getMeasuredHeight()) {
			return;
		}
		ValueAnimator heightAnimator = ValueAnimator.ofInt(fromHeight, toHeight);
		heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				Integer value = (Integer) valueAnimator.getAnimatedValue();
				if (null == value) return;
				changeViewHeight(view, value);
			}
		});
		if (null != animatorListener) {
			heightAnimator.addListener(animatorListener);
		}
		heightAnimator.setInterpolator(new LinearInterpolator());
		heightAnimator.setDuration(300/*ms*/);
		heightAnimator.start();
	}

	AnimatorListenerAdapter normalAnimatorListener = new AnimatorListenerAdapter() {
		@Override
		public void onAnimationEnd(Animator animation) {
			super.onAnimationEnd(animation);
			setRefreshState(STATE_REFRESH_NORMAL); // 回归正常状态
		}
	};

	public void setRefreshArrivedStateHeight(int refreshArrivedStateHeight) {
		this.refreshArrivedStateHeight = getOriginRefreshHeight();
	}

	public void setRefreshingHeight(int refreshingHeight) {
		this.refreshingHeight = getOriginRefreshHeight();
	}

	public void setRefreshNormalHeight(int refreshNormalHeight) {
		this.refreshNormalHeight = getOriginRefreshHeight() / 3;
	}

	public int getOriginRefreshHeight() {
		return originRefreshHeight;
	}
	private onRefreshListener onreListener;

	public void setOnRefreshListener(onRefreshListener onreListener) {
		this.onreListener = onreListener;
	}
	public interface onRefreshListener {
		public void refresh();
	}
}
