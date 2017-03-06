package bean;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Jason on 2017/1/9.
 */
@RealmClass
public class RealmConversation implements RealmModel {

    private String id;
    private String creator;

    private String name;
    private RealmUser toTarget;
    private String toTargetId;
    private long time;
    private boolean isNew;
    private boolean isHaveMsg;
    private RealmList<RealmMessage> msgList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<RealmMessage> getMsgList() {
        return msgList;
    }

    public void setMsgList(RealmList<RealmMessage> msgList) {
        this.msgList = msgList;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isHaveMsg() {
        return isHaveMsg;
    }

    public void setHaveMsg(boolean haveMsg) {
        isHaveMsg = haveMsg;
    }

    public RealmUser getToTarget() {
        return toTarget;
    }

    public void setToTarget(RealmUser toTarget) {
        this.toTarget = toTarget;
        this.toTargetId=toTarget.getId();
    }

    public String getToTargetId() {
        return toTargetId;
    }

    public void setToTargetId(String toTargetId) {
        this.toTargetId = toTargetId;
    }
}
