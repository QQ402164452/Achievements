package utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import interfaces.Irecy;

/**
 * Created by Jason on 2016/12/16.
 */

public class WeakHandler extends Handler {
    private WeakReference<Irecy> weakReference;

    public WeakHandler(Irecy object){
        super();
        weakReference=new WeakReference<>(object);
    }

    @Override
    public void handleMessage(Message msg){
        super.handleMessage(msg);
        Irecy object=weakReference.get();
        if(object!=null){
            object.doLoadData(msg.what);
        }
    }
}
