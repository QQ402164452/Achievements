package interfaces;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by Jason on 2016/12/15.
 */

public interface ItodoTask {
    void showToast(String str);
    void onError(String error);
    void onSuccess(List<AVObject> list,int type);
}
