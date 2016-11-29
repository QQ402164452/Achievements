package presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;

import interfaces.Iverify;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/11/28.
 */

public class Pverify {
    private Iverify iverify;

    public Pverify(Iverify iverify) {
        this.iverify = iverify;
    }

    public void getVerifyCode(String phone) {
        if (NetworkUtil.isNewWorkAvailable()) {
            AVUser.requestMobilePhoneVerifyInBackground(phone, new RequestMobileCodeCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        //发送成功
                        iverify.setVerifyBtn(true);
                        iverify.setGetVerifyBtn(false);
                        iverify.showToast("发送成功");
                    } else {
                        iverify.setVerifyBtn(false);
                        iverify.setGetVerifyBtn(true);
                        iverify.showErrorMessage(e.getMessage());
                    }
                }
            });
        } else {
            iverify.showToast(NetworkUtil.tip);
        }
    }

    public void verifyCode(String code) {
        if (NetworkUtil.isNewWorkAvailable()) {
            AVUser.verifyMobilePhoneInBackground(code, new AVMobilePhoneVerifyCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        //验证成功
                        iverify.onVerifyResult(true, "验证成功!");
                        AVUser.getCurrentUser().refreshInBackground(new RefreshCallback<AVObject>() {//更新云端数据到本地
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                if (e == null) {

                                } else {
                                    iverify.showErrorMessage(e.getMessage());
                                }
                            }
                        });
                    } else {
                        iverify.onVerifyResult(false, e.getMessage());
                    }
                }
            });
        } else {
            iverify.showToast(NetworkUtil.tip);
        }
    }

    public void setPhone() {
        AVUser avUser = AVUser.getCurrentUser();
        if (avUser != null) {
            if (avUser.getMobilePhoneNumber() != null) {
                iverify.setPhone(avUser.getMobilePhoneNumber());
            }
        }
    }
}
