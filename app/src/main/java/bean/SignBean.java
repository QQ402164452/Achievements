package bean;


/**
 * Created by Jason on 2016/12/1.
 */
public class SignBean {
    private String date;
    private String location;
    private int sign;

    public SignBean(String date, String location,int sign){
        this.date=date;
        this.location=location;
        this.sign=sign;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
