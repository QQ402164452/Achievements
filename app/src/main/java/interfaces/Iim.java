package interfaces;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.List;

import bean.RealmMessage;
import io.realm.RealmList;

/**
 * Created by Jason on 2017/1/3.
 */

public interface Iim  extends Ibase{
    void onSendSuccess(AVIMTextMessage msg);
    void setNewMsgList(RealmList<RealmMessage> newMsgList);
    void setCacheMsgList(RealmList<RealmMessage> realmMessages);
}
