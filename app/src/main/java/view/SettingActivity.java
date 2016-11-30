package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.SettingListAdapter;
import customView.DividerItemDecoration;
import customView.DividerItemExceptLastDecoration;
import interfaces.onCustomItemClickListener;

/**
 * Created by Jason on 2016/11/24.
 */

public class SettingActivity extends BaseActivity {
    private Button mLoginOut;
    private RecyclerView mRecyclerView;
    private SettingListAdapter mAdapter;

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
        }else{
            mLoginOut.setVisibility(View.VISIBLE);
        }
    }

    public void initView(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.SettingActivity_toolbar);
        setCustomToolbar(toolbar);
        mLoginOut= (Button) findViewById(R.id.SettingActivity_loginOut);
        mRecyclerView= (RecyclerView) findViewById(R.id.SettingActivity_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemExceptLastDecoration(this,DividerItemDecoration.VERTICAL_LIST));

    }

    public void initListener(){
        mLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser user=AVUser.getCurrentUser();
                if(user!=null){
                    user.logOut();
                    Toast.makeText(SettingActivity.this,"退出账号成功！",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        mAdapter.setOnClickListener(new onCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent;
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        intent=new Intent(SettingActivity.this,AboutActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        ArrayList<String> mList=new ArrayList<>();
        mList.add("新消息通知");
        mList.add("检查版本更新");
        mList.add("关于我们");
        mAdapter=new SettingListAdapter(this,mList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
