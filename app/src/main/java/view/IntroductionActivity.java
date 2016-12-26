package view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/27.
 */

public class IntroductionActivity extends BaseActivity {
    private EditText mInput;
    private TextView mCount;
    private TextView mConfirm;

    @Override
    protected void initPre() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_introduction);
        Toolbar toolbar= (Toolbar) findViewById(R.id.IntroductionActivity_Toolbar);
        setCustomToolbar(toolbar);
        mInput= (EditText) findViewById(R.id.IntroductionActivity_input);
        mCount= (TextView) findViewById(R.id.IntroductionActivity_count);
        mConfirm= (TextView) findViewById(R.id.IntroductionActivity_btn_confirm);
    }

    @Override
    public void initListener() {
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mCount.setText(String.format("%d/50",mInput.getText().length()));
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intro=mInput.getText().toString();
                Intent intent=new Intent();
                intent.putExtra(EditActivity.INTRO_RESULT_STRING,intro);
                setResult(EditActivity.INTRO_RESULT_CODE,intent);
                finish();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mConfirm.getWindowToken(),0);
    }


    @Override
    public void initData() {
        Intent intent=getIntent();
        if(intent!=null){
            mInput.setText(intent.getStringExtra(EditActivity.INTRO_REQUEST_STRING));
        }
    }
}
