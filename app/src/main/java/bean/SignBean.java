package bean;


import java.util.Date;

/**
 * Created by Jason on 2016/12/1.
 */
public class SignBean {
    private Date date;
    private String location;
    private int sign;
    private int type;

    public SignBean(Date date, String location, int sign,int type){
        this.date=date;
        this.location=location;
        this.sign=sign;
        this.type=type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
