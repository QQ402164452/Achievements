package presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import adapter.SignAdapter;
import bean.SignBean;
import interfaces.Isign;
import utils.NetworkUtil;
import view.SignActivity;

/**
 * Created by Jason on 2016/12/1.
 */

public class Psign {
    private Isign mView;
    private Context mContext;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener=null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    public Psign(Isign isign,Context context){
        this.mView=isign;
        mContext=context.getApplicationContext();

        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation!=null){
                    if(aMapLocation.getErrorCode()==0){
                        addItem(aMapLocation);
                    }else{
                        mView.onError(aMapLocation.getErrorInfo());
                    }
                }
                mLocationClient.stopLocation();
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    public void getLocation(){
        if(NetworkUtil.isNewWorkAvailable()){
            //启动定位
            mLocationClient.startLocation();
        }else {
            mView.showToast(NetworkUtil.tip);
        }
    }

    public void onDestroy(){
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    public void addItem(final AMapLocation aMapLocation){
        final Date current=new Date();
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH)+1;
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final String street=aMapLocation.getCity()+aMapLocation.getDistrict()+aMapLocation.getStreet()+aMapLocation.getStreetNum();
        AVQuery<AVObject> avQuery=new AVQuery<>("checkIn");
        avQuery.whereEqualTo("user",AVUser.getCurrentUser());
        avQuery.whereEqualTo("year",year);
        avQuery.whereEqualTo("month",month);
        avQuery.whereEqualTo("day",day);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e==null){
                    if(list.size()==0){
                        AVObject avObject=new AVObject("checkIn");
                        avObject.put("year",year);
                        avObject.put("month",month);
                        avObject.put("day",day);
                        avObject.put("user",AVUser.getCurrentUser());
                        avObject.put("StartTime",current);
                        AVGeoPoint geo=new AVGeoPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                        avObject.put("StartCoordinates",geo);
                        avObject.put("StartLocal",street);
                        if(current.getHours()<=9){
                            avObject.put("StartSign",1);
                        }else{
                            avObject.put("StartSign",2);
                        }
                        avObject.put("State",0);
                        avObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if(e==null){
                                    setListData(1);
                                }else{
                                    mView.onError(e.getMessage());
                                }
                            }
                        });
                    }else{
                        AVObject curDay=list.get(0);
                        if(curDay.getInt("EndSign")==0){
                            curDay.put("EndTime",current);
                            AVGeoPoint geo=new AVGeoPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                            curDay.put("EndCoordinate",geo);
                            curDay.put("EndLocal",street);
                            if(current.getHours()<=17){
                                curDay.put("EndSign",2);
                            }else{
                                curDay.put("EndSign",1);
                            }
                            curDay.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if(e==null){
                                        setListData(1);
                                    }else{
                                        mView.onError(e.getMessage());
                                    }
                                }
                            });
                        }else{
                            mView.onResult(2,null);
                        }
                    }
                }else{
                    mView.onError(e.getMessage());
                }
            }
        });
    }

    public void setListData(final int type){
        if(NetworkUtil.isNewWorkAvailable()){
            Calendar calendar=Calendar.getInstance();
            AVQuery<AVObject> query=new AVQuery<>("checkIn");
            query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
            query.setMaxCacheAge(120*3600);
            query.orderByDescending("createdAt");
            query.whereEqualTo("month",calendar.get(Calendar.MONTH)+1);
            query.whereEqualTo("year",calendar.get(Calendar.YEAR));
            query.whereEqualTo("user",AVUser.getCurrentUser());
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        ArrayList<SignBean> signList=new ArrayList<>();
                        for(AVObject object:list){
                            if(object.getInt("EndSign")!=0){
                                SignBean end=new SignBean(object.getDate("EndTime"),object.getString("EndLocal"),object.getInt("EndSign"),1);
                                signList.add(end);
                            }
                            if(object.getInt("StartSign")!=0){
                                SignBean start=new SignBean(object.getDate("StartTime"),object.getString("StartLocal"),object.getInt("StartSign"),0);
                                signList.add(start);
                            }
                        }
                        mView.onResult(type,signList);
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }

}
