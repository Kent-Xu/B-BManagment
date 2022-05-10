package com.turquoise.hotelbookrecomendation.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.turquoise.hotelbookrecomendation.Activities.AirbnbApplication;
import com.turquoise.hotelbookrecomendation.Adapters.HotelAdapter;
import com.turquoise.hotelbookrecomendation.Adapters.OrderAdapter;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.HotelResult;
import com.turquoise.hotelbookrecomendation.model.OrderResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserOrderFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AirbnbApplication airbnbApplication;
    // TODO: Rename and change types of parameters
    private String js="{" +
            "  \"orders\": []}";
    private String mParam1;
    private String mParam2;
    private OrderAdapter orderAdapter;
    OrderResult orderResult;
    RecyclerView recyclerView;
    int lastPos=0;

    public UserOrderFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Thread thread =new Thread();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_user_order_frag, container, false);
        recyclerView=view.findViewById(R.id.orderList);


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            lastPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();}
        catch (Exception e){
            lastPos=0;
        }finally {
            return;
        }


    }



    @Override
    public void onResume() {
        super.onResume();
        getOrderJson();
        OrderAdapter orderAdapter=new OrderAdapter(getContext());
        orderAdapter.FormAdapter(getActivity());
        Gson gson=new Gson();
        System.out.println(js);
        SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        new CountDownTimer(200 * 7, 200) {
            int i=-1;
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                i++;
                switch (i){
                    case 0:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                }
            }

            public void onFinish() {
                i = -1;
                pDialog.dismiss();
                orderResult =gson.fromJson(js, OrderResult.class);
                orderAdapter.setOrders(orderResult.getOrders());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(orderAdapter);

                recyclerView.getLayoutManager().scrollToPosition(lastPos);
            }
        }.start();







    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void updateList() {
        if(orderAdapter!=null){

            orderAdapter.setOrders(orderResult.getOrders());

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void getOrderJson(){
        airbnbApplication= (AirbnbApplication)  getActivity().getApplication();
        String id =airbnbApplication.getId();
        final String[] outcome = new String[1];
        RequestBody body = new FormBody.Builder()
                .add("userid",id).build();
        Request request = new Request.Builder()
                .url("http://192.168.233.1:8080/Airbnb/GetUserOrders")
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
                Log.d("1115", "onResponse: " + outcome[0]);

                if(  outcome[0].equals("failed")){
                    System.out.println("获取失败");
                    Message msg = new Message();
                    msg.what = 1;

                    mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                }
                else {
                    js=outcome[0];
                    Gson gson=new Gson();
                    orderResult =gson.fromJson(js, OrderResult.class);
                }
            }
            Handler mHandler = new Handler(){

                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("错误")
                                    .setContentText("服务器无法连接")
                                    .show();
                            break;

                    }
                };
            };

        });



    }
}
