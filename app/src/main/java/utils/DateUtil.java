package utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jason on 2016/12/2.
 */

public class DateUtil {

    public static String getCurrentDate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        return format.format(date);
    }

    public static String getCurrentDay(){
        Calendar calendar=Calendar.getInstance();
        return getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static String getDayOfWeek(int day){
        switch(day){
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
        }
        return "";
    }

    public static ArrayList<String> getDay(int num){
        ArrayList<String> days=new ArrayList<>();
        Calendar cd=Calendar.getInstance();
        cd.add(Calendar.DATE, num);
        int month=cd.get(Calendar.MONTH)+1;
        int day=cd.get(Calendar.DATE);
        days.add(month+"/"+day);
        days.add(getDayOfWeek(cd.get(Calendar.DAY_OF_WEEK)));
        return days;
    }

    public static ArrayList<String> getWeek(int num){
        ArrayList<String> weeks=new ArrayList<>();
        Calendar cd=Calendar.getInstance();
        cd.set(Calendar.YEAR, cd.get(Calendar.YEAR));
        cd.set(Calendar.DAY_OF_WEEK,2);
        cd.set(Calendar.WEEK_OF_YEAR,cd.get(Calendar.WEEK_OF_YEAR));
        cd.add(Calendar.WEEK_OF_YEAR, num);
        int month=cd.get(Calendar.MONTH)+1;
        int day=cd.get(Calendar.DATE);
        cd.add(Calendar.DATE, 6);
        int month2=cd.get(Calendar.MONTH)+1;
        int day2=cd.get(Calendar.DATE);

        weeks.add(month+"/"+day+"-"+month2+"/"+day2);
        weeks.add(String.valueOf(cd.get(Calendar.WEEK_OF_YEAR)));
        return weeks;
    }

    public static ArrayList<String> getMonth(int num){
        ArrayList<String> months=new ArrayList<>();
        Calendar cd=Calendar.getInstance();
        cd.add(Calendar.MONTH, num);
        int month=cd.get(Calendar.MONTH)+1;
        int year=cd.get(Calendar.YEAR);

        months.add(String.valueOf(year));
        months.add(String.valueOf(month));
        return months;
    }
}