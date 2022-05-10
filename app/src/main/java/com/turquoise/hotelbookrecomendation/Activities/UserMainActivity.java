package com.turquoise.hotelbookrecomendation.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.turquoise.hotelbookrecomendation.Fragments.UserHomeFrag;
import com.turquoise.hotelbookrecomendation.Fragments.UserOrderFrag;
import com.turquoise.hotelbookrecomendation.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserMainActivity extends AppCompatActivity implements Serializable {

    private static Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout pricebar;
    private int userid;
    private Button settime;
    TextView startdate,enddate;

   private  boolean selected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        Intent intent =getIntent();

        userid=Integer.valueOf(intent.getStringExtra("userid"));
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    pricebar.setVisibility(View.VISIBLE);
                    //  ((UserHomeFrag)viewPagerAdapter.getItem(position)).updateList();

                }
                else{
                    pricebar.setVisibility(View.GONE);
                    // ((Recommendation)viewPagerAdapter.getItem(position)).updateList();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);

        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        startdate=findViewById(R.id.starttime);
        enddate=findViewById(R.id.finishtime);
       pricebar=findViewById(R.id.pricebar);
       settime=findViewById(R.id.btn);
        pricebar.setVisibility(View.VISIBLE);
        Calendar cd1=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        startdate.setText(sdf.format(cd1.getTime()));
        enddate.setText(sdf.format(cd1.getTime()));
       startdate.setOnClickListener(v->{showDatePickerDialog(startdate); });
        enddate.setOnClickListener(v->{showDatePickerDialog(enddate); });
        settime.setOnClickListener(view -> {

            try {

                Date dt0= sdf.parse(startdate.getText().toString());
                Date dt1=sdf.parse(enddate.getText().toString());
                selected=true;

                ViewPagerAdapter vp=new ViewPagerAdapter(getSupportFragmentManager());
                vp.addFrag(new UserHomeFrag(),"可用民宿");
                vp.addFrag(new Fragment(),"我的订单");
                viewPager.setAdapter(vp);


            } catch (ParseException e) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("错误")
                        .setContentText("日期填写错误")
                        .show();
            }
        });
    }


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
    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new UserHomeFrag(),"请按日期搜索房屋");
        viewPagerAdapter.addFrag(new UserOrderFrag(),"我的订单");
        viewPager.setAdapter(viewPagerAdapter);
    }




    public static void updatec(int n) {
        int cur=Integer.valueOf(((TextView)toolbar.findViewById(R.id.cartCount)).getText().toString());
        ((TextView)toolbar.findViewById(R.id.cartCount)).setText(String.valueOf(cur+n));
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }




        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
