package com.myplas.q.myinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myplas.q.R;
import com.myplas.q.common.api.API;
import com.myplas.q.common.netresquset.ResultCallBack;
import com.myplas.q.common.utils.TextUtils;
import com.myplas.q.common.view.MyListview;
import com.myplas.q.common.view.NoResultLayout;
import com.myplas.q.guide.activity.BaseActivity;
import com.myplas.q.myinfo.adapter.TradeOrderListviewAdapter;
import com.myplas.q.myinfo.beans.IntegralBean;
import com.myplas.q.myinfo.beans.OrderListsBean;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.Information;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编写： 黄双
 * 电话：15378412400
 * 邮箱：15378412400@163.com
 * 时间：2017/3/28 10:25
 */
public class TradeOrderActivity extends BaseActivity implements OnClickListener, TradeOrderListviewAdapter.MyOnClickListener
        , ResultCallBack {
    private Information information;
    private String appkey = "c1ff771c06254db796cd7ce1433d2004";

    //private View mViewHeader;
    private EditText mEditText;
    private ImageView mImageView;
    private RecyclerView mListView;
    private NoResultLayout mNoResultLayout;

    private TradeOrderListviewAdapter mAdapter;
    private List<OrderListsBean.DataBean.ListBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_tradeorder);
        goBack(findViewById(R.id.img_back));

        initView();
        getorderLists("", 1);
    }

    public void initView() {
        mImageView = F(R.id.img_contact);

        mListView = F(R.id.trade_order_listview);
        mNoResultLayout = F(R.id.trade_order_noresultlayout);
        //mViewHeader = View.inflate(this, R.layout.header_layout_tradeorder, null);
        mEditText = F(R.id.trade_order_edittext);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);//设置为一个1列的纵向网格布局
        mListView.setLayoutManager(mLayoutManager);

        mImageView.setOnClickListener(this);
        //edittext 回车监听
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH | (arg2 != null && arg2.getAction() == KeyEvent.ACTION_DOWN)) {
                    String keywords = mEditText.getText().toString();
                    if (TextUtils.isNullOrEmpty(keywords)) {
                        getorderLists(keywords, 1);
                    } else {
                        TextUtils.Toast(TradeOrderActivity.this, "你还没有输入搜索内容！");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void getorderLists(String keywords, int type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", "1");
        map.put("size", "20");
        map.put("order_sn", keywords);
        postAsyn(this, API.BASEURL + API.BILLINGLIST, map, this, type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_contact:
                information = new Information();
                information.setAppkey(appkey);
                SobotApi.startSobotChat(TradeOrderActivity.this, information);
                break;
        }
    }

    //申请开票
    @Override
    public void onClick1(String order_sn) {
        Intent intent = new Intent(this, ApplyInvoicesActivity.class);
        intent.putExtra("order_sn", order_sn);
        startActivity(intent);
    }

    //确认签收后的刷新
    @Override
    public void onClick2() {
        getorderLists("", 2);
    }

    //发票详情
    @Override
    public void onClick3(String order_sn) {
        Intent intent = new Intent(this, InvoicesDetailActivity.class);
        intent.putExtra("order_sn", order_sn);
        startActivity(intent);
    }


    @Override
    public void callBack(Object object, int type) {
        try {
            Gson gson = new Gson();
            String err = new JSONObject(object.toString()).getString("err");
            Log.e("------", object.toString());
            if (type == 1) {
                if (err.equals("0")) {
                    mNoResultLayout.setVisibility(false);
                    mListView.setVisibility(View.VISIBLE);
                    OrderListsBean bean = gson.fromJson(object.toString(), OrderListsBean.class);
                    mList = bean.getData().getList();
                    mAdapter = new TradeOrderListviewAdapter(this, mList);
                    mAdapter.setMyOnClickListener(this);
                    mListView.setAdapter(mAdapter);
                } else {
                    mListView.setVisibility(View.GONE);
                    String msg = new JSONObject(object.toString()).getString("msg");
                    mNoResultLayout.setNoResultData(R.drawable.icon_invoices_null, "您当前没有订单记录", true);
                }
            }
            if (type == 2) {
                if (err.equals("0")) {
                    OrderListsBean bean = gson.fromJson(object.toString(), OrderListsBean.class);
                    mList = bean.getData().getList();
                    mAdapter.setList(mList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void failCallBack(int type) {

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        getorderLists("", 2);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
