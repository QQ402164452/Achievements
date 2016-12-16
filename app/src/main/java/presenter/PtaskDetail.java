package presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.Date;
import java.util.List;

import interfaces.ItaskDetail;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/14.
 */

public class PtaskDetail {
    private ItaskDetail mView;

    public PtaskDetail(ItaskDetail itaskDetail){
        this.mView=itaskDetail;
    }

    public void getFeedBack(AVObject avObject){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("taskFeedback");
            query.whereEqualTo("task",avObject);
            query.include("user");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        mView.setFeedBack(list);
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }

    public void getTaskDetail(final AVObject avObject){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("task");
            query.whereEqualTo("objectId",avObject.getObjectId());
            query.include("principal");
            query.include("participant");
            query.include("user");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        if(list.size()>0){
                            mView.setPeople(list.get(0));
                            getFeedBack(avObject);
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

    public void sendFeedBack(String content,AVObject task){
        if(content==null||content.isEmpty()){
            mView.showToast("反馈内容不能为空");
        }else if(task==null){
            mView.showToast("程序出错");
        } else if(NetworkUtil.isNewWorkAvailable()){
            AVObject object=new AVObject("taskFeedback");
            object.put("task",task);
            object.put("user",AVUser.getCurrentUser());
            object.put("content",content);
            object.put("type",1);
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e==null){
                        mView.onSuccess("反馈成功");
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }

    public void setTaskDone(final AVObject task){
        if(NetworkUtil.isNewWorkAvailable()){
            task.put("complete",new Date());
            task.put("sign",1);
            task.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e==null){
                        AVObject Object=new AVObject("taskFeedback");
                        Object.put("type", 1);
                        Object.put("content", "任务设置为已完成");
                        Object.put("user", AVUser.getCurrentUser());
                        Object.put("task", task);
                        Object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    mView.setDoneSuccess("任务完成");
                                } else {
                                    mView.onError(e.getMessage());
                                }
                            }
                        });
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
