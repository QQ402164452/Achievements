package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

import bean.RealmMessage;
import bean.RealmUser;
import customView.GlideCircleTransform;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Jason on 2017/1/9.
 */

public class RealmIMAdapter extends RealmRecyclerViewAdapter<RealmMessage,RecyclerView.ViewHolder> {
    private final int MY_ITEM=0;
    private final int TO_USER_ITEM=1;
    private RealmUser toUser;
    private SimpleDateFormat mFormat;

    // 时间间隔最小为十分钟
    private final long TIME_INTERVAL = 10 * 60 * 1000;

    public RealmIMAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<RealmMessage> data, boolean autoUpdate,RealmUser toUser) {
        super(context, data, autoUpdate);
        this.toUser=toUser;
        mFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case MY_ITEM:
                viewHolder = new RealmIMAdapter.MeHolder(inflater.inflate(R.layout.chat_me, parent, false));
                break;
            case TO_USER_ITEM:
                viewHolder = new RealmIMAdapter.OtherHolder(inflater.inflate(R.layout.chat_other, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RealmMessage msg = getData().get(position);
        AVFile imgFile;
        AVUser user;
        switch (getItemViewType(position)) {
            case MY_ITEM:
                RealmIMAdapter.MeHolder me = (RealmIMAdapter.MeHolder) holder;
                user = AVUser.getCurrentUser();
                imgFile = user.getAVFile("portrait");
                if (imgFile != null) {
                    Glide.with(context).
                            load(imgFile.getUrl()).
                            centerCrop().
                            dontAnimate().
                            transform(new GlideCircleTransform(context)).
                            into(me.img);
                } else {
                    me.img.setImageResource(R.drawable.dayhr_userphoto_def);
                }
                me.content.setText(msg.getContent());
                if(shouldShowTime(position)){
                    me.time.setVisibility(View.VISIBLE);
                    me.time.setText(mFormat.format(msg.getDate()));
                }else {
                    me.time.setVisibility(View.GONE);
                }
                break;
            case TO_USER_ITEM:
                RealmIMAdapter.OtherHolder other = (RealmIMAdapter.OtherHolder) holder;
                if(!toUser.getPortrait().equals("null")){
                    Glide.with(context).
                            load(toUser.getPortrait()).
                            centerCrop().
                            dontAnimate().
                            transform(new GlideCircleTransform(context)).
                            into(other.img);
                }else{
                    other.img.setImageResource(R.drawable.dayhr_userphoto_def);
                }
                other.content.setText(msg.getContent());
                if(shouldShowTime(position)){
                    other.time.setVisibility(View.VISIBLE);
                    other.time.setText(mFormat.format(msg.getDate()));
                }else {
                    other.time.setVisibility(View.GONE);
                }
                break;
        }
    }


    @Override
    public int getItemViewType(int pos) {
        return getData().get(pos).getType();
    }

    private boolean shouldShowTime(int position){
        if(position==0){
            return true;
        }
        long lastTime=getData().get(position-1).getDate();
        long currentTime=getData().get(position).getDate();
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
