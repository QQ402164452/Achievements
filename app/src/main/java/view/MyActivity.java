package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.MyDataAdapter;
import bean.MyDataBean;
import customView.DividerItemDecoration;
import interfaces.Imy;
import presenter.Pmy;

/**
 * Created by Jason on 2016/11/25.
 */

public class MyActivity extends BaseActivity implements Imy{
    private RecyclerView mRecyclerView;
    private MyDataAdapter mAdapter;
    private Pmy mPmy;
    private TextView mEditBtn;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_my);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public void initView(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.MyActivity_toolbar);
        setCustomToolbar(toolbar);

        mRecyclerView= (RecyclerView) findViewById(R.id.MyActivity_RecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mEditBtn= (TextView) findViewById(R.id.MyActivity_btn_edit);
        mPmy=new Pmy(this);
    }

    public void initData(){
        mAdapter=new MyDataAdapter(this);
        mPmy.setData();
    }

    public void initListener(){
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyActivity.this,EditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setDataSource(ArrayList<MyDataBean> list) {
        mAdapter.setData(list);
        mRecyclerView.setAdapter(mAdapter);
    }
}
