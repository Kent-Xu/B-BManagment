package com.turquoise.hotelbookrecomendation.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.turquoise.hotelbookrecomendation.R;

public class AssessDialog extends Dialog {
    Activity context;

    private Button btn_save;
    public RatingBar ratingBar;
    public EditText text_assess;
    private View.OnClickListener mClickListener;
   int state=0;
    public AssessDialog(Activity context) {
        super(context);
        this.context = context;
    }
    public AssessDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.dialog_assess);
        ratingBar=findViewById(R.id.ratingBar2);
        text_assess=findViewById(R.id.text_assess);
        btn_save = (Button) findViewById(R.id.btn_save);
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        Point size = new Point();
        d.getSize(size);
        int width = size.x;
        int height = size.y;
        p.height = (int) (height * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (width * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
        btn_save.setOnClickListener(mClickListener);
        this.setCancelable(true);
        if(state>0){ratingBar.setVisibility(View.GONE);
        text_assess.setHint("描述");}
    }

    public void setRatingBarInvisible() {
        state=1;
    }
}
