package com.turquoise.hotelbookrecomendation.CalendarView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.HotelResult;
import com.turquoise.hotelbookrecomendation.model.PriceList;
import com.turquoise.hotelbookrecomendation.model.PriceListResult;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 思路
 * 1、生成日历数据
 * 生成每个月的每一天的数据
 */

public class MainActivity_ extends AppCompatActivity {
    String js;
    PriceListResult results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getResultsJson();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
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
                pDialog.setTitleText("Success!")
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                CalendarList calendarList=findViewById(R.id.calendarList);
                Gson gson=new Gson();
                results =gson.fromJson(js, PriceListResult.class);
                for(PriceList pl:results.getPriceLists()){
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                    Date date=new Date();
                    date.setTime(Long.valueOf(pl.getOrderdate()));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(calendar.DATE,1);
                    date=calendar.getTime();
                    calendarList.setPrice(date, pl.getPrice());
                    calendarList.setState(date,pl.getState());
                    calendarList.setOnDateSelected(new CalendarList.OnDateSelected() {

                        @Override
                        public void selected(String startDate, String endDate) {
                            Intent i= new Intent();
//将想要传递的数据用putExtra封装在intent中
                            setResult(RESULT_OK,i);
                            i.putExtra("startdate",startDate);
                            i.putExtra("enddate",endDate);

                            finish();
                        }
                    });
                }
            }
        }.start();


    }
    public void getResultsJson(){

        final String[] outcome = new String[1];
        RequestBody body = new FormBody.Builder()
                .add("houseid", getIntent().getStringExtra("houseid")
                ).build();
        Request request = new Request.Builder()
                .url("http://192.168.233.1:8080/Airbnb/GetHousePriceList")
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
                //Log.d("1115", "onResponse: " + outcome[0]);

                if(  outcome[0].equals("failed")){
                    System.out.println("获取失败");
                    Message msg = new Message();
                    msg.what = 1;

                    mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                }
                else {
                    js=outcome[0];


                }
            }
            Handler mHandler = new Handler(){

                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            new SweetAlertDialog(getApplication(), SweetAlertDialog.ERROR_TYPE)
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
