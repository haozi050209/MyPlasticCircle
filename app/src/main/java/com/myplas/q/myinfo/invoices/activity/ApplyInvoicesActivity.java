package com.myplas.q.myinfo.invoices.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myplas.q.R;
import com.myplas.q.common.api.API;
import com.myplas.q.common.netresquset.ResultCallBack;
import com.myplas.q.common.utils.TextUtils;
import com.myplas.q.common.view.MyListview;
import com.myplas.q.guide.activity.BaseActivity;
import com.myplas.q.myinfo.invoices.adapter.ApplyInvoiceAdapter;
import com.myplas.q.myinfo.beans.ApplyInvoiceBean;
import com.myplas.q.myinfo.invoices.adapter.ApplyInvoiceAdapter;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.Information;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编写： 黄双
 * 电话：15378412400
 * 邮箱：15378412400@163.com
 * 时间：2017/3/28 10:25
 */
public class ApplyInvoicesActivity extends BaseActivity implements View.OnClickListener, ApplyInvoiceAdapter.MyOnClickListener, ResultCallBack {
    private Information information;
    private String appkey = "c1ff771c06254db796cd7ce1433d2004";

    private Button mButton;
    private EditText mEditText;
    private MyListview mListView;
    private ImageView mImageView;
    private TextView mTextView_cm, mTextView_tprice, mTextView_notapplied, mTextView_apply, textView_allprice;

    private ApplyInvoiceBean bean;
    private ApplyInvoiceAdapter mAdapter;

    private StringBuffer ids, b_number;
    private String keywords, billing_price, rise, unbilling_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_applyinvoices);
        goBack(findViewById(R.id.img_back));

        initView();
        getInvioceList(getIntent().getStringExtra("order_sn"));
    }

    public void initView() {
        ids = new StringBuffer();
        b_number = new StringBuffer();

        mImageView = F(R.id.img_contact);
        mButton = F(R.id.applyinvoices_confirm);
        mEditText = F(R.id.applyinvoices_remark);
        mListView = F(R.id.applyinvoices_listview);
        mTextView_cm = F(R.id.applyinvoices_company);
        mTextView_apply = F(R.id.applyinvoices_apply);
        mTextView_tprice = F(R.id.applyinvoices_tprice);
        textView_allprice = F(R.id.item_lv_invoice_allprice);
        mTextView_notapplied = F(R.id.applyinvoices_notapplied);

        mButton.setOnClickListener(this);
        mImageView.setOnClickListener(this);
    }

    public void getInvioceList(String keywords) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", "1");
        map.put("size", "20");
        map.put("order_sn", keywords);
        postAsyn(this, API.BASEURL + API.INVOICE, map, this, 1);
    }

    public void applyInvioce() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("rise", rise);
        map.put("remark", keywords);
        map.put("id", ids.toString());
        map.put("billing_price", billing_price);
        map.put("b_number", b_number.toString());
        map.put("unbilling_price", unbilling_price);
        map.put("order_sn", getIntent().getStringExtra("order_sn"));
        postAsyn(this, API.BASEURL + API.INVOICEDETAILADD, map, this, 2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_contact:
                information = new Information();
                information.setAppkey(appkey);
                SobotApi.startSobotChat(this, information);
                break;
            case R.id.applyinvoices_confirm:
                mButton.setClickable(false);
                mButton.setBackgroundColor(getResources().getColor(R.color.color_litlegray));
                keywords = mEditText.getText().toString();
                applyInvioce();
                break;

        }
    }

    @Override
    public void callBack(Object object, int type) {
        try {
            Gson gson = new Gson();
            String err = new JSONObject(object.toString()).getString("err");
            if (type == 1) {
                if (err.equals("0")) {
                    mListView.setVisibility(View.VISIBLE);
                    bean = gson.fromJson(object.toString(), ApplyInvoiceBean.class);
                    mAdapter = new ApplyInvoiceAdapter(this, bean.getData().getList());
                    mAdapter.setMyOnClickListener(this);
                    mListView.setAdapter(mAdapter);
                    showInfo();
                    getArray();
                }
            }
            if (type == 2) {
                if (err.equals("0")) {
                    TextUtils.Toast(this, "提交成功！");
                    finish();
                } else {
                    mButton.setClickable(true);
                    mButton.setBackgroundColor(getResources().getColor(R.color.color_red));
                    String msg = new JSONObject(object.toString()).getString("msg");
                    TextUtils.Toast(this, msg);
                }
            }
        } catch (Exception e) {
        }
    }

    public void showInfo() {
        rise = bean.getData().getDetail().getRise();
        billing_price = bean.getData().getDetail().getBilling_price();
        unbilling_price = bean.getData().getDetail().getUnbilling_price();

        mTextView_cm.setText(bean.getData().getDetail().getRise());
        mTextView_tprice.setText(getDecimalFormatData(bean.getData().getDetail().getTotal_price()) + "");
        mTextView_notapplied.setText(getDecimalFormatData(bean.getData().getDetail().getUnbilling_price()) + "");
        textView_allprice.setText("申请开票金额合计：" + getDecimalFormatData(bean.getData().getDetail().getBilling_price()) + "");
        mTextView_apply.setText(getDecimalFormatData(bean.getData().getDetail().getBilling_price()) + "");
    }

    public void getArray() {
        for (int i = 0; i < bean.getData().getList().size(); i++) {
            ids = ids.append(bean.getData().getList().get(i).getId());
            b_number = b_number.append(bean.getData().getList().get(i).getB_number());
            if (i != bean.getData().getList().size() - 1) {
                ids = ids.append(",");
                b_number = b_number.append(",");
            }
        }
    }

    @Override
    public void failCallBack(int type) {
        if (type == 2) {
            mButton.setClickable(true);
            mButton.setBackgroundColor(getResources().getColor(R.color.color_red));
        }
    }

    //适配器的回调
    @Override
    public void onClick(Map<Integer, Double> map, Map<Integer, String> map2) {
        double d = 0;
        b_number = new StringBuffer();
        List<Integer> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        try {
            for (int key : map2.keySet()) {
                list.add(key);
            }
            Collections.sort(list);

            for (int i = 0; i < list.size(); i++) {
                b_number = b_number.append(map2.get(i)).append(",");
                d += map.get(i);
            }
            b_number = new StringBuffer(b_number.subSequence(0, b_number.lastIndexOf(",")));
            billing_price = d + "";
            mTextView_apply.setText(d + "");
            textView_allprice.setText("申请开票金额合计：" + d + "");
        } catch (Exception e) {
        }
    }

    public String getDecimalFormatData(String data) {
        DecimalFormat format = new DecimalFormat("#.0000");
//        return Double.parseDouble(format.format(Double.parseDouble(data)));
        return data;
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
