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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import adapter.MultiContactAdapter;
import customView.DividerItemDecoration;
import interfaces.ImultiContact;
import presenter.PmultiContact;

/**
 * Created by Jason on 2016/12/10.
 */

public class MultiContactActivity extends BaseActivity implements ImultiContact {
    private TextView mSave;
    private TextView mCompany;
    private RecyclerView mRecycler;
    private MultiContactAdapter mAdapter;
    private PmultiContact mPresenter;
    private ArrayList<AVUser> mSelectPar;

    public static final String MULTI_CONTACT_SELECT_BUNDLE="MULTI_CONTACT_SELECT_BUNDLE";

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_multi_contact);

        Intent intent=getIntent();
        if(intent!=null){
            ArrayList<AVUser> pars= (ArrayList<AVUser>) intent.getSerializableExtra(MULTI_CONTACT_SELECT_BUNDLE);
            if(pars!=null&&pars.size()>0){
                mSelectPar=pars;
            }
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.MultiContactActivity_toolbar);
        setCustomToolbar(toolbar);

        mSave= (TextView) findViewById(R.id.MultiContactActivity_btn_save);
        mCompany= (TextView) findViewById(R.id.MultiContactActivity_company_name);
        mRecycler= (RecyclerView) findViewById(R.id.MultiContactActivity_recyclerView);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void initListener() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable(MULTI_CONTACT_SELECT_BUNDLE,mPresenter.getSelectAVUser(mAdapter.getTreeSet()));
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        mPresenter=new PmultiContact(this);
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
        mAdapter=new MultiContactAdapter(this);
        mRecycler.setAdapter(mAdapter);
        mPresenter.getUserData();
    }

    @Override
    public void setAdapterData(List<AVUser> list) {
        mAdapter.setDataSource(list);
        if(mSelectPar!=null&&mSelectPar.size()>0){
            mAdapter.setSelectPos(mSelectPar);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(String str) {

    }
}
