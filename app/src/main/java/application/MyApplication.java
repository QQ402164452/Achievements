package application;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Jason on 2016/11/23.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        //leanCloud初始化 context,AppId,Appkey
        AVOSCloud.initialize(this,"SlkhUxnoe7XS4QszPExfRI6b-gzGzoHsz","71Nb0mUa2mGd5Nn8T1Umr6Jj");
    }
}
