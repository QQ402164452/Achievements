package presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import interfaces.ImultiContact;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/10.
 */

public class PmultiContact {
    private ImultiContact mView;
    private List<AVUser> mList;
    private ArrayList<AVUser> mSelect;

    public PmultiContact(ImultiContact imultiContact){
        mView=imultiContact;
    }

    public void getUserData(){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVUser> query=new AVQuery<>("_User");
            query.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(List<AVUser> list, AVException e) {
                    if(e==null){
                        if(list.size()>0){
                            mList=list;
                            mView.setAdapterData(mList);
                        }else{
                            mView.showToast("获取数据失败");
                        }
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }

    }

    public ArrayList<AVUser> getSelectAVUser(TreeSet<Integer> treeSet){
        mSelect=new ArrayList<>();
        Iterator<Integer> iterator=treeSet.iterator();
        while(iterator.hasNext()){
            mSelect.add(mList.get(iterator.next()));
        }
        return mSelect;
    }

}
