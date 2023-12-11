package com.jnu.student;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.student.data.BookLocation;
import com.jnu.student.data.DataDownload;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.ArrayList;


public class TencentMapFragment extends Fragment {


    public TencentMapFragment() {
        // Required empty public constructor
    }


    public static TencentMapFragment newInstance(String param1, String param2) {
        TencentMapFragment fragment = new TencentMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public class DataDownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return new DataDownload().download(urls[0]);
        }
        @Override
        protected void onPostExecute(String responseData) {
            super.onPostExecute(responseData);
            if (responseData != null) {
                ArrayList<BookLocation> shopLocations= new DataDownload().parseJsonObjects(responseData);
                TencentMap tencentMap = mapView.getMap();
                for (BookLocation shopLocation : shopLocations) {
                    LatLng point1 = new LatLng(shopLocation.getLatitude(), shopLocation.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions(point1)
                            .title(shopLocation.getName());
                    Marker marker = tencentMap.addMarker(markerOptions);


                }
            }
        }
    }


    private com.tencent.tencentmap.mapsdk.maps.TextureMapView mapView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_baidu_map, container, false);
        mapView = rootView.findViewById(R.id.mapView);

        new DataDownloadTask().execute("http://file.nidama.net/class/mobile_develop/data/bookstore.json");
        TencentMap tencentMap = mapView.getMap();

        LatLng point1 = new LatLng(22.255453, 113.54145);

        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        point1,   //坐标
                        16,         //目标缩放级别
                        0f,         //目标倾斜角[0.0 ~ 45.0] (垂直地图时为0)
                        0f));       //目标旋转角 0~360° (正北方为0)

        tencentMap.moveCamera(CameraUpdateFactory.newLatLng(point1));


        BitmapDescriptor custom = BitmapDescriptorFactory.fromResource(R.drawable.jnu);
        // 创建一个Marker对象
        MarkerOptions markerOptions = new MarkerOptions(point1)
                .title("JNU(珠海暨南大学)");
       //         .icon(custom) // 设置自定义的BitmapDescriptor
        //        .anchor(0.5f, 0.5f) // 设置图片的锚点为中心
         //       .alpha(1f)
         //       .flat(true)
         //       .clockwise(false)
          //      .rotation(0);

        // 添加标记到地图上
        Marker marker = tencentMap.addMarker(markerOptions);

        // 设置InfoWindow的内容
        marker.setSnippet("这里是暨南大学珠海校区，北纬22°，东经113°.\n\n"+
                "暨南大学珠海校区（Jinan University Zhuhai Campus）\n"+
                "是暨南大学的一个分校区，位于广东省珠海市。暨南大\n" +
                "学于1998年在珠海成立珠海学院，并于1998年8月28日\n"+
                "与珠海市人民政府签订《共建暨南大学珠海学院协议》。\n"+
                "2009年4月1日，学校实施校区化改革，珠海学院更名为\n"+
                "暨南大学珠海校区。\n"
        );

        //设置Marker支持点击
        marker.setClickable(true);

        //设置Marker点击事件监听
        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getId().equals(marker.getId())) {
                    //自定义Marker被点击
                    marker.showInfoWindow(); //显示InfoWindow
                }
                return true;
            }
        });


        
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


}