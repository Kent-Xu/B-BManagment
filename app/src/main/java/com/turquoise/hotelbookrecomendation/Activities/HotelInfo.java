package com.turquoise.hotelbookrecomendation.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import com.turquoise.hotelbookrecomendation.CalendarView.MainActivity_;
import com.turquoise.hotelbookrecomendation.Fragments.HistoryAssessFrag;
import com.turquoise.hotelbookrecomendation.R;

import com.turquoise.hotelbookrecomendation.dialog.AssessDialog;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.HotelResult;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

public class HotelInfo extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView hotelImage;
    private TextView hotelDesc, views, drafts, date0,date1,price;
    private Button calendarprice,orders,applyforchange,pricebutton;
    private DatePicker datePicker;
    Hotel hotel;
    int pos;
    HotelResult hotelResult;
    AssessDialog assessDialog;
    View.OnClickListener mcicklistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_hotel_info);

        toolbar = findViewById(R.id.toolbarInfo);
        hotelImage = findViewById(R.id.hotelImage);
        hotelDesc = findViewById(R.id.hotelDesc);

        views = findViewById(R.id.views);
        drafts = findViewById(R.id.draftText);
        drafts.setOnClickListener(v->{
            Intent i=new Intent(HotelInfo.this, MapActivity.class);
            i.putExtra("address",hotel.getAddress());
            startActivityForResult(i,1);
        });
        date0=findViewById(R.id.starttime);
        date0.setOnClickListener(v->{showDatePickerDialog(date0); });
        date1=findViewById(R.id.finishtime);
        date1.setOnClickListener(v->{showDatePickerDialog(date1); });
        price=findViewById(R.id.setprice);
        pricebutton=findViewById(R.id.pricebutton);
        pricebutton.setOnClickListener(v -> {
            hotel = (Hotel) getIntent().getExtras().getSerializable("data");
            String dt0=date0.getText().toString().trim();
            String dt1=date1.getText().toString().trim();
            Date dat0;
            Date dat1;
            int pri;
            try {
                pri=Integer.valueOf(price.getText().toString().trim());
            } catch  (Exception e){
                new SweetAlertDialog(HotelInfo.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("错误")
                        .setContentText("价格输入有误")
                        .show();
                return;
            }



                if(dt0.equals("")|dt1.equals("")){
                    Calendar ca = Calendar.getInstance();
                    dat0=ca.getTime();
                    ca.add(Calendar.DATE,60);
                    dat1=ca.getTime();
                }
                else{
                    try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               dat0=sdf.parse(dt0);
                dat1=sdf.parse(dt1);
                Calendar ca = Calendar.getInstance();
                ca.add(Calendar.DATE,-1);
                if(dat1.before(dat0)) {
                    new SweetAlertDialog(HotelInfo.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("错误")
                            .setContentText("开始时间不得晚于结束时间")
                            .show();
                    return;
                }
               else if(dat0.before(ca.getTime())) {
                    new SweetAlertDialog(HotelInfo.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("错误")
                            .setContentText("开始时间不得早于今日")
                            .show();
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                new SweetAlertDialog(HotelInfo.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("错误")
                        .setContentText("日期输入有误")
                        .show();
                        return;
            }
            }
           setPrices(hotel.getId(),pri,dat0,dat1);
        });
        calendarprice=findViewById(R.id.priceCalendar);
        calendarprice.setOnClickListener(v -> {
            Intent i=new Intent(HotelInfo.this, MainActivity_.class);
            i.putExtra("houseid",hotel.getId());
            startActivityForResult(i,1);
        });
        orders=findViewById(R.id.orders);
        orders.setOnClickListener(v-> {
            Intent i=new Intent(HotelInfo.this, HistoryAssessActivity.class);
            i.putExtra("houseid",hotel.getId());
            i.putExtra("mode","1");
            startActivityForResult(i,1);
                });
        applyforchange=findViewById(R.id.applyForChange);
       applyforchange.setOnClickListener(v-> {

          mcicklistener=new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  switch (view.getId()) {
                      case R.id.btn_save:
                          try {
                              sendHotelDetail(assessDialog.text_assess.getText().toString());
                          } catch (UnsupportedEncodingException e) {
                              e.printStackTrace();
                          }
                          hotelDesc.setText(assessDialog.text_assess.getText());
                          assessDialog.dismiss();
                          break;
                  }
              }
          };
           assessDialog = new  AssessDialog(this, R.style.alert_dialog_dark, mcicklistener);
           assessDialog.setRatingBarInvisible();
           assessDialog.show();
        });
    }

    private void sendHotelDetail(String text) throws UnsupportedEncodingException {
        final String[] outcome = new String[1];
        RequestBody body = new FormBody.Builder()
                .add("houseid",hotel.getId())

                .add("description", URLEncoder.encode(text,"UTF-8")).build();

        Request request = new Request.Builder()
                .url("http://192.168.233.1:8080/Airbnb/ReDescribeHouse")
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
                    System.out.println("修改失败");
                    Message msg = new Message();
                    msg.what = 1;

                    mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                }
                else {
                    System.out.println("修改成功");
                    Message msg = new Message();
                    msg.what = 2;

                    mHandler.sendMessage(msg);
                }
            }
            Handler mHandler = new Handler(){

                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            new SweetAlertDialog(HotelInfo.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("错误")
                                    .setContentText("服务器无法连接")
                                    .show();
                            break;
                        case 1:
                            new SweetAlertDialog(HotelInfo.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("修改失败")

                                    .show();
                            break;
                        case 2:
                            new SweetAlertDialog(HotelInfo.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("修改成功")

                                    .show();
                            break;
                    }
                };
            };

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

   public void setPrices(String houseid,int pri,Date dat0,Date dat1){
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
           final String[] outcome = new String[1];
           RequestBody body = new FormBody.Builder()
                   .add("id",houseid)
                   .add("price",String.valueOf(pri))
                   .add("date0",sdf.format(dat0))
                   .add("date1",sdf.format(dat1)).build();

           Request request = new Request.Builder()
                   .url("http://192.168.233.1:8080/Airbnb/SetPrices")
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
                       System.out.println("设置失败");
                       Message msg = new Message();
                       msg.what = 1;

                       mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                   }
                   else {
                       System.out.println("设置成功");
                       Message msg = new Message();
                       msg.what = 2;
                       mHandler.sendMessage(msg);
                   }
               }
               Handler mHandler = new Handler(){

                   public void handleMessage(Message msg) {
                       switch (msg.what) {
                           case 0:
                               new SweetAlertDialog(HotelInfo.this, SweetAlertDialog.ERROR_TYPE)
                                       .setTitleText("错误")
                                       .setContentText("服务器无法连接")
                                       .show();
                               break;
                           case 1:
                               new SweetAlertDialog(HotelInfo.this, SweetAlertDialog.ERROR_TYPE)
                                       .setTitleText("登陆失败")
                                       .setContentText("账号或密码错误")
                                       .show();
                               break;
                           case 2:
                               new SweetAlertDialog(HotelInfo.this, SweetAlertDialog.WARNING_TYPE)
                                       .setTitleText("设置成功")
                                       .show();
                               break;
                       }
                   };
               };

           });

       }
   ;
    public void showDatePickerDialog(TextView textView){
        Calendar ca = Calendar.getInstance();
        int[] mYear = {ca.get(Calendar.YEAR)};
        int[] mMonth = {ca.get(Calendar.MONTH)};
        int[] mDay = {ca.get(Calendar.DAY_OF_MONTH)};
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear[0] = year;
                        mMonth[0] = month;
                        mDay[0] = dayOfMonth;
                        final String data =  year+"-"+(month+1) + "-" + dayOfMonth ;
                        textView.setText(data);
                    }
                },
                mYear[0], mMonth[0], mDay[0]);
        datePickerDialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();

        hotel = (Hotel) getIntent().getExtras().getSerializable("data");
        pos = getIntent().getExtras().getInt("pos");

        toolbar.setTitle(hotel.getName());

        setSupportActionBar(toolbar);
        Picasso
                .with(HotelInfo.this)
                .load(Uri.parse(hotel.getImageUrl()))
                .into(hotelImage);
        hotelDesc.setText(hotel.getDetail());

        views.setText(hotel.getName());
        drafts.setText(hotel.getAddress());




    }





}
