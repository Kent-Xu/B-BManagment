package com.turquoise.hotelbookrecomendation.Activities;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.turquoise.hotelbookrecomendation.R;

public class MapActivity extends Activity {

    MapView mMapView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       String address= getIntent().getStringExtra("address");
        super.onCreate(savedInstanceState);
        try{
        ServiceSettings.updatePrivacyShow(this, true, true);
        ServiceSettings.updatePrivacyAgree(this,true);} catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_map);//设置对应的XML布局文件
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap=mMapView.getMap();

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();

        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);

        aMap.setMyLocationEnabled(true);
        GeocodeSearch geocoderSearch;
        try {
            geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                    if(i==1000){
                        double latitude=geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint().getLatitude();
                        double longitude=geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint().getLongitude();
                        LatLng la = new LatLng(latitude, longitude);
                        aMap.moveCamera(CameraUpdateFactory.newLatLng(la));
                        final Marker marker = aMap.addMarker(new MarkerOptions().position(la).snippet("DefaultMarker"));
                    }

                }
            });
            GeocodeQuery query = new GeocodeQuery(address,address);
            geocoderSearch.getFromLocationNameAsyn(query);
        } catch (AMapException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
