package view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.AttendanceAdapter;
import adapter.CalendarAdapter;
import bean.AttendanceBean;
import customView.GlideCircleTransform;
import interfaces.WeakObject;
import interfaces.OnCustomItemClickListener;
import utils.DateUtil;
import utils.NetworkUtil;
import utils.WeakHandler;

/**
 * Created by Jason on 2016/12/17.
 */

public class AttendanceActivity extends BaseActivity implements WeakObject {
    private Toolbar mToolbar;
    private ImageView mCalendar;
    private ArrayList<String> mYears;
    private int mCurrentYear=3;
    private int mCurrentMonth=0;
    private TextView mTime;
    private ImageView mUserImg;
    private TextView mUserName;
    private TextView mUserDep;
    private TextView mUserPos;
    private XRecyclerView mRecy;
    private View mEmptyView;
    private AttendanceAdapter mAdapter;
    private ArrayList<AttendanceBean> mList;
    private WeakHandler mHandler;
    private double totalTime=0;
    private int absenceCount=0;
    private TextView mTotalTime;
    private TextView mAbsenceCount;

    @Override
    protected void initPre() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_attendance);
        mToolbar= (Toolbar) findViewById(R.id.AttendanceActivity_toolbar);
        setCustomToolbar(mToolbar);
        mCalendar= (ImageView) findViewById(R.id.AttendanceActivity_btn_Calendar);
        mTime= (TextView) findViewById(R.id.AttendanceActivity_time);
        mUserImg= (ImageView) findViewById(R.id.AttendanceActivity_User_img);
        mUserName= (TextView) findViewById(R.id.AttendanceActivity_User_name);
        mUserDep= (TextView) findViewById(R.id.AttendanceActivity_User_department);
        mUserPos= (TextView) findViewById(R.id.AttendanceActivity_User_position);
        mRecy= (XRecyclerView) findViewById(R.id.AttendanceActivity_recyclerView);
        mEmptyView=findViewById(R.id.AttendanceActivity_empty_view);
        mTotalTime= (TextView) findViewById(R.id.AttendanceActivity_Count_total);
        mAbsenceCount= (TextView) findViewById(R.id.AttendanceActivity_Count_absence);
        mRecy.setLayoutManager(new LinearLayoutManager(this));
        mRecy.setEmptyView(mEmptyView);
        mRecy.setLoadingMoreEnabled(false);
    }

    @Override
    public void initListener() {
        mCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
        mRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(0,1000);
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    @Override
    public void initData() {
        mHandler=new WeakHandler(this);
        mYears=new ArrayList<>(Arrays.asList("2013","2014","2015","2016","2017","2018","2019","2020"));
        mTime.setText(String.format(Locale.CHINA,"考勤数据:%s年%d月",mYears.get(mCurrentYear),mCurrentMonth+1));
        AVUser user = AVUser.getCurrentUser();
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

        mList=new ArrayList<>();
        mAdapter=new AttendanceAdapter(this,mList);
        mRecy.setAdapter(mAdapter);
        updateList();
    }

    public void showCalendar(){
        View view=getLayoutInflater().inflate(R.layout.wage_calendar_popup,null);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.WageActivity_calendar_popup_RecyclerView);
        ImageView left= (ImageView) view.findViewById(R.id.Calendar_Popup_LeftBtn);
        ImageView right= (ImageView) view.findViewById(R.id.Calendar_Popup_RightBtn);
        final TextView year= (TextView) view.findViewById(R.id.Calendar_Popup_Year);
        year.setText(mYears.get(mCurrentYear));

        recyclerView.setLayoutManager(new GridLayoutManager(this,6));
        final CalendarAdapter adapter=new CalendarAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setCurrent(mCurrentMonth);
        adapter.notifyDataSetChanged();
        adapter.setListener(new OnCustomItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                mCurrentMonth=position;
                hideBasePopup();
                mTime.setText(String.format(Locale.CHINA,"考勤数据:%s年%d月",mYears.get(mCurrentYear),position+1));
                updateList();
            }
        });
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.Calendar_Popup_LeftBtn:
                        if(mCurrentYear>0){
                            mCurrentYear--;
                            year.setText(mYears.get(mCurrentYear));
                        }
                        break;
                    case R.id.Calendar_Popup_RightBtn:
                        if(mCurrentYear<mYears.size()-1){
                            mCurrentYear++;
                            year.setText(mYears.get(mCurrentYear));
                        }
                        break;
                }
            }
        };
        left.setOnClickListener(onClickListener);
        right.setOnClickListener(onClickListener);
        showAsDropDownPopup(view,mToolbar, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void updateList(){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("checkIn");
            query.whereEqualTo("year",Integer.valueOf(mYears.get(mCurrentYear)));
            query.whereEqualTo("month",mCurrentMonth+1);
            query.whereEqualTo("user",AVUser.getCurrentUser());
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        if(list.size()>0){
                            ArrayMap<Integer,AVObject> map=new ArrayMap<>();
                            totalTime=0;
                            absenceCount=0;
                            for(int i=0;i<list.size();i++){
                                AVObject object=list.get(i);
                                map.put(object.getInt("day"),object);
                                int startSign=object.getInt("StartSign");
                                int endSign=object.getInt("EndSign");
                                switch (startSign){
                                    case 1://准时上班
                                        switch (endSign){
                                            case 1://准时下班
                                                totalTime+=8*60;
                                                break;
                                            case 2://早退下班
                                                absenceCount++;
                                                Date date=object.getDate("EndTime");
                                                int hour=date.getHours();
                                                int minutes=date.getMinutes();
                                                int temp=(hour-10)*60+minutes;
                                                if(temp<0){
                                                    temp=0;
                                                }
                                                totalTime+=temp;
                                                break;
                                        }
                                        break;
                                    case 2://迟到上班
                                        absenceCount++;
                                        switch (endSign){
                                            case 1://准时下班
                                                Date date=object.getDate("StartTime");
                                                int hour=date.getHours();
                                                int minutes=date.getMinutes();
                                                int temp=(17-hour)*60+60-minutes;
                                                if(temp<0){
                                                    temp=0;
                                                }
                                                totalTime+=temp;
                                                break;
                                            case 2://早退下班
                                                absenceCount++;
                                                Date startDate=object.getDate("StartTime");
                                                int startHour=startDate.getHours();
                                                int startMin=startDate.getMinutes();
                                                Date endDate=object.getDate("EndTime");
                                                int endHour=endDate.getHours();
                                                int endMin=endDate.getMinutes();
                                                totalTime+=((endHour-startHour)*60+endMin-startMin);
                                                break;
                                        }
                                        break;
                                }
                            }
                            Calendar calendar=Calendar.getInstance();
                            calendar.set(Calendar.YEAR,Integer.valueOf(mYears.get(mCurrentYear)));
                            calendar.set(Calendar.MONTH,mCurrentMonth);
                            mList.clear();
                            int size=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                            for(int i=1;i<=size;i++){
                                calendar.set(Calendar.DAY_OF_MONTH,i);
                                String date=String.format(Locale.CHINA,"%d月%d日 %s",mCurrentMonth+1,i, DateUtil.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
                                if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                                    mList.add(new AttendanceBean(1,date,map.get(i)));
                                }else{
                                    mList.add(new AttendanceBean(0,date,map.get(i)));
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                            mRecy.smoothScrollToPosition(0);


                            int totalHour= (int) (totalTime/60);
                            int totalMin= (int) (totalTime%60);
                            mTotalTime.setText(String.format(Locale.CHINA,"总工时:%d小时%d分钟",totalHour,totalMin));
                            mAbsenceCount.setText(String.format(Locale.CHINA,"缺勤:%d次",absenceCount));
                        }else{
                            mTotalTime.setText("总工时:0小时");
                            mAbsenceCount.setText("缺勤:0次");
                            mList.clear();
                            mAdapter.notifyDataSetChanged();
                        }

                    }else{
                        onError(e.getMessage());
                    }
                }
            });
        }else{
            showToast(NetworkUtil.tip);
        }
    }

    public void setText(TextView view, String str, String title) {
        if (str == null || str.isEmpty()) {
            view.setText(String.format("%s:%s", title, "暂无信息"));
        } else {
            view.setText(String.format("%s:%s", title, str));
        }
    }

    @Override
    public void doLoadData(int type) {
        mRecy.refreshComplete();
        updateList();
    }
}
