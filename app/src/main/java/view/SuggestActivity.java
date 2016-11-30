package view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.ChatAdapter;
import bean.ChatBean;
import interfaces.Isuggest;
import presenter.Psuggest;

/**
 * Created by Jason on 2016/11/29.
 */

public class SuggestActivity extends BaseActivity implements Isuggest,View.OnLayoutChangeListener {
    private Button mSend;
    private EditText mInput;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private Psuggest mPresenter;
    private RelativeLayout mRootView;
    private boolean isFirstStart=true;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_suggest);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.SuggestActivity_toolbar);
        setCustomToolbar(toolbar);

        mSend= (Button) findViewById(R.id.SuggestActivity_send);
        mInput= (EditText) findViewById(R.id.SuggestActivity_input);
        mRecyclerView= (RecyclerView) findViewById(R.id.SuggestActivity_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRootView= (RelativeLayout) findViewById(R.id.SuggestActivity_rootView);
    }

    @Override
    public void initListener() {
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addListData(mInput.getText().toString());
                mInput.setText("");
            }
        });
        mRootView.addOnLayoutChangeListener(this);
    }

    @Override
    public void initData() {
        mPresenter=new Psuggest(this);
        mAdapter=new ChatAdapter(this);
        mPresenter.getListData();
    }

    @Override
    public void setRecyclerView(ArrayList<ChatBean> data) {
        mAdapter.setSource(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void notifyRecyclerView() {
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        isFirstStart=false;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(!isFirstStart){
            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
        }
    }
}
