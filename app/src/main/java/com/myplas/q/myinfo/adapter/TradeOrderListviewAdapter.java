package com.myplas.q.myinfo.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myplas.q.R;
import com.myplas.q.common.api.API;
import com.myplas.q.common.netresquset.ResultCallBack;
import com.myplas.q.common.utils.TextUtils;
import com.myplas.q.common.view.MyListview;
import com.myplas.q.guide.activity.BaseActivity;
import com.myplas.q.myinfo.beans.EDuBean;
import com.myplas.q.myinfo.beans.OrderListsBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 编写：黄双
 * 邮箱：15378412400@163.com
 * 时间： 2017/6/121519.
 */

public class TradeOrderListviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ResultCallBack {
    private Context context;
    private List<OrderListsBean.DataBean.ListBean> mList;

    private int position;
    private Handler mHandler;
    private MyOnClickListener mListener;

    private Map<Integer, viewHolder> mMapViews;
    private Map<Integer, TextView> mMapTextViews;
    private Map<Integer, ImageView> mImageViewMap1;

    private String invoice_status, sign_status, billing_status, isHaveInvoicesDetail;

    public TradeOrderListviewAdapter(Context context, List<OrderListsBean.DataBean.ListBean> mList) {
        this.mList = mList;
        mHandler = new Handler();
        this.context = context;
        mMapViews = new HashMap<>();
        mMapTextViews = new HashMap<>();
        mImageViewMap1 = new HashMap<>();
    }


