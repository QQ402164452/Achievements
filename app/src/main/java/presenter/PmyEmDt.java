package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;

import interfaces.ImyEmDt;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/7.
 */

public class PmyEmDt {
    private ImyEmDt mView;

    public PmyEmDt(ImyEmDt imyEmDt){
        mView=imyEmDt;
    }

    public void setUrgeState(AVObject avObject){
        if(NetworkUtil.isNewWorkAvailable()){
            if(avObject!=null){
                avObject.put("sign",3);
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            mView.onSuccess("催办成功！",0);
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

    public void setRevokeState(AVObject avObject){
        if(NetworkUtil.isNewWorkAvailable()){
            if(avObject!=null){
                avObject.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            mView.onSuccess("撤销成功！",1);
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
