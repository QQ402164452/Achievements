package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import interfaces.Icheck;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/6.
 */

public class Pcheck {
    private Icheck mView;

    public Pcheck(Icheck icheck){
        mView=icheck;
    }

    public void sumbit(AVUser user,AVUser approver,int type,int schedule,int beginNoon,int endNoon,String beginTime,String endTime,String reason){
        if(type==0&&schedule==-1){
            mView.showToast("未选择请假类型");
            return;
        }else if(beginTime.isEmpty()){
            mView.showToast("未选择开始时间");
            return;
        }else if(endTime.isEmpty()){
            mView.showToast("未选择结束时间");
            return;
        }else if(reason.isEmpty()){
            mView.showToast("未输入事由");
            return;
        }else if(user==null){
            mView.showToast("用户未登录");
            return;
        }else if(approver==null){
            mView.showToast("未选择审批人");
        }else if(NetworkUtil.isNewWorkAvailable()){
            mView.showLoading();
            Date beginDate=null;
            Date endDate=null;
            SimpleDateFormat simpleDateFormat;
            switch (type){
                case 0:
                case 2:
                    simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
                    try {
                        beginDate=simpleDateFormat.parse(beginTime);
                        endDate=simpleDateFormat.parse(endTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日hh时");
                    try {
                        beginDate=simpleDateFormat.parse(beginTime);
                        endDate=simpleDateFormat.parse(endTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    beginNoon=-1;
                    endNoon=-1;
                    break;
            }
            AVObject object=new AVObject("approval");
            object.put("user",user);
            object.put("approver",approver);
            object.put("begin",beginDate);
            object.put("end",endDate);
            object.put("type",type);
            object.put("schedule",schedule);
            object.put("reason",reason);
            object.put("sign",0);
            object.put("beginNoon",beginNoon);
            object.put("endNoon",endNoon);
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e==null){
                        mView.onSuccess("申请成功");
                    }else {
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else {
            mView.showToast(NetworkUtil.tip);
        }
    }

}
