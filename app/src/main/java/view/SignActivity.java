package view;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.jason.achievements.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.SignAdapter;
import bean.SignBean;
import interfaces.WeakObject;
import interfaces.Isign;
import presenter.Psign;
import utils.DateUtil;
import utils.WeakHandler;

/**
 * Created by Jason on 2016/12/1.
 */

public class SignActivity extends BaseActivity implements Isign {
    private XRecyclerView mRecyclerView;
    private View mEmptyView;
    private SignAdapter mAdapter;
    private Button mSign;
    private Psign mPresenter;
    private WeakHandler mHandler;
    public static String SIGN_SHAR="SignActivity";
    public static String SIGN_TYPE="SIGN_TYPE";
    public static String SIGN_DAY="SIGN_DAY";
    private TextView mDay;
    private TextView mTime;
    private List<SignBean> mList;

    @Override
    protected void initPre() {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_sign);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Toolbar toolbar= (Toolbar) findViewById(R.id.SignActivity_toolbar);
        setCustomToolbar(toolbar);
        mRecyclerView= (XRecyclerView) findViewById(R.id.SignActivity_recyclerView);
        mEmptyView=findViewById(R.id.SignActivity_empty_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setLoadingMoreEnabled(false);
        mSign= (Button) findViewById(R.id.SignActivity_sign_button);
        mTime= (TextView) findViewById(R.id.SignActivity_time);
        mDay= (TextView) findViewById(R.id.SignActivity_day);
    }

    @Override
    public void initListener() {
        mSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(mRecyclerView);
                mPresenter.getLocation();
            }
        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(0,1000);
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    @Override
    public void initData() {
        mList=new ArrayList<>();
        mTime.setText(DateUtil.getCurrentDate());
        mDay.setText(DateUtil.getCurrentDay());
        mPresenter=new Psign(this,this);
        mHandler=new WeakHandler(new WeakObject() {
            @Override
            public void doLoadData(int type) {
                mPresenter.setListData(0);
            }
        });
        mAdapter=new SignAdapter(this,mList);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.setListData(0);
    }

    @Override
    public void onResult(int type,ArrayList<SignBean> list) {
        switch (type){
            case 0:
                mRecyclerView.refreshComplete();
                if(list.size()>0){
                    mList.clear();
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case 1:
                hideBasePopup();
                if(list.size()>0){
                    mList.clear();
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
                showToast("签到成功");
                break;
            case 2:
                hideBasePopup();
                showToast("今天已经签到完成 不能再签到");
                break;
        }
    }


    @Override
    public void onError(String str) {
        hideBasePopup();
        showToast(str);
    }

}
