package com.turquoise.hotelbookrecomendation.Activities;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.turquoise.hotelbookrecomendation.R;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button login,register;
    private TextInputLayout username;
    private TextInputLayout password;
    private RadioGroup select;
    private RadioButton user,holder;
    private int Service = 0;//0为向用户服务，1为向房东服务
    private AirbnbApplication airbnbApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.loginBtn);
        password= findViewById(R.id.password);
        username = findViewById(R.id.username);
        select=findViewById(R.id.select);
        user=findViewById(R.id.user);
        holder=findViewById(R.id.holder);
        password.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        register=findViewById(R.id.registerBtn);
        login.setOnClickListener(v->{if(user.isChecked())Login("UserLogin");
                                       else{   Login("HolderLogin");  } });
        register.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            Bundle bundle = new Bundle();
            if(user.isChecked())
            {bundle.putString("mode","user");}
            else
            {bundle.putString("mode","holder");}
            intent.putExtras(bundle);
            startActivity(intent); });

    }
    public void Login(String url){
        final String[] outcome = new String[1];
        RequestBody body = new FormBody.Builder()
                .add("phoneNumber",username.getEditText().getText().toString())
                .add("password",password.getEditText().getText().toString()).build();

        Request request = new Request.Builder()
                .url("http://192.168.233.1:8080/Airbnb/"+url)
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
                    System.out.println("登录失败");
                    Message msg = new Message();
                    msg.what = 1;

                    mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                }
                else {
                    System.out.println("登陆成功");
                    Intent i = new Intent("android.intent.action.mydialog");
                    airbnbApplication= (AirbnbApplication) getApplication();
                    airbnbApplication.setId(outcome[0]);
                    if(url=="HolderLogin")
                    {

                        i.setClass(LoginActivity.this, HolderMainActivity.class);
                     i.putExtra("holderid", outcome[0]);
                    startActivity(i);}
                    else if(url=="UserLogin"){
                        i.setClass(LoginActivity.this, UserMainActivity.class);
                        i.putExtra("userid", outcome[0]);
                        startActivity(i);
                    }
                }
            }
            Handler mHandler = new Handler(){

                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("错误")
                                    .setContentText("服务器无法连接")
                                    .show();
                            break;
                        case 1:
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("登陆失败")
                                    .setContentText("账号或密码错误")
                                    .show();
                            break;
                    }
                };
            };

        });

    }
    }



