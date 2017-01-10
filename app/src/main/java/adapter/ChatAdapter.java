package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import bean.ChatBean;
import customView.GlideCircleTransform;

/**
 * Created by Jason on 2016/11/30.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<ChatBean> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public ChatAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
    }

    public void setSource(ArrayList<ChatBean> list){
        this.mList=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder=null;
        switch (viewType){
            case 0:
                viewHolder=new MeHolder(mInflater.inflate(R.layout.chat_me,parent,false));
                break;
            case 1:
                viewHolder = new OtherHolder(mInflater.inflate(R.layout.chat_other,parent,false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatBean data=mList.get(position);
        switch (getItemViewType(position)){
            case 0:
                MeHolder me= (MeHolder) holder;
                Glide.with(mContext).
                        load(data.getImg()).
                        centerCrop().
                        dontAnimate().
                        transform(new GlideCircleTransform(mContext)).
                        placeholder(R.drawable.dayhr_userphoto_def).
                        into(me.img);
                me.content.setText(data.getContent());
                if(data.isSuccess()){
                    me.error.setVisibility(View.GONE);
                }else{
                    me.error.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                OtherHolder other= (OtherHolder) holder;
                Glide.with(mContext).
                        load(data.getImg()).
                        centerCrop().
                        dontAnimate().
                        transform(new GlideCircleTransform(mContext)).
                        placeholder(R.mipmap.ic_launcher).
                        into(other.img);
                other.content.setText(data.getContent());
                if(data.isSuccess()){
                    other.error.setVisibility(View.GONE);
                }else{
                    other.error.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int pos){
        return mList.get(pos).getType();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private static class MeHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView content;
        ImageView error;

        private MeHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.Chat_me_img);
            content= (TextView) itemView.findViewById(R.id.Chat_me_content);
            error= (ImageView) itemView.findViewById(R.id.Chat_me_error_btn);
        }
    }

    private static class OtherHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView content;
        ImageView error;

        private OtherHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.Chat_other_img);
            content= (TextView) itemView.findViewById(R.id.Chat_other_content);
            error= (ImageView) itemView.findViewById(R.id.Chat_other_error_btn);
        }
    }
}
