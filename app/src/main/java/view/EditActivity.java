package view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.EditListAdapter;
import bean.EditListBean;
import customView.DividerItemDecoration;
import interfaces.Iedit;
import presenter.Pedit;

/**
 * Created by Jason on 2016/11/25.
 */

public class EditActivity extends BaseActivity implements Iedit{
    private TextView mSave;
    private Pedit mPedit;
    private RecyclerView mRecyclerView;
    private EditListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_edit);
        super.onCreate(savedInstanceState);
    }

    public void initView(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.EditActivity_Toolbar);
        setCustomToolbar(toolbar);
        mSave= (TextView) findViewById(R.id.EditActivity_btn_save);
        mRecyclerView= (RecyclerView) findViewById(R.id.EditActivity_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mPedit=new Pedit(this);
    }

    public void initListener(){
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void initData() {
        mAdapter=new EditListAdapter(this);
        mPedit.setData();
    }

    @Override
    public void setDataSource(ArrayList<EditListBean> list) {
        mAdapter.setData(list);
        mRecyclerView.setAdapter(mAdapter);
    }
}
