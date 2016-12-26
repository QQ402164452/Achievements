package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

import java.util.List;

import adapter.ContactSelectAdapter;
import customView.DividerItemDecoration;
import interfaces.IcontactSelect;
import presenter.PcontactSelect;

/**
 * Created by Jason on 2016/12/3.
 */
public class ContactSelectActivity extends BaseActivity implements IcontactSelect {
    private RecyclerView mRecyclerView;
    private TextView mCompany;
    private ContactSelectAdapter mAdapter;
    private PcontactSelect mPresenter;
    private TextView mSave;

    @Override
    protected void initPre() {

    }


    @Override
    public void initView() {
        setContentView(R.layout.activity_contact_select);
        Toolbar toolbar= (Toolbar) findViewById(R.id.ContactSelectActivity_toolbar);
        setCustomToolbar(toolbar);
        mCompany= (TextView) findViewById(R.id.ContactSelectActivity_company_name);
        mRecyclerView= (RecyclerView) findViewById(R.id.ContactSelectActivity_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mSave= (TextView) findViewById(R.id.ContactSelectActivity_btn_save);
    }

    @Override
    public void initListener() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapter.getSelectedPos()!=-1){
                    Intent intent=new Intent();
                    AVUser selectUser= mPresenter.getSelectAVUser(mAdapter.getSelectedPos());
                    //AVObject的序列化
                    String serializedString = selectUser.toString();
                    intent.putExtra(WriteReportActivity.SELECT_USER,serializedString);
                    setResult(RESULT_OK,intent);
                }
                finish();
            }
        });
    }

    @Override
    public void initData() {
        mPresenter=new PcontactSelect(this);
        AVUser user=AVUser.getCurrentUser();
        if(user!=null){
            if(user.getString("company")!=null){
                mCompany.setText(String.format("公司:%s",user.getString("company")));
            }else{
                mCompany.setText("公司:暂无信息");
            }
        }else{
            mCompany.setText("公司:暂无信息");
        }
        mAdapter=new ContactSelectAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getUserData();
    }

    @Override
    public void setAdapterData(List<AVUser> list) {
        mAdapter.setDataSource(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(String str) {

    }
}
