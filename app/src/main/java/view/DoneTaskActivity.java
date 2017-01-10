package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.jason.achievements.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import adapter.MyTaskAdapter;
import adapter.MyTaskSelAdapter;
import customView.DividerItemDecoration;
import customView.DividerItemExceptLastDecoration;
import fragment.TodoFragment;
import interfaces.IdoneTask;
import interfaces.WeakObject;
import interfaces.OnCustomItemClickListener;
import presenter.PdoneTask;
import utils.WeakHandler;

/**
 * Created by Jason on 2016/12/15.
 */

public class DoneTaskActivity extends OtherBaseActivity implements IdoneTask{
    private TextView mTime;
    private Toolbar mToolbar;
    private View mMask;
    private int mCurTime=0;
    private ArrayList<String> mTimes;
    private View mEmptyView;
    private RelativeLayout mTop;
    private int year=-1;
    private int month=-1;
    private int week=-1;

    private PdoneTask mPresenter;
    private XRecyclerView mRecy;
    private MyTaskAdapter mAdapter;
    private List<AVObject> mList;

    private int mSkip=0;
    private WeakHandler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_done_task);
        mTimes=new ArrayList<>(Arrays.asList("全部","本周","上周","本月","上月"));
        mList=new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mToolbar= (Toolbar) findViewById(R.id.DoneTaskActivity_toolbar);
        setCustomToolbar(mToolbar);

        mTime= (TextView) findViewById(R.id.DoneTaskActivity_btn_time);
        mMask=findViewById(R.id.DoneTaskActivity_mask);
        mTop= (RelativeLayout) findViewById(R.id.DoneTaskActivity_top);
        mRecy= (XRecyclerView) findViewById(R.id.DoneTaskActivity_recyclerView);
        mEmptyView=findViewById(R.id.DoneTaskActivity_empty_view);
        mRecy.setLayoutManager(new LinearLayoutManager(this));
        mRecy.setEmptyView(mEmptyView);
    }

    @Override
    public void initListener() {
        mHandler=new WeakHandler(new WeakObject() {
            @Override
            public void doLoadData(int type) {
                switch (type){
                    case 0:
                        mSkip=0;
                        mPresenter.getData(year,month,week,mSkip);
                        break;
                    case 1:
                        mPresenter.getData(year,month,week,mSkip);
                        break;
                }
            }
        });
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected()){
                    v.setSelected(false);
                    hideBasePopup();
                }else{
                    v.setSelected(true);
                    showSelectPopup(mTime);
                }
            }
        });
        mAdapter.setListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent=new Intent(DoneTaskActivity.this,TaskDetailActivity.class);
                intent.putExtra(TodoFragment.TASK_DETAIL_SERIZALIZE,mList.get(position).toString());
                startActivity(intent);
            }
        });
        mRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(0,1000);
            }

            @Override
            public void onLoadMore() {
                mHandler.sendEmptyMessageDelayed(1,1000);
            }
        });
    }

    @Override
    public void initData() {
        mPresenter=new PdoneTask(this);
        mAdapter=new MyTaskAdapter(this,mList);
        mRecy.setAdapter(mAdapter);
        mPresenter.getData(year,month,week,mSkip);
    }

    public void showSelectPopup(final TextView btn){
        hideBasePopup();
        View view=getLayoutInflater().inflate(R.layout.my_task_fragment_select_popup,null);
        RecyclerView mSelRecy= (RecyclerView) view.findViewById(R.id.MyTaskFragment_select_popup_recyclerView);
        mSelRecy.setLayoutManager(new LinearLayoutManager(this));
        mSelRecy.addItemDecoration(new DividerItemExceptLastDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        final MyTaskSelAdapter mAdapter=new MyTaskSelAdapter(this);
        mAdapter.setListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                hideBasePopup();
                mCurTime=position;
                mTime.setText(mTimes.get(mCurTime));
                Calendar calendar=Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=-1;
                week=-1;
                switch (mCurTime){
                    case 1:
                        week=calendar.get(Calendar.WEEK_OF_YEAR);
                        break;
                    case 2:
                        week=calendar.get(Calendar.WEEK_OF_YEAR)-1;
                        break;
                    case 3:
                        month=calendar.get(Calendar.MONTH)+1;
                        break;
                    case 4:
                        month=calendar.get(Calendar.MONTH);
                        break;

                }
                mSkip=0;
                mPresenter.getData(year,month,week,mSkip);
            }
        });
        mAdapter.setDataSource(mTimes);
        mAdapter.setCurrent(mCurTime);
        mSelRecy.setAdapter(mAdapter);
        if(mBasePopup==null||!mBasePopup.isShowing()){
            mBasePopup=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
            mBasePopup.setFocusable(false);
            mBasePopup.setOutsideTouchable(false);
            mBasePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mMask.setVisibility(View.GONE);
                    btn.setSelected(false);
                }
            });
            mMask.setVisibility(View.VISIBLE);
            mBasePopup.showAsDropDown(mTop);
        }
    }

    @Override
    public void setListData(List<AVObject> list) {
        if(mSkip==0){
            mRecy.setNoMore(false);
            mRecy.refreshComplete();
            mList.clear();
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
            mRecy.scrollToPosition(0);
            mSkip=mList.size();
        }else{
            mRecy.loadMoreComplete();
            if(list.size()>0){
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
                mSkip=mList.size();
            }else{
                mRecy.setNoMore(true);
            }
        }
    }

    @Override
    public void onSuccess(String str) {

    }
}
