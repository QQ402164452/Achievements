package view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapter.CalendarAdapter;
import adapter.WageAdapter;
import customView.DividerItemDecoration;
import interfaces.OnCustomItemClickListener;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/8.
 */

public class WageActivity extends BaseActivity{
    private Toolbar mToolbar;
    private ImageView mCalBtn;
    private TextView mTime;
    private ArrayList<String> mYears;
    private int mCurrentYear=3;
    private int mCurrentMonth=0;
    private RecyclerView mRecyclerView;
    private WageAdapter mAdapter;
    private TextView mTotalWage;
    private TextView mRealWage;
    private LinearLayout mEmptyView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_wage);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mToolbar= (Toolbar) findViewById(R.id.WageActivity_toolbar);
        setCustomToolbar(mToolbar);

        mCalBtn= (ImageView) findViewById(R.id.WageActivity_btn_Calendar);
        mTime= (TextView) findViewById(R.id.WageActivity_title_time);
        mRecyclerView= (RecyclerView) findViewById(R.id.WageActivity_RecyclerView);
        mTotalWage= (TextView) findViewById(R.id.WageActivity_text_totalWage);
        mRealWage= (TextView) findViewById(R.id.WageActivity_text_RealWage);
        mEmptyView= (LinearLayout) findViewById(R.id.WageActivity_empty_View);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void initListener() {
        mCalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
    }

    @Override
    public void initData() {
        mYears=new ArrayList<>(Arrays.asList("2013","2014","2015","2016","2017","2018","2019","2020"));
        mTime.setText(String.format("%s年1月",mYears.get(mCurrentYear)));
        mAdapter=new WageAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        getData();
    }

    public void getData(){
        if(NetworkUtil.isNewWorkAvailable()){
            if(AVUser.getCurrentUser()!=null){
                AVQuery<AVObject> query=new AVQuery<>("salary");
                query.whereEqualTo("year",Integer.parseInt(mYears.get(mCurrentYear)));
                query.whereEqualTo("month",mCurrentMonth+1);
                query.whereEqualTo("user",AVUser.getCurrentUser());
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if(e==null){
                            if(list.size()==0){
                                showToast("暂无数据");
                                mTotalWage.setText("暂无信息");
                                mRealWage.setText("暂无信息");
                                showEmpty(true);
                            }else{
                                mAdapter.setDataSource(list.get(0));
                                mAdapter.notifyDataSetChanged();
                                mTotalWage.setText(String.valueOf(mAdapter.getTotalWage())+"¥");
                                mRealWage.setText(String.valueOf(mAdapter.getRealWage())+"¥");
                                showEmpty(false);
                                showToast("获取数据成功");
                            }
                        }else{
                            onError(e.getMessage());
                        }
                    }
                });
            }
        }else{
            mTotalWage.setText("暂无信息");
            mRealWage.setText("暂无信息");
            showEmpty(true);
            showToast(NetworkUtil.tip);
        }
    }

    public void showEmpty(boolean isShow){
        if(isShow){
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else{
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void showCalendar(){
        View view=getLayoutInflater().inflate(R.layout.wage_calendar_popup,null);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.WageActivity_calendar_popup_RecyclerView);
        ImageView left= (ImageView) view.findViewById(R.id.Calendar_Popup_LeftBtn);
        ImageView right= (ImageView) view.findViewById(R.id.Calendar_Popup_RightBtn);
        final TextView year= (TextView) view.findViewById(R.id.Calendar_Popup_Year);
        year.setText(mYears.get(mCurrentYear));

        recyclerView.setLayoutManager(new GridLayoutManager(this,6));
        final CalendarAdapter adapter=new CalendarAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setCurrent(mCurrentMonth);
        adapter.notifyDataSetChanged();
        adapter.setListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                mCurrentMonth=position;
                hideBasePopup();
                mTime.setText(String.format("%s年%d月",mYears.get(mCurrentYear),position+1));
                getData();
            }
        });
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.Calendar_Popup_LeftBtn:
                        if(mCurrentYear>0){
                            mCurrentYear--;
                            year.setText(mYears.get(mCurrentYear));
                        }
                        break;
                    case R.id.Calendar_Popup_RightBtn:
                        if(mCurrentYear<mYears.size()-1){
                            mCurrentYear++;
                            year.setText(mYears.get(mCurrentYear));
                        }
                        break;
                }
            }
        };
        left.setOnClickListener(onClickListener);
        right.setOnClickListener(onClickListener);
//        showBasePopup(view,mToolbar, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//                , Gravity.TOP,0,0);
        mBasePopup=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        setWindowAlpha(0.5F);
        mBasePopup.setFocusable(false);
        mBasePopup.setOutsideTouchable(false);
        mBasePopup.showAsDropDown(mToolbar);
        mBasePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(1.0f);
            }
        });
    }
}
