package interfaces;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by Jason on 2016/12/13.
 */

public interface ImyTask {
    void showToast(String str);
    void onError(String error);
    void onSuccess(List<AVObject> list);
}
