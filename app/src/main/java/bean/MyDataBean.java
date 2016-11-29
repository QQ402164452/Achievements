package bean;

/**
 * Created by Jason on 2016/11/25.
 */

public class MyDataBean {
    private int type;
    private String title;
    private String content;

    public MyDataBean(int type,String title,String content){
        this.type=type;
        this.title=title;
        if(content==null){
            this.content="暂无信息";
        }else{
            this.content=content;
        }
    }

    public MyDataBean(int type){
        this.type=type;
        this.title="";
        this.content="";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
