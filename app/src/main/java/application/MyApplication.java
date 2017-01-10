package application;

import android.app.Application;
import android.widget.Toast;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import im.AVIMClientManager;
import im.MessageHandler;
import io.realm.Realm;

/**
 * Created by Jason on 2016/11/23.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;
    private MessageHandler messageHandler;

    @Override
    public void onCreate(){
        super.onCreate();

        //leanCloud初始化 context,AppId,Appkey
        AVOSCloud.initialize(this,"SlkhUxnoe7XS4QszPExfRI6b-gzGzoHsz","71Nb0mUa2mGd5Nn8T1Umr6Jj");
        myApplication=this;

        Realm.init(this);
        // 必须在启动的时候注册 MessageHandler
        // 应用一启动就会重连，服务器会推送离线消息过来，需要 MessageHandler 来处理
        messageHandler=new MessageHandler(this);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,messageHandler);
        if(AVUser.getCurrentUser()!=null){
            AVIMClientManager.getInstance().open(AVUser.getCurrentUser().getObjectId(), new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if(e!=null){
                        Toast.makeText(MyApplication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                .build()
        );
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        messageHandler.onDestroy();
    }

    public static MyApplication getInstance(){
        return myApplication;
    }
}
