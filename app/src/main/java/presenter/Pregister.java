package presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import interfaces.Iregister;
import utils.CheckUtils;

/**
 * Created by Jason on 2016/11/24.
 */

public class Pregister  {

    private Iregister mRegisterView;

    public Pregister(Iregister iregister){
        this.mRegisterView=iregister;
    }

    public void register(String name,String phone,String password){
        if(name.isEmpty()){
            mRegisterView.onResult(false,"用户名不能为空");
            return;
        }
        if(phone.isEmpty()){
            mRegisterView.onResult(false,"手机号码不能为空");
            return;
        }else{
            if(phone.length()!=11||CheckUtils.isChinaPhoneLegal(phone)){
                mRegisterView.onResult(false,"手机号码格式不正确");
                return;
            }
        }
        if(password.isEmpty()){
            mRegisterView.onResult(false,"密码不能为空");
            return;
        }
        AVUser user=new AVUser();
        user.setUsername(phone);
        user.setMobilePhoneNumber(phone);
        user.setPassword(password);
        user.put("name",name);
        user.increment("employeeId");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    //注册成功，把用户对象赋值给当前用户AVUser.getCurrentUser()
                    mRegisterView.onResult(true,"ok");
                }else{
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    mRegisterView.onResult(false,e.getMessage());
                }
            }
        });
    }
}
