package view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.jason.achievements.R;

import bean.ErrorBean;
import interfaces.Ipassword;
import presenter.Ppassword;
import utils.ErrorUtil;

/**
 * Created by Jason on 2016/11/28.
 */

public class PasswordActivity extends BaseActivity implements Ipassword{
    private Button mConfrim;
    private EditText mNew;
    private Ppassword mPpw;
    private Button mVerify;
    private EditText mVerifyCode;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_password);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.PasswordActivity_toolbar);
        setCustomToolbar(toolbar);

        mConfrim= (Button) findViewById(R.id.PasswordActivity_btn_confrim);
        mNew= (EditText) findViewById(R.id.PasswordActivity_new_password);
        mVerify= (Button) findViewById(R.id.PasswordActivity__btn_getVerify);
        mVerifyCode= (EditText) findViewById(R.id.PasswordActivity_verify_code);
    }

    @Override
    public void initListener() {
        mConfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPpw.checkPw(mNew.getText().toString(),mVerifyCode.getText().toString());
            }
        });
        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPpw.getVerifyCode();
                showProgress(mNew);
            }
        });
    }

    @Override
    public void initData() {
        mPpw=new Ppassword(this);
        setConfirmBtn(false);
        setVerifyBtn(true);
    }

    @Override
    public void setConfirmBtn(boolean isEnable) {
        if(!isEnable){
            mConfrim.setEnabled(false);
            mConfrim.setBackgroundColor(getResources().getColor(R.color.grey));
        }else {
            mConfrim.setEnabled(true);
            mConfrim.setBackground(getResources().getDrawable(R.drawable.passwordactivity_verify_bg));
        }
    }

    @Override
    public void setVerifyBtn(boolean isEnable) {
        if(!isEnable){
            mVerify.setEnabled(false);
            mVerify.setText("获取成功");
            mVerify.setBackgroundColor(getResources().getColor(R.color.grey));
        }else{
            mVerify.setEnabled(true);
            mVerify.setText("获取验证");
            mVerify.setBackground(getResources().getDrawable(R.drawable.passwordactivity_verify_bg));
        }
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String str) {
        Toast.makeText(this, ErrorUtil.getErrorMessage(str),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPasswordResult(boolean isSuccess, String str) {
        if(isSuccess){
            showToast(str);
            finish();
        }else {
            showErrorMessage(str);
        }
    }
}
