package view;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.jason.achievements.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.IMAdapter;
import adapter.RealmIMAdapter;
import bean.RealmMessage;
import bean.RealmUser;
import im.AVIMClientManager;
import im.event.ImTypeMessageEvent;
import interfaces.Iim;
import io.realm.Realm;
import io.realm.RealmList;
import presenter.Pim;

/**
 * Created by Jason on 2017/1/3.
 */

public class IMActivity extends BaseActivity implements Iim, View.OnLayoutChangeListener {
    private TextView mTitle;
    private Button mSend;
    private EditText mInput;
    private RecyclerView mRecyclerView;
    private RelativeLayout mRootView;
    private RealmUser toUser;
    private RealmIMAdapter mAdapter;
    private Pim mPresenter;
    private RealmList<RealmMessage> mMsgList;

    private boolean isFirstStart = true;
    public static final String IM_TO_USER = "IM_TO_USER";

    @Override
    protected void initPre() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_im);
        Toolbar toolbar = (Toolbar) findViewById(R.id.IMActivity_toolbar);
        setCustomToolbar(toolbar);

        mRootView = (RelativeLayout) findViewById(R.id.IMActivity_rootView);
        mTitle = (TextView) findViewById(R.id.IMActivity_Title);
        mSend = (Button) findViewById(R.id.IMActivity_send);
        mInput = (EditText) findViewById(R.id.IMActivity_input);
        mRecyclerView = (RecyclerView) findViewById(R.id.IMActivity_RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        mPresenter = new Pim(this);
        Intent intent = getIntent();
        if (intent != null) {
            toUser=mPresenter.getToUser(intent.getStringExtra(IM_TO_USER));
            mTitle.setText(toUser.getName());
            mPresenter.createConversation(AVUser.getCurrentUser(), toUser);
        }
    }

    @Override
    protected void initListener() {
        mRootView.addOnLayoutChangeListener(this);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.sendMessage(mInput.getText().toString());
            }
        });
    }

    @Override
    public void onSendSuccess(AVIMTextMessage msg) {
        mAdapter.notifyDataSetChanged();
        scrollToBottom();
        mInput.setText("");
    }

    @Override
    public void setNewMsgList(RealmList<RealmMessage> newMsgList) {
        this.mMsgList=newMsgList;
        mAdapter = new RealmIMAdapter(this,mMsgList,false,toUser);
        mRecyclerView.setAdapter(mAdapter);
        scrollToBottom();
    }

    @Override
    public void setCacheMsgList(RealmList<RealmMessage> realmMessages) {
        this.mMsgList=realmMessages;
        mAdapter = new RealmIMAdapter(this,mMsgList,true,toUser);
        mRecyclerView.setAdapter(mAdapter);
        scrollToBottom();
    }

    @Override
    public void onSuccess(String str) {

    }

    @Override
    public void onResume() {
        super.onResume();
        isFirstStart = false;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        mPresenter.onDestroy();
    }

    /**
     * 处理推送过来的消息
     * 同理，避免无效消息，此处加了 conversation id 判断
     */
    @Subscribe
    public void onEventMessage(ImTypeMessageEvent event) {
        if (mPresenter.getConversation() != null && event != null &&
                mPresenter.getConversation().getConversationId().equals(event.conversation.getConversationId())) {
            mAdapter.notifyDataSetChanged();
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);//减1就成功了
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (!isFirstStart && bottom < oldBottom) {
            scrollToBottom();
        }
    }
}
