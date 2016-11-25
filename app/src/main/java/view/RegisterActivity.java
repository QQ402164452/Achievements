package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

import bean.ErrorBean;
import interfaces.Iregister;
import presenter.Pregister;

/**
 * Created by Jason on 2016/11/24.
 */

public class RegisterActivity extends BaseActivity implements Iregister{
    private EditText mPhoneText;
    private EditText mNameText;
    private EditText mCodeText;
    private Button mRegister;
    private Pregister mPregister;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
    }

    public void initView(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.RegisterActivity_toolbar);
        setCustomToolbar(toolbar);

        mPhoneText= (EditText) findViewById(R.id.RegisterActivity_phone_text);
        mNameText= (EditText) findViewById(R.id.RegisterActivity_account_text);
        mCodeText= (EditText) findViewById(R.id.RegisterActivity_yzcode_text);
        mRegister= (Button) findViewById(R.id.RegisterActivity_Btn);

        mPregister=new Pregister(this);
    }

    public void initListener(){
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=mNameText.getText().toString();
                String phone=mPhoneText.getText().toString();
                String code=mCodeText.getText().toString();
                mPregister.register(name,phone,code);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onResult(boolean isSuccess, String result) {
        if(!isSuccess){
            ErrorBean error= JSON.parseObject(result,ErrorBean.class);
            Toast.makeText(this,error.getError(),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"注册成功！",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }
    }
}
