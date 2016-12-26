package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import interfaces.IdoneTask;
import interfaces.ItodoTask;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/15.
 */

public class PdoneTask {
    private IdoneTask mView;

    public PdoneTask(IdoneTask idoneTask){
        this.mView=idoneTask;
    }

    public void getData(int year,int month,int week,int skip){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("task");
            query.whereEqualTo("participant", AVUser.getCurrentUser());
            query.orderByDescending("createdAt");
            query.whereEqualTo("sign",1);
            query.limit(20);
            query.skip(skip);
            if(year!=-1){
                query.whereEqualTo("year",year);
            }
            if(month!=-1){
                query.whereEqualTo("month",month);
            }
            if(week!=-1){
                query.whereEqualTo("week",week);
            }
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        mView.setListData(list);
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
