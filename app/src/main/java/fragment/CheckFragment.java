package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;
import com.example.wheelview.OnWheelChangedListener;
import com.example.wheelview.WheelView;
import com.example.wheelview.adapters.ListWheelAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import customView.GlideCircleTransform;
import interfaces.Icheck;
import presenter.Pcheck;
import utils.DateUtil;
import utils.ExamineUtil;
import view.CheckActivity;
import view.ContactSelectActivity;

import static android.app.Activity.RESULT_OK;
import static view.WriteReportActivity.CONTACT_SELECT_REQUEST;
import static view.WriteReportActivity.SELECT_USER;

/**
 * Created by Jason on 2016/12/5.
 */

public class CheckFragment extends LazyFragment implements View.OnClickListener, Icheck{
    private int mType;
    private LinearLayout mTypeView;
    private TextView mTypeText;
    private TextView mBeginText;
    private TextView mEndText;
    private EditText mReasonEdit;
    private TextView mReasonLength;
    private RelativeLayout mBottom;
    private ImageView mUserImg;
    private TextView mUserName;
    private AVUser mApprover;
    private CheckActivity mParent;
    private Button mSubmit;
    private ArrayList<String> mTypes;
    private ArrayList<String> mYears;
    private ArrayList<String> mMonths;
    private ArrayList<String> mDays;
    private ArrayList<String> mMon;
    private ArrayList<String> mHours;
    private int mSchedule=-1;//请假类型
    private int mBeginNoon=-1;//上下午
    private int mEndNoon=-1;

