package interfaces;

import com.avos.avoscloud.AVObject;

import java.util.ArrayList;
import java.util.List;

import bean.SignBean;

/**
 * Created by Jason on 2016/12/1.
 */

public interface Isign {
    void onResult(int type,ArrayList<SignBean> list);
    void showToast(String str);
    void onError(String str);
}
