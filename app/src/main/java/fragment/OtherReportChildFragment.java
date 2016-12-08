package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.jason.achievements.R;

import java.util.List;

import adapter.ReportAdapter;
import bean.ErrorBean;
import interfaces.OnCustomItemClickListener;
import utils.NetworkUtil;
import view.ReportDetailActivity;

/**
 * Created by Jason on 2016/12/4.
 */

public class OtherReportChildFragment extends LazyFragment {
    private RecyclerView mRecyclerView;
    private ReportAdapter mAdapter;
    private List<AVObject> mList;
    private int mType;
    private RelativeLayout mEmptyView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mType= (int) getArguments().getSerializable("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_other_report_child,parent,false);
        init(view);
        isPrepared=true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if(isPrepared&&isVisible&&isFirst){
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
    }

    @Override
    public void initView(View view) {
        mEmptyView= (RelativeLayout) view.findViewById(R.id.OtherFragment_child_emptyView);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.OtherFragment_child_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new ReportAdapter(getActivity());
        mAdapter.setClickListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent=new Intent(getActivity(), ReportDetailActivity.class);
                String serializedString=mList.get(position).toString();
                intent.putExtra("REPORT_DETAIL_STRING",serializedString);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {

    }

    public void setAdapterData(final int type){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("report");
            query.whereEqualTo("approver", AVUser.getCurrentUser());
            query.whereEqualTo("type",type);
            query.orderByDescending("createdAt");
            query.include("reporter");// 关键代码，用 include 告知服务端需要返回的关联属性对应的对象的详细信息，而不仅仅是 objectId
            query.include("approver");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        mList=list;
                        mAdapter.setDataSource(list);
                        mAdapter.notifyDataSetChanged();
                        isFirst=false;
                        if(mList.size()==0){
                            showEmptyView(true);
                        }else{
                            showEmptyView(false);
                        }
                    }else{
                        ErrorBean error= JSON.parseObject(e.getMessage(),ErrorBean.class);
                        Toast.makeText(getActivity(),error.getError(),Toast.LENGTH_SHORT).show();
                        showEmptyView(true);
                    }
                }
            });
        }else {
            showEmptyView(true);
            Toast.makeText(getActivity(),NetworkUtil.tip,Toast.LENGTH_SHORT).show();
        }
    }

    public void showEmptyView(boolean isShow){
        if(isShow){
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else{
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public static OtherReportChildFragment newInstance(int type){
        Bundle bundle=new Bundle();
        bundle.putSerializable("type",type);
        OtherReportChildFragment fragment=new OtherReportChildFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
