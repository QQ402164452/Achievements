package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.AppAdapter;
import bean.AppBean;
import customView.DividerGridItemDecoration;
import interfaces.OnCustomItemClickListener;
import view.ReportActivity;
import view.SignActivity;

/**
 * Created by Jason on 2016/11/23.
 */

public class AppFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private AppAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_app,viewGroup,false);
        init(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {
        ArrayList<AppBean> list=new ArrayList<>();
        list.add(new AppBean(R.drawable.content_icon_sign_def,"移动签到"));
        list.add(new AppBean(R.drawable.content_icon_mycheck_def,"我的考勤"));
        list.add(new AppBean(R.drawable.content_icon_examine_def,"审批查询"));
        list.add(new AppBean(R.drawable.content_icon_check_def,"假期申请"));
        list.add(new AppBean(R.drawable.content_icon_work_report_def,"工作汇报"));
        list.add(new AppBean(R.drawable.content_icon_mission_manager_def,"任务"));
        list.add(new AppBean(R.drawable.content_icon_wadge_def,"工资条"));
        list.add(new AppBean(R.drawable.user_add_more,"更多"));
        mAdapter=new AppAdapter(getActivity(),list);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mAdapter.setListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent;
                switch (position){
                    case 0:
                        intent=new Intent(getActivity(), SignActivity.class);
                        startActivity(intent);
                        break;
                    case 1:

                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        intent=new Intent(getActivity(), ReportActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        showToast("暂无更多");
                        break;
                }
            }
        });
    }

    public void showToast(String str){
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initView(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.AppFragment_recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
    }
}
