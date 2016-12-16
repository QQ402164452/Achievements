package interfaces;

import com.avos.avoscloud.AVObject;

/**
 * Created by Jason on 2016/12/10.
 */

public interface InewTask extends Ibase {
    void onModifyTaskSuccess(String str);
    void setDoneSuccess(String str);
    void onDeleteSuccess(String str);
}
