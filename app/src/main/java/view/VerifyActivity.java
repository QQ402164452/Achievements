package view;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.jason.achievements.R;

import interfaces.Iverify;
import presenter.Pverify;

/**
 * Created by Jason on 2016/11/28.
 */

public class VerifyActivity extends BaseActivity implements Iverify{
    private TextView mPhone;
    private EditText mVerifyCode;
    private Button mGetCode;
    private Button mVerify;
    private Pverify mPv;

    @Override
    protected void initPre() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_verify);
        Toolbar toolbar= (Toolbar) findViewById(R.id.VerifyActivity_toolbar);
        setCustomToolbar(toolbar);

        mVerify= (Button) findViewById(R.id.VerifyActivity_verify_Btn);
        mGetCode= (Button) findViewById(R.id.VerifyActivity__btn_getVerify);
        mPhone= (TextView) findViewById(R.id.VerifyActivity_phone);
        mVerifyCode= (EditText) findViewById(R.id.VerifyActivity_verify_code);
    }

    @Override
    public void initListener() {
        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPv.verifyCode(mVerifyCode.getText().toString());
            }
        });
        mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("1111","1111");
                mPv.getVerifyCode(mPhone.getText().toString());
            }
        });
    }

    @Override
    public void initData() {
        mPv=new Pverify(this);
        mPv.setPhone();
        setVerifyBtn(false);
        setGetVerifyBtn(true);
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setVerifyBtn(boolean isEnable) {
        if(!isEnable){
            mVerify.setEnabled(false);
            mVerify.setBackgroundColor(getResources().getColor(R.color.grey));
        }else {
            mVerify.setEnabled(true);
            mVerify.setBackground(getResources().getDrawable(R.drawable.passwordactivity_verify_bg));
        }
    }

    @Override
    public void setGetVerifyBtn(boolean isEnable) {
        if(!isEnable){
            mGetCode.setEnabled(false);
            mGetCode.setText("获取成功");
            mGetCode.setBackgroundColor(getResources().getColor(R.color.grey));
        }else{
            mGetCode.setEnabled(true);
            mGetCode.setText("获取验证");
            mGetCode.setBackground(getResources().getDrawable(R.drawable.passwordactivity_verify_bg));
        }
    }

    @Override
    public void onVerifyResult(boolean isSuccess, String message) {
        if(isSuccess){
            showToast(message);
            finish();
        }else{
            showErrorMessage(message);
        }
    }

    @Override
    public void setPhone(String phone) {
        mPhone.setText(phone);
    }
}
