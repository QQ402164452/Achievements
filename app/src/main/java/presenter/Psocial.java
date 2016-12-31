package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import interfaces.Isocial;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/28.
 */

public class Psocial {
    private Isocial mView;

    public Psocial(Isocial isocial){
        this.mView=isocial;
    }

    public void getData(final int skip){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> avQuery=new AVQuery<>("microblog");
            avQuery.include("user");
            avQuery.include("likes");
            avQuery.include("likes.user");
            avQuery.include("transmit");
            avQuery.include("transmit.user");
            avQuery.include("transmit.photos");
            avQuery.orderByDescending("createdAt");
            avQuery.limit(20);
            avQuery.skip(skip);
            avQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        if(skip==0){
                            mView.setListData(list,0);
                        }else{
                            mView.setListData(list,1);
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
}
