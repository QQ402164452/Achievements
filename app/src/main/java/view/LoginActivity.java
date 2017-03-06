package view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.jason.achievements.R;

import application.MyApplication;
import im.AVIMClientManager;
import im.RealmUtils;
import interfaces.Ilogin;
import presenter.Plogin;
import utils.ErrorUtil;

/**
 * Created by Jason on 2016/11/23.
 */

public class LoginActivity extends BaseActivity implements Ilogin{
    private TextView mRegister;
    private Button mLoginIn;
    private Plogin mPlogin;
    private EditText mPhone;
    private EditText mPassWord;

    @Override
    protected void initPre() {

    }

    public void initView(){
        setContentView(R.layout.activity_login);
        Toolbar toolbar= (Toolbar) findViewById(R.id.LoginActivity_toolbar);
        setCustomToolbar(toolbar);

        mRegister= (TextView) findViewById(R.id.LoginActivity_Register_Btn);
        mLoginIn= (Button) findViewById(R.id.LoginActivity_Login_Btn);
        mPhone= (EditText) findViewById(R.id.LoginActivity_phone_text);
        mPassWord= (EditText) findViewById(R.id.LoginActivity_password_text);
        mPlogin=new Plogin(this);
    }

    public void initListener(){
        mLoginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=mPhone.getText().toString();
                String password=mPassWord.getText().toString();
                InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//强制关闭输入法
                inputMethodManager.hideSoftInputFromWindow(mBase.getWindowToken(),0);
                mPlogin.LoginIn(phone,password);
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onResult(boolean isSuccess, final String result) {
        hideBasePopup();
        if(isSuccess){
            if(AVIMClientManager.getInstance().getClient()!=null){
                AVIMClientManager.getInstance().getClient().close(null);
            }
            AVIMClientManager.getInstance().open(AVUser.getCurrentUser().getObjectId(), new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if(e==null){
                        RealmUtils.getInstance().insertRealmUser();
                        showToast(result);
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        showToast(e.getMessage());
                    }
                }
            });
        }else{
            showToast(result);
        }
    }

    @Override
    public void onSuccess(String str) {

    }
}