    private Pcheck mPresenter;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParent= (CheckActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mType= (int) getArguments().getSerializable("Check_Type");

        mTypes= ExamineUtil.getInstance().getTypes();
        mMon=ExamineUtil.getInstance().getMon();
        mYears=new ArrayList<>(Arrays.asList("2016年","2017年","2018年","2019年","2020年","2021年","2022年","2023年"));
        mMonths=new ArrayList<>(Arrays.asList("01月","02月","03月","04月","05月","06月","07月","08月","09月","10月","11月","12月"));
        mHours=new ArrayList<>(Arrays.asList("00时","01时","02时","03时","04时","05时","06时","07时","08时","09时","10时","11时","12时","13时",
                "14时","15时","16时","17时","18时","19时","20时","21时","22时","23时"));
        mDays= DateUtil.getTotalDays(mYears.get(0)+mMonths.get(0));
        mPresenter=new Pcheck(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_check,parent,false);
        init(view);
        isPrepared=true;
        lazyLoad();
        return view;
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    protected void initView(View view) {
        mTypeView= (LinearLayout) view.findViewById(R.id.CheckFragment_type_ViewGroup);
        if(mType!=0){
            mTypeView.setVisibility(View.GONE);
        }else {
            mTypeView.setVisibility(View.VISIBLE);
        }
        mTypeText= (TextView) view.findViewById(R.id.CheckFragment_type_text);
        mBeginText= (TextView) view.findViewById(R.id.CheckFragment_begin_text);
        mEndText= (TextView) view.findViewById(R.id.CheckFragment_end_text);
        mReasonEdit= (EditText) view.findViewById(R.id.CheckFragment_edittext_reason);
        mReasonLength= (TextView) view.findViewById(R.id.CheckFragment_reason_length);
        mBottom= (RelativeLayout) view.findViewById(R.id.CheckFragment_bottom_ViewGroup);
        mUserImg= (ImageView) view.findViewById(R.id.CheckFragment_bottom_img);
        mUserName= (TextView) view.findViewById(R.id.CheckFragment_bottom_name);
        mSubmit= (Button) view.findViewById(R.id.CheckFrament_btn_submit);
        mUserName.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        mTypeText.setOnClickListener(this);
        mBeginText.setOnClickListener(this);
        mEndText.setOnClickListener(this);
        mBottom.setOnClickListener(this);
        mReasonEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mReasonLength.setText(String.format("%d/100",mReasonEdit.getText().length()));
            }
        });
        mSubmit.setOnClickListener(this);
    }

    public static CheckFragment newInstance(int type){
        Bundle bundle=new Bundle();
        bundle.putSerializable("Check_Type",type);
        CheckFragment checkFragment=new CheckFragment();
        checkFragment.setArguments(bundle);
        return checkFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.CheckFragment_type_text:
                showSingleWheel();
                break;
            case R.id.CheckFragment_begin_text:
                showFourWheel(0);
                break;
            case R.id.CheckFragment_end_text:
                showFourWheel(1);
                break;
            case R.id.CheckFragment_bottom_ViewGroup:
                Intent intent=new Intent(getActivity(), ContactSelectActivity.class);
                startActivityForResult(intent, CONTACT_SELECT_REQUEST);
                break;
            case R.id.CheckFrament_btn_submit:
                mPresenter.sumbit(AVUser.getCurrentUser(),mApprover,mType,mSchedule,mBeginNoon,mEndNoon,
                        mBeginText.getText().toString(),mEndText.getText().toString(),mReasonEdit.getText().toString());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK){
            if(data!=null){
                mUserName.setVisibility(View.VISIBLE);
                String serializedString=data.getStringExtra(SELECT_USER);
                try {
                    //AVObject的parseAVObject 反序列化
                    AVUser user= (AVUser) AVUser.parseAVObject(serializedString);
                    if(user!=null){
                        mApprover=user;
                        AVFile img=user.getAVFile("portrait");
                        mUserName.setText(user.getString("name"));
                        if(img!=null){
                            Glide.with(this).load(img.getUrl())
                                    .centerCrop()
                                    .transform(new GlideCircleTransform(getActivity()))
                                    .dontAnimate()
                                    .placeholder(R.drawable.dayhr_userphoto_def)
                                    .into(mUserImg);
                        }else {
                            mUserImg.setImageResource(R.drawable.dayhr_userphoto_def);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showSingleWheel(){
        View view=mParent.getLayoutInflater().inflate(R.layout.fragment_check_popup_select_single,null);
        TextView mCancel= (TextView) view.findViewById(R.id.CheckFragment_popup_single_cancel);
        TextView mConfirm= (TextView) view.findViewById(R.id.CheckFragment_popup_single_confirm);
        final WheelView wheelView= (WheelView) view.findViewById(R.id.CheckFragment_popup_single_wheel);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.CheckFragment_popup_single_cancel:
                        mParent.hideBasePopup();
                        break;
                    case R.id.CheckFragment_popup_single_confirm:
                        mParent.hideBasePopup();
                        mTypeText.setText(mTypes.get(wheelView.getCurrentItem()));
                        mSchedule=wheelView.getCurrentItem();
                        break;
                }
            }
        };
        mCancel.setOnClickListener(onClickListener);
        mConfirm.setOnClickListener(onClickListener);
        wheelView.setViewAdapter(new ListWheelAdapter<>(mParent,mTypes));
        mParent.showBasePopup(view,mBeginText, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM,0,0);
    }

    public void showFourWheel(final int type){
        View view=mParent.getLayoutInflater().inflate(R.layout.fragment_check_popup_select_four,null);
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
                        mParent.hideBasePopup();
                        break;
                    case R.id.CheckFragment_popup_four_confirm:
                        mParent.hideBasePopup();
                        String time=mYears.get(one.getCurrentItem())+
                                mMonths.get(two.getCurrentItem())+
                                mDays.get(three.getCurrentItem());
                        switch (mType){
                            case 0:
                            case 2:
                                time=time+mMon.get(four.getCurrentItem());
                                break;
                            case 1:
                                time=time+mHours.get(four.getCurrentItem());
                                break;
                        }
                        switch (type){
                            case 0:
                                mBeginText.setText(time);
                                mBeginNoon=four.getCurrentItem();
                                break;
                            case 1:
                                mEndNoon=four.getCurrentItem();
                                mEndText.setText(time);
                                break;
                        }

                        break;
                }
            }
        };
        mCancel.setOnClickListener(onClickListener);
        mConfirm.setOnClickListener(onClickListener);
        one.setViewAdapter(new ListWheelAdapter<>(mParent,mYears));
        two.setViewAdapter(new ListWheelAdapter<>(mParent,mMonths));
        three.setViewAdapter(new ListWheelAdapter<>(mParent,mDays));
        switch (mType){
            case 0:
            case 2:
                four.setViewAdapter(new ListWheelAdapter<>(mParent,mMon));
                break;
            case 1:
                four.setViewAdapter(new ListWheelAdapter<>(mParent,mHours));
        }
        one.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                three.setViewAdapter(new ListWheelAdapter<>(mParent,DateUtil.getTotalDays(mYears.get(newValue)+mMonths.get(two.getCurrentItem()))));
                three.setCurrentItem(0);
            }
        });
        two.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                three.setViewAdapter(new ListWheelAdapter<>(mParent,DateUtil.getTotalDays(mYears.get(one.getCurrentItem())+mMonths.get(newValue))));
                three.setCurrentItem(0);
            }
        });
        mParent.showBasePopup(view,mBeginText, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM,0,0);

    }

    @Override
    public void showToast(String str) {
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {
        showToast(error);
    }

    @Override
    public void onSuccess(String str) {
        mParent.hideBasePopup();
        showToast(str);
        mBeginText.setText("");
        mEndText.setText("");
        mReasonEdit.setText("");
        mTypeText.setText("");
        mApprover=null;
        mUserImg.setImageResource(R.drawable.manual_icon_add_def);
        mUserName.setVisibility(View.GONE);
        mSchedule=-1;
        mBeginNoon=-1;
        mEndNoon=-1;
    }

    @Override
    public void showLoading() {
        mParent.showLoading(mBeginText);
    }
}
