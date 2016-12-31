package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.MyExamineAdapter;
import interfaces.Irecy;
import interfaces.OnCustomItemClickListener;
import utils.NetworkUtil;
import utils.WeakHandler;
import view.ExamineDetailActivity;

/**
 * Created by Jason on 2016/12/6.
 */

public class MyExamineFragment extends LazyFragment{
    private XRecyclerView mRecyclerView;
    private MyExamineAdapter mAdapter;
    private List<AVObject> mList;
    private View mEmptyView;

    private WeakHandler mHandler;
    private int mSkip=0;

    public static int MY_EXAMINE_REQUEST_CODE=210;
    public static String MY_EXAMINE_UPDATE="MY_EXAMINE_UPDATE";
    public static String MY_EXAMINE_DELETE="MY_EXAMINE_DELETE";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mList=new ArrayList<>();
        mHandler=new WeakHandler(new Irecy() {
            @Override
            public void doLoadData(int type) {
                switch (type){
                    case 0:
                        mSkip=0;
                        getData();
                        break;
                    case 1:
                        getData();
                        break;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_my_examine,parent,false);
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

    public void getData(){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("approval");
            query.orderByDescending("createdAt");
            query.whereEqualTo("user", AVUser.getCurrentUser());
            query.include("user");
            query.include("approver");
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
                    }else {
                        showToast(e.getMessage());
                    }
                }
            });
        }else{
            Toast.makeText(getActivity(),NetworkUtil.tip,Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(String str){
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initView(View view) {
        mEmptyView=view.findViewById(R.id.MyExamineFragment_empty_view);
        mRecyclerView= (XRecyclerView) view.findViewById(R.id.MyExamineFragment_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setEmptyView(mEmptyView);
        mAdapter=new MyExamineAdapter(getActivity(),mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mAdapter.setListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent=new Intent(getActivity(), ExamineDetailActivity.class);
                String object=mList.get(position).toString();
                intent.putExtra("MyExamineDetail",object);
                intent.putExtra("MyExamineDetail_Position",position);
                startActivityForResult(intent,MY_EXAMINE_REQUEST_CODE);
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
        int position;
        switch (resultCode){
            case 211:
                position=data.getIntExtra(MY_EXAMINE_UPDATE,0);
                mAdapter.notifyItemChanged(position,0);
                break;
            case 212:
                position=data.getIntExtra(MY_EXAMINE_DELETE,0);
                mList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position,mList.size()-position+1,1);//只刷新删除位置之后的所有元素
                break;
        }

    }

}
