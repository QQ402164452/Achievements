package bean;

/**
 * Created by Jason on 2016/11/25.
 */

public class EditListBean {
    private int type;
    private String key;
    private String value;
    private String hint;

    public EditListBean(int type){
        this(type,"","");
    }

    public EditListBean(int type,String key,String value){
        this(type,key,value,"");
    }

    public EditListBean(int type,String key,String value,String hint){
        this.type=type;
        this.key=key;
        this.value=value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
