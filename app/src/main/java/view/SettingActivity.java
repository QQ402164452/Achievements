package view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

import adapter.SettingListAdapter;
import customView.DividerItemDecoration;

/**
 * Created by Jason on 2016/11/24.
 */

public class SettingActivity extends BaseActivity {
    private TextView mLoginOut;
    private View mDivider;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
        AVUser user=AVUser.getCurrentUser();
        if(user==null){
            mLoginOut.setVisibility(View.GONE);
            mDivider.setVisibility(View.GONE);
        }else{
            mLoginOut.setVisibility(View.VISIBLE);
            mDivider.setVisibility(View.VISIBLE);
        }
    }

    public void initView(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.SettingActivity_toolbar);
        setCustomToolbar(toolbar);
        mLoginOut= (TextView) findViewById(R.id.SettingActivity_loginOut);
        mDivider=findViewById(R.id.SettingActivity_divider_view);
        mRecyclerView= (RecyclerView) findViewById(R.id.SettingActivity_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(new SettingListAdapter(this));
    }

    public void initListener(){
        mLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser user=AVUser.getCurrentUser();
                if(user!=null){
                    user.logOut();
                    finish();
                }
            }
        });
    }

    @Override
    public void initData() {

    }
}
