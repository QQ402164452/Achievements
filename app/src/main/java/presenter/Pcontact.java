package presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import bean.RealmUser;
import interfaces.Icontact;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import utils.NetworkUtil;
import utils.PinyinUtil;

/**
 * Created by Jason on 2017/1/2.
 */

public class Pcontact {
    private Icontact mView;
    private Realm realm;
    private HashMap<Character,Integer> letterMap;

    public Pcontact(Icontact icontact){
        this.mView=icontact;
        realm=Realm.getDefaultInstance();
        letterMap=new HashMap<>();
    }

    public void onDestroy(){
        realm.close();
    }

    public void getCacheMsg(){
        RealmResults<RealmUser> realmResults=realm.where(RealmUser.class).findAll();
        mView.setCacheUserList(realmResults);
    }

    public void getNewMessage(){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVUser> query=new AVQuery<>("_User");
            query.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(final List<AVUser> list, AVException e) {
                    if(e==null){
                        if(list.size()>0){
                            list.remove(AVUser.getCurrentUser());
                            Collections.sort(list,new PinYinComparator());

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    RealmResults<RealmUser> realmResults=realm.where(RealmUser.class).findAll();
                                    realmResults.deleteAllFromRealm();
                                    for(int i=0;i<list.size();i++){
                                        AVUser user=list.get(i);
                                        RealmUser realmUser=realm.createObject(RealmUser.class,user.getObjectId());
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

                                        AVFile img=user.getAVFile("portrait");
                                        if(img!=null){
                                            realmUser.setPortrait(img.getUrl());
                                        }else{
                                            realmUser.setPortrait("null");
                                        }
                                    }
                                    realmResults=realm.where(RealmUser.class).findAll();
                                    mView.updateUserDate(realmResults);
                                }
                            });
                        }else{
                            mView.showToast("获取数据失败");
                        }
                    }else{
                        mView.showToast(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }

    private static class PinYinComparator implements Comparator<AVUser> {

        @Override
        public int compare(AVUser o1, AVUser o2) {
            char c1= PinyinUtil.getPinyinHeadChar(o1.getString("name"));
            char c2=PinyinUtil.getPinyinHeadChar(o2.getString("name"));
            if(c1>c2){
                return 1;
            }else {
                return -1;
            }
        }
    }

    public void getLetterMap(RealmResults<RealmUser> realmResults){
        letterMap.clear();
        for(int i=0;i<realmResults.size();i++){
            if (i == 0) {
                letterMap.put(Character.toUpperCase(PinyinUtil.getPinyinHeadChar(realmResults.get(i).getName())),1);
            } else {
                char last = PinyinUtil.getPinyinHeadChar(realmResults.get(i - 1).getName());
                char current = PinyinUtil.getPinyinHeadChar(realmResults.get(i).getName());
                if (current != last) {
                    letterMap.put(Character.toUpperCase(PinyinUtil.getPinyinHeadChar(realmResults.get(i).getName())),i+1);
                }
            }
        }
        mView.setLetterMap(letterMap);
    }
}
