package utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import interfaces.WeakObject;

/**
 * Created by Jason on 2016/12/16.
 */

public class WeakHandler extends Handler {
    private WeakReference<WeakObject> weakObject;

    public WeakHandler(WeakObject object){
        super();
        weakObject = new WeakReference<WeakObject>(object);
    }

    @Override
    public void handleMessage(Message msg){
        super.handleMessage(msg);
        WeakObject object=weakObject.get();
        if(object!=null){
            object.doLoadData(msg.what);
        }
    }
}
