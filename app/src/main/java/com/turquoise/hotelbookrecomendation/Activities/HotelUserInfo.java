package com.turquoise.hotelbookrecomendation.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import com.turquoise.hotelbookrecomendation.CalendarView.MainActivity_;
import com.turquoise.hotelbookrecomendation.R;

import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.HotelResult;


import java.io.IOException;
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

public class HotelUserInfo extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView hotelImage;
    private TextView hotelDesc, views, drafts, date0,date1,price;
    private Button calendarprice,orders,applyforchange,pricebutton;
    private DatePicker datePicker;
    Hotel hotel;
    int pos;
    HotelResult hotelResult;
     private AirbnbApplication airbnbApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_user_info);
        toolbar = findViewById(R.id.toolbarInfo);
        hotelImage = findViewById(R.id.hotelImage);
        hotelDesc = findViewById(R.id.hotelDesc);
        views = findViewById(R.id.views);
        drafts = findViewById(R.id.draftText);
        drafts.setOnClickListener(v->{
            Intent i=new Intent(HotelUserInfo.this, MapActivity.class);
            i.putExtra("address",hotel.getAddress());
            startActivityForResult(i,1);
        });
        date0=findViewById(R.id.starttime);
        date1=findViewById(R.id.finishtime);
        price=findViewById(R.id.setprice);
        pricebutton=findViewById(R.id.pricebutton);
        calendarprice=findViewById(R.id.priceCalendar);
        calendarprice.setOnClickListener(v -> {
            Intent i=new Intent(HotelUserInfo.this, MainActivity_.class);
            i.putExtra("houseid",hotel.getId());
            startActivityForResult(i,1);
        });
        pricebutton.setOnClickListener(v->{
                   createorder();
        });
        orders=findViewById(R.id.orders);
        orders.setOnClickListener(v-> {
            Intent i=new Intent(HotelUserInfo.this, HistoryAssessActivity.class);
            i.putExtra("houseid",hotel.getId());
            i.putExtra("mode","2");
            startActivityForResult(i,2);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    date0.setText(data.getStringExtra("startdate"));
                    date1.setText(data.getStringExtra("enddate"));
                }
                break;
            default:
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        hotel = (Hotel) getIntent().getExtras().getSerializable("data");
        pos = getIntent().getExtras().getInt("pos");

        toolbar.setTitle(hotel.getName());

        setSupportActionBar(toolbar);
        Picasso
                .with(HotelUserInfo.this)
                .load(Uri.parse(hotel.getImageUrl()))
                .into(hotelImage);
        hotelDesc.setText(hotel.getDetail());

        views.setText(hotel.getName());
        drafts.setText(hotel.getAddress());
        price.setText("￥"+hotel.getPrice());
        date0.setText(getIntent().getStringExtra("dt0"));
        date1.setText(getIntent().getStringExtra("dt1"));

    }

     public void createorder(){
         airbnbApplication= (AirbnbApplication) getApplication();
       String id =airbnbApplication.getId();
         final String[] outcome = new String[1];
         RequestBody body = new FormBody.Builder()
                 .add("houseid",hotel.getId())
                 .add("userid",id)
                 .add("startdate",date0.getText().toString())
                 .add("enddate",date1.getText().toString())
                 .add("totalprice",price.getText().toString().substring(1)).build();

         Request request = new Request.Builder()
                 .url("http://192.168.233.1:8080/Airbnb/CreateOrder")
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
                     System.out.println("预定失败");
                     Message msg = new Message();
                     msg.what = 1;

                     mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                 }
                 else {
                     System.out.println("预定成功");
                     Message msg = new Message();
                     msg.what = 2;

                     mHandler.sendMessage(msg); // 向Handler发送消息,更新UI


                 }
             }
             Handler mHandler = new Handler(){

                 public void handleMessage(Message msg) {
                     switch (msg.what) {
                         case 0:
                             new SweetAlertDialog(HotelUserInfo.this, SweetAlertDialog.ERROR_TYPE)
                                     .setTitleText("错误")
                                     .setContentText("服务器无法连接")
                                     .show();
                             break;
                         case 1:
                             new SweetAlertDialog(HotelUserInfo.this, SweetAlertDialog.ERROR_TYPE)
                                     .setTitleText("预定失败")
                                     .setContentText("请检查是否重复预订")
                                     .show();
                             break;
                         case 2:
                             new SweetAlertDialog(HotelUserInfo.this, SweetAlertDialog.WARNING_TYPE)
                                     .setTitleText("预定成功")
                                     .show();
                             break;
                     }
                 };
             };

         });

     }



}
