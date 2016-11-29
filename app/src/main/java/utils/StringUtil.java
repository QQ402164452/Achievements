package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jason on 2016/11/26.
 */

public class StringUtil {

    private StringUtil(){

    }

    private static final class StringHolder{
        private static final StringUtil util=new StringUtil();
    }

    public static StringUtil getInstance(){
        return StringHolder.util;
    }

    public String readText(InputStream is) throws Exception{
        InputStreamReader reader=new InputStreamReader(is);
        BufferedReader bufferedReader=new BufferedReader(reader);
        StringBuilder builder=new StringBuilder("");
        String str;
        while((str=bufferedReader.readLine())!=null){
            builder.append(str);
        }
        return builder.toString();
    }

}
