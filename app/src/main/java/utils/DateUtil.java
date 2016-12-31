package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jason on 2016/12/2.
 */

public class DateUtil {

    public static String calTime(Date date){
        int disparity= (int) ((System.currentTimeMillis()-date.getTime())/1000);//距离当前时间 多少秒
        if(disparity==0){
            return "刚刚";
        }else if(disparity>0&&disparity<60){
            return disparity+"秒前";
        }else if(disparity>=60&&disparity<3600){
            return Math.max(disparity/60,1)+"分钟前";
        }else if(disparity>=3600&&disparity<86400){
            return disparity/3600+"小时前";
        }else if(disparity>=86400&&disparity<2592000){
            int day=disparity/86400;
            return day+"天前";
        }else if(disparity>=2592000&&disparity<31104000){
            return disparity/2592000+"月前";
        }else{
            return disparity/31104000+"年前";
        }
    }

    public static String getCurrentDate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        return format.format(date);
    }

    public static String getCurrentDay(){
        Calendar calendar=Calendar.getInstance();
        return getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static String getDayOfDate(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
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

    public static ArrayList<String> getTotalDays(String time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月");
        ArrayList<String> list=new ArrayList<>();
        try {
            Date date=format.parse(time);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            int size=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for(int i=1;i<=size;i++){
                if(i<10){
                    list.add("0"+i+"日");
                }else{
                    list.add(i+"日");
                }
            }
            return list;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getNormal(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM月dd日HH:mm", Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String getNormalDetail(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String getNoon(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd",Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String getExtra(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm",Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String getDetail(Date date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.CHINA);
        return simpleDateFormat.format(date);
    }
}
