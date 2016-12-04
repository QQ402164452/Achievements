package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;

import interfaces.Iwritereport;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/2.
 */

public class PwriteReport {
    private Iwritereport mView;

    public PwriteReport(Iwritereport iwritereport){
        mView=iwritereport;
    }

    public void upReport(final int type, String summary, String plan, AVUser user, String firstday, String lastday){
        if(NetworkUtil.isNewWorkAvailable()){
            try {
                AVObject object=new AVObject("report");
                object.put("reporter",AVUser.getCurrentUser());
                object.put("type",type);
                object.put("approver",user);
                object.put("summary",summary);
                object.put("plan",plan);
                object.put("firstday",firstday);
                object.put("lastday",lastday);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            mView.onSuccess("保存成功！");
                        }else{
                            mView.onError(e.getMessage());
                        }
                    }
                });
            }catch (Exception e){

            }
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }
}
