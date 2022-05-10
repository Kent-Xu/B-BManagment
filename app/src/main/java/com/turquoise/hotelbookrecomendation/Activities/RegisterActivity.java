package com.turquoise.hotelbookrecomendation.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private String mode;
    private TextInputLayout username;
    private TextInputLayout phonenum;
    private TextInputLayout password;
    private TextInputLayout password0;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent=getIntent();
        mode=intent.getStringExtra("mode");
        username=findViewById(R.id.username);
        phonenum=findViewById(R.id.phonenum);
        password=findViewById(R.id.password);
        password0=findViewById(R.id.password1);
        password.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        password0.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        register=findViewById(R.id.registerBtn);
        register.setOnClickListener(v->{

            String pnm=phonenum.getEditText().getText().toString().replaceAll("\r|\n","");
            String usnm=username.getEditText().getText().toString().replaceAll("\r|\n","");
            String pwd=password.getEditText().getText().toString().replaceAll("\r|\n","");
            String pwd0=password0.getEditText().getText().toString().replaceAll("\r|\n","");

            if(usnm.equals(""))
            {setError(username, "昵称不能为空");return;}
            else
                setError(username,null);
            if(pnm.equals(""))
            {setError(phonenum, "电话不能为空");return;}
             else
                setError(phonenum, null);
            if(pwd.equals(""))
            {setError(password, "密码不能为空");return;}
            else
                setError(password, null);
            if(!pwd.equals(pwd0))
            {setError(password0, "密码不一致");return;}
            else{
                setError(password0, null);

                if (mode.equals("user")) {
                    try {
                        Register("UserRegister");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if(mode.equals("holder")) {
                    try {
                        Register("HolderRegister");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }
    private void setError(@NonNull TextInputLayout username, String s) {
        username.setError(s);
    }
    public void Register(String url) throws UnsupportedEncodingException {
        final String[] outcome = new String[1];
        RequestBody body = new FormBody.Builder()
                .add("phoneNumber",phonenum.getEditText().getText().toString())
                .add("name", URLEncoder.encode(username.getEditText().getText().toString(),"UTF-8"))
                .add("password",password.getEditText().getText().toString()).build();
        Request request = new Request.Builder()
                .url("http://192.168.233.1/Airbnb/"+url)
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
                    System.out.println("注册失败");
                    Message msg = new Message();
                    msg.what = 1;

                    mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                }
                else {
                    System.out.println("注册成功");
                    Intent i = new Intent("android.intent.action.mydialog");

                    i.setClass(RegisterActivity.this, LoginActivity.class);
                   startActivity(i);
                }
            }
            Handler mHandler = new Handler(){

                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("错误")
                                    .setContentText("服务器无法连接")
                                    .show();
                            break;
                        case 1:
                            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("注册失败")
                                    .setContentText("该号码已被注册")
                                    .show();
                            break;
                    }
                };
            };

        });

    };
}