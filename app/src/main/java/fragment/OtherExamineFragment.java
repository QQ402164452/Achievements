package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.jason.achievements.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapter.OtherExamineAdapter;
import interfaces.WeakObject;
import interfaces.OnCustomItemClickListener;
import utils.NetworkUtil;
import utils.WeakHandler;
import view.PendingActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jason on 2016/12/6.
 */

public class OtherExamineFragment extends LazyFragment implements WeakObject {
    private XRecyclerView mRecyclerView;
    private View mEmptyView;
    private OtherExamineAdapter mAdapter;
    private List<AVObject> mList;

    private WeakHandler mHandler;
    private int mSkip=0;

    public static int OTHER_EXAMINE_REQUEST_CODE=220;
    public static String OTHER_EXAMINE_BACK="OTHER_EXAMINE_BACK";
    public static String OTHER_EXAMINE_PASS="OTHER_EXAMINE_PASS";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mList=new ArrayList<>();
        mHandler=new WeakHandler(this);
    }

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
            mSkip=0;
            getData();
            isFirst=false;
        }
    }

    public void showToast(String str){
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }

    public void getData(){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("approval");
            query.orderByDescending("createdAt");
            query.whereEqualTo("approver", AVUser.getCurrentUser());
            query.whereContainedIn("sign", Arrays.asList(0,3));
            query.include("approver");
            query.include("user");
            query.limit(10);
            query.skip(mSkip);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        if(mSkip==0){
                            mRecyclerView.setNoMore(false);
                            mRecyclerView.refreshComplete();
                            mList.clear();
                            mList.addAll(list);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollToPosition(0);
                            mSkip=mList.size();
                        }else{
                            mRecyclerView.loadMoreComplete();
                            if(list.size()>0){
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                                mSkip=mList.size();
                            }else{
                                mRecyclerView.setNoMore(true);
                            }
                        }
                    }else{
                        showToast(e.getMessage());
                    }

                }
            });
        }else{
            showToast(NetworkUtil.tip);
        }

    }

    @Override
    protected void initView(View view) {
        mRecyclerView= (XRecyclerView) view.findViewById(R.id.OtherExamineFragment_RecycleView);
        mEmptyView=view.findViewById(R.id.OtherExamineFragment_empty_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setEmptyView(mEmptyView);
        mAdapter=new OtherExamineAdapter(getActivity(),mList);
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
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
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
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            int position=data.getIntExtra("Pending_Position",0);
            mList.remove(position);
            mAdapter.notifyItemRemoved(position);
            mAdapter.notifyItemRangeChanged(position,mList.size()-position+1,1);
        }
    }

    @Override
    public void doLoadData(int code) {
        switch (code){
            case 0:
                mSkip=0;
                getData();
                break;
            case 1:
                getData();
                break;
        }
    }
}
