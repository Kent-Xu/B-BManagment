package com.turquoise.hotelbookrecomendation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.turquoise.hotelbookrecomendation.Fragments.DataFrag;
import com.turquoise.hotelbookrecomendation.Fragments.HomeFrag;

import com.turquoise.hotelbookrecomendation.Fragments.ManageFrag;
import com.turquoise.hotelbookrecomendation.R;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HolderMainActivity extends AppCompatActivity implements Serializable {

    private static Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int holderid;
    private FloatingActionButton addHouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addHouse=findViewById(R.id.addHouse);
        Intent intent =getIntent();

        holderid=Integer.valueOf(intent.getStringExtra("holderid"));
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        addHouse.setOnClickListener(v->{
            Intent i=new Intent(HolderMainActivity.this,HouseInfoMngActivity.class);
            i.putExtra("holderid",intent.getStringExtra("holderid"));
            startActivityForResult(i,1);

        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {

                }
                break;
            default:
        }
    }


    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new HomeFrag(),"我的房屋");


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){

                    ((HomeFrag)viewPagerAdapter.getItem(position)).updateList();

                }
                else{

                   // ((Recommendation)viewPagerAdapter.getItem(position)).updateList();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
