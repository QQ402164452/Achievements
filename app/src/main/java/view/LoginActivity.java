package view;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.achievements.R;

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
    public void onResult(boolean isSuccess, String result) {
        if(isSuccess){
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
            this.finish();
        }else{
            Toast.makeText(this, result,Toast.LENGTH_SHORT).show();
        }
    }

}
