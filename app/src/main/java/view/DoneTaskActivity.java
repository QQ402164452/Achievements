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
import interfaces.OnCustomItemClickListener;
import presenter.PdoneTask;

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
    private RecyclerView mRecy;
    private MyTaskAdapter mAdapter;
    private List<AVObject> mList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_done_task);
        super.onCreate(savedInstanceState);
        mTimes=new ArrayList<>(Arrays.asList("全部","本周","上周","本月","上月"));
    }

    @Override
    public void initView() {
        mToolbar= (Toolbar) findViewById(R.id.DoneTaskActivity_toolbar);
        setCustomToolbar(mToolbar);

        mTime= (TextView) findViewById(R.id.DoneTaskActivity_btn_time);
        mMask=findViewById(R.id.DoneTaskActivity_mask);
        mTop= (RelativeLayout) findViewById(R.id.DoneTaskActivity_top);
        mRecy= (RecyclerView) findViewById(R.id.DoneTaskActivity_recyclerView);
        mEmptyView=findViewById(R.id.DoneTaskActivity_empty_view);
        mRecy.setLayoutManager(new LinearLayoutManager(this));
        mRecy.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
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
    }

    @Override
    public void initData() {
        mPresenter=new PdoneTask(this);
        mAdapter=new MyTaskAdapter(this);
        mRecy.setAdapter(mAdapter);
        mPresenter.getData(year,month,week);
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
                mPresenter.getData(year,month,week);
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
        if(list.size()>0){
            mList=list;
            mAdapter.setDataSource(list);
            mAdapter.notifyDataSetChanged();
            mRecy.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }else{
            mRecy.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(String str) {

    }
}
