package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.jason.achievements.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.MyTaskAdapter;
import interfaces.ItodoTask;
import interfaces.OnCustomItemClickListener;
import presenter.PtodoTask;
import view.DoneTaskActivity;
import view.TaskActivity;
import view.TaskDetailActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jason on 2016/12/9.
 */

public class TodoFragment extends LazyFragment implements ItodoTask{
    private TaskActivity mParent;
    private TextView mTodoCount;
    private TextView mDoneCount;
    private TextView mTime;
    private RecyclerView mRecy;
    private TextView mDoneBtn;
    private MyTaskAdapter mAdapter;
    private List<AVObject> mList;

    private PtodoTask mPresenter;

    public static final String TASK_DETAIL_SERIZALIZE="TASK_DETAIL_SERIZALIZE";
    public static final int TASK_DETAIL_RESULT_UPDATE=311;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParent= (TaskActivity) getActivity();
        mPresenter=new PtodoTask(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_todo,parent,false);
        init(view);
        isPrepared=true;
        lazyLoad();
        return view;
    }


    @Override
    public void lazyLoad() {
        if(isVisible&&isPrepared&&isFirst){
            mPresenter.getTodoList();
            mPresenter.getDoneList();
            isFirst=false;
        }
    }

    @Override
    protected void initView(View view) {
        mTodoCount= (TextView) view.findViewById(R.id.TodoFragment_count_todo);
        mDoneCount= (TextView) view.findViewById(R.id.TodoFragment_count_done);
        mRecy= (RecyclerView) view.findViewById(R.id.TodoFragment_recyclerView);
        mDoneBtn= (TextView) view.findViewById(R.id.TodoFragment_btn_done);
        mTime= (TextView) view.findViewById(R.id.TodoFragment_time);
        SimpleDateFormat format=new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        mTime.setText(format.format(new Date()));
        setCount(mTodoCount,"0",R.color.colorAccent);
        setCount(mDoneCount,"0",R.color.green_light);
        mRecy.setLayoutManager(new LinearLayoutManager(mParent));
        mAdapter=new MyTaskAdapter(mParent);
        mRecy.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mParent, DoneTaskActivity.class);
                startActivity(intent);
            }
        });
        mAdapter.setListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent=new Intent(mParent, TaskDetailActivity.class);
                intent.putExtra(TASK_DETAIL_SERIZALIZE,mList.get(position).toString());
                startActivityForResult(intent,TASK_DETAIL_RESULT_UPDATE);
            }
        });
    }

    public void setCount(TextView view,String count,int color){//SpannableString设置字体大小 和 颜色
        SpannableString spannableString=new SpannableString(count+" 个");
        spannableString.setSpan(new RelativeSizeSpan(2.0f),0,count.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(color)),0,count.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannableString);
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
    public void onSuccess(List<AVObject> list,int type) {//type 0-待办 1-已完成
        switch (type){
            case 0:
                mList=list;
                setCount(mTodoCount,String.valueOf(list.size()),R.color.colorAccent);
                mAdapter.setDataSource(list);
                mAdapter.notifyDataSetChanged();
                break;
            case 1:
                setCount(mDoneCount,String.valueOf(list.size()),R.color.green_light);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestId,int resultId,Intent data){
        super.onActivityResult(requestId,resultId,data);
//        Log.e("TADO","onActivityResult"+requestId);
//        if(resultId==RESULT_OK){
//            mPresenter.getTodoList();
//        }
    }
}
