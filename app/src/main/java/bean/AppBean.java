package bean;

/**
 * Created by Jason on 2016/12/1.
 */

public class AppBean {
    private int img;
    private String title;

    public AppBean(int img,String title){
        this.img=img;
        this.title=title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
