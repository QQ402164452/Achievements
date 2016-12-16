package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import interfaces.IcontactSelect;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/3.
 */

public class PcontactSelect  {
    private List<AVUser> mList;
    private IcontactSelect mView;

    public PcontactSelect(IcontactSelect icontactSelect){
        mView=icontactSelect;
        mList=new ArrayList<>();
    }

    public void getUserData(){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVUser> query=new AVQuery<>("_User");
            query.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(List<AVUser> list, AVException e) {
                    if(e==null){
                        mList=list;
                        mView.setAdapterData(mList);
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }

    public AVUser getSelectAVUser(int pos){
        return mList.get(pos);
    }


}
