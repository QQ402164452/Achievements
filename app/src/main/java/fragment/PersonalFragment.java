package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

import java.util.Collections;
import java.util.Comparator;

import adapter.PersonalAdapter;
import bean.RealmConversation;
import customView.DividerItemDecoration;
import im.NotificationUtil;
import interfaces.OnItemClickListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import view.IMActivity;
import view.MainActivity;

/**
 * Created by Jason on 2016/11/23.
 */

public class PersonalFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private MainActivity mParent;
    private PersonalAdapter mAdapter;
    private Realm realm;
    private RealmResults<RealmConversation> results;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParent= (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view=layoutInflater.inflate(R.layout.fragment_personal,container,false);
        init(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        realm=Realm.getDefaultInstance();
    }

    @Override
    public void initData() {
        results=realm.where(RealmConversation.class).
                equalTo("isHaveMsg",true).
                equalTo("creator",AVUser.getCurrentUser().getObjectId()).
                findAll().
                sort("time", Sort.DESCENDING);
        mAdapter=new PersonalAdapter(mParent,results,true);
        mRecyclerView.setAdapter(mAdapter);
        NotificationUtil.addAllTag(results);
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mParent, IMActivity.class);
                intent.putExtra(IMActivity.IM_TO_USER,results.get(position).getToTarget().getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initView(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.PersonalFragment_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mParent));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mParent,DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(results!=null){
            if(hidden){
                NotificationUtil.removeAllTag(results);
            }else{
                NotificationUtil.addAllTag(results);
            }
        }
    }

}
