package interfaces;

import com.avos.avoscloud.AVUser;

import java.util.HashMap;
import java.util.List;

import bean.RealmUser;
import io.realm.RealmResults;

/**
 * Created by Jason on 2017/1/2.
 */

public interface Icontact {
    void showToast(String str);
    void setCacheUserList(RealmResults<RealmUser> list);
    void setLetterMap(HashMap<Character,Integer> letters);
    void updateUserDate(RealmResults<RealmUser> list);
}
