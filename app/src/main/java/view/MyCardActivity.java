package view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/24.
 */

public class MyCardActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_mycard);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public void initView(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.MyCardActivity_toolbar);
        setCustomToolbar(toolbar);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
