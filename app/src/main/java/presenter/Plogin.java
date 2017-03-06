package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import interfaces.Ilogin;
import utils.CheckUtils;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/11/24.
 */

public class Plogin  {
    private Ilogin mIlogin;

    public Plogin(Ilogin ilogin){
        this.mIlogin=ilogin;
    }

    public void LoginIn(String phone,String password){
        if(NetworkUtil.isNewWorkAvailable()){
            mIlogin.showLoading();
            if(phone.isEmpty()){
                mIlogin.onResult(false,"手机号不能为空");
                return;
            }else{
                if(phone.length()!=11||CheckUtils.isChinaPhoneLegal(phone)){
                    mIlogin.onResult(false,"手机号码不正确");
                    return;
                }
            }
            if(password.isEmpty()){
                mIlogin.onResult(false,"密码不能为空");
                return;
            }
            AVUser.loginByMobilePhoneNumberInBackground(phone, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if(e==null){
                        mIlogin.onResult(true,"登录成功");
                    }else{
                        mIlogin.onResult(false,e.getMessage());
                    }
                }
            });
        }else {
            mIlogin.showToast("网络不能连接！请检查网络！");
        }
    }

}
