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

import adapter.ReportAdapter;
import interfaces.Irecy;
import interfaces.OnCustomItemClickListener;
import utils.NetworkUtil;
import utils.WeakHandler;
import view.ReportDetailActivity;

/**
 * Created by Jason on 2016/12/2.
 */

public class MyReportFragment extends LazyFragment  {
    private XRecyclerView mRecyclerView;
    private View mEmptyView;
    private ReportAdapter mAdapter;
    private List<AVObject> mList;

    private WeakHandler mHandler;
    private int mSkip=0;

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
        View view=inflater.inflate(R.layout.fragment_my_report,parent,false);
        init(view);
        isPrepared=true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if(isVisible&&isPrepared&&isFirst){
            mSkip=0;
            getData();
            isFirst=false;
        }
    }

    public void getData(){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("report");
            query.whereEqualTo("reporter", AVUser.getCurrentUser());
            query.orderByDescending("createdAt");
            query.include("reporter");// 关键代码，用 include 告知服务端需要返回的关联属性对应的对象的详细信息，而不仅仅是 objectId
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

                    }else{
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(),NetworkUtil.tip,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initView(View view) {
        mRecyclerView= (XRecyclerView) view.findViewById(R.id.MyReportFragment_recyclerview);
        mEmptyView=view.findViewById(R.id.MyReportFragment_empty_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setEmptyView(mEmptyView);
        mAdapter=new ReportAdapter(getActivity(),mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mAdapter.setClickListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent=new Intent(getActivity(), ReportDetailActivity.class);
                String serializedString=mList.get(position).toString();
                intent.putExtra("REPORT_DETAIL_STRING",serializedString);
                startActivity(intent);
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
}
