package application;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Jason on 2016/11/23.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    @Override
    public void onCreate(){
        super.onCreate();

        //leanCloud初始化 context,AppId,Appkey
        AVOSCloud.initialize(this,"SlkhUxnoe7XS4QszPExfRI6b-gzGzoHsz","71Nb0mUa2mGd5Nn8T1Umr6Jj");
        myApplication=this;

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public static MyApplication getInstance(){
        return myApplication;
    }
}
