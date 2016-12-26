package bean;

import com.avos.avoscloud.AVObject;

/**
 * Created by Jason on 2016/12/18.
 */

public class AttendanceBean {
    private String date;
    private AVObject avObject;
    private int type;//0正常 1周末

    public AttendanceBean(int type,String date,AVObject avObject){
        this.type=type;
        this.date=date;
        this.avObject=avObject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AVObject getAvObject() {
        return avObject;
    }

    public void setAvObject(AVObject avObject) {
        this.avObject = avObject;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
