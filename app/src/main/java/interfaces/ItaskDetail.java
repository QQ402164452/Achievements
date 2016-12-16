package interfaces;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by Jason on 2016/12/14.
 */

public interface ItaskDetail extends Ibase {
    void setPeople(AVObject avObject);
    void setFeedBack(List<AVObject> list);
    void setDoneSuccess(String str);
}
