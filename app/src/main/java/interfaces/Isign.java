package interfaces;

import java.util.ArrayList;

import bean.SignBean;

/**
 * Created by Jason on 2016/12/1.
 */

public interface Isign {
    void setListAdapter(ArrayList<SignBean> list);
    void notifyRecyclerView();
    void showToast(String str);
    void onError(String str);
}
