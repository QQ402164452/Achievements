package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import application.MyApplication;

/**
 * Created by Jason on 2016/11/29.
 */

public class NetworkUtil {
    public static String tip="网络不能连接！请检查网络";

    public static boolean isNewWorkAvailable(){
        ConnectivityManager manager= (ConnectivityManager) MyApplication.getInstance().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        return info!=null&&info.isAvailable();
    }

}
