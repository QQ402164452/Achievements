package view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;
import com.example.wheelview.OnWheelChangedListener;
import com.example.wheelview.WheelView;
import com.example.wheelview.adapters.ListWheelAdapter;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import adapter.NewTaskParAdapter;
import customView.GlideCircleTransform;
import customView.ImageTextView;
import interfaces.InewTask;
import presenter.PnewTask;
import utils.DateUtil;

/**
 * Created by Jason on 2016/12/9.
 */

public class NewTaskActivity extends BaseActivity implements View.OnClickListener, InewTask{
    private TextView mCancel;
    private TextView mConfirm;
    private EditText mDetail;
    private EditText mTitle;
    private RelativeLayout mParticipant;
    private RelativeLayout mPrincipal;
    private RelativeLayout mDeadline;
    private RecyclerView mParRecycler;
    private LinearLayout mBottom;
    private ImageTextView mDelete;
    private ImageTextView mDone;

    private ArrayList<AVUser> mParList;
    private NewTaskParAdapter mParAdapter;

    private AVUser mPriUser;//负责人
    private LinearLayout mPriViewGroup;
    private ImageView mPriImg;
    private TextView mPriName;

    private TextView mDeadTime;//截止时间

    private ArrayList<String> mYears;
    private ArrayList<String> mMonths;
    private ArrayList<String> mDays;
    private ArrayList<String> mHours;

    private PnewTask mPresenter;

    private NewTaskHandler mHandler;

    public final static int NEW_TASK_PARTICIPANT_REQUEST=260;
    public final static int NEW_TASK_PRINCIPAL_REQUEST=261;
    public final static int MOTIDY_RESULT_SUCCESS=320;
    public final static int TASK_DONE_RESULT_SUCCESS=321;

    private AVObject mTask;
    private TextView mToolbarTitle;
    private int mType;//标志位 0-新建任务 1-修改任务

