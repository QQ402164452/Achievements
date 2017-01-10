package im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import view.IMActivity;
import view.LoginActivity;

/**
 * Created by Jason on 2017/1/4.
 * 因为 notification 点击时，控制权不在 app，此时如果 app 被 kill 或者上下文改变后，
 * 有可能对 notification 的响应会做相应的变化，所以此处将所有 notification 都发送至此类，
 * 然后由此类做分发。
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(AVIMClientManager.getInstance().getClient()==null){
            gotoLoginActivity(context);
        }else{
            gotoIMActivity(context,intent);
        }
    }

    /**
     *如果 app 上下文已经缺失，则跳转到登陆页面，走重新登陆的流程
     **/
    private void gotoLoginActivity(Context context){
        Intent intent=new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转至单聊页面
     */
    private void gotoIMActivity(Context context,Intent intent){
        Intent startActivityIntent=new Intent(context, IMActivity.class);
        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityIntent.putExtra(IMActivity.IM_TO_USER,intent.getStringExtra(Constants.MEMBER_ID));
        startActivityIntent.putExtra(Constants.CONVERSATION_ID,intent.getStringExtra(Constants.CONVERSATION_ID));
        context.startActivity(startActivityIntent);
    }
}
