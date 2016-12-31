package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.jason.achievements.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import bean.AttendanceBean;
import customView.AttendanceView;

/**
 * Created by Jason on 2016/12/17.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<AttendanceBean> mList;
    private SimpleDateFormat mFormat;
    private double totalTime=480.0;//8小时x60分钟

    public AttendanceAdapter(Context context,ArrayList<AttendanceBean> list){
        this.mContext=context;
        this.mList=list;
        this.mInflater=LayoutInflater.from(mContext);
        mFormat=new SimpleDateFormat("HH:mm", Locale.CHINA);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AttendanceHolder(mInflater.inflate(R.layout.attendance_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AttendanceHolder attendance= (AttendanceHolder) holder;
        AttendanceBean bean=mList.get(position);
        attendance.time.setText(bean.getDate());
        int itemType=bean.getType();
        switch (itemType){
            case 0:
                attendance.attendanceView.setVisibility(View.VISIBLE);
                attendance.weekend.setVisibility(View.GONE);
                attendance.stateViewGroup.setVisibility(View.VISIBLE);

                AVObject object=bean.getAvObject();
                if(object!=null){
                    int startSign=object.getInt("StartSign");
                    int endSign=object.getInt("EndSign");
                    if(startSign==1&&endSign==1){
                        attendance.state.setText("正常");
                        attendance.state.setTextColor(mContext.getResources().getColor(R.color.green_light));
                        attendance.stateImg.setImageResource(R.drawable.approval_icon_pass_def);
                    }else{
                        attendance.state.setText("异常");
                        attendance.state.setTextColor(mContext.getResources().getColor(R.color.red_light));
                        attendance.stateImg.setImageResource(R.drawable.approval_icon_back_def);
                    }
                    attendance.attendanceView.setDefaultColor(0xff99cc00);
                    switch (startSign){
                        case 0:
                            attendance.attendanceView.setStartTime("00:00");
                            attendance.attendanceView.setBeginStateText("空");
                            attendance.attendanceView.setBeginStateColor(0xffFF7223);
                            break;
                        case 1:
                            attendance.attendanceView.setStartTime(mFormat.format(object.getDate("StartTime")));
                            attendance.attendanceView.setBeginStateText("准时");
                            attendance.attendanceView.setBeginStateColor(0xff99cc00);
                            break;
                        case 2:
                            attendance.attendanceView.setStartTime(mFormat.format(object.getDate("StartTime")));
                            attendance.attendanceView.setBeginStateText("迟到");
                            attendance.attendanceView.setBeginStateColor(0xffff4444);
                            Date date=object.getDate("StartTime");
                            int hour=date.getHours();
                            int minutes=date.getMinutes();
                            double percentage=((hour-10)*60+minutes)/totalTime;
                            if(percentage>=1.0){
                                percentage=1;
                            }else if(percentage<0){
                                percentage=0;
                            }
                            attendance.attendanceView.setLeftPercentage(percentage);
                            break;
                    }
                    switch (endSign){
                        case 0:
                            attendance.attendanceView.setEndTime("00:00");
                            attendance.attendanceView.setEndStateText("空");
                            attendance.attendanceView.setEndStateColor(0xffFF7223);
                            break;
                        case 1:
                            attendance.attendanceView.setEndTime(mFormat.format(object.getDate("EndTime")));
                            attendance.attendanceView.setEndStateText("准时");
                            attendance.attendanceView.setEndStateColor(0xff99cc00);
                            break;
                        case 2:
                            attendance.attendanceView.setEndTime(mFormat.format(object.getDate("EndTime")));
                            attendance.attendanceView.setEndStateText("早退");
                            attendance.attendanceView.setEndStateColor(0xffff4444);
                            Date date=object.getDate("EndTime");
                            int hour=date.getHours();
                            int minutes=date.getMinutes();
                            double percentage=((17-hour)*60+60-minutes)/totalTime;
                            if(percentage>1.0){
                                percentage=1;
                            }else if(percentage<0){
                                percentage=0;
                            }
                            attendance.attendanceView.setRightPercentage(percentage);
                            break;
                    }
                }else{
                    attendance.attendanceView.setStartTime("00:00");
                    attendance.attendanceView.setBeginStateText("未签");
                    attendance.attendanceView.setEndTime("00:00");
                    attendance.attendanceView.setEndStateText("未签");
                    attendance.attendanceView.setBeginStateColor(0xffFF7223);
                    attendance.attendanceView.setEndStateColor(0xffFF7223);
                    attendance.attendanceView.setDefaultColor(0xffFF7223);
                    attendance.state.setText("异常");
                    attendance.state.setTextColor(mContext.getResources().getColor(R.color.red_light));
                    attendance.stateImg.setImageResource(R.drawable.approval_icon_back_def);
                }
                break;
            case 1:
                attendance.attendanceView.setVisibility(View.GONE);
                attendance.weekend.setVisibility(View.VISIBLE);
                attendance.stateViewGroup.setVisibility(View.GONE);
                break;
        }

        attendance.attendanceView.invalidate();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class AttendanceHolder extends RecyclerView.ViewHolder{
        TextView time;
        AttendanceView attendanceView;
        TextView weekend;
        TextView state;
        ImageView stateImg;
        LinearLayout stateViewGroup;

        public AttendanceHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.AttendanceActivity_list_item_time);
            attendanceView= (AttendanceView) itemView.findViewById(R.id.AttendanceActivity_list_item_attendanceView);
            weekend= (TextView) itemView.findViewById(R.id.AttendanceActivity_list_item_weekend);
            state= (TextView) itemView.findViewById(R.id.AttendanceActivity_list_item_state);
            stateImg= (ImageView) itemView.findViewById(R.id.AttendanceActivity_list_item_state_img);
            stateViewGroup= (LinearLayout) itemView.findViewById(R.id.AttendanceActivity_list_item_state_ViewGroup);
        }
    }
}