    public void setList(List<OrderListsBean.DataBean.ListBean> list) {
        mList = list;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_tradeorder, parent, false);//这个布局就是一个imageview用来显示图片
        viewHolder holder = new viewHolder(view, viewType);
        mMapViews.put(viewType, holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        viewHolder viewHolder = mMapViews.get(position);
        try {
            if (position == mList.size() - 1) {
                viewHolder.mView.setVisibility(View.GONE);
            }
            String html1 = "   合计 " + "<font color='#ff5550'>¥" + mList.get(position).getTotal_price() + "</font>";
            String html2 = "   (含运费 " + "<font color='#ff5550'>¥" + mList.get(position).getTransport() + "</font>" + ")";
            viewHolder.textView_title.setText("订单号：" + mList.get(position).getOrder_sn());
            viewHolder.textView_num2.setText("共 " + mList.get(position).getTotal_num() + "吨  ");
            viewHolder.textView_tprice.setText(Html.fromHtml(html1));
            //viewHolder.textView_feight.setText(Html.fromHtml(html2));


            List<OrderListsBean.DataBean.ListBean.ProductBean> listProduct = mList.get(position).getProduct();
            TradeOrderLV_ItemAdapter itemAdapter = new TradeOrderLV_ItemAdapter(context, listProduct);
            viewHolder.mMyListview.setAdapter(itemAdapter);

            setStatus(position);

            //设置按钮的状态
            showStatusInfo(viewHolder, position);

            //签收类
            viewHolder.mImageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStatus(position);
                    if (sign_status.equals("0")) {
                        TradeOrderListviewAdapter.this.position = position;
                        confirmSign(mList.get(position).getO_id());
                    } else if (sign_status.equals("2")) {
                        TextUtils.Toast(context, "订单未全部发货，暂无法签收!");
                    }
                }
            });
            //申请---
            viewHolder.mImageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStatus(position);
                    if (billing_status.equals("0")) {
                        mListener.onClick1(mList.get(position).getOrder_sn());
                    } else if (billing_status.equals("2")) {
                        TextUtils.Toast(context, "订单未签收或未付款，暂无法开票!");
                    }
                }
            });
            //发票详情
            viewHolder.mImageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStatus(position);
                    mListener.onClick3(mList.get(position).getOrder_sn());
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }


    private void setStatus(int position) {
        sign_status = mList.get(position).getConfirm_receipt();
        invoice_status = mList.get(position).getInvoice_status();
        billing_status = mList.get(position).getApply_billing();
        isHaveInvoicesDetail = mList.get(position).getBilling_detail_list();
    }

    private void showStatusInfo(viewHolder viewHolder, int position) {
        if (invoice_status.equals("1")) {
            viewHolder.textView_status.setText("未开票");
        } else if (invoice_status.equals("2")) {
            viewHolder.textView_status.setText("部分开票");
        } else if (invoice_status.equals("3")) {
            //viewHolder.mImageView2.setVisibility(View.GONE); //如果全部开票
            viewHolder.textView_status.setText("全部开票");
        }

        if (sign_status.equals("1")) {//已签收
            viewHolder.mImageView1.setImageResource(R.drawable.btn_signed);
        } else {                      //未签收
            viewHolder.mImageView1.setImageResource(R.drawable.btn_submit);
        }

        if (!billing_status.equals("3")) { //判断是否显示申请开票的按钮
            if (billing_status.equals("1")) { //申请中。。。。
                viewHolder.mImageView2.setImageResource(R.drawable.btn_in_application);
            } else {
                viewHolder.mImageView2.setImageResource(R.drawable.btn_apply);
            }
        } else {
            viewHolder.mImageView2.setVisibility(View.GONE);
        }

        setMarginRight(viewHolder.mImageView1, 30);
        setMarginRight(viewHolder.mImageView2, 0);

//        if (invoice_status.equals("2") || invoice_status.equals("3")) {
//            setMarginRight(viewHolder.mImageView2,30);
//            viewHolder.mImageView3.setImageResource(R.drawable.btn_invoice_details);
//        }
        if (isHaveInvoicesDetail.equals("1") || invoice_status.equals("3")) {
            setMarginRight(viewHolder.mImageView2, 30);
            viewHolder.mImageView3.setImageResource(R.drawable.btn_invoice_details);
        }
    }

    public void setMarginRight(ImageView imageView, int right) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        lp.rightMargin = right;
        imageView.setLayoutParams(lp);
    }

    public void confirmSign(String orderNum) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("o_id", orderNum);
        BaseActivity.postAsyn(context, API.BASEURL + API.ORDERSIGN, map, this, 1);
    }

    @Override
    public void callBack(Object object, int type) {
        try {
            String err = new JSONObject(object.toString()).getString("err");
            if (err.equals("0")) {
                mListener.onClick2();
//                String time = new JSONObject(object.toString()).getString("time");
//                mMapTextViews.get(position).setVisibility(View.VISIBLE);
//                mMapTextViews.get(position).setText("签收时间：" + time);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mMapTextViews.get(position).setVisibility(View.GONE);
//                    }
//                }, 1500);

            } else {
                String msg = new JSONObject(object.toString()).getString("msg");
                TextUtils.Toast(context, msg);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void failCallBack(int type) {

    }

    class viewHolder extends RecyclerView.ViewHolder {
        View mView;
        MyListview mMyListview;
        ImageView mImageView1, mImageView2, mImageView3;
        TextView textView_title, textView_tprice, textView_feight, textView_num2, textView_signed, textView_status;

        public viewHolder(View itemView, int position) {
            super(itemView);
            textView_signed = (TextView) itemView.findViewById(R.id.popou_layout_signed_text);
            textView_title = (TextView) itemView.findViewById(R.id.tradeorder_item_title);
            textView_tprice = (TextView) itemView.findViewById(R.id.tradeorder_item_tprice);
            textView_feight = (TextView) itemView.findViewById(R.id.tradeorder_item_feight);
            textView_num2 = (TextView) itemView.findViewById(R.id.tradeorder_item_num2);
            mImageView1 = (ImageView) itemView.findViewById(R.id.tradeorder_item_img1);
            mImageView2 = (ImageView) itemView.findViewById(R.id.tradeorder_item_img2);
            mImageView3 = (ImageView) itemView.findViewById(R.id.tradeorder_item_img3);
            textView_status = (TextView) itemView.findViewById(R.id.tradeorder_item_zht);
            mMyListview = (MyListview) itemView.findViewById(R.id.tradeorder_item_listview);
            mView = itemView.findViewById(R.id.tradeorder_item_divider);

            mImageViewMap1.put(position, mImageView1);
            mMapTextViews.put(position, textView_signed);
        }
    }

    public interface MyOnClickListener {
        void onClick1(String order_sn);

        void onClick2();

        void onClick3(String order_sn);
    }

    public void setMyOnClickListener(MyOnClickListener myOnClickListener) {
        this.mListener = myOnClickListener;
    }
}
