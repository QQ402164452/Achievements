package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import interfaces.ImyTask;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/13.
 */

public class PmyTask  {
    private ImyTask mView;

    public PmyTask(ImyTask imyTask){
        this.mView=imyTask;
    }

    public void getData(int year,int month,int week,int sign){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("task");
            query.whereEqualTo("user", AVUser.getCurrentUser());
            query.orderByDescending("createdAt");
            if(year!=-1){
                query.whereEqualTo("year",year);
            }
            if(month!=-1){
                query.whereEqualTo("month",month);
            }
            if(week!=-1){
                query.whereEqualTo("week",week);
            }
            if(sign!=0){
                query.whereEqualTo("sign",sign-1);
            }
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        mView.onSuccess(list);
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
