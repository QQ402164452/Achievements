package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import interfaces.InewTask;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/10.
 */

public class PnewTask {
    private InewTask mView;
    private SimpleDateFormat mFormat;

    public PnewTask(InewTask inewTask) {
        mView = inewTask;
        mFormat = new SimpleDateFormat("yyyy年MM月DD日 HH时", Locale.CHINA);
    }

    public void upNewTask(String detail,String title, ArrayList<AVUser> participant, final AVUser principal, String time) {
        if (detail.isEmpty()) {
            mView.showToast("任务详情不能为空");
        } else if(title.isEmpty()){
            mView.showToast("任务名不能为空");
        } else if (participant == null || participant.size() < 0) {
            mView.showToast("参与人不能为空");
        } else if (principal == null) {
            mView.showToast("负责人不能为空");
        } else if (time.isEmpty()) {
            mView.showToast("截止时间不能为空");
        } else if (NetworkUtil.isNewWorkAvailable()) {
            mView.showLoading();
            final AVObject task = new AVObject("task");
            final AVObject taskFeedBack = new AVObject("taskFeedback");
            try {
                Date endTime = mFormat.parse(time);
                Calendar calendar=Calendar.getInstance();
                task.put("principal", principal);
                task.put("user",AVUser.getCurrentUser());
                task.put("participant", participant);
                task.put("detail", detail);
                task.put("title",title);
                task.put("endTime", endTime);
                task.put("sign", 0);
                task.put("year",calendar.get(Calendar.YEAR));
                task.put("month",calendar.get(Calendar.MONTH)+1);
                task.put("week",calendar.get(Calendar.WEEK_OF_YEAR));
                task.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            taskFeedBack.put("type", 1);
                            taskFeedBack.put("content", "创建了任务");
                            taskFeedBack.put("user", AVUser.getCurrentUser());
                            taskFeedBack.put("task", task);
                            taskFeedBack.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        mView.onSuccess("新建任务成功");
                                    } else {
                                        mView.onError(e.getMessage());
                                    }
                                }
                            });

                        } else {
                            mView.onError(e.getMessage());
                        }
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            mView.showToast(NetworkUtil.tip);
        }
    }

    public void modifyTask(final AVObject task, String detail, String title, ArrayList<AVUser> participant, final AVUser principal, String time){
        if (detail.isEmpty()) {
            mView.showToast("任务详情不能为空");
        } else if(time.isEmpty()){
            mView.showToast("任务名不能为空");
        } else if (participant == null || participant.size() < 0) {
            mView.showToast("参与人不能为空");
        } else if (principal == null) {
            mView.showToast("负责人不能为空");
        } else if (time.isEmpty()) {
            mView.showToast("截止时间不能为空");
        } else if (NetworkUtil.isNewWorkAvailable()) {
            mView.showLoading();
            try {
                Date endTime = mFormat.parse(time);
                task.put("principal", principal);
                task.put("participant", participant);
                task.put("detail", detail);
                task.put("title",title);
                task.put("endTime", endTime);
                task.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            mView.onModifyTaskSuccess("修改任务成功");
                        }else{
                            mView.onError(e.getMessage());
                        }
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            mView.showToast(NetworkUtil.tip);
        }
    }

    public void setTaskDone(final AVObject task){
        if(NetworkUtil.isNewWorkAvailable()){
            mView.showLoading();
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
                                if(e==null){
                                    mView.setDoneSuccess("设置任务成功");
                                }else{
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

    public void deleteTask(AVObject task){
        if(NetworkUtil.isNewWorkAvailable()){
            mView.showLoading();
            task.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(AVException e) {
                    if(e==null){
                        mView.onDeleteSuccess("删除成功");
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
