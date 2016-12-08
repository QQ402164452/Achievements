package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.jason.achievements.R;

import java.util.Arrays;
import java.util.List;

import adapter.OtherExamineAdapter;
import bean.ErrorBean;
import interfaces.OnCustomItemClickListener;
import utils.NetworkUtil;
import view.ExamineDetailActivity;
import view.PendingActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jason on 2016/12/6.
 */

public class OtherExamineFragment extends LazyFragment {
    private RecyclerView mRecyclerView;
    private OtherExamineAdapter mAdapter;
    private List<AVObject> mList;

    public static int OTHER_EXAMINE_REQUEST_CODE=220;
    public static String OTHER_EXAMINE_BACK="OTHER_EXAMINE_BACK";
    public static String OTHER_EXAMINE_PASS="OTHER_EXAMINE_PASS";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_other_examine,parent,false);
        init(view);
        isPrepared=true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if(isPrepared&&isVisible&&isFirst){
            if(NetworkUtil.isNewWorkAvailable()){
                getData();
                isFirst=false;
            }else{
                showToast(NetworkUtil.tip);
            }
        }
    }

    public void showToast(String str){
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }

    public void getData(){
        AVQuery<AVObject> query=new AVQuery<>("approval");
        query.orderByDescending("createdAt");
        query.whereEqualTo("approver", AVUser.getCurrentUser());
        query.whereContainedIn("sign", Arrays.asList(0,3));
        query.include("approver");
        query.include("user");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e==null){
                    mList=list;
                    mAdapter.setDataSource(mList);
                    mAdapter.notifyDataSetChanged();
                }else{
                    ErrorBean errorBean= JSON.parseObject(e.getMessage(),ErrorBean.class);
                    showToast(errorBean.getError());
                }
            }
        });
    }

    @Override
    protected void initView(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.OtherExamineFragment_RecycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new OtherExamineAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mAdapter.setListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent=new Intent(getActivity(), PendingActivity.class);
                String object=mList.get(position).toString();
                intent.putExtra("PendingDetail",object);
                intent.putExtra("PendingDetail_Position",position);
                startActivityForResult(intent,OTHER_EXAMINE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            int position=data.getIntExtra("Pending_Position",0);
            mList.remove(position);
            mAdapter.notifyItemRemoved(position);
            mAdapter.notifyItemRangeChanged(position,mList.size()-position+1,1);
        }
    }
}
