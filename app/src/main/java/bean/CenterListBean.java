package bean;

/**
 * Created by Jason on 2016/11/23.
 */

public class CenterListBean {
    private int type;
    private String name;
    private int resId;
    private int id;

    public CenterListBean(int type,String name,int resId,int id){
        this.type=type;
        this.name=name;
        this.resId=resId;
        this.id=id;
    }

    public CenterListBean(int type){
        this.type=type;
        name="";
        resId=0;
        id=0;
    }

    public CenterListBean(int type,String name,int resId){
        this.type=type;
        this.name=name;
        this.resId=resId;
        id=0;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
