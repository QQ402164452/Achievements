package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import customView.GlideCircleTransform;
import im.AVIMClientManager;

/**
 * Created by Jason on 2017/1/3.
 */

public class IMAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AVIMMessage> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private AVUser toUser;
    private SimpleDateFormat mFormat;

    private final int MY_ITEM=0;
    private final int TO_USER_ITEM=1;

    // 时间间隔最小为十分钟
    private final long TIME_INTERVAL = 10 * 60 * 1000;

    public IMAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList=new ArrayList<>();
        mFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
    }

    public void setSource(List<AVIMMessage> list) {
        this.mList = list;
    }

    public void setToUser(AVUser toUser){
        this.toUser=toUser;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case MY_ITEM:
                viewHolder = new MeHolder(mInflater.inflate(R.layout.chat_me, parent, false));
                break;
            case TO_USER_ITEM:
                viewHolder = new OtherHolder(mInflater.inflate(R.layout.chat_other, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AVIMMessage data = mList.get(position);
        AVFile imgFile;
        AVUser user;
        switch (getItemViewType(position)) {
            case MY_ITEM:
                MeHolder me = (MeHolder) holder;
                user = AVUser.getCurrentUser();
                imgFile = user.getAVFile("portrait");
                if (imgFile != null) {
                    Glide.with(mContext).
                            load(imgFile.getUrl()).
                            centerCrop().
                            dontAnimate().
                            transform(new GlideCircleTransform(mContext)).
                            into(me.img);
                } else {
                    me.img.setImageResource(R.drawable.dayhr_userphoto_def);
                }
                if(data instanceof AVIMTextMessage){
                    me.content.setText(((AVIMTextMessage)data).getText());
                }
                if(shouldShowTime(position)){
                    me.time.setVisibility(View.VISIBLE);
                    me.time.setText(mFormat.format(data.getTimestamp()));
                }else {
                    me.time.setVisibility(View.GONE);
                }
                break;
            case TO_USER_ITEM:
                OtherHolder other = (OtherHolder) holder;
                imgFile=toUser.getAVFile("portrait");
                if(imgFile!=null){
                    Glide.with(mContext).
                            load(imgFile.getUrl()).
                            centerCrop().
                            dontAnimate().
                            transform(new GlideCircleTransform(mContext)).
                            into(other.img);
                }else{
                    other.img.setImageResource(R.drawable.dayhr_userphoto_def);
                }
                if(data instanceof AVIMTextMessage){
                    other.content.setText(((AVIMTextMessage)data).getText());
                }
                if(shouldShowTime(position)){
                    other.time.setVisibility(View.VISIBLE);
                    other.time.setText(mFormat.format(data.getTimestamp()));
                }else {
                    other.time.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int pos) {
        AVIMMessage message = mList.get(pos);
        if (message.getFrom().equals(AVIMClientManager.getInstance().getClientId())) {
            return MY_ITEM;
        } else {
            return TO_USER_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private boolean shouldShowTime(int position){
        if(position==0){
            return true;
        }
        long lastTime=mList.get(position-1).getTimestamp();
        long currentTime=mList.get(position).getTimestamp();
        return currentTime-lastTime>TIME_INTERVAL;
    }

    private static class MeHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView content;
        ImageView error;
        TextView time;

        private MeHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.Chat_me_img);
            content = (TextView) itemView.findViewById(R.id.Chat_me_content);
            error = (ImageView) itemView.findViewById(R.id.Chat_me_error_btn);
            time= (TextView) itemView.findViewById(R.id.Chat_me_Time);
        }
    }

    private static class OtherHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView content;
        ImageView error;
        TextView time;

        private OtherHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.Chat_other_img);
            content = (TextView) itemView.findViewById(R.id.Chat_other_content);
            error = (ImageView) itemView.findViewById(R.id.Chat_other_error_btn);
            time= (TextView) itemView.findViewById(R.id.Chat_other_Time);
        }
    }
}
