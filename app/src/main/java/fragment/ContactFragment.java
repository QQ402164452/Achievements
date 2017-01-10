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
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;

import adapter.ContactAdapter;
import adapter.RealmContactAdapter;
import bean.RealmUser;
import customView.DividerItemDecoration;
import customView.LetterView;
import interfaces.Icontact;
import interfaces.OnItemClickListener;
import io.realm.RealmResults;
import presenter.Pcontact;
import utils.PinyinUtil;
import view.IMActivity;
import view.MainActivity;

/**
 * Created by Jason on 2016/11/23.
 */

public class ContactFragment extends BaseFragment implements Icontact {
    private XRecyclerView mRecyclerView;
    private MainActivity mParent;
    private LetterView mLetterView;
    private RealmContactAdapter mAdapter;
    private Pcontact mPresenter;
    private RealmResults<RealmUser> mUserList;
    private HashMap<Character,Integer> mLetterMap;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParent= (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view=layoutInflater.inflate(R.layout.fragment_contact,container,false);
        init(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        mLetterMap=new HashMap<>();
        mPresenter=new Pcontact(this);
        mPresenter.getCacheMsg();
    }

    @Override
    public void initListener() {
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.getNewMessage();
            }

            @Override
            public void onLoadMore() {

            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mParent, IMActivity.class);
                intent.putExtra(IMActivity.IM_TO_USER,mUserList.get(position).getId());
                startActivity(intent);
            }
        });
        mLetterView.setListener(new LetterView.LetterOnClickListener() {
            @Override
            public void onItemClickListener(int position, char letter) {
                if(mLetterMap!=null){
                    if(mLetterMap.get(letter)!=null){
                        if(mRecyclerView.getScrollState()==RecyclerView.SCROLL_STATE_IDLE){
                            mRecyclerView.smoothScrollToPosition(mLetterMap.get(letter));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void initView(View view) {
        mRecyclerView= (XRecyclerView) view.findViewById(R.id.ContactFragment_RecyclerView);
        mLetterView= (LetterView) view.findViewById(R.id.ContactFragment_LetterView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mParent));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mParent,DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLoadingMoreEnabled(false);
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(mParent,str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCacheUserList(RealmResults<RealmUser> list) {
        this.mUserList=list;
        mAdapter=new RealmContactAdapter(mParent,mUserList,false);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getLetterMap(mUserList);
    }

    @Override
    public void setLetterMap(HashMap<Character, Integer> letters) {
        mLetterMap.clear();
        mLetterMap.putAll(letters);
    }

    @Override
    public void updateUserDate(RealmResults<RealmUser> list) {
        this.mUserList=list;
        mAdapter.updateData(list);
        mPresenter.getLetterMap(mUserList);
        mRecyclerView.refreshComplete();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
