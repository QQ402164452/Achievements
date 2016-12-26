package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.List;

import interfaces.OnCustomItemClickListener;
import utils.DateUtil;
import utils.ExamineUtil;

/**
 * Created by Jason on 2016/12/7.
 */

public class OtherExamineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AVObject> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mTypes;
    private ArrayList<String> mType;
    private ArrayList<String> mMon;
    private OnCustomItemClickListener mListener;


    public OtherExamineAdapter(Context context,List<AVObject> list) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        mList = list;
        mTypes = ExamineUtil.getInstance().getTypes();
        mType = ExamineUtil.getInstance().getType();
        mMon = ExamineUtil.getInstance().getMon();
    }

    public void setListener(OnCustomItemClickListener onCustomItemClickListener) {
        mListener = onCustomItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OtherExamineHolder holder = new OtherExamineHolder(mInflater.inflate(R.layout.fragment_other_examine_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, List payLoad) {
        final OtherExamineHolder myHolder = (OtherExamineHolder) holder;
        if(payLoad.isEmpty()){
            AVObject object = mList.get(position);
            AVUser user = object.getAVUser("user");
            int type = object.getInt("type");
            myHolder.title.setText(String.format("%s的%s申请", user.getString("name"), mType.get(type)));
            myHolder.time.setText(DateUtil.getNormal(object.getCreatedAt()));
            switch (type) {
                case 0:
                    myHolder.type.setText(mTypes.get(object.getInt("schedule")));
                    myHolder.beginTime.setText("开始时间:" + DateUtil.getNoon(object.getDate("begin")) + " " +
                            mMon.get(object.getInt("beginNoon")) + " " + DateUtil.getDayOfDate(object.getDate("begin")));
                    myHolder.endTime.setText("结束时间:" + DateUtil.getNoon(object.getDate("end")) + " " +
                            mMon.get(object.getInt("endNoon")) + " " + DateUtil.getDayOfDate(object.getDate("end")));
                    break;
                case 1:
                    myHolder.type.setText(mType.get(type));
                    myHolder.beginTime.setText("开始时间:" + DateUtil.getExtra(object.getDate("begin")) + " " + DateUtil.getDayOfDate(object.getDate("begin")));
                    myHolder.endTime.setText("结束时间:" + DateUtil.getExtra(object.getDate("end")) + " " + DateUtil.getDayOfDate(object.getDate("end")));
                    break;
                case 2:
                    myHolder.type.setText(mType.get(type));
                    myHolder.beginTime.setText("开始时间:" + DateUtil.getNoon(object.getDate("begin")) + " " +
                            mMon.get(object.getInt("beginNoon")) + " " + DateUtil.getDayOfDate(object.getDate("begin")));
                    myHolder.endTime.setText("结束时间:" + DateUtil.getNoon(object.getDate("end")) + " " +
                            mMon.get(object.getInt("endNoon")) + " " + DateUtil.getDayOfDate(object.getDate("end")));
                    break;
            }
            myHolder.reason.setText("原因:" + object.getString("reason"));
            int sign = object.getInt("sign");
            if (sign == 3) {
                myHolder.urgeImg.setVisibility(View.VISIBLE);
            }else{
                myHolder.urgeImg.setVisibility(View.GONE);
            }

        }else{

        }
        if (mListener != null)

        {
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClickListener(myHolder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class OtherExamineHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView type;
        ImageView urgeImg;
        TextView beginTime;
        TextView endTime;
        TextView reason;

        public OtherExamineHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.OtherExamineFragment_list_item_title);
            time = (TextView) itemView.findViewById(R.id.OtherExamineFragment_list_item_time);
            type = (TextView) itemView.findViewById(R.id.OtherExamineFragment_list_item_type);
            urgeImg = (ImageView) itemView.findViewById(R.id.OtherExamineFragment_Urge_Img);
            beginTime = (TextView) itemView.findViewById(R.id.OtherExamineFragment_list_item_beginTime);
            endTime = (TextView) itemView.findViewById(R.id.OtherExamineFragment_list_item_endTime);
            reason = (TextView) itemView.findViewById(R.id.OtherExamineFragment_list_item_reason);
        }
    }

}
