package view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import customView.GlideCircleTransform;
import interfaces.Ipending;
import presenter.Ppending;
import utils.DateUtil;
import utils.ExamineUtil;

/**
 * Created by Jason on 2016/12/7.
 */

public class PendingActivity extends BaseActivity implements Ipending{
    private ImageView mUserImg;
    private TextView mTitle;
    private TextView mTime;
    private TextView mState;
    private TextView mExamType;
    private TextView mBeginTime;
    private TextView mEndTime;
    private TextView mReason;
    private ImageView mApproverImg;
    private TextView mApproverState;
    private TextView mApproverTime;
    private TextView mBack;
    private TextView mPass;
    private AVObject mObject;

    private ArrayList<String> mTypes;
    private ArrayList<String> mType;
    private ArrayList<String> mMon;
    private int mPosition;
    private String mRemarks="";

    private PendingHandler mHandler;
    private Ppending mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_pending);

        Intent intent=getIntent();
        if(intent!=null){
            try {
                mObject=AVObject.parseAVObject(intent.getStringExtra("PendingDetail"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            mPosition=intent.getIntExtra("PendingDetail_Position",0);
        }
        mTypes= ExamineUtil.getInstance().getTypes();
        mType=ExamineUtil.getInstance().getType();
        mMon=ExamineUtil.getInstance().getMon();

        super.onCreate(savedInstanceState);
    }


    @Override
    public void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.PendingActivity_toolbar);
        setCustomToolbar(toolbar);

        mUserImg= (ImageView) findViewById(R.id.PendingActivity_User_img);
        mTitle= (TextView) findViewById(R.id.PendingActivity_title);
        mTime= (TextView) findViewById(R.id.PendingActivity_time);
        mState= (TextView) findViewById(R.id.PendingActivity_state);
        mExamType= (TextView) findViewById(R.id.PendingActivity_type);
        mBeginTime= (TextView) findViewById(R.id.PendingActivity_beginTime);
        mEndTime= (TextView) findViewById(R.id.PendingActivity_endTime);
        mReason= (TextView) findViewById(R.id.PendingActivity_reason);
        mApproverImg= (ImageView) findViewById(R.id.PendingActivity_approver_img);
        mApproverState= (TextView) findViewById(R.id.PendingActivity_approver_state);
        mApproverTime= (TextView) findViewById(R.id.PendingActivity_approver_time);
        mBack= (TextView) findViewById(R.id.PendingActivity_back);
        mPass= (TextView) findViewById(R.id.PendingActivity_pass);
        mBase=mTitle;
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemarksInput();
            }
        });
        mPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmPopup();
            }
        });
    }

    @Override
    public void initData() {
        mHandler=new PendingHandler(this);
        mPresenter=new Ppending(this);
        if(mObject!=null){
            int type=mObject.getInt("type");
            mTime.setText(DateUtil.getNormalDetail(mObject.getCreatedAt()));
            AVUser user=mObject.getAVUser("user");
            AVFile user_img=user.getAVFile("portrait");
            if(user_img!=null){
                Glide.with(this).load(user_img.getUrl())
                        .centerCrop()
                        .dontAnimate()
                        .transform(new GlideCircleTransform(this))
                        .placeholder(R.drawable.dayhr_userphoto_def)
                        .into(mUserImg);
            }else{
                mUserImg.setImageResource(R.drawable.dayhr_userphoto_def);
            }
            mTitle.setText(String.format("%s的%s申请",user.getString("name"),mType.get(type)));
            int sign=mObject.getInt("sign");
            AVUser approver=mObject.getAVUser("approver");
            AVFile approver_img=approver.getAVFile("portrait");
            String approver_name=approver.getString("name");
            if(approver_img!=null){
                Glide.with(this).load(approver_img.getUrl())
                        .centerCrop()
                        .dontAnimate()
                        .transform(new GlideCircleTransform(this))
                        .placeholder(R.drawable.dayhr_userphoto_def)
                        .into(mApproverImg);
            }else{
                mApproverImg.setImageResource(R.drawable.dayhr_userphoto_def);
            }
            switch (sign){
                case 0:
                    mState.setText("审核中");
                    mState.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.list_icon_doing_def),null,null,null);
                    mState.setTextColor(getResources().getColor(R.color.red_light));
                    mApproverTime.setVisibility(View.GONE);
                    mApproverState.setText(approver_name+"审批中");
                    break;
                case 3:
                    mState.setText("催办中");
                    mState.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.list_icon_doing_def),null,null,null);
                    mState.setTextColor(getResources().getColor(R.color.red_light));
                    mApproverTime.setVisibility(View.GONE);
                    mApproverState.setText(approver_name+"审批中");
                    break;
            }
            switch (type){
                case 0:
                    mExamType.setText(mTypes.get(mObject.getInt("schedule")));
                    mBeginTime.setText(Html.fromHtml("开始时间：<font color='black'>"+DateUtil.getNoon(mObject.getDate("begin"))+" "+
                            mMon.get(mObject.getInt("beginNoon"))+" "+DateUtil.getDayOfDate(mObject.getDate("begin"))+"</font>"));
                    mEndTime.setText(Html.fromHtml("结束时间：<font color='black'>"+DateUtil.getNoon(mObject.getDate("end"))+" "+
                            mMon.get(mObject.getInt("endNoon"))+" "+DateUtil.getDayOfDate(mObject.getDate("end"))+"</font>"));
                    break;
                case 1:
                    mExamType.setText(mType.get(type));
                    mBeginTime.setText(Html.fromHtml("开始时间：<font color='black'>"+DateUtil.getExtra(mObject.getDate("begin"))+" "+DateUtil.getDayOfDate(mObject.getDate("begin"))+"</font>"));
                    mEndTime.setText(Html.fromHtml("结束时间：<font color='black'>"+DateUtil.getExtra(mObject.getDate("end"))+" "+DateUtil.getDayOfDate(mObject.getDate("end"))+"</font>"));
                    break;
                case 2:
                    mExamType.setText(mType.get(type));
                    mBeginTime.setText(Html.fromHtml("开始时间：<font color='black'>"+DateUtil.getNoon(mObject.getDate("begin"))+" "+
                            mMon.get(mObject.getInt("beginNoon"))+" "+DateUtil.getDayOfDate(mObject.getDate("begin"))+"</font>"));
                    mEndTime.setText(Html.fromHtml("结束时间：<font color='black'>"+DateUtil.getNoon(mObject.getDate("end"))+" "+
                            mMon.get(mObject.getInt("endNoon"))+" "+DateUtil.getDayOfDate(mObject.getDate("end"))+"</font>"));
                    break;
            }
            mReason.setText(Html.fromHtml("原因：<font color='black'>"+mObject.getString("reason")+"</font>"));
        }
    }

    public void showConfirmPopup(){
        View view=getLayoutInflater().inflate(R.layout.popup__confirm,null);
        Button confirm= (Button) view.findViewById(R.id.popup_Warn_Confirm);
        Button cancel= (Button) view.findViewById(R.id.popup_Warn_Cancel);
        TextView title= (TextView) view.findViewById(R.id.popup_Warn_title);
        title.setText("确认要通过该单据吗？");
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.popup_Warn_Confirm:
                        hideBasePopup();
                        mPresenter.setPass(mObject);
                        break;
                    case R.id.popup_Warn_Cancel:
                        hideBasePopup();
                        break;
                }
            }
        };
        confirm.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
        showBasePopup(view,mTitle, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.CENTER,0,0);
    }

    public void showRemarksInput(){
        View view=getLayoutInflater().inflate(R.layout.pending_remarks_input_popup,null);
        final EditText input= (EditText) view.findViewById(R.id.PendingActivity_remarks_input);
        final TextView counts= (TextView) view.findViewById(R.id.PendingActivity_remarks_count);
        Button cancel= (Button) view.findViewById(R.id.PendingActivity_remarks_Cancel);
        Button confirm= (Button) view.findViewById(R.id.PendingActivity_remarks_Confirm);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.PendingActivity_remarks_Confirm:
                        hideBasePopup();
                        mPresenter.setBack(mObject,input.getText().toString());
                        break;
                    case R.id.PendingActivity_remarks_Cancel:
                        hideBasePopup();
                        break;
                }
            }
        };
        cancel.setOnClickListener(onClickListener);
        confirm.setOnClickListener(onClickListener);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mRemarks=input.getText().toString();
                counts.setText(String.format("%d/100",input.getText().length()));
            }
        });
        input.setText(mRemarks);
        showBasePopup(view,mBack, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ,Gravity.CENTER,0,0);
        mBasePopup.setFocusable(true);
        mBasePopup.update();

    }

    @Override
    public void onSuccess(String str) {
        Message msg=Message.obtain();
        Bundle bundle=new Bundle();
        bundle.putString("showToast",str);
        msg.setData(bundle);
        mHandler.sendMessageDelayed(msg,1000);

        Intent intent=new Intent();
        intent.putExtra("Pending_Position",mPosition);
        setResult(RESULT_OK,intent);
    }

    private static class PendingHandler extends Handler{
        private WeakReference<PendingActivity> weakReference;

        private PendingHandler(PendingActivity pendingActivity){
            weakReference=new WeakReference<>(pendingActivity);
        }

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            PendingActivity activity=weakReference.get();
            if(activity!=null){
                Bundle bundle=msg.getData();
                String str=bundle.getString("showToast");
                activity.hideBasePopup();
                activity.showToast(str);
                activity.finish();
            }
        }
    }
}
