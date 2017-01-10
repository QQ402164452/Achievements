package im;

import android.text.TextUtils;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

/**
 * Created by Jason on 2017/1/4.
 */

public class AVIMClientManager {
    private String clientId;
    private AVIMClient mClient;

    private AVIMClientManager(){

    }

    private static class AVIMClientManagerHolder{
        private static final AVIMClientManager manager=new AVIMClientManager();
    }

    public static AVIMClientManager getInstance(){
        return AVIMClientManagerHolder.manager;
    }

    public void open(String clientId, AVIMClientCallback callback){
        this.clientId=clientId;
        mClient=AVIMClient.getInstance(clientId);
        mClient.open(callback);
    }

    public AVIMClient getClient(){
        return mClient;
    }

    public String getClientId(){
        if(TextUtils.isEmpty(clientId)){
            throw new IllegalStateException("Please call AVImClientManager.open first");
        }
        return clientId;
    }

}
