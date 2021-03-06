package com.myplas.q.myinfo.supdem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myplas.q.R;
import com.myplas.q.common.view.NoResultLayout;
import com.myplas.q.guide.activity.BaseActivity;
import com.myplas.q.common.netresquset.ResultCallBack;
import com.myplas.q.common.utils.SharedUtils;
import com.myplas.q.common.view.XListView;
import com.myplas.q.common.api.API;
import com.myplas.q.myinfo.beans.MyCommentBean;
import com.myplas.q.myinfo.supdem.adapter.SupDemAdapter;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编写： 黄双
 * 电话：15378412400
 * 邮箱：15378412400@163.com
 * 时间：2017/3/23 16:22
 */
public class MySupDemActivity extends BaseActivity implements ResultCallBack, SupDemAdapter.MyInterface
        , XListView.IXListViewListener {

    private String type;
    private SharedUtils sharedUtils;
    private int page = 1, visibleItemCount;

    private TextView textView;
    private XListView listView;
    private NoResultLayout mNoResultLayout;
    private List<MyCommentBean.DataBean> list;
    private List<MyCommentBean.DataBean> list_more;

    private SupDemAdapter supplyDemandAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_myself_supdem);
        initTileBar();
        setTitle(getIntent().getStringExtra("title"));

        list_more = new ArrayList<>();
        sharedUtils = SharedUtils.getSharedUtils();
        mNoResultLayout = F(R.id.mysupdem_noresultlayout);
        listView = (XListView) findViewById(R.id.wd_gj_listview);

        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(false);
        listView.setXListViewListener(this);
        type = getIntent().getStringExtra("type");
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && listView.getCount() > visibleItemCount) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        page++;
                        getSupplyDemandList(String.valueOf(page));
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                MySupDemActivity.this.visibleItemCount = visibleItemCount;
            }
        });

        getSupplyDemandList("1");
    }

    public void getSupplyDemandList(String page) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", sharedUtils.getData(this, "token"));
        map.put("type", type);
        map.put("page", page);
        map.put("size", "10");
        postAsyn(this, API.BASEURL + API.GET_MY_MSG, map, this, 1);
    }

    @Override
    public void callBack(Object object, int type) {
        try {
            Gson gson = new Gson();
            if (type == 1) {
                if (new JSONObject(object.toString()).getString("err").equals("0")) {
                    MyCommentBean supplyDemandBean = gson.fromJson(object.toString(), MyCommentBean.class);
                    list = supplyDemandBean.getData();
                    if (page == 1) {
                        mNoResultLayout.setVisibility(false);
                        listView.setVisibility(View.VISIBLE);

                        supplyDemandAdapter = new SupDemAdapter(this, list, getIntent().getStringExtra("type"), this);
                        listView.setAdapter(supplyDemandAdapter);
                        listView.stopRefresh();
                        list_more.clear();
                        list_more.addAll(list);
                    } else {
                        if (list != null && list.size() != 0) {
                            listView.stopLoadMore();
                            list_more.addAll(list);
                            supplyDemandAdapter.setList(list_more);
                            supplyDemandAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if (page == 1) {
                        list = null;
                        listView.setVisibility(View.GONE);
                        String msg = new JSONObject(object.toString()).getString("msg");
                        mNoResultLayout.setNoResultData(R.drawable.icon_intelligent_recommendation2, msg, true);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void failCallBack(int type) {

    }

    @Override
    public void reQuestNet() {
        page = 1;
        getSupplyDemandList(String.valueOf(page));
    }

    //刷新
    @Override
    public void onRefresh() {
        page = 1;
        getSupplyDemandList(String.valueOf(page));
    }

    //加载
    @Override
    public void onLoadMore() {
//        page++;
//        getSupplyDemandList(String.valueOf(page));
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
