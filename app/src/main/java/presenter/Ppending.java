package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.Date;

import interfaces.Ipending;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/8.
 */

public class Ppending {
    private Ipending mView;

    public Ppending(Ipending ipending){
        mView=ipending;
    }

    public void setPass(AVObject object){
        if(NetworkUtil.isNewWorkAvailable()){
            if(object!=null){
                mView.showLoading();
                object.put("sign",1);
                object.put("approverTime",new Date());
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            mView.onSuccess("已同意该申请!");
                        }else{
                            mView.onError(e.getMessage());
                        }
                    }
                });
            }
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }

    public void setBack(AVObject object,String remarks){
        if(NetworkUtil.isNewWorkAvailable()){
            if(object!=null){
                mView.showLoading();
                object.put("remarks",remarks);
                object.put("sign",2);
                object.put("approverTime",new Date());
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            mView.onSuccess("已打回该申请！");
                        }else{
                            mView.onError(e.getMessage());
                        }
                    }
                });
            }
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }
}
