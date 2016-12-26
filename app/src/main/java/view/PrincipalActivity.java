package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.PrincipalAdapter;

/**
 * Created by Jason on 2016/12/10.
 */
//参与人选择
public class PrincipalActivity extends BaseActivity {
    private TextView mConfirm;
    private RecyclerView mRecycler;
    private PrincipalAdapter mAdapter;

    private ArrayList<AVUser> mParList;

    public static final String PRINCIPAL_USER_LISR="PRINCIPAL_USER_LISR";
    public static final String PRINCIPAL_USER_SELECT="PRINCIPAL_USER_SELECT";

    @Override
    protected void initPre() {
        Intent intent=getIntent();
        if(intent!=null){
            ArrayList<AVUser> list= (ArrayList<AVUser>) intent.getSerializableExtra(PRINCIPAL_USER_LISR);
            if(list!=null&&list.size()>0){
                mParList=list;
            }
        }
    }


    @Override
    public void initView() {
        setContentView(R.layout.activity_principal);
        Toolbar toolbar= (Toolbar) findViewById(R.id.PrincipalActivity_toolbar);
        setCustomToolbar(toolbar);

        mConfirm= (TextView) findViewById(R.id.PrincipalActivity_confirm);
        mRecycler= (RecyclerView) findViewById(R.id.PrincipalActivity_RecyclerView);
        mRecycler.setLayoutManager(new GridLayoutManager(this,4));
    }

    @Override
    public void initListener() {
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra(PRINCIPAL_USER_SELECT,mAdapter.getCurrentSelect().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        mAdapter=new PrincipalAdapter(this);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setDataSource(mParList);
        mAdapter.notifyDataSetChanged();
    }
}
