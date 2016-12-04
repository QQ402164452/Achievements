package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import customView.GlideCircleTransform;
import interfaces.OnCustomItemClickListener;

/**
 * Created by Jason on 2016/12/3.
 */

public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<AVObject> mList;
    private LayoutInflater mInflater;
    private OnCustomItemClickListener mListener;

    public ReportAdapter(Context context){
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mList=new ArrayList<>();
    }

    public void setDataSource(List<AVObject> list){
        this.mList=list;
    }

    public void setClickListener(OnCustomItemClickListener onCustomItemClickListener){
        this.mListener=onCustomItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReportViewHolder holder=new ReportViewHolder(mInflater.inflate(R.layout.my_report_fragment_list_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ReportViewHolder item= (ReportViewHolder) holder;
        AVObject object=mList.get(position);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        item.upTime.setText(format.format(object.getCreatedAt().getTime()));
        item.content.setText(String.format("工作总结\n%s\n工作计划:\n%s",object.getString("summary"),object.getString("plan")));
        int type=object.getInt("type");
        switch (type){
            case 0:
                item.time.setText(String.format("日报(%s %s)",object.getString("firstday"),object.getString("lastday")));
                break;
            case 1:
                item.time.setText(String.format("周报(%s)",object.getString("firstday")));
                break;
            case 2:
                item.time.setText(String.format("月报(%s年%s月)",object.getString("firstday"),object.getString("lastday")));
                break;
        }

        AVUser user=object.getAVUser("reporter");
        item.name.setText(user.getString("name"));
        AVFile img=user.getAVFile("portrait");
        if(img!=null){
            Glide.with(mContext).load(img.getUrl())
                    .centerCrop()
                    .transform(new GlideCircleTransform(mContext))
                    .dontAnimate()
                    .placeholder(R.drawable.dayhr_userphoto_def)
                    .into(item.img);
        }else{
            item.img.setImageResource(R.drawable.dayhr_userphoto_def);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClickListener(holder.itemView,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;
        TextView time;
        TextView content;
        TextView upTime;

        public ReportViewHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.MyReportFragment_list_item_img);
            name= (TextView) itemView.findViewById(R.id.MyReportFragment_list_item_name);
            time= (TextView) itemView.findViewById(R.id.MyReportFragment_list_item_time);
            content= (TextView) itemView.findViewById(R.id.MyReportFragment_list_item_content);
            upTime= (TextView) itemView.findViewById(R.id.MyReportFragment_list_item_upTime);
        }
    }
}
