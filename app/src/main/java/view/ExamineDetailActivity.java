package view;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import customView.GlideCircleTransform;
import fragment.MyExamineFragment;
import interfaces.ImyEmDt;
import presenter.PmyEmDt;
import utils.DateUtil;
import utils.ExamineUtil;

/**
 * Created by Jason on 2016/12/7.
 */

public class ExamineDetailActivity extends BaseActivity implements ImyEmDt{
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
    private TextView mUrge;
    private TextView mRevoke;
    private AVObject mObject;

    private LinearLayout mRemarks;
    private TextView mRemark;

    private ArrayList<String> mTypes;
    private ArrayList<String> mType;
    private ArrayList<String> mMon;
    private int mPosition;

    private PmyEmDt mPresenter;

    @Override
    protected void initPre() {
        Intent intent=getIntent();
        if(intent!=null){
            try {
                mObject=AVObject.parseAVObject(intent.getStringExtra("MyExamineDetail"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            mPosition=intent.getIntExtra("MyExamineDetail_Position",0);
        }
        mTypes= ExamineUtil.getInstance().getTypes();
        mType=ExamineUtil.getInstance().getType();
        mMon=ExamineUtil.getInstance().getMon();

        mPresenter=new PmyEmDt(this);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_examine_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.ExamineDetailActivity_toolbar);
        setCustomToolbar(toolbar);

        mUserImg= (ImageView) findViewById(R.id.ExamineDetailActivity_User_img);
        mTitle= (TextView) findViewById(R.id.ExamineDetailActivity_title);
        mTime= (TextView) findViewById(R.id.ExamineDetailActivity_time);
        mState= (TextView) findViewById(R.id.ExamineDetailActivity_state);
        mExamType= (TextView) findViewById(R.id.ExamineDetailActivity_type);
        mBeginTime= (TextView) findViewById(R.id.ExamineDetailActivity_beginTime);
        mEndTime= (TextView) findViewById(R.id.ExamineDetailActivity_endTime);
        mReason= (TextView) findViewById(R.id.ExamineDetailActivity_reason);
        mApproverImg= (ImageView) findViewById(R.id.EXamineDetailActivity_approver_img);
        mApproverState= (TextView) findViewById(R.id.EXamineDetailActivity_approver_state);
        mApproverTime= (TextView) findViewById(R.id.EXamineDetailActivity_approver_time);
        mUrge= (TextView) findViewById(R.id.EXamineDetailActivity_urge);
        mRevoke= (TextView) findViewById(R.id.EXamineDetailActivity_revoke);
        mRemark= (TextView) findViewById(R.id.EXamineDetailActivity_remarks);
        mRemarks= (LinearLayout) findViewById(R.id.EXamineDetailActivity_remarks_ViewGroup);
    }

    @Override
    public void initListener() {
        mUrge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setUrgeState(mObject);
            }
        });
        mRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRevokeConfirm();
            }
        });
    }

    @Override
    public void initData() {
        if(mObject!=null){
            int type=mObject.getInt("type");
            mTitle.setText(String.format("我的%s申请",mType.get(type)));
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
                case 1:
                    mState.setText("已通过");
                    mState.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.list_icon_done_def),null,null,null);
                    mState.setTextColor(getResources().getColor(R.color.green_light));
                    mApproverTime.setText(DateUtil.getDetail(mObject.getDate("approverTime")));
                    mApproverState.setText(approver_name+"审批完成");
                    setBtnEnabled(0);
                    break;
                case 2:
                    mState.setText("未通过");
                    mState.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.list_icon_backup_def),null,null,null);
                    mState.setTextColor(getResources().getColor(R.color.red_light));
                    mApproverTime.setText(DateUtil.getDetail(mObject.getDate("approverTime")));
                    mApproverState.setText(approver_name+"审批完成");
                    mRemarks.setVisibility(View.VISIBLE);
                    mRemark.setText(mObject.getString("remarks"));
                    setBtnEnabled(0);
                    break;
                case 3:
                    mState.setText("催办中");
                    mState.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.list_icon_doing_def),null,null,null);
                    mState.setTextColor(getResources().getColor(R.color.red_light));
                    mApproverTime.setVisibility(View.GONE);
                    mApproverState.setText(approver_name+"审批中");
                    setBtnEnabled(1);
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

    @Override
    public void onSuccess(String str, int type) {
        showToast(str);
        Intent intent;
        switch (type){
            case 0:
                mState.setText("催办中");
                setBtnEnabled(1);
                intent=new Intent();
                intent.putExtra(MyExamineFragment.MY_EXAMINE_UPDATE,mPosition);
                setResult(211,intent);
                break;
            case 1:
                intent=new Intent();
                intent.putExtra(MyExamineFragment.MY_EXAMINE_DELETE,mPosition);
                setResult(212,intent);
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(String str) {

    }

    public void setBtnEnabled(int type){
        switch (type){
            case 0:
                mUrge.setEnabled(false);
                mRevoke.setEnabled(false);
                break;
            case 1:
                mUrge.setEnabled(false);
                break;
        }
    }

    public void showRevokeConfirm(){
        View view=getLayoutInflater().inflate(R.layout.popup__confirm,null);
        Button confirm= (Button) view.findViewById(R.id.popup_Warn_Confirm);
        Button cancel= (Button) view.findViewById(R.id.popup_Warn_Cancel);
        TextView title= (TextView) view.findViewById(R.id.popup_Warn_title);
        title.setText("是否确认撤销？");
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.popup_Warn_Confirm:
                        hideBasePopup();
                        mPresenter.setRevokeState(mObject);
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
}
