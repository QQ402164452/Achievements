package view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapter.MyTaskSelAdapter;
import adapter.NewTaskParAdapter;
import adapter.TaskFbAdapter;
import customView.DividerItemDecoration;
import customView.DividerItemExceptLastDecoration;
import customView.GlideCircleTransform;
import fragment.MyTaskFragment;
import interfaces.ItaskDetail;
import presenter.PtaskDetail;

/**
 * Created by Jason on 2016/12/14.
 */

public class TaskDetailActivity extends BaseActivity implements ItaskDetail {
    private TextView mModify;
    private TextView mDone;
    private RecyclerView mParRecy;
    private RecyclerView mFeedBackRecy;
    private ImageView mUserImg;
    private TextView mUserName;
    private TextView mUserDep;
    private TextView mUserPos;
    private TextView mTaskTitle;
    private TextView mTaskDetail;
    private TextView mTaskTime;
    private TextView mTaskDeTime;
    private NewTaskParAdapter mParAdapter;
    private TaskFbAdapter mFbAdapter;
    private ImageView mPriImg;
    private TextView mPriName;

    private AVObject mObject;
    private PtaskDetail mPresenter;
    private SimpleDateFormat mFormat;

    private RelativeLayout mFbViewGroup;
    private EditText mFbInput;
    private Button mFbSend;

    private int mType = 0;
    private ImageView mStateImg;

