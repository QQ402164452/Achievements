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
    private ArrayList<SignBean> mList;
    private SharedPreferences mType;
    private long timeSpace=86400000;

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

        mType=mContext.getSharedPreferences(SignActivity.SIGN_SHAR,Context.MODE_PRIVATE);
    }

    public void getLocation(){
        long day=mType.getLong(SignActivity.SIGN_DAY,getStartTime()-timeSpace);
        Date current=new Date();
        if(current.getTime()-day>timeSpace){
            //启动定位
            mLocationClient.startLocation();
        }else {
            mView.onError("今天已经签到完成 无法继续签到！");
        }
    }

    public void onDestroy(){
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    public void addItem(final AMapLocation aMapLocation){
        final int type=mType.getInt(SignActivity.SIGN_TYPE,0);
        Date current=new Date();
        int sign=0;
        final String typeStr=type==0?"上班":"下班";
        final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM-dd hh:mm:ss");
        final String street=aMapLocation.getCity()+aMapLocation.getDistrict()+aMapLocation.getStreet()+aMapLocation.getStreetNum();
        switch (type){
            case 0:
                saveIntSharePreference(SignActivity.SIGN_TYPE,1);
                if(current.getHours()<=9){
                    sign=0;
                }else {
                    sign=1;
                }
                break;
            case 1:
                saveIntSharePreference(SignActivity.SIGN_TYPE,0);
                saveLongSharePreference(SignActivity.SIGN_DAY,getStartTime());
                if(current.getHours()>=18){
                    sign=0;
                }else {
                    sign=2;
                }
                break;
        }

        final int fSign=sign;
        AVObject signObject=new AVObject("checkIn");
        signObject.put("user_id", AVUser.getCurrentUser());
        AVGeoPoint geo=new AVGeoPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
        signObject.put("coordinates",geo);
        signObject.put("local",street);
        signObject.put("type",type);
        signObject.put("sign",sign);
        signObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    mList.add(0,new SignBean(simpleDateFormat.format(aMapLocation.getTime())+"  "+typeStr,
                            street,fSign));
                    mView.notifyRecyclerView();
                }else{
                    mView.onError(e.getMessage());
                }
            }
        });
    }

    public void setListData(){
        mList=new ArrayList<>();
        if(NetworkUtil.isNewWorkAvailable()){
            final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM-dd hh:mm:ss");
            AVQuery<AVObject> query=new AVQuery<>("checkIn");
            query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
            query.setMaxCacheAge(120*3600);
            query.orderByDescending("createdAt");
            query.whereEqualTo("user_id",AVUser.getCurrentUser());
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        if(list!=null){
                            for(AVObject object:list){
                                final String typeStr=object.getInt("type")==0?"上班":"下班";
                                mList.add(new SignBean(simpleDateFormat.format(object.getCreatedAt().getTime())+"  "+typeStr,
                                        object.getString("local"),object.getInt("sign")));
                            }
                            mView.setListAdapter(mList);
                        }
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }
        mView.setListAdapter(mList);
    }

    public void saveIntSharePreference(String key,int value){
        SharedPreferences.Editor editor=mType.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public void saveLongSharePreference(String key,long value){
        SharedPreferences.Editor editor=mType.edit();
        editor.putLong(key,value);
        editor.commit();
    }

    //获取当天的开始时间
    private long getStartTime(){
        Calendar todayStart = new GregorianCalendar();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);

        return todayStart.getTime().getTime();
    }
}
