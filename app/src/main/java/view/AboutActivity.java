package view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/30.
 */

public class AboutActivity extends BaseActivity{

    @Override
    protected void initPre() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_about);

        Toolbar toolbar= (Toolbar) findViewById(R.id.AboutActivity_toolbar);
        setCustomToolbar(toolbar);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

}
