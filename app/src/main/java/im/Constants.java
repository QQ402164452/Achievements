package im;

/**
 * Created by Jason on 2017/1/4.
 */

public class Constants {

    private static final String LEANMESSAGE_CONSTANTS_PREFIX = "Achievements";

    public static final String MEMBER_ID = getPrefixConstant("member_id");
    public static final String CONVERSATION_ID = getPrefixConstant("conversation_id");
    public static final String ACTIVITY_TITLE = getPrefixConstant("activity_title");

    private static String getPrefixConstant(String str) {
        return LEANMESSAGE_CONSTANTS_PREFIX + str;
    }

}
