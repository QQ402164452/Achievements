package view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.achievements.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import adapter.SignAdapter;
import bean.SignBean;
import interfaces.Isign;
import presenter.Psign;
import utils.DateUtil;

/**
 * Created by Jason on 2016/12/1.
 */

public class SignActivity extends BaseActivity implements Isign {
    private RecyclerView mRecyclerView;
    private SignAdapter mAdapter;
    private Button mSign;
    private Psign mPresenter;
    private SignHandler mHandler;
    public static String SIGN_SHAR="SignActivity";
    public static String SIGN_TYPE="SIGN_TYPE";
    public static String SIGN_DAY="SIGN_DAY";
    private TextView mDay;
    private TextView mTime;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_sign);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.SignActivity_toolbar);
        setCustomToolbar(toolbar);
        mRecyclerView= (RecyclerView) findViewById(R.id.SignActivity_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
    }

    @Override
    public void initData() {
        mTime.setText(DateUtil.getCurrentDate());
        mDay.setText(DateUtil.getCurrentDay());
        mPresenter=new Psign(this,this);
        mHandler=new SignHandler(this);
        mAdapter=new SignAdapter(this);
        mPresenter.setListData();
    }

    @Override
    public void setListAdapter(ArrayList<SignBean> list) {
        mAdapter.setList(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void notifyRecyclerView() {
        mHandler.sendEmptyMessageDelayed(0,1000);
    }

    @Override
    public void onError(String str) {
        hideBasePopup();
        showToast(str);
    }

    private static class SignHandler extends Handler{
        private WeakReference<SignActivity> reference;

        public SignHandler(SignActivity signActivity){
            reference=new WeakReference<SignActivity>(signActivity);
        }

        public void handleMessage(Message msg){
            if(reference!=null){
                SignActivity activity=reference.get();
                activity.hideBasePopup();
                activity.mAdapter.notifyDataSetChanged();
                activity.showToast("签到成功！");
            }
        }


    }
}
