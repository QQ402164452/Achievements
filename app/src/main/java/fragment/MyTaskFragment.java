package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import interfaces.ImyTask;
import interfaces.OnCustomItemClickListener;
import presenter.PmyTask;
import view.TaskActivity;
import view.TaskDetailActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jason on 2016/12/9.
 */

public class MyTaskFragment extends LazyFragment implements ImyTask {
    private TaskActivity mParent;
    private RecyclerView mRecycler;
    private MyTaskAdapter mAdapter;
    private View mEmptyView;
    private TextView mRadTime;
    private TextView mRadType;
    private LinearLayout mTop;
    private View mMask;
    private ArrayList<String> mTimes;
    private ArrayList<String> mTypes;

    private int mCurTime=0;
    private int mCurType=0;
    private int year=-1;
    private int month=-1;
    private int week=-1;

    private PmyTask mPresenter;
    private List<AVObject> mList;

    public final static String TASK_DETAIL_SERIZALIZE="TASK_DETAIL_SERIZALIZE";
    public final static int TASK_DETAIL_RESULT_UPDATE=302;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParent= (TaskActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mTimes=new ArrayList<>(Arrays.asList("全部","本周","上周","本月","上月"));
        mTypes=new ArrayList<>(Arrays.asList("全部","未完成","已完成"));
        mPresenter=new PmyTask(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_mytask,parent,false);
        init(view);
        isPrepared=true;
        lazyLoad();
        return view;
    }

    @Override
    public void lazyLoad() {
        if(isVisible&&isPrepared&&isFirst){
            mPresenter.getData(year,month,week,mCurType);
            isFirst=false;
        }
    }

    @Override
    protected void initView(View view) {
        mRadType= (TextView) view.findViewById(R.id.MyTaskFragment_btn_type);
        mRadTime= (TextView) view.findViewById(R.id.MyTaskFragment_btn_time);
        mRecycler= (RecyclerView) view.findViewById(R.id.MyTaskFragment_recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mParent);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        mTop= (LinearLayout) view.findViewById(R.id.MyTaskFragment_top);
        mEmptyView=view.findViewById(R.id.MyTaskFragment_empty_view);
        mMask=view.findViewById(R.id.MyTaskFragment_mask);
        mMask.setVisibility(View.GONE);

        mAdapter=new MyTaskAdapter(mParent);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mRadTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.isSelected()){
                    mRadTime.setSelected(true);
                    showSelectPopup(mRadTime,1);
                }else{
                    v.setSelected(false);
                    mMask.setVisibility(View.GONE);
                    mParent.hideBasePopup();
                }
            }
        });
        mRadType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.isSelected()){
                    mRadType.setSelected(true);
                    showSelectPopup(mRadType,0);
                }else{
                    v.setSelected(false);
                    mMask.setVisibility(View.GONE);
                    mParent.hideBasePopup();
                }
            }
        });
        mAdapter.setListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                mParent.hideBasePopup();
                Intent intent=new Intent(mParent, TaskDetailActivity.class);
                intent.putExtra(TASK_DETAIL_SERIZALIZE,mList.get(position).toString());
                startActivityForResult(intent,TASK_DETAIL_RESULT_UPDATE);
            }
        });
    }

    public void showSelectPopup(final TextView btn, final int type){
        mParent.hideBasePopup();
        View view=mParent.getLayoutInflater().inflate(R.layout.my_task_fragment_select_popup,null);
        RecyclerView mSelRecy= (RecyclerView) view.findViewById(R.id.MyTaskFragment_select_popup_recyclerView);
        mSelRecy.setLayoutManager(new LinearLayoutManager(mParent));
        mSelRecy.addItemDecoration(new DividerItemExceptLastDecoration(mParent,DividerItemDecoration.VERTICAL_LIST));
        final MyTaskSelAdapter mAdapter=new MyTaskSelAdapter(mParent);
        mAdapter.setListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                switch (type){
                    case 0:
                        mCurType=position;
                        mRadType.setText(mTypes.get(mCurType));
                        break;
                    case 1:
                        mCurTime=position;
                        mRadTime.setText(mTimes.get(mCurTime));
                        break;
                }
                mParent.mBasePopup.dismiss();
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
                mPresenter.getData(year,month,week,mCurType);
            }
        });
        switch (type){
            case 0:
                mAdapter.setDataSource(mTypes);
                mAdapter.setCurrent(mCurType);
                break;
            case 1:
                mAdapter.setDataSource(mTimes);
                mAdapter.setCurrent(mCurTime);
                break;
        }
        mSelRecy.setAdapter(mAdapter);
        if(mParent.mBasePopup==null||!mParent.mBasePopup.isShowing()){
            mParent.mBasePopup=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
            mParent.mBasePopup.setFocusable(false);
            mParent.mBasePopup.setOutsideTouchable(false);
            mParent.mBasePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mMask.setVisibility(View.GONE);
                    btn.setSelected(false);
                }
            });
            mMask.setVisibility(View.VISIBLE);
            mParent.mBasePopup.showAsDropDown(mTop);
        }
    }

    @Override
    public void showToast(String str) {
        mParent.showToast(str);
    }

    @Override
    public void onError(String error) {
        mParent.onError(error);
    }

    @Override
    public void onSuccess(List<AVObject> list) {
        if(list.size()>0){
            mEmptyView.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
            mList=list;
            mAdapter.setDataSource(list);
            mAdapter.notifyDataSetChanged();
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestId,int resultId,Intent data){
        super.onActivityResult(requestId,resultId,data);
//        Log.e("MyTAKK","onActivityResult"+requestId);
//        if(resultId==RESULT_OK){
//            mPresenter.getData(year,month,week,mCurType);
//        }
    }
}
