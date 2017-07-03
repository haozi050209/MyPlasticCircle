package com.myplas.q.myinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.myplas.q.R;
import com.myplas.q.common.utils.DialogShowUtils;
import com.myplas.q.common.utils.TextUtils;
import com.myplas.q.guide.activity.BaseActivity;
import com.myplas.q.common.netresquset.ResultCallBack;
import com.myplas.q.common.utils.SharedUtils;
import com.myplas.q.common.view.XListView;
import com.myplas.q.myinfo.adapter.MyIntroductionAdapter;
import com.myplas.q.common.api.API;
import com.myplas.q.myinfo.beans.MyIntroductionBean;
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
 * 时间：2017/3/23 17:46
 */
public class MyIntroductionActivity extends BaseActivity implements ResultCallBack
,XListView.IXListViewListener,DialogShowUtils.DialogShowInterface{
    private XListView listView;
    private List<MyIntroductionBean.DataBean> list;
    private List<MyIntroductionBean.DataBean> list_more;
    private MyIntroductionAdapter wdyjAdapter;
    private SharedUtils sharedUtils;
    private String userid;
    private int page=1,visibleItemCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wd_yj_activity_layout);
        goBack(findViewById(R.id.back));
        listView= (XListView) findViewById(R.id.wd_yj_listview);
        list_more=new ArrayList<>();
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(false);
        //listView.setXListViewListener(this);
        sharedUtils=SharedUtils.getSharedUtils();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userid=((list_more.size()==0)?(list):(list_more)).get(position-1).getUser_id();
                getPersonInfoData(userid, "1", 5);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&listView.getCount()>visibleItemCount) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        page++;
                        getMyFans(String.valueOf(page));
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                MyIntroductionActivity.this.visibleItemCount=visibleItemCount;
            }
        });
        getMyFans("1");
    }
    public void getMyFans(String page) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", sharedUtils.getData(this, "token"));
        map.put("page", page);
        map.put("size", "10");
        postAsyn(this, API.BASEURL +API.GET_MY_INTRODUCTION, map, this,1);
    }
    public void getPersonInfoData(String userid,String showtype,int type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", sharedUtils.getData(this, "token"));
        map.put("user_id", userid);
        map.put("showType", showtype);
        String url = API.BASEURL + API.GET_ZONE_FRIEND;
        postAsyn(this, url, map, this, type);
    }
    @Override
    public void callBack(Object object, int type) {
        try {
            Gson gson=new Gson();
            String err=new JSONObject(object.toString()).getString("err");
            if (type==1) {
                if (err.equals("0")) {
                    MyIntroductionBean myIntroductionBean=gson.fromJson(object.toString(),MyIntroductionBean.class);
                    list=myIntroductionBean.getData();
                    if (page==1) {
                        wdyjAdapter=new MyIntroductionAdapter(this,list);
                        listView.setAdapter(wdyjAdapter);
                        listView.stopRefresh();
                        list_more.clear();
                        list_more.addAll(list);
                    }else {
                        if(list!=null&&list.size()!=0){
                            listView.stopLoadMore();
                            list_more.addAll(list);
                            wdyjAdapter.setList(list_more);
                            wdyjAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    list=null;
                    TextUtils.Toast(this,new JSONObject(object.toString()).getString("msg"));
                }
            }
            //是否消耗积分
            if (type == 5 && err.equals("99")) {
                String content = new JSONObject(object.toString()).getString("msg");
                DialogShowUtils dialogShowUtils = new DialogShowUtils();
                dialogShowUtils.showDialog(this, content, 1, this);
            }
            //已经消耗积分
            if (type == 5 && err.equals("0")) {
                Intent intent = new Intent(this, PersonInfoActivity.class);
                intent.putExtra("userid", userid);
                intent.putExtra("id", userid);
                startActivity(intent);
            }
            //减积分成功
            if(type==2 &&err.equals("0")){
                sharedUtils.setBooloean(this,userid,true);
                Intent intent=new Intent(this,PersonInfoActivity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("id",userid);
                startActivity(intent);
            }
            //积分不足
            if (type == 2 && !err.equals("0")) {
                String content = new JSONObject(object.toString()).getString("msg");
                DialogShowUtils dialogShowUtils = new DialogShowUtils();
                dialogShowUtils.showDialog(this, content, (err.equals("100")) ? (2) : (3), this);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void failCallBack(int type) {

    }

    @Override
    public void ok(int type) {
        switch (type) {
            case 1:
                getPersonInfoData(userid, "5", 2);
                break;
            case 2:
                startActivity(new Intent(this, IntegralPayActivtity.class));
                break;
        }
    }
    @Override
    public void onRefresh() {
        page=1;
        getMyFans(String.valueOf(page));
    }
    @Override
    public void onLoadMore() {
//        page++;
//        getMyFans(String.valueOf(page));
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