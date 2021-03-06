package im;

import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import bean.RealmConversation;
import bean.RealmMessage;
import bean.RealmUser;
import im.event.ImTypeMessageEvent;
import io.realm.Realm;
import utils.Constants;
import utils.NetworkUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jason on 2017/1/4.
 */

public class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    private Context mContext;
    private Realm realm;

    public MessageHandler(Context context) {
        this.mContext = context;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        String clientId;
        try {
            clientId = AVIMClientManager.getInstance().getClientId();
            if (client.getClientId().equals(clientId)) {
                if (!message.getFrom().equals(clientId)) {//过滤掉自己发的消息
                    saveMsgAndSendEvent(message, conversation);
                    if (mContext.getSharedPreferences(Constants.SYS_SETTING_SHAREPREFERENCE,MODE_PRIVATE).getBoolean(Constants.NOTIFICATION_SWITCH,true)&&
                            NotificationUtil.isShowNotification(conversation.getConversationId())) {
                        sendNotification(message, conversation);
                    }
                }
            } else {
                client.close(null);
            }
        } catch (IllegalStateException e) {
            client.close(null);
        }
    }

    private void saveMsgAndSendEvent(final AVIMTypedMessage msg, final AVIMConversation conversation) {
        final RealmConversation realmConversation = realm.
                where(RealmConversation.class).
                equalTo("id", conversation.getConversationId()).
                equalTo("creator",AVUser.getCurrentUser().getObjectId()).
                findFirst();

        if (realmConversation == null) {
            getConversation(conversation,msg);
        }else{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    saveMsg(realmConversation,msg);
                }
            });
            sendEvent(msg,conversation);
        }
    }

    private void getConversation(final AVIMConversation conversation, final AVIMTypedMessage msg){
       if(NetworkUtil.isNewWorkAvailable()){
           AVQuery<AVObject> query=new AVQuery<>("_Conversation");
           query.whereEqualTo("objectId",conversation.getConversationId());
           query.findInBackground(new FindCallback<AVObject>() {
               @Override
               public void done(List<AVObject> list, AVException e) {
                   if(e==null){
                       if(list.size()>0){
                           final AVObject avObject=list.get(0);
                           realm.executeTransaction(new Realm.Transaction() {
                               @Override
                               public void execute(Realm realm) {
                                   insertNewConversation(avObject,msg);
                               }
                           });
                           sendEvent(msg,conversation);
                       }
                   }
               }
           });
       }
    }

    private void insertNewConversation(final AVObject avObject, final AVIMTypedMessage msg){
        final RealmConversation newConversation = realm.
                createObject(RealmConversation.class);
        newConversation.setId(avObject.getObjectId());
        newConversation.setName(avObject.getString("name"));
        newConversation.setNew(true);
        newConversation.setCreator(AVUser.getCurrentUser().getObjectId());
        newConversation.setHaveMsg(true);

        RealmUser realmUser=realm.
                where(RealmUser.class).
                equalTo("id",msg.getFrom()).
                findFirst();
        newConversation.setToTarget(realmUser);
        saveMsg(newConversation,msg);
    }

    private void saveMsg(final RealmConversation conversation,final AVIMTypedMessage msg){
        RealmMessage realmMsg = new RealmMessage();
        realmMsg.setId(msg.getMessageId());
        realmMsg.setContent(((AVIMTextMessage) msg).getText());
        realmMsg.setConversationId(msg.getConversationId());
        realmMsg.setDate(msg.getTimestamp());
        realmMsg.setSenderId(msg.getFrom());
        if (msg.getFrom().equals(AVUser.getCurrentUser().getObjectId())) {
            realmMsg.setType(0);
        } else {
            realmMsg.setType(1);
        }
        conversation.getMsgList().add(realm.copyToRealmOrUpdate(realmMsg));
        conversation.setTime(msg.getTimestamp());
        conversation.setNew(true);
    }

    private void sendEvent(final AVIMTypedMessage msg, final AVIMConversation conversation){
        ImTypeMessageEvent event = new ImTypeMessageEvent();
        event.message = msg;
        event.conversation = conversation;
        EventBus.getDefault().post(event);
    }

    private void sendNotification(AVIMTypedMessage message, AVIMConversation conversation) {
        String notificationContent = message instanceof AVIMTypedMessage ?
                ((AVIMTextMessage) message).getText() : "暂不支持此消息类型";
        Intent intent = new Intent(mContext, NotificationBroadcastReceiver.class);
        intent.putExtra(Constants.CONVERSATION_ID, conversation.getConversationId());
        intent.putExtra(Constants.MEMBER_ID, message.getFrom());
        RealmUser realmUser=realm.where(RealmUser.class).equalTo("id",message.getFrom()).findFirst();
        NotificationUtil.showNotification(mContext, realmUser.getName(), notificationContent, intent);
    }

    public void onDestroy() {
        realm.close();
    }

}
