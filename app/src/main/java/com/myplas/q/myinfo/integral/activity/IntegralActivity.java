package com.myplas.q.myinfo.integral.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myplas.q.R;
import com.myplas.q.common.api.API;
import com.myplas.q.common.netresquset.ResultCallBack;
import com.myplas.q.common.utils.SharedUtils;
import com.myplas.q.common.utils.TextUtils;
import com.myplas.q.guide.activity.BaseActivity;
import com.myplas.q.myinfo.beans.IntegralBean;
import com.myplas.q.myinfo.integral.adapter.IntegralAdapter;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编写： 黄双
 * 电话：15378412400
 * 邮箱：15378412400@163.com
 * 时间：2017/3/28 14:10
 */
public class IntegralActivity extends BaseActivity implements ResultCallBack, View.OnClickListener, IntegralAdapter.MyInterface {
    private String type1;
    private boolean move;
    private SharedUtils sharedUtils;
    private RecyclerView mRecyclerView;
    private IntegralAdapter integralAdapter;
    private List<IntegralBean.InfoBean> list;
    private LinearLayoutManager manager;
    private TextView integral_all, integral_all_, intergral_chz, intergral_record, intergral_rule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_integral);
        goBack(findViewById(R.id.back));

        sharedUtils = SharedUtils.getSharedUtils();
        intergral_chz = F(R.id.jf_chz);
        intergral_rule = F(R.id.jf_rule);
        integral_all = F(R.id.integral_all);
        mRecyclerView = F(R.id.jf_gridview);
        intergral_record = F(R.id.jf_record);
        integral_all_ = F(R.id.integral_all_);

        manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setNestedScrollingEnabled(false);

        intergral_rule.setOnClickListener(this);
        intergral_chz.setOnClickListener(this);
        intergral_record.setOnClickListener(this);
        integral_all.setOnClickListener(this);
        integral_all_.setOnClickListener(this);
        type1 = getIntent().getStringExtra("type");
        //getProducts((type1 == null || "".equals(type1)) ? (3) : (3));
        getProducts(1);
    }

    public void getProducts(int type) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("token", sharedUtils.getData(this, "token"));
            map.put("page", "1");
            map.put("size", "20");
            postAsyn(this, API.BASEURL + API.GET_PRODUCT_LIST, map, this, type);
        } catch (Exception e) {
        }
    }

    @Override
    public void callBack(Object object, int type) {
        try {
            Gson gson = new Gson();
            String err = new JSONObject(object.toString()).getString("err");
            if (type == 1 && err.equals("0")) {
                IntegralBean integralBean = gson.fromJson(object.toString(), IntegralBean.class);
                list = integralBean.getInfo();
                integral_all.setText(" " + integralBean.getPointsAll().toString());
                integralAdapter = new IntegralAdapter(this, this, list, this);
                mRecyclerView.setAdapter(integralAdapter);
            }
            if (type == 2 && err.equals("0")) {
                IntegralBean integralBean = gson.fromJson(object.toString(), IntegralBean.class);
                integral_all.setText(" " + integralBean.getPointsAll().toString());
            }
//            if (type == 3 && err.equals("0")) {
//                Log.e("------------", type + "");
//                IntegralBean integralBean = gson.fromJson(object.toString(), IntegralBean.class);
//                list = integralBean.getInfo();
//                integral_all.setText(" " + integralBean.getPointsAll().toString());
//                integralAdapter = new IntegralAdapter(this, this, list, this);
//                mRecyclerView.setAdapter(integralAdapter);
//                MoveToPosition(manager, mRecyclerView, 5);
//                for (int i = 0; i < list.size(); i++) {
//
//                }
//
//            }
        } catch (Exception e) {
            TextUtils.Toast(this, "数据解析错啦！");
        }
    }

    public void MoveToPosition(final LinearLayoutManager manager, RecyclerView recyclerView, final int p) {
        int fir = manager.findFirstVisibleItemPosition();
        int end = manager.findLastVisibleItemPosition();
        if (p <= fir) {
            recyclerView.scrollToPosition(p);
        } else if (p <= end) {
            int top = recyclerView.getChildAt(p - fir).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            recyclerView.scrollToPosition(p);    //先让当前view滚动到列表内
            move = true;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (move) {
                    move = false;
                    int n = p - manager.findFirstVisibleItemPosition();
                    if (n >= 0 && n < recyclerView.getChildCount()) {
                        recyclerView.scrollBy(0, recyclerView.getChildAt(n).getTop()); //滚动到顶部
                    }
                }
            }
        });

    }

    @Override
    public void failCallBack(int type) {

    }

    @Override
    public void refresgData() {
        getProducts(2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.integral_all:
            case R.id.integral_all_://明细
                startActivity(new Intent(this, IntegralDetialActivtity.class));
                break;
            case R.id.jf_chz://充值
                startActivity(new Intent(this, IntegralPayActivtity.class));
                break;
            case R.id.jf_record://兑换记录
                startActivity(new Intent(this, IntegralRecordActivtity.class));
                break;
            case R.id.jf_rule://规则
                startActivity(new Intent(this, IntegralRuleActivtity.class));
                break;
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
