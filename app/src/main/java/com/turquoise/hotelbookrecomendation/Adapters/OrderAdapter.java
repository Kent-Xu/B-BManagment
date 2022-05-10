package com.turquoise.hotelbookrecomendation.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.turquoise.hotelbookrecomendation.Activities.AirbnbApplication;
import com.turquoise.hotelbookrecomendation.Activities.HolderMainActivity;
import com.turquoise.hotelbookrecomendation.Activities.LoginActivity;
import com.turquoise.hotelbookrecomendation.Activities.UserMainActivity;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.dialog.AssessDialog;
import com.turquoise.hotelbookrecomendation.model.Order;
import com.turquoise.hotelbookrecomendation.model.OrderResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private Activity activity;
    private View view;
    private OrderViewHolder OrderViewHolder;
    private List<Order> orders;
    private OrderResult orderResult = new OrderResult();
    private View.OnClickListener mcicklistener;
   private AssessDialog assessDialog;
   private int state =0;

    public void setState(int state) {
        this.state = state;

    }

    public OrderAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

    }
    public void FormAdapter(Activity activity) {

        this.activity =activity;
    }
    public void setOrders(List<Order> lists) {
        this.orders = lists;
       orderResult.setOrders(orders);
        OrderResult orderResult = new OrderResult();
        orderResult.setOrders(orders);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.ordercard, parent, false);
        OrderViewHolder = new OrderViewHolder(view);

        return OrderViewHolder;
    }

    public String state(int i){
        switch (i){
            case 0:
                return "订单未开始";

            case 1:
                return"订单进行中";
            case 2:
                return"订单已完成";
            case 3:
                return "订单已取消";
            case 4:
                return "订单已退款";
            case 5:
                return "订单退款中";
            case 6:
                return "订单退款被驳回";
        }
        return "未知订单状态";
    }
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        holder.hotelName.setText(orders.get(position).getName());

        holder.orderstate.setText( state(orders.get(position).getOrderstate()));
        holder.priceView.setText("￥"+orders.get(position).getTotalprice());

            holder.starttime.setText(sdf.format(new Date(Long.valueOf(orders.get(position).getStartdate())+1000*3600*24)));
            holder.endtime.setText(sdf.format(new Date(Long.valueOf(orders.get(position).getEnddate())+1000*3600*24)));


       holder.assessment.setText(orders.get(position).getAssessment());
           holder.ratingBar.setRating((float)orders.get(position).getOrderpoint()/2);

            if(orders.get(position).getOrderstate()==5){
            holder.buttonbar.setVisibility(View.GONE);

        }
           if(orders.get(position).getOrderstate()==2|orders.get(position).getOrderpoint()!=0)
           {holder.assessment.setVisibility(View.VISIBLE);
               holder.ratingBar.setVisibility(View.VISIBLE);
               holder.assessbutton.setVisibility(View.VISIBLE);}

           else
               {holder.assess.setVisibility(View.GONE);
                   holder.ratingBar.setVisibility(View.INVISIBLE);
               holder.assessbutton.setVisibility(View.GONE);}
           if(state>0)holder.buttonbar.setVisibility(View.GONE);


        holder.assessbutton.setOnClickListener(v->{
            mcicklistener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_save:

                            holder.ratingBar.setRating(assessDialog.ratingBar.getRating());
                            holder.assessment.setText(assessDialog.text_assess.getEditableText().toString());
                            try {
                                sendAssessment(orders.get(position).getOrderid()
                                    ,(int)assessDialog.ratingBar.getRating()*2
                                    ,assessDialog.text_assess.getText().toString());
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            assessDialog.dismiss();
                            break;
                    }
                }
            };
         assessDialog = new  AssessDialog(activity, R.style.alert_dialog_dark, mcicklistener);
            assessDialog.show();
        });
           holder.refundbutton.setOnClickListener(v ->{
               applyForRefunding(orders.get(position).getOrderid());
               holder.orderstate.setText("订单退款中");

           } );
    }

    private void applyForRefunding(int orderid) {
        final String[] outcome = new String[1];
        RequestBody body = new FormBody.Builder()
                .add("orderid",String.valueOf(orderid)).build();
        Request request = new Request.Builder()
                .url("http://192.168.233.1:8080/Airbnb/RefundOrder")
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("1111", "onFailure: " + e.getMessage());
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg); // 向Handler发送消息,更新UI


            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("1112", response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("1113", headers.name(i) + ":" + headers.value(i));
                }
                outcome[0] =response.body().string().replaceAll("\r|\n","");
                Log.d("1114", "onResponse: " + outcome[0]);

                if(  outcome[0].equals("failed")){
                    System.out.println("申请退款失败");
                    Message msg = new Message();
                    msg.what = 1;

                    mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                }
                else {
                    System.out.println("申请退款成功");
                    Message msg = new Message();
                    msg.what = 2;

                    mHandler.sendMessage(msg);
                }
            }
            Handler mHandler = new Handler(){

                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("错误")
                                    .setContentText("服务器无法连接")
                                    .show();
                            break;
                        case 1:
                            new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("申请退款失败")

                                    .show();
                            break;
                        case 2:
                            new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("申请退款成功")

                                    .show();
                            break;
                    }
                };
            };

        });
    }

    private void sendAssessment(int orderid, int orderpoint, String assessment) throws UnsupportedEncodingException {

        final String[] outcome = new String[1];
        RequestBody body = new FormBody.Builder()
                .add("orderid",String.valueOf(orderid))
                .add("orderpoint",String.valueOf(orderpoint))
                .add("assessment", URLEncoder.encode(assessment,"UTF-8")).build();

        Request request = new Request.Builder()
                .url("http://192.168.233.1:8080/Airbnb/AssessOrder")
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("1111", "onFailure: " + e.getMessage());
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg); // 向Handler发送消息,更新UI


            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("1112", response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d("1113", headers.name(i) + ":" + headers.value(i));
                }
                outcome[0] =response.body().string().replaceAll("\r|\n","");
                Log.d("1114", "onResponse: " + outcome[0]);

                if(  outcome[0].equals("failed")){
                    System.out.println("评价失败");
                    Message msg = new Message();
                    msg.what = 1;

                    mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                }
                else {
                    System.out.println("评价成功");
                    Message msg = new Message();
                    msg.what = 2;

                    mHandler.sendMessage(msg);
                }
            }
            Handler mHandler = new Handler(){

                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("错误")
                                    .setContentText("服务器无法连接")
                                    .show();
                            break;
                        case 1:
                            new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("评价失败")

                                    .show();
                            break;
                        case 2:
                            new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("评价成功")

                                    .show();
                            break;
                    }
                };
            };

        });

    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView hotelName,orderstate,starttime,endtime,priceView,assessment;
        Button assessbutton,refundbutton;
        RatingBar ratingBar;
       LinearLayout assess;
       RelativeLayout buttonbar;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

              hotelName=itemView.findViewById(R.id.hotelName);
            orderstate=itemView.findViewById(R.id.orderstate);
            starttime=itemView.findViewById(R.id.starttime);
            endtime=itemView.findViewById(R.id.finishtime);
            priceView=itemView.findViewById(R.id.priceView);
            assessment=itemView.findViewById(R.id.assessment);
            assessbutton=itemView.findViewById(R.id.assessbutton);
            refundbutton=itemView.findViewById(R.id.refundbutton);
            ratingBar=itemView.findViewById(R.id.ratingBar);
           assess=itemView.findViewById(R.id.assess);
            buttonbar=itemView.findViewById(R.id.buttonbar);
        }
    }


}
