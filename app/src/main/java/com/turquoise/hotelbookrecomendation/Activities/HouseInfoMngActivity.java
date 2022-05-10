package com.turquoise.hotelbookrecomendation.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.aware.DiscoverySession;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.textfield.TextInputLayout;
import com.turquoise.hotelbookrecomendation.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HouseInfoMngActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private TextInputLayout housename;
    private TextInputLayout addressBig;
    private TextInputLayout addressSmall;
    private TextInputLayout detail;
    private ImageView img;
    private Button imgadd,ok;
    private File file;
    private int holderid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_house_info_mng);
        setSupportActionBar(findViewById(R.id.toolbar));
       housename=findViewById(R.id.housename);
       addressBig=findViewById(R.id.address);
       addressSmall=findViewById(R.id.detailaddress);
       detail=findViewById(R.id.Detail);
       img=findViewById(R.id.imageView);
       imgadd=findViewById(R.id.buttonpic);
       ok=findViewById(R.id.buttonconfirm);
       Intent i=getIntent();
        holderid=Integer.valueOf(i.getStringExtra("holderid"));
        imgadd.setOnClickListener(v->{

            AlertDialog.Builder alert = new AlertDialog.Builder(HouseInfoMngActivity.this);
            alert.setTitle("");
            String[] str={"相机","相册"};
            alert.setItems(str,  new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    switch (which) {
                        case 0:
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 200);
                            break;
                        case 1:
                            Intent it = new Intent(Intent.ACTION_PICK);
                            it.setType("image/*");//相片类型
                            startActivityForResult(it, 4000);
                            break;
                        default:
                            break;
                    }


                }
            });
            alert.create();
            alert.show();

        });
        ok.setOnClickListener(v->{
            try {
                addHouse("AddHouse");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        ActivityCompat.requestPermissions(HouseInfoMngActivity.this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        if(data==null)return;
        if(requestCode == 200){
            Bitmap bitmap = data.getParcelableExtra("data");
            img.setImageBitmap(bitmap);
            file=new File("/storage/emulated/0/"+housename.getEditText().toString()+"_cover.jpg");//将要保存图片的路径
            try {
                BufferedOutputStream bos = null;
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        if(requestCode == 4000 ){

            Uri uri = data.getData();
            try {
                String filePath;
                String[] filePathColumn = {MediaStore.MediaColumns.DATA};

                ContentResolver cr = this.getContentResolver();
                Cursor cursor = cr.query(uri, filePathColumn, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();
                file = new File(filePath);
                Bitmap bm = BitmapFactory.decodeFile(filePath);
                img.setImageBitmap(bm);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }

    public void addHouse(String url) throws UnsupportedEncodingException {
        final String[] outcome = new String[1];
        if(file==null){ new SweetAlertDialog(HouseInfoMngActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("错误")
                .setContentText("请选择封面图片")
                .show();
        return;}

        RequestBody image = RequestBody.create( MediaType.parse( "image/*" ), file );
        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart("holderid", holderid+"")
                .addFormDataPart("name",URLEncoder.encode(housename.getEditText().getText().toString(),"UTF-8"))
                .addFormDataPart("address",URLEncoder.encode(addressBig.getEditText().getText().toString()
                        +addressSmall.getEditText().getText().toString(),"UTF-8"))
                .addFormDataPart("Detail",URLEncoder.encode(detail.getEditText().getText().toString(),"UTF-8"))
                .addFormDataPart("file", file.getName(), image ).build();
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
                    System.out.println("添加失败");
                    Message msg = new Message();
                    msg.what = 1;

                    mHandler.sendMessage(msg); // 向Handler发送消息,更新UI
                }
                else {
                    System.out.println("添加成功");
                    Intent i= new Intent();
//将想要传递的数据用putExtra封装在intent中
                    setResult(RESULT_OK,i);
                    finish();
                }
            }
            Handler mHandler = new Handler(){

                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            new SweetAlertDialog(HouseInfoMngActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("错误")
                                    .setContentText("服务器无法连接")
                                    .show();
                            break;
                        case 1:
                            new SweetAlertDialog(HouseInfoMngActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("添加失败")
                                    .setContentText("请核对相关信息")
                                    .show();
                            break;
                    }
                };
            };

        });

    }
}