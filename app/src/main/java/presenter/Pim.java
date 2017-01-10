package presenter;

import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.Arrays;
import java.util.List;

import bean.RealmConversation;
import bean.RealmMessage;
import bean.RealmUser;
import im.AVIMClientManager;
import im.NotificationUtil;
import interfaces.Iim;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import utils.NetworkUtil;

/**
 * Created by Jason on 2017/1/3.
 */

public class Pim {
    private Iim mView;
    private AVIMConversation mConversation;
    private Realm realm;

    public Pim(Iim view){
        this.mView=view;
        realm=Realm.getDefaultInstance();
    }

    public RealmUser getToUser(String toUserId){
        return realm.where(RealmUser.class).equalTo("id",toUserId).findFirst();
    }

    public void createConversation(final AVUser me, final RealmUser toUser){
        if(NetworkUtil.isNewWorkAvailable()){
            AVIMClient client= AVIMClientManager.getInstance().getClient();
            client.createConversation(Arrays.asList(me.getObjectId(), toUser.getId())
                    , me.getString("name") + "&" + toUser.getName(), null, false, true, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation avimConversation, AVIMException e) {
                            if(e==null){
                                mConversation=avimConversation;
                                NotificationUtil.addTag(mConversation.getConversationId());
                                final RealmConversation conversation=realm.where(RealmConversation.class).equalTo("id",mConversation.getConversationId()).findFirst();
                                if(conversation==null){
                                    final RealmConversation newConversation=new RealmConversation();
                                    newConversation.setId(mConversation.getConversationId());
                                    newConversation.setName(mConversation.getName());
                                    newConversation.setNew(false);
                                    newConversation.setTime(mConversation.getCreatedAt().getTime());
                                    newConversation.setCreator(mConversation.getCreator());
                                    newConversation.setToTarget(toUser);
                                    newConversation.setHaveMsg(false);
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.copyToRealm(newConversation);
                                        }
                                    });
                                }else{
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            conversation.setNew(false);
                                        }
                                    });
                                }
                                getCacheMsg();
                            }else{
                                mView.onError(e.getMessage());
                            }
                        }
                    });
        }else{
            mView.showToast(NetworkUtil.tip);
            RealmConversation conversation=realm.where(RealmConversation.class).equalTo("toTargetId",toUser.getId()).findFirst();
            if(conversation!=null){
                mView.setCacheMsgList(conversation.getMsgList());
            }
        }
    }

    public void sendMessage(String content){
        if(NetworkUtil.isNewWorkAvailable()){
            final AVIMTextMessage msg=new AVIMTextMessage();
            msg.setText(content);
            mConversation.sendMessage(msg, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if(e==null){
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmConversation conversation=realm.where(RealmConversation.class).equalTo("id",mConversation.getConversationId()).findFirst();
                                RealmMessage realmMsg=realm.createObject(RealmMessage.class,msg.getMessageId());
                                realmMsg.setContent(msg.getText());
                                realmMsg.setConversationId(msg.getConversationId());
                                realmMsg.setDate(msg.getTimestamp());
                                realmMsg.setSenderId(msg.getFrom());
                                if(msg.getFrom().equals(AVUser.getCurrentUser().getObjectId())){
                                    realmMsg.setType(0);
                                }else{
                                    realmMsg.setType(1);
                                }
                                conversation.setHaveMsg(true);
                                conversation.setTime(msg.getTimestamp());
                                conversation.getMsgList().add(realmMsg);
                                mView.onSendSuccess(msg);
                            }
                        });
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }

    public void getCacheMsg(){
        RealmList<RealmMessage> msgList=realm.where(RealmConversation.class).equalTo("id",mConversation.getConversationId()).findFirst().getMsgList();
        if(msgList.size()>0){
            mView.setCacheMsgList(msgList);
        }else {
            getMessage();
        }
    }

    private void getMessage(){
        if(NetworkUtil.isNewWorkAvailable()){
            mConversation.queryMessages(new AVIMMessagesQueryCallback() {
                @Override
                public void done(final List<AVIMMessage> list, AVIMException e) {
                    if(e==null){
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
//                                realm.where(RealmMessage.class).findAll().deleteAllFromRealm();
                                RealmConversation conversation=realm.where(RealmConversation.class).equalTo("id",mConversation.getConversationId()).findFirst();
                                RealmList<RealmMessage> msgList=new RealmList<>();
                                if(list.size()>0){
                                    conversation.setHaveMsg(true);
                                    for(int i=0;i<list.size();i++){
                                        AVIMMessage msg=list.get(i);
                                        RealmMessage realmMsg=realm.createObject(RealmMessage.class,msg.getMessageId());
                                        if(msg instanceof AVIMTextMessage){
                                            realmMsg.setContent(((AVIMTextMessage)msg).getText());
                                        }
                                        realmMsg.setConversationId(msg.getConversationId());
                                        realmMsg.setDate(msg.getTimestamp());
                                        realmMsg.setSenderId(msg.getFrom());
                                        if(msg.getFrom().equals(AVUser.getCurrentUser().getObjectId())){
                                            realmMsg.setType(0);
                                        }else{
                                            realmMsg.setType(1);
                                        }
                                        msgList.add(realmMsg);
                                    }
                                    conversation.setTime(list.get(list.size()-1).getTimestamp());
                                }
                                conversation.setMsgList(msgList);
                            }
                        });
                        RealmConversation conversation=realm.where(RealmConversation.class).equalTo("id",mConversation.getConversationId()).findFirst();
                        mView.setNewMsgList(conversation.getMsgList());
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else {
            mView.showToast(NetworkUtil.tip);
        }
    }

    public AVIMConversation getConversation() {
        return mConversation;
    }

    public void onDestroy(){
        if(mConversation!=null){
            NotificationUtil.removeTag(mConversation.getConversationId());
        }
        realm.close();
    }

}
