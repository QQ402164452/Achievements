package interfaces;

import java.util.ArrayList;

import bean.CityBean;
import bean.EditListBean;

/**
 * Created by Jason on 2016/11/25.
 */

public interface Iedit {
    void setDataSource(ArrayList<EditListBean> list);
    void updateRecyclerView(int pos);
    void showToast(String str);
    void onSaveResult(boolean isSuccess,String str);
}
