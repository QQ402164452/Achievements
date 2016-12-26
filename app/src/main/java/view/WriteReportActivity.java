package view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.SelectTimeAdapter;
import customView.DividerItemExceptLastDecoration;
import customView.GlideCircleTransform;
import interfaces.Iwritereport;
import interfaces.OnCustomItemClickListener;
import presenter.PwriteReport;
import utils.DateUtil;

/**
 * Created by Jason on 2016/12/2.
 */

public class WriteReportActivity extends BaseActivity implements Iwritereport{
    private TextView mSave;
    private TextView mTitle;
    private int mType=0;
    private RelativeLayout mSelectTimeBtn;
    private TextView mSummaryTitle;
    private TextView mPlanTitle;
    private TextView mSelectDateText;
    private ArrayList<String>  mList;
    private RelativeLayout mSelectContact;
    private LinearLayout mBottomGroup;
    private ImageView mBottomImg;
    private TextView mBottomName;
    private EditText mSummaryText;
    private EditText mPlanText;
    private ArrayList<ArrayList<String>> mTime;
    private int mSelectTime=1;
    private AVUser mToReportUser=null;
    private PwriteReport mPresenter;

    public static int CONTACT_SELECT_REQUEST=300;
    public static int CONTACT_SELECT_RESULT=301;
    public static String SELECT_USER="SelectUser";

    @Override
    protected void initPre() {

    }


    @Override
    public void initView() {
        setContentView(R.layout.activity_write_report);
        Toolbar toolbar= (Toolbar) findViewById(R.id.WriteReportActivity_Toolbar);
        setCustomToolbar(toolbar);
        mSave= (TextView) findViewById(R.id.WriteReportActivity_btn_save);
        mTitle= (TextView) findViewById(R.id.WriteReportActivity_title);
        mSelectTimeBtn= (RelativeLayout) findViewById(R.id.WriteReportActivity_select_time_btn);
        mSummaryTitle= (TextView) findViewById(R.id.ReportActivity_summary_title);
        mPlanTitle= (TextView) findViewById(R.id.ReportActivity_plan_title);
        mSelectDateText= (TextView) findViewById(R.id.WriteReportActivity_select_time_date);
        mSelectContact= (RelativeLayout) findViewById(R.id.WriteReportActivity_btn_selectContact);
        mSummaryText= (EditText) findViewById(R.id.ReportActivity_summary_text);
        mPlanText= (EditText) findViewById(R.id.ReportActivity_plan_text);

        mBottomGroup= (LinearLayout) findViewById(R.id.WriteReportActivity_bottom_group);
        mBottomImg= (ImageView) findViewById(R.id.WriteReportActivity_bottom_img);
        mBottomName= (TextView) findViewById(R.id.WriteReportActivity_bottom_name);
        mBottomGroup.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        mSelectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelectPopup();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mToReportUser==null){
                    showToast("未选择汇报对象");
                }else{
                    showLoading(mTitle);
                    mPresenter.upReport(mType,mSummaryText.getText().toString(),mPlanText.getText().toString(),
                            mToReportUser,mTime.get(mSelectTime).get(0),mTime.get(mSelectTime).get(1));
                }
            }
        });
        mSelectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WriteReportActivity.this,ContactSelectActivity.class);
                startActivityForResult(intent,CONTACT_SELECT_REQUEST);
            }
        });
    }

    @Override
    public void initData() {
        Intent intent=getIntent();
        mPresenter=new PwriteReport(this);
        mList=new ArrayList<>();
        mTime=new ArrayList<>();
        ArrayList<String> mDate;
        if(intent!=null){
            mType=intent.getIntExtra(ReportActivity.REPORT_TYPE,0);
            switch (mType){
                case 0:
                    mTitle.setText("写日报");
                    mSummaryTitle.setText("今日工作总结");
                    mPlanTitle.setText("明日工作计划");

                    mDate= DateUtil.getDay(-1);
                    mTime.add(mDate);
                    mList.add("昨天("+mDate.get(0)+" "+mDate.get(1)+")");
                    mDate= DateUtil.getDay(0);
                    mTime.add(mDate);
                    mList.add("今天("+mDate.get(0)+" "+mDate.get(1)+")");
                    mDate= DateUtil.getDay(1);
                    mTime.add(mDate);
                    mList.add("明天("+mDate.get(0)+" "+mDate.get(1)+")");
                    break;
                case 1:
                    mTitle.setText("写周报");
                    mSummaryTitle.setText("本周工作总结");
                    mPlanTitle.setText("下周工作计划");

                    mDate=DateUtil.getWeek(-1);
                    mTime.add(mDate);
                    mList.add("上周("+mDate.get(0)+" 第"+mDate.get(1)+"周)");
                    mDate= DateUtil.getWeek(0);
                    mTime.add(mDate);
                    mList.add("本周("+mDate.get(0)+" 第"+mDate.get(1)+"周)");
                    mDate= DateUtil.getWeek(1);
                    mTime.add(mDate);
                    mList.add("下周("+mDate.get(0)+" 第"+mDate.get(1)+"周)");
                    break;
                case 2:
                    mTitle.setText("写月报");
                    mSummaryTitle.setText("本月工作总结");
                    mPlanTitle.setText("下月工作计划");

                    mDate=DateUtil.getMonth(-1);
                    mTime.add(mDate);
                    mList.add("上月("+mDate.get(0)+"年 "+mDate.get(1)+"月份)");
                    mDate=DateUtil.getMonth(0);
                    mTime.add(mDate);
                    mList.add("本月("+mDate.get(0)+"年 "+mDate.get(1)+"月份)");
                    mDate=DateUtil.getMonth(1);
                    mTime.add(mDate);
                    mList.add("下月("+mDate.get(0)+"年 "+mDate.get(1)+"月份)");
                    break;
            }
        }
        mSelectDateText.setText(mList.get(1));
    }

    public void showTimeSelectPopup(){
        View view=getLayoutInflater().inflate(R.layout.report_popup_select_time,null);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.ReportActivity_popup_select_listview);
        Button cancel= (Button) view.findViewById(R.id.ReportActivity_popup_select_cancel);

        SelectTimeAdapter adapter=new SelectTimeAdapter(this);
        adapter.setDataSoure(mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemExceptLastDecoration(this,DividerItemExceptLastDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBasePopup.dismiss();
            }
        });
        adapter.setOnClickListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                mSelectDateText.setText(mList.get(position));
                mSelectTime=position;
                mBasePopup.dismiss();
            }
        });
        showBasePopup(view,mTitle,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM,0,0);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(data!=null){
                mBottomGroup.setVisibility(View.VISIBLE);
                String serializedString=data.getStringExtra(SELECT_USER);
                try {
                    //AVObject的parseAVObject 反序列化
                    AVUser user= (AVUser) AVUser.parseAVObject(serializedString);
                    if(user!=null){
                        mToReportUser=user;
                        AVFile img=user.getAVFile("portrait");
                        mBottomName.setText(user.getString("name"));
                        if(img!=null){
                            Glide.with(this).load(img.getUrl())
                                    .centerCrop()
                                    .transform(new GlideCircleTransform(this))
                                    .dontAnimate()
                                    .placeholder(R.drawable.dayhr_userphoto_def)
                                    .into(mBottomImg);
                        }else {
                            mBottomImg.setImageResource(R.drawable.dayhr_userphoto_def);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onSuccess(String str) {
        mHandler.sendEmptyMessageDelayed(0,1000);
    }

    private Handler mHandler=new Handler(){
      public void handleMessage(Message message){
          mBasePopup.dismiss();
          showToast("保存成功！");
          finish();
      }
    };
}
