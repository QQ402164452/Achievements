package im.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

/**
 * Created by Jason on 2017/1/4.
 */

public class ImTypeMessageEvent {
    public AVIMTypedMessage message;
    public AVIMConversation conversation;
}
