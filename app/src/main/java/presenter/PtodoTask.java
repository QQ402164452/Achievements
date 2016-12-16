package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import interfaces.ItodoTask;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/15.
 */

public class PtodoTask  {
    private ItodoTask mView;

    public PtodoTask(ItodoTask itodoTask){
        mView=itodoTask;
    }

    public void getTodoList(){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("task");
            query.whereEqualTo("participant", AVUser.getCurrentUser());
            query.whereEqualTo("sign",0);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        mView.onSuccess(list,0);
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }

    public void getDoneList(){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("task");
            query.whereEqualTo("participant",AVUser.getCurrentUser());
            query.whereEqualTo("sign",1);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        mView.onSuccess(list,1);
                    }else {
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.onError(NetworkUtil.tip);
        }
    }

}
