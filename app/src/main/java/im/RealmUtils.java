package im;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bean.RealmUser;
import io.realm.Realm;
import utils.NetworkUtil;
import utils.PinyinUtil;

/**
 * Created by Jason on 2017/1/11.
 */

public class RealmUtils {

    private Realm realm;

    private RealmUtils(){
        realm=Realm.getDefaultInstance();
    }

    private static class RealmUtilsHolder{
        private static final RealmUtils realmUtils=new RealmUtils();
    }

    public static RealmUtils getInstance(){
        return RealmUtilsHolder.realmUtils;
    }

    public void closeRealm(){
        if(realm!=null&&!realm.isClosed()){
            realm.close();
        }
    }

    public void insertRealmUser() {
        if (NetworkUtil.isNewWorkAvailable()) {
            AVQuery<AVUser> query = new AVQuery<>("_User");
            query.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(final List<AVUser> list, AVException e) {
                    if (e == null) {
                        if (list.size() > 0) {
                            Collections.sort(list, new PinYinComparator());
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    for (int i = 0; i < list.size(); i++) {
                                        AVUser user = list.get(i);
                                        RealmUser realmUser = new RealmUser();
                                        realmUser.setId(user.getObjectId());
                                        realmUser.setName(user.getString("name"));
                                        realmUser.setCompany(user.getString("company"));
                                        realmUser.setDepartment(user.getString("department"));
                                        realmUser.setEmail(user.getEmail());
                                        realmUser.setPhone(user.getMobilePhoneNumber());
                                        realmUser.setGender(user.getString("gender"));
                                        realmUser.setEmployeeId(user.getInt("employeeId"));
                                        realmUser.setIntroduce(user.getString("introduce"));
                                        realmUser.setPosition(user.getString("position"));
                                        realmUser.setJob(user.getString("job"));

                                        AVFile img = user.getAVFile("portrait");
                                        if (img != null) {
                                            realmUser.setPortrait(img.getUrl());
                                        } else {
                                            realmUser.setPortrait("null");
                                        }
                                        realm.copyToRealmOrUpdate(realmUser);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    private static class PinYinComparator implements Comparator<AVUser> {

        @Override
        public int compare(AVUser o1, AVUser o2) {
            char c1 = PinyinUtil.getPinyinHeadChar(o1.getString("name"));
            char c2 = PinyinUtil.getPinyinHeadChar(o2.getString("name"));
            if (c1 > c2) {
                return 1;
            } else {
                return -1;
            }
        }
    }

}