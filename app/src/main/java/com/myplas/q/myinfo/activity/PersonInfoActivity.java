package com.myplas.q.myinfo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.myplas.q.R;
import com.myplas.q.guide.activity.BaseActivity;
import com.myplas.q.guide.activity.BigImageViewActivity;
import com.myplas.q.common.netresquset.ResultCallBack;
import com.myplas.q.common.utils.SharedUtils;
import com.myplas.q.common.utils.TextUtils;
import com.myplas.q.common.view.MyListview;
import com.myplas.q.myinfo.adapter.PersonSupplyDemandAdapter;
import com.myplas.q.common.api.API;
import com.myplas.q.myinfo.beans.PersonInfoBean;
import com.myplas.q.myinfo.beans.PersonSupplyDemadBean;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编写： 黄双
 * 电话：15378412400
 * 邮箱：15378412400@163.com
 * 时间：2017/3/23 13:39
 */
public class PersonInfoActivity extends BaseActivity implements View.OnClickListener, ResultCallBack {
    private ImageView image_rz, image_show, image_call;
    private TextView btn_care,text_name, text_gs, text_dh, text_gj, textView_dzh, textView_zhy, textView_show1, textView_show2, textView_num, textView_product;
    private TextView textView_supply, textView_demand;
    private SharedUtils sharedUtils;
    private Map<String, String> map;
    private List<PersonSupplyDemadBean.DataBean> list;
    private ImageView image_tx;
    private MyListview myListview_supply, myListview_demand;
    private PersonInfoBean personinfo;
    private PersonSupplyDemadBean p1, p2;
    private PersonSupplyDemandAdapter p_adapter;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personinfo_layout_activity);
        goBack(findViewById(R.id.back));
        initView();
        getPersonInfoData();
        getTaPur("1", 2);
        getTaPur("2", 3);
    }

    public void initView() {
        map = new HashMap<String, String>();
        sharedUtils = SharedUtils.getSharedUtils();

        image_tx = f(R.id.xq_tx);
        image_rz = f(R.id.xq_rz);
        text_gj = f(R.id.wd_zl_gjqg);
        text_name = f(R.id.wd_zl_name);
        image_show = f(R.id.wd_zl_show);
        textView_supply = f(R.id.more_supply);
        textView_demand = f(R.id.more_demand);
        textView_num = f(R.id.wd_zl_text_num);
        btn_care = f(R.id.personinfo_care_btn);
        text_dh = f(R.id.personinfo_phone_text);
        image_call = f(R.id.personinfo_call_img);
        linearLayout = f(R.id.linear_show_close);
        text_gs = f(R.id.personinfo_company_text);
        textView_zhy = f(R.id.personinfo_mysale_text);
        textView_product = f(R.id.wd_zl_text_products);
        textView_dzh = f(R.id.personinfo_address_text);
        myListview_demand = f(R.id.personinfo_more_demand_listview);
        myListview_supply = f(R.id.personinfo_more_supply_listview);
        textView_show1 = f(R.id.person_supplydemand_listview_text_show1);
        textView_show2 = f(R.id.person_supplydemand_listview_text_show2);

        btn_care.setOnClickListener(this);
        image_tx.setOnClickListener(this);
        image_call.setOnClickListener(this);
        image_show.setOnClickListener(this);
        textView_demand.setOnClickListener(this);
        textView_supply.setOnClickListener(this);
    }

    public <T extends View> T f(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onClick(View v) {
        if (personinfo != null) {
            switch (v.getId()) {
                case R.id.xq_tx:
                    Intent In = new Intent(this, BigImageViewActivity.class);
                    In.putExtra("imgurl", personinfo.getData().getThumb());
                    startActivity(In);
                    break;
                case R.id.more_demand:
                    Intent intent = new Intent(this, LookPersonInfoActivity.class);
                    intent.putExtra("tel", personinfo.getData().getMobile());
                    intent.putExtra("bean", p2);
                    intent.putExtra("type", "供给信息");
                    startActivity(intent);
                    break;
                case R.id.more_supply:
                    Intent intent1 = new Intent(this, LookPersonInfoActivity.class);
                    intent1.putExtra("tel", personinfo.getData().getMobile());
                    intent1.putExtra("bean", p1);
                    intent1.putExtra("type", "求购信息");
                    startActivity(intent1);
                    break;
                case R.id.wd_zl_show:
                    Intent In1 = new Intent(this, BigImageViewActivity.class);
                    In1.putExtra("imgurl", personinfo.getData().getThumbcard());
                    startActivity(In1);
                    break;
                case R.id.personinfo_care_btn:
                    focusOrCance();
                    break;
                case R.id.personinfo_call_img:
                    Intent intent3 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + personinfo.getData().getMobile()));
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent3);
                    break;
            }
        }
    }

    public void focusOrCance() {
        String userid = sharedUtils.getData(this, "userid");
        String s = getIntent().getStringExtra("userid");
        if (userid.equals(s)) {
            TextUtils.Toast(this, "自己不能关注自己！");
        } else {
            map.put("token", sharedUtils.getData(this, "token"));
            map.put("focused_id", getIntent().getStringExtra("id"));
            String url = API.BASEURL + API.FOCUS_OR_CANCEL;
            postAsyn(this, url, map, this, 4);
        }
    }
    public void getPersonInfoData() {
        map.put("token", sharedUtils.getData(this, "token"));
        map.put("user_id", getIntent().getStringExtra("userid"));
        map.put("showType", "1");
        String url = API.BASEURL + API.GET_ZONE_FRIEND;
        postAsyn(this, url, map, this, 1);
    }

    public void getTaPur(String type, int type1) {
        Map<String, String> map = new HashMap<>();
        map.put("token", sharedUtils.getData(this, "token"));
        map.put("userid", getIntent().getStringExtra("userid"));
        map.put("type", type);
        map.put("page", "1");//求购
        map.put("size", "50");
        String url = API.BASEURL + API.GET_TA_PUR;
        postAsyn(this, url, map, this, type1);
    }

    public void showInfo(PersonInfoBean mySelfInfo) {
        try {
            Glide.with(this)
                    .load(mySelfInfo.getData().getThumb())
                    .placeholder((mySelfInfo.getData().getSex().equals("男")) ? (R.drawable.contact_image_defaul_male) : (R.drawable.contact_image_defaul_female))
                    .into(image_tx);
            image_rz.setImageResource((mySelfInfo.getData().getIs_pass().equals("0")) ? (R.drawable.icon_identity) : (R.drawable.icon_identity_hl));
            btn_care.setText(mySelfInfo.getData().getStatus());
            text_name.setText(mySelfInfo.getData().getName() + "  " + mySelfInfo.getData().getSex());
            text_gs.setText("公司：" + mySelfInfo.getData().getC_name());
            text_dh.setText("联系电话： " + mySelfInfo.getData().getMobile());
            text_gj.setText("发布供给：" + mySelfInfo.getData().getSale() + "条" + "  发布求购：" + mySelfInfo.getData().getBuy() + "条");
            textView_dzh.setText("地址：" + mySelfInfo.getData().getAddress());
            image_call.setImageResource(mySelfInfo.getData().getMobile().contains("*")?(0):(R.drawable.btn_dial));
            String zhy = mySelfInfo.getData().getNeed_product();
            String type = mySelfInfo.getData().getType();
            textView_zhy.setText(type.equals("1") ? ("我的需求：" + zhy) : ("我的主营：" + zhy));
            Glide.with(this).load(mySelfInfo.getData().getThumbcard()).placeholder(R.drawable.card).into(image_show);
            if (mySelfInfo.getData().getType().equals("1")) {
                linearLayout.setVisibility(View.VISIBLE);
                textView_num.setText(mySelfInfo.getData().getMonth_consum());
                textView_product.setText(mySelfInfo.getData().getMain_product());
            } else {
                linearLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void callBack(Object object, int type) {
        try {
            Gson gson = new Gson();
            if (type == 1 && new JSONObject(object.toString()).getString("err").equals("0")) {
                personinfo = gson.fromJson(object.toString(), PersonInfoBean.class);
                if (personinfo != null) {
                    showInfo(personinfo);
                }
            }
            if (type == 2) {
                if (new JSONObject(object.toString()).getString("err").equals("0")) {
                    p1 = gson.fromJson(object.toString(), PersonSupplyDemadBean.class);
                    list = p1.getData();
                    p_adapter = new PersonSupplyDemandAdapter(this, list);
                    myListview_supply.setAdapter(p_adapter);
                    textView_supply.setClickable(true);
                } else {
                    textView_show1.setText("没有更多求购信息！");
                    textView_supply.setClickable(false);
                }

            }
            if (type == 3) {
                if (new JSONObject(object.toString()).getString("err").equals("0")) {
                    p2 = gson.fromJson(object.toString(), PersonSupplyDemadBean.class);
                    list = p2.getData();
                    p_adapter = new PersonSupplyDemandAdapter(this, list);
                    myListview_demand.setAdapter(p_adapter);
                    textView_demand.setClickable(true);
                } else {
                    textView_show2.setText("没有更多供给信息！");
                    textView_demand.setClickable(false);
                }
            }
            if (type == 4 && new JSONObject(object.toString()).getString("err").equals("0")) {
                TextUtils.Toast(this, new JSONObject(object.toString()).getString("msg"));
                if (new JSONObject(object.toString()).getString("msg").equals("关注成功")) {
                    btn_care.setText("取消关注");
                } else {
                    btn_care.setText("关   注");
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
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
