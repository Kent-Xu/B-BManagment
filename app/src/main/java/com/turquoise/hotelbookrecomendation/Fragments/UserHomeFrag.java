package com.turquoise.hotelbookrecomendation.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.turquoise.hotelbookrecomendation.Activities.HouseInfoMngActivity;
import com.turquoise.hotelbookrecomendation.Activities.LoginActivity;
import com.turquoise.hotelbookrecomendation.Activities.UserMainActivity;
import com.turquoise.hotelbookrecomendation.Adapters.HouseAdapter;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.HotelResult;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserHomeFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
     int n=0;
    // TODO: Rename and change types of parameters
    private String js="{\"hotels\":[]}";
    private String js0=js;
    private String mParam1;
    private String mParam2;
    private HouseAdapter houseAdapter;
    HotelResult hotelResult;
    RecyclerView recyclerView;
    int lastPos=0;
   TextView startdate,enddate;
    public UserHomeFrag() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.hotelList);


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("PPPPP"+n++);
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
        getHotelJson();

        SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        new CountDownTimer(150 * 7, 150) {
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
                if(js.equals(js0))
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("当日无可用民宿")
                            .setContentText("请更换日期")
                            .show();

                pDialog.dismiss();
                HouseAdapter houseAdapter=new HouseAdapter(getContext());
                houseAdapter.dt0=startdate.getText().toString();
                houseAdapter.dt1=enddate.getText().toString();
                Gson gson=new Gson();
                System.out.println(js);

                hotelResult =gson.fromJson(js, HotelResult.class);
                for(Hotel hotel:hotelResult.getHotels()){

                    hotel.setImageUrl("http://192.168.233.1:8080/img/"+hotel.getName()+"_cover.jpg");
                    System.out.println(hotelResult.toString());
                    houseAdapter.setHotels(hotelResult.getHotels());

                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(houseAdapter);

                    recyclerView.getLayoutManager().scrollToPosition(lastPos); }
            }
        }.start();


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       startdate= ((UserMainActivity) context).findViewById(R.id.starttime);
        enddate= ((UserMainActivity) context).findViewById(R.id.finishtime);
    }

    public void updateList() {
        if(houseAdapter!=null){

            houseAdapter.setHotels(hotelResult.getHotels());

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void getHotelJson(){
        final String[] outcome = new String[1];
        RequestBody body = new FormBody.Builder()
                .add("userid",getActivity().getIntent().getStringExtra("userid"))
                .add("startdate",startdate.getText().toString())
                .add("enddate",enddate.getText().toString()).build();
        Request request = new Request.Builder()
                .url("http://192.168.233.1:8080/Airbnb/GetAvailableHouses")
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
                    hotelResult =gson.fromJson(js, HotelResult.class);
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
