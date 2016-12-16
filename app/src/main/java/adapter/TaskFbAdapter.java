package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by Jason on 2016/12/14.
 */

public class TaskFbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<AVObject> mList;
    private SimpleDateFormat mFormat;

    public TaskFbAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mList=new ArrayList<>();
        mFormat=new SimpleDateFormat("MM月dd日HH:mm", Locale.CHINA);
    }

    public void setDataSource(List<AVObject> list){
        this.mList=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskFbHolder(mInflater.inflate(R.layout.task_detail_feedbak_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TaskFbHolder fb= (TaskFbHolder) holder;
        AVObject object=mList.get(position);
        int type=object.getInt("type");
        AVUser user=object.getAVUser("user");
        switch (type){
            case 0:
                fb.img.setImageResource(R.drawable.task_img_system_def);
                fb.name.setText("任务组");
                fb.name.setTextColor(Color.RED);
                fb.content.setText(object.getString("content"));
                break;
            case 1:
                AVFile img=user.getAVFile("portrait");
                if(img!=null){
                    Glide.with(mContext).load(img.getUrl())
                            .dontAnimate()
                            .centerCrop()
                            .bitmapTransform(new GlideCircleTransform(mContext))
                            .placeholder(R.drawable.dayhr_userphoto_def)
                            .into(fb.img);
                }else{
                    fb.img.setImageResource(R.drawable.dayhr_userphoto_def);
                }
                fb.name.setText(user.getString("name"));
                fb.content.setText(object.getString("content"));
                break;
        }
        fb.time.setText(mFormat.format(object.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TaskFbHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;
        TextView content;
        TextView time;

        public TaskFbHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.TaskActivity_Feedback_item_img);
            name= (TextView) itemView.findViewById(R.id.TaskActivity_Feedback_item_name);
            content= (TextView) itemView.findViewById(R.id.TaskActivity_Feedback_item_content);
            time= (TextView) itemView.findViewById(R.id.TaskActivity_Feedback_item_time);
        }
    }
}
