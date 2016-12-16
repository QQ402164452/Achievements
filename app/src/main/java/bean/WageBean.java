package bean;

/**
 * Created by Jason on 2016/12/9.
 */

public class WageBean {
    private int type;
    private String title;
    private String content;

    public WageBean(int type,String title,String content){
        this.type=type;
        this.title=title;
        this.content=content;
    }

    public WageBean(int type,String title){
        this.type=type;
        this.title=title;
        content="";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

