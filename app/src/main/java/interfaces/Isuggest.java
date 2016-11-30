package interfaces;

import java.util.ArrayList;

import bean.ChatBean;

/**
 * Created by Jason on 2016/11/30.
 */

public interface Isuggest {
    void setRecyclerView(ArrayList<ChatBean> data);
    void notifyRecyclerView();
    void showToast(String str);
}
