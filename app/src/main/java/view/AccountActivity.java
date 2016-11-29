package view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.SettingListAdapter;
import customView.DividerItemExceptLastDecoration;
import interfaces.onCustomItemClickListener;

/**
 * Created by Jason on 2016/11/28.
 */

public class AccountActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private SettingListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_account);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.AccountActivity_toolbar);
        setCustomToolbar(toolbar);

        mRecyclerView= (RecyclerView) findViewById(R.id.AccountActivity_List);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemExceptLastDecoration(this,DividerItemExceptLastDecoration.VERTICAL_LIST));
    }

    @Override
    public void initListener() {
        mAdapter.setOnClickListener(new onCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent;
                switch (position){
                    case 0:
                        if(!AVUser.getCurrentUser().isMobilePhoneVerified()){
                            intent=new Intent(AccountActivity.this,VerifyActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(AccountActivity.this, "该账号已经验证手机号码！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if(AVUser.getCurrentUser().isMobilePhoneVerified()){
                            intent=new Intent(AccountActivity.this,PasswordActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(AccountActivity.this, "该账号还没验证手机号码，请验证手机号码！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        ArrayList<String> list=new ArrayList<>();
        list.add("验证手机号码");
        list.add("密码修改");
        mAdapter=new SettingListAdapter(this,list);
        mRecyclerView.setAdapter(mAdapter);
    }
}
