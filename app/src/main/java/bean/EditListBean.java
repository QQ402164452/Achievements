package bean;

/**
 * Created by Jason on 2016/11/25.
 */

public class EditListBean {
    private int type;
    private String key;
    private Object value;
    private String hint="";

    public EditListBean(int type){
        this(type,"",null);
    }

    public EditListBean(int type,String key,Object value){
        this.type=type;
        this.key=key;
        this.value=value;
    }

    public EditListBean(int type,String key,Object value,String hint){
        this.type=type;
        this.key=key;
        if(value==null){
            this.value="";
        }else{
            this.value=value;
        }
        this.hint=hint;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
