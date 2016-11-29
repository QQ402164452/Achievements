package presenter;

import android.net.Network;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;

import interfaces.Ipassword;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/11/28.
 */

public class Ppassword {
    private Ipassword mIpw;

    public Ppassword(Ipassword ipassword){
        this.mIpw=ipassword;
    }

    public void checkPw(String newPw,String verifyCode){
        if(NetworkUtil.isNewWorkAvailable()){
            if(newPw.isEmpty()){
                mIpw.showToast("新密码不能为空！");
                return;
            }else{
                if(verifyCode.isEmpty()){
                    mIpw.showToast("验证码不能为空！");
                }else {
                    AVUser.resetPasswordBySmsCodeInBackground(verifyCode, newPw, new UpdatePasswordCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e==null){
                                mIpw.onPasswordResult(true,"修改密码成功！");
                            }else{
                                mIpw.showErrorMessage(e.getMessage());
                            }
                        }
                    });
                }
            }
        }else{
            mIpw.showToast("网络不能连接！请检查网络！");
        }
    }

    public void getVerifyCode(){
        if(NetworkUtil.isNewWorkAvailable()){
            AVUser avUser=AVUser.getCurrentUser();
            if(avUser!=null){
                AVUser.requestPasswordResetBySmsCodeInBackground(avUser.getMobilePhoneNumber(), new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            mIpw.setVerifyBtn(false);
                            mIpw.setConfirmBtn(true);
                            mIpw.showToast("获取验证码成功");
                        }else{
                            mIpw.setVerifyBtn(true);
                            mIpw.setConfirmBtn(false);
                            mIpw.showErrorMessage(e.getMessage());
                        }
                    }
                });
            }
        }else {
            mIpw.showToast(NetworkUtil.tip);
        }
    }

}
