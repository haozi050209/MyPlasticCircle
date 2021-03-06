package com.myplas.q.myinfo.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.myplas.q.R;
import com.myplas.q.common.api.API;
import com.myplas.q.common.netresquset.ResultCallBack;
import com.myplas.q.guide.activity.BaseActivity;
import com.myplas.q.myinfo.beans.MyMessageBean;
import com.myplas.q.myinfo.message.adapter.MessageListsAdapter;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编写： 黄双
 * 电话：15378412400
 * 邮箱：15378412400@163.com
 * 时间：2017/3/29 14:35
 */
public class MessageListsActivity extends BaseActivity implements ResultCallBack {
    private ListView listView;
    private MessageListsAdapter mAdapter;
    private List<MyMessageBean.DataBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_messagelist);
        initTileBar();
        setTitle("我的消息");

        listView = (ListView) findViewById(R.id.messagelist_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringBuffer stringBuffer = null;
                if (list.get(position).getType().equals("1")) {
                    stringBuffer = new StringBuffer("供求消息");
                } else if (list.get(position).getType().equals("2")) {
                    stringBuffer = new StringBuffer("出价消息");
                } else {
                    stringBuffer = new StringBuffer("回复消息");
                }
                Intent intent = new Intent(MessageListsActivity.this, MessageCommonActivity.class);
                intent.putExtra("title", stringBuffer.toString());
                startActivity(intent);
            }
        });
    }

    public void getMyMsg() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", "1");
        map.put("size", "10");
        postAsyn(this, API.BASEURL + API.MYMSG, map, this, 1);
    }

    @Override
    public void callBack(Object object, int type) {
        try {
            Log.e("--------------", object.toString());
            if (new JSONObject(object.toString()).getString("err").equals("0")) {
                Gson gson = new Gson();
                MyMessageBean myMessageBean = gson.fromJson(object.toString(), MyMessageBean.class);
                list = myMessageBean.getData();
                mAdapter = new MessageListsAdapter(this, list);
                listView.setAdapter(mAdapter);
            } else {
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
        getMyMsg();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
