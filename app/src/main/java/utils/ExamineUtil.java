package utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jason on 2016/12/6.
 */

public class ExamineUtil {

    private ArrayList<String> mTypes;
    private ArrayList<String> mType;
    private ArrayList<String> mMon;

    private ExamineUtil(){
        mTypes=new ArrayList<>(Arrays.asList("年假","事假","病假","调休假","婚假","产假","工伤假"));
        mType=new ArrayList<>(Arrays.asList("假期","加班","出差"));
        mMon=new ArrayList<>(Arrays.asList("上午","下午"));
    }

    private static class ExamineHolder{
        private static ExamineUtil instance=new ExamineUtil();
    }

    public static ExamineUtil getInstance(){
        return ExamineHolder.instance;
    }

    public ArrayList<String> getTypes(){
        return mTypes;
    }

    public ArrayList<String> getType(){
        return mType;
    }

    public ArrayList<String> getMon(){
        return mMon;
    }

}
