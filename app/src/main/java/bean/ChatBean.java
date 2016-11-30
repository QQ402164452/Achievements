package bean;

/**
 * Created by Jason on 2016/11/30.
 */

public class ChatBean {
    private int type;
    private String content;
    private String img;
    private boolean isSuccess;

    public ChatBean(int type, boolean isSuccess,String content, String img){
        this.type=type;
        this.isSuccess=isSuccess;
        this.content=content;
        this.img=img;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
