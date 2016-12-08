package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.jason.achievements.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import interfaces.OnCustomItemClickListener;
import utils.DateUtil;
import utils.ExamineUtil;

/**
 * Created by Jason on 2016/12/6.
 */

public class MyExamineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AVObject> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mTypes;
    private ArrayList<String> mType;
    private ArrayList<String> mMon;
    private OnCustomItemClickListener mListener;


    public MyExamineAdapter(Context context){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(mContext);
        mList=new ArrayList<>();
        mTypes= ExamineUtil.getInstance().getTypes();
        mType=ExamineUtil.getInstance().getType();
        mMon=ExamineUtil.getInstance().getMon();
    }

    public void setDataSource(List<AVObject> list){
        this.mList=list;
    }

    public void setListener(OnCustomItemClickListener onCustomItemClickListener){
        mListener=onCustomItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyExamineHolder holder=new MyExamineHolder(mInflater.inflate(R.layout.fragment_my_examine_list_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position,List payLoad) {
        final MyExamineHolder myHolder= (MyExamineHolder) holder;
        if(payLoad.isEmpty()){
            AVObject object=mList.get(position);
            int type=object.getInt("type");
            myHolder.title.setText(String.format("我的%s申请",mType.get(type)));
            myHolder.time.setText(DateUtil.getNormal(object.getCreatedAt()));
            switch (type){
                case 0:
                    myHolder.type.setText(mTypes.get(object.getInt("schedule")));
                    myHolder.beginTime.setText("开始时间:"+DateUtil.getNoon(object.getDate("begin"))+" "+
                            mMon.get(object.getInt("beginNoon"))+" "+DateUtil.getDayOfDate(object.getDate("begin")));
                    myHolder.endTime.setText("结束时间:"+DateUtil.getNoon(object.getDate("end"))+" "+
                            mMon.get(object.getInt("endNoon"))+" "+DateUtil.getDayOfDate(object.getDate("end")));
                    break;
                case 1:
                    myHolder.type.setText(mType.get(type));
                    myHolder.beginTime.setText("开始时间:"+DateUtil.getExtra(object.getDate("begin"))+" "+DateUtil.getDayOfDate(object.getDate("begin")));
                    myHolder.endTime.setText("结束时间:"+DateUtil.getExtra(object.getDate("end"))+" "+DateUtil.getDayOfDate(object.getDate("end")));
                    break;
                case 2:
                    myHolder.type.setText(mType.get(type));
                    myHolder.beginTime.setText("开始时间:"+DateUtil.getNoon(object.getDate("begin"))+" "+
                            mMon.get(object.getInt("beginNoon"))+" "+DateUtil.getDayOfDate(object.getDate("begin")));
                    myHolder.endTime.setText("结束时间:"+DateUtil.getNoon(object.getDate("end"))+" "+
                            mMon.get(object.getInt("endNoon"))+" "+DateUtil.getDayOfDate(object.getDate("end")));
                    break;
            }
            myHolder.reason.setText("原因:"+object.getString("reason"));
            int sign=object.getInt("sign");
            switch (sign){
                case 0:
                    myHolder.state.setText("审核中");
                    myHolder.state.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.list_icon_doing_def),null,null,null);
                    myHolder.state.setTextColor(mContext.getResources().getColor(R.color.red_light));
                    break;
                case 1:
                    myHolder.state.setText("已通过");
                    myHolder.state.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.list_icon_done_def),null,null,null);
                    myHolder.state.setTextColor(mContext.getResources().getColor(R.color.green_light));
                    break;
                case 2:
                    myHolder.state.setText("未通过");
                    myHolder.state.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.list_icon_backup_def),null,null,null);
                    myHolder.state.setTextColor(mContext.getResources().getColor(R.color.red_light));
                    break;
                case 3:
                    myHolder.state.setText("催办中");
                    myHolder.state.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.list_icon_doing_def),null,null,null);
                    myHolder.state.setTextColor(mContext.getResources().getColor(R.color.red_light));
                    break;

            }
        }else{
            int sign= (int) payLoad.get(0);
            if(sign==0){
                myHolder.state.setText("催办中");
            }
        }

        if(mListener!=null){
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClickListener(myHolder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyExamineHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        TextView type;
        TextView state;
        TextView beginTime;
        TextView endTime;
        TextView reason;

        public MyExamineHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.MyExamineFragment_list_item_title);
            time= (TextView) itemView.findViewById(R.id.MyExamineFragment_list_item_time);
            type= (TextView) itemView.findViewById(R.id.MyExamineFragment_list_item_type);
            state= (TextView) itemView.findViewById(R.id.MyExamineFragment_list_item_state);
            beginTime= (TextView) itemView.findViewById(R.id.MyExamineFragment_list_item_beginTime);
            endTime= (TextView) itemView.findViewById(R.id.MyExamineFragment_list_item_endTime);
            reason= (TextView) itemView.findViewById(R.id.MyExamineFragment_list_item_reason);
        }
    }
}
