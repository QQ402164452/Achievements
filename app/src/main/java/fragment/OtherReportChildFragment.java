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
import java.util.List;

import adapter.ReportAdapter;
import interfaces.WeakObject;
import interfaces.OnCustomItemClickListener;
import utils.NetworkUtil;
import utils.WeakHandler;
import view.ReportDetailActivity;

/**
 * Created by Jason on 2016/12/4.
 */

public class OtherReportChildFragment extends BaseFragment implements WeakObject {
    private XRecyclerView mRecyclerView;
    private View mEmptyView;
    private ReportAdapter mAdapter;
    private List<AVObject> mList;
    private int mType;
    private WeakHandler mHandler;
    private int mSkip=0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mType= (int) getArguments().getSerializable("type");
        mList=new ArrayList<>();
        mHandler=new WeakHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_other_report_child,parent,false);
        init(view);
        return view;
    }

    @Override
    public void initView(View view) {
        mEmptyView=view.findViewById(R.id.OtherFragment_child_empty_view);
        mRecyclerView= (XRecyclerView) view.findViewById(R.id.OtherFragment_child_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setEmptyView(mEmptyView);
        mAdapter=new ReportAdapter(getActivity(),mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        switch (mType){
            case 0:
                setAdapterData(0);
                break;
            case 1:
                setAdapterData(1);
                break;
            case 2:
                setAdapterData(2);
                break;
        }
    }

    @Override
    public void initListener() {
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

    public void setAdapterData(final int type){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("report");
            query.whereEqualTo("approver", AVUser.getCurrentUser());
            query.whereEqualTo("type",type);
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


    public static OtherReportChildFragment newInstance(int type){
        Bundle bundle=new Bundle();
        bundle.putSerializable("type",type);
        OtherReportChildFragment fragment=new OtherReportChildFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void doLoadData(int code) {
        switch (code){
            case 0:
                mSkip=0;
                initData();
                break;
            case 1:
                initData();
                break;
        }
    }
}