    public final static String TASK_DETAIL_ACTIVITY_MODIFY="TASK_DETAIL_ACTIVITY_MODIFY";
    public final static int TASK_DETAIL_UPDATE_REQUEST=301;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_task_detail);
        Intent intent = getIntent();
        if (intent != null) {
            try {
                mObject = AVObject.parseAVObject(intent.getStringExtra(MyTaskFragment.TASK_DETAIL_SERIZALIZE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onCreate(savedInstanceState);
        mPresenter = new PtaskDetail(this);
        if (mObject != null) {
            mPresenter.getTaskDetail(mObject);
        }
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.TaskDetailActivity_toolbar);
        setCustomToolbar(toolbar);

        mModify = (TextView) findViewById(R.id.TaskDetailActivity_modify);
        mParRecy = (RecyclerView) findViewById(R.id.TaskDetailActivity_participant_RecyclerView);
        mFeedBackRecy = (RecyclerView) findViewById(R.id.TaskDetailActivity_feedback_recyclerView);
        mUserImg = (ImageView) findViewById(R.id.TaskDetailActivity_User_img);
        mUserName = (TextView) findViewById(R.id.TaskDetailActivity_User_name);
        mUserDep = (TextView) findViewById(R.id.TaskDetailActivity_User_department);
        mUserPos = (TextView) findViewById(R.id.TaskDetailActivity_User_position);
        mTaskDetail = (TextView) findViewById(R.id.TaskDetailActivity_Task_detail);
        mTaskTime = (TextView) findViewById(R.id.TaskDetailActivity_Task_CreateTime);
        mTaskDeTime = (TextView) findViewById(R.id.TaskDetailActivity_Task_DeadTime);
        mPriImg = (ImageView) findViewById(R.id.TaskDetailActivity_principal_User_img);
        mPriName = (TextView) findViewById(R.id.TaskDetailActivity_principal_User_name);
        mTaskTitle = (TextView) findViewById(R.id.TaskDetailActivity_Task_title);
        mFbInput = (EditText) findViewById(R.id.TaskDetailActivity_feedback_input);
        mFbSend = (Button) findViewById(R.id.TaskDetailActivity_feedback_send);
        mFbViewGroup = (RelativeLayout) findViewById(R.id.TaskDetailActivity_feedback);
        mStateImg= (ImageView) findViewById(R.id.TaskDetailActivity_state_img);
        mDone= (TextView) findViewById(R.id.TaskDetailActivity_done);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mParRecy.setLayoutManager(linearLayoutManager);
        mFeedBackRecy.setLayoutManager(new LinearLayoutManager(this));
        mFeedBackRecy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void initListener() {
        mFbSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.sendFeedBack(mFbInput.getText().toString(), mObject);
            }
        });
        mModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TaskDetailActivity.this,NewTaskActivity.class);
                intent.putExtra(TASK_DETAIL_ACTIVITY_MODIFY,mObject.toString());
                startActivityForResult(intent,TASK_DETAIL_UPDATE_REQUEST);
            }
        });
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirm();
            }
        });
    }

    @Override
    public void initData() {
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        mParAdapter = new NewTaskParAdapter(this);
        mParRecy.setAdapter(mParAdapter);
        mFbAdapter = new TaskFbAdapter(this);
        mFeedBackRecy.setAdapter(mFbAdapter);
    }

    public void setText(TextView view, String str, String title) {
        if (str == null || str.isEmpty()) {
            view.setText(String.format("%s:%s", title, "暂无信息"));
        } else {
            view.setText(String.format("%s:%s", title, str));
        }
    }

    @Override
    public void onSuccess(String str) {//发送反馈成功
        showToast(str);
        mPresenter.getFeedBack(mObject);
        mFbInput.setText("");
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//强制关闭输入法
        inputMethodManager.hideSoftInputFromWindow(mFbInput.getWindowToken(), 0);
    }

    @Override
    public void setPeople(AVObject avObject) {
        if (avObject != null) {
            mObject=avObject;
            AVUser user = avObject.getAVUser("user");
            AVFile img = user.getAVFile("portrait");
            if (img != null) {
                Glide.with(this).load(img.getUrl())
                        .dontAnimate()
                        .centerCrop()
                        .bitmapTransform(new GlideCircleTransform(this))
                        .placeholder(R.drawable.dayhr_userphoto_def)
                        .into(mUserImg);
            } else {
                mUserImg.setImageResource(R.drawable.dayhr_userphoto_def);
            }
            mUserName.setText(user.getString("name"));
            setText(mUserDep, user.getString("department"), "部门");
            setText(mUserPos, user.getString("job"), "职位");

            mTaskTitle.setText(mObject.getString("title"));
            mTaskDetail.setText(mObject.getString("detail"));
            mTaskTime.setText(String.format("发布时间:%s", mFormat.format(mObject.getCreatedAt())));
            mTaskDeTime.setText(String.format("截止时间:%s", mFormat.format(mObject.getDate("endTime"))));
            mType = mObject.getInt("sign");
            AVUser principal = avObject.getAVUser("principal");

            switch (mType) {
                case 0:
                    mStateImg.setVisibility(View.GONE);
                    if (mType == 0) {
                        if (AVUser.getCurrentUser().getObjectId().equals(principal.getObjectId())) {//当前用户是负责人 显示完成按钮
                            mDone.setVisibility(View.VISIBLE);
                        }
                        if(AVUser.getCurrentUser().getObjectId().equals(user.getObjectId())){//当前用户是发起人 显示修改按钮
                            mModify.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case 1:
                    mStateImg.setImageResource(R.drawable.task_img_over_def);
                    mStateImg.setVisibility(View.VISIBLE);
                    mFbViewGroup.setVisibility(View.GONE);
                    mModify.setVisibility(View.GONE);
                    mDone.setVisibility(View.GONE);
                    break;
                case 2:
                    mStateImg.setImageResource(R.drawable.task_img_expire_def);
                    mStateImg.setVisibility(View.VISIBLE);
                    mFbViewGroup.setVisibility(View.GONE);
                    mModify.setVisibility(View.GONE);
                    mDone.setVisibility(View.GONE);
                    break;
            }
            AVFile priImg = principal.getAVFile("portrait");
            if (priImg != null) {
                Glide.with(this).load(priImg.getUrl())
                        .dontAnimate()
                        .centerCrop()
                        .bitmapTransform(new GlideCircleTransform(this))
                        .placeholder(R.drawable.dayhr_userphoto_def)
                        .into(mPriImg);
            } else {
                mPriImg.setImageResource(R.drawable.dayhr_userphoto_def);
            }
            mPriName.setText(principal.getString("name"));
            List<AVUser> parList = avObject.getList("participant", AVUser.class);
            mParAdapter.setDataSource(new ArrayList<>(parList));//LinkedList 转为 ArrayList
            mParAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setFeedBack(List<AVObject> list) {
        if (list != null) {
            mFbAdapter.setDataSource(list);
            mFbAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setDoneSuccess(String str) {
        mDone.setVisibility(View.GONE);
        mModify.setVisibility(View.GONE);
        mPresenter.getFeedBack(mObject);
        mStateImg.setImageResource(R.drawable.task_img_over_def);
        mStateImg.setVisibility(View.VISIBLE);
        mFbViewGroup.setVisibility(View.GONE);
        showToast(str);
        setResult(RESULT_OK);//任务设置成功 刷新任务列表
    }

    public void showConfirm(){
        View view=getLayoutInflater().inflate(R.layout.popup__confirm,null);
        Button confirm= (Button) view.findViewById(R.id.popup_Warn_Confirm);
        Button cancel= (Button) view.findViewById(R.id.popup_Warn_Cancel);
        TextView title= (TextView) view.findViewById(R.id.popup_Warn_title);
        title.setText("确认要设置任务完成吗？");
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.popup_Warn_Confirm:
                        hideBasePopup();
                        mPresenter.setTaskDone(mObject);
                        break;
                    case R.id.popup_Warn_Cancel:
                        hideBasePopup();
                        break;
                }
            }
        };
        confirm.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
        showBasePopup(view,mModify, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.CENTER,0,0);
    }

    @Override
    public void onActivityResult(int requestId,int resultId,Intent data){
        super.onActivityResult(requestId,resultId,data);
        switch (resultId){
            case NewTaskActivity.MOTIDY_RESULT_SUCCESS:
                mPresenter.getTaskDetail(mObject);
                setResult(RESULT_OK);//修改成功 刷新任务列表
                break;
            case NewTaskActivity.TASK_DONE_RESULT_SUCCESS:
                mPresenter.getTaskDetail(mObject);
                setResult(RESULT_OK);//设置任务成功 刷新任务列表
                break;
        }
    }
}