    @Override
    protected void initPre() {
        Intent intent=getIntent();
        if(intent!=null){
            try {
                mTask=AVObject.parseAVObject(intent.getStringExtra(TaskDetailActivity.TASK_DETAIL_ACTIVITY_MODIFY));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_new_task);
        mCancel= (TextView) findViewById(R.id.NewTaskActivity_cancel);
        mConfirm= (TextView) findViewById(R.id.NewTaskActivity_confirm);
        mDetail= (EditText) findViewById(R.id.NewTaskActivity_detail_input);
        mParticipant= (RelativeLayout) findViewById(R.id.NewTaskActivity_participant);
        mPrincipal= (RelativeLayout) findViewById(R.id.NewTaskActivity_principal);
        mDeadline= (RelativeLayout) findViewById(R.id.NewTaskActivity_deadline);
        mParRecycler= (RecyclerView) findViewById(R.id.NewTaskActivity_participant_RecyclerView);
        mPriViewGroup= (LinearLayout) findViewById(R.id.NewTaskActivity_principal_User);
        mPriImg= (ImageView) findViewById(R.id.NewTaskActivity_principal_User_img);
        mPriName= (TextView) findViewById(R.id.NewTaskActivity_principal_User_name);
        mDeadTime= (TextView) findViewById(R.id.NewTaskActivity_deadline_time);
        mTitle= (EditText) findViewById(R.id.NewTaskActivity_title_input);
        mToolbarTitle= (TextView) findViewById(R.id.NewTaskActivity_toolbar_title);
        mBottom= (LinearLayout) findViewById(R.id.NewTaskActivity_bottom);
        mDelete= (ImageTextView) findViewById(R.id.NewTaskActivity_delete);
        mDone= (ImageTextView) findViewById(R.id.NewTaskActivity_done);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置横向布局
        mParRecycler.setLayoutManager(linearLayoutManager);
        mBase=mConfirm;
    }

    @Override
    public void initListener() {
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mParticipant.setOnClickListener(this);
        mPrincipal.setOnClickListener(this);
        mDeadline.setOnClickListener(this);
        mDone.setOnClickListener(this);
        mDelete.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mPresenter=new PnewTask(this);
        mHandler=new NewTaskHandler(this);
        mYears=new ArrayList<>(Arrays.asList("2016年","2017年","2018年","2019年","2020年","2021年","2022年","2023年"));
        mMonths=new ArrayList<>(Arrays.asList("01月","02月","03月","04月","05月","06月","07月","08月","09月","10月","11月","12月"));
        mHours=new ArrayList<>(Arrays.asList("00时","01时","02时","03时","04时","05时","06时","07时","08时","09时","10时","11时","12时","13时",
                "14时","15时","16时","17时","18时","19时","20时","21时","22时","23时"));
        mDays= DateUtil.getTotalDays(mYears.get(0)+mMonths.get(0));
        mParList=new ArrayList<>();
        mParAdapter=new NewTaskParAdapter(this);
        mParRecycler.setAdapter(mParAdapter);
        if(mTask!=null){//从任务详情 到修改页面
            mToolbarTitle.setText("修改任务");
            mType=1;
            mBottom.setVisibility(View.VISIBLE);
            SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月DD日 HH时", Locale.CHINA);
            mTitle.setText(mTask.getString("title"));
            mDetail.setText(mTask.getString("detail"));
            List<AVUser> linkedList=mTask.getList("participant",AVUser.class);
            mParList=new ArrayList<>(linkedList);
            mParAdapter.setDataSource(mParList);
            mParAdapter.notifyDataSetChanged();
            mPriViewGroup.setVisibility(View.VISIBLE);
            AVUser principal = mTask.getAVUser("principal");
            mPriUser=principal;
            AVFile img = principal.getAVFile("portrait");
            if (img != null) {
                Glide.with(this).load(img.getUrl())
                        .dontAnimate()
                        .centerCrop()
                        .bitmapTransform(new GlideCircleTransform(this))
                        .placeholder(R.drawable.dayhr_userphoto_def)
                        .into(mPriImg);
            } else {
                mPriImg.setImageResource(R.drawable.dayhr_userphoto_def);
            }
            mPriName.setText(principal.getString("name"));
            mDeadTime.setText(format.format(mTask.getDate("endTime")));
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case NEW_TASK_PARTICIPANT_REQUEST:
                    if(data!=null){
                        ArrayList<AVUser> list= (ArrayList<AVUser>) data.getSerializableExtra(MultiContactActivity.MULTI_CONTACT_SELECT_BUNDLE);
                        if(list!=null){
                            mParList=list;
                            mParAdapter.setDataSource(mParList);
                            mParAdapter.notifyDataSetChanged();
                            mPriUser=null;
                            mPriViewGroup.setVisibility(View.GONE);
                        }else {
                            showToast("选择参与人失败");
                        }
                    }else{
                        showToast("选择参与人失败");
                    }
                    break;
                case NEW_TASK_PRINCIPAL_REQUEST:
                    if(data!=null){
                        try {
                            AVUser user= (AVUser) AVUser.parseAVObject(data.getStringExtra(PrincipalActivity.PRINCIPAL_USER_SELECT));
                            if(user!=null){
                                mPriUser=user;
                                mPriViewGroup.setVisibility(View.VISIBLE);
                                mPriName.setText(user.getString("name"));
                                AVFile img=mPriUser.getAVFile("portrait");
                                if(img!=null){
                                    Glide.with(this).load(img.getUrl())
                                            .centerCrop()
                                            .dontAnimate()
                                            .bitmapTransform(new GlideCircleTransform(this))
                                            .placeholder(R.drawable.dayhr_userphoto_def)
                                            .into(mPriImg);
                                }else{
                                    mPriImg.setImageResource(R.drawable.dayhr_userphoto_def);
                                }
                            }else{
                                showToast("选择负责人失败");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.NewTaskActivity_cancel:
                finish();
                break;
            case R.id.NewTaskActivity_confirm:
                switch (mType){
                    case 0://新建任务
                        mPresenter.upNewTask(mDetail.getText().toString(),mTitle.getText().toString(),mParList,mPriUser,mDeadTime.getText().toString());
                        break;
                    case 1://修改任务
                        mPresenter.modifyTask(mTask,mDetail.getText().toString(),mTitle.getText().toString(),mParList,mPriUser,mDeadTime.getText().toString());
                        break;
                }
                break;
            case R.id.NewTaskActivity_participant:
                intent=new Intent(this,MultiContactActivity.class);
                if(mParList!=null&&mParList.size()>0){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(MultiContactActivity.MULTI_CONTACT_SELECT_BUNDLE,mParList);
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent,NEW_TASK_PARTICIPANT_REQUEST);
                break;
            case R.id.NewTaskActivity_principal:
                intent=new Intent(this,PrincipalActivity.class);
                if(mParList!=null&&mParList.size()>0){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(PrincipalActivity.PRINCIPAL_USER_LISR,mParList);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,NEW_TASK_PRINCIPAL_REQUEST);
                }else {
                    showToast("未选择参与人");
                }
                break;
            case R.id.NewTaskActivity_deadline:
                showFourWheel();
                break;
            case R.id.NewTaskActivity_delete:
                showConfirm("是否删除任务",0);
                break;
            case R.id.NewTaskActivity_done:
                showConfirm("是否设置任务成功",1);
                break;
        }
    }

    public void showFourWheel(){
        View view=getLayoutInflater().inflate(R.layout.fragment_check_popup_select_four,null);
        TextView mCancel= (TextView) view.findViewById(R.id.CheckFragment_popup_four_cancel);
        TextView mConfirm= (TextView) view.findViewById(R.id.CheckFragment_popup_four_confirm);
        final WheelView one= (WheelView) view.findViewById(R.id.CheckFragment_popup_four_wheel_one);
        final WheelView two= (WheelView) view.findViewById(R.id.CheckFragment_popup_four_wheel_two);
        final WheelView three= (WheelView) view.findViewById(R.id.CheckFragment_popup_four_wheel_three);
        final WheelView four= (WheelView) view.findViewById(R.id.CheckFragment_popup_four_wheel_four);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.CheckFragment_popup_four_cancel:
                        hideBasePopup();
                        break;
                    case R.id.CheckFragment_popup_four_confirm:
                        hideBasePopup();
                        String time=mYears.get(one.getCurrentItem())+
                                mMonths.get(two.getCurrentItem())+
                                mDays.get(three.getCurrentItem())+" "+mHours.get(four.getCurrentItem());
                        mDeadTime.setText(time);
                        break;
                }
            }
        };
        mCancel.setOnClickListener(onClickListener);
        mConfirm.setOnClickListener(onClickListener);
        one.setViewAdapter(new ListWheelAdapter<>(this,mYears));
        two.setViewAdapter(new ListWheelAdapter<>(this,mMonths));
        three.setViewAdapter(new ListWheelAdapter<>(this,mDays));
        four.setViewAdapter(new ListWheelAdapter<>(this,mHours));
        one.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                three.setViewAdapter(new ListWheelAdapter<>(NewTaskActivity.this,DateUtil.getTotalDays(mYears.get(newValue)+mMonths.get(two.getCurrentItem()))));
                three.setCurrentItem(0);
            }
        });
        two.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                three.setViewAdapter(new ListWheelAdapter<>(NewTaskActivity.this,DateUtil.getTotalDays(mYears.get(one.getCurrentItem())+mMonths.get(newValue))));
                three.setCurrentItem(0);
            }
        });
        showBasePopup(view,mBase, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM,0,0);

    }

    public void showConfirm(String str, final int type){
        View view=getLayoutInflater().inflate(R.layout.popup__confirm,null);
        Button confirm= (Button) view.findViewById(R.id.popup_Warn_Confirm);
        Button cancel= (Button) view.findViewById(R.id.popup_Warn_Cancel);
        TextView title= (TextView) view.findViewById(R.id.popup_Warn_title);
        title.setText(str);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.popup_Warn_Confirm:
                        switch (type){
                            case 0:
                                hideBasePopup();
                                mPresenter.deleteTask(mTask);
                                break;
                            case 1:
                                hideBasePopup();
                                mPresenter.setTaskDone(mTask);
                                break;
                        }
                        break;
                    case R.id.popup_Warn_Cancel:
                        switch (type){
                            case 0:
                                hideBasePopup();
                                break;
                            case 1:
                                hideBasePopup();
                                break;
                        }
                        break;
                }
            }
        };
        confirm.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
        showBasePopup(view,mBottom, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.CENTER,0,0);
    }

    @Override
    public void onSuccess(String str) {
        mHandler.sendEmptyMessageDelayed(0,1000);
    }

    public void onFinish(String str,int type){
        hideBasePopup();
        showToast(str);
        switch (type){
            case 0:
                setResult(RESULT_OK);//新建任务返回
                break;
            case 1:
                setResult(MOTIDY_RESULT_SUCCESS);//修改任务返回
                break;
            case 2:
                setResult(TASK_DONE_RESULT_SUCCESS);//设置任务成功返回
                break;
            case 3:
                Intent intent=new Intent(this,TaskActivity.class);
                startActivity(intent);
                break;
        }
        finish();
    }

    @Override
    public void onModifyTaskSuccess(String str) {
        mHandler.sendEmptyMessageDelayed(1,1000);
    }

    @Override
    public void setDoneSuccess(String str) {
        mHandler.sendEmptyMessageDelayed(2,1000);
    }

    @Override
    public void onDeleteSuccess(String str) {
        mHandler.sendEmptyMessageDelayed(3,1000);
    }

    private static class NewTaskHandler extends Handler{
        private WeakReference<NewTaskActivity> weakReference;

        public NewTaskHandler(NewTaskActivity activity){
            weakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg){
            if(weakReference!=null){
                NewTaskActivity activity=weakReference.get();
                switch (msg.what){
                    case 0:
                        activity.onFinish("创建任务成功",0);
                        break;
                    case 1:
                        activity.onFinish("修改任务成功",1);
                        break;
                    case 2:
                        activity.onFinish("设置任务成功",2);
                        break;
                    case 3:
                        activity.onFinish("删除任务成功",3);
                        break;
                }

            }
        }
    }
}
