package view;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.jason.achievements.R;

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
    protected void initPre() {

    }

    public void initView(){
        setContentView(R.layout.activity_register);
        Toolbar toolbar= (Toolbar) findViewById(R.id.RegisterActivity_toolbar);
        setCustomToolbar(toolbar);

        mPhoneText= (EditText) findViewById(R.id.RegisterActivity_phone_text);
        mNameText= (EditText) findViewById(R.id.RegisterActivity_account_text);
        mCodeText= (EditText) findViewById(R.id.RegisterActivity_yzcode_text);
        mRegister= (Button) findViewById(R.id.RegisterActivity_Btn);
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
        mPregister=new Pregister(this);
    }

    @Override
    public void onResult(boolean isSuccess, String result) {
        if(!isSuccess){
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"注册成功！",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }
    }

}
