package view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/30.
 */

public class AboutActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_about);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void initView() {
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
