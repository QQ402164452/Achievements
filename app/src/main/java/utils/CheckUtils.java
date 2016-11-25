package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jason on 2016/11/24.
 */

public class CheckUtils {

    public static boolean isChinaPhoneLegal(String str){
        String regExp="^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\\\d{8}$";
        Pattern p= Pattern.compile(regExp);
        Matcher m=p.matcher(str);
        return m.matches();
    }

    public static boolean isEmailLegal(String str){
        String regExp="^([a-z0-9A-Z]+[-|_|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";
        Pattern p=Pattern.compile(regExp);
        Matcher m=p.matcher(str);
        return m.matches();
    }
}
