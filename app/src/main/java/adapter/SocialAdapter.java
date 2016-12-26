package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2016/12/20.
 */

public class SocialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public SocialAdapter(Context context){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(mContext);
        this.mList=new ArrayList<>();
        for(int i=0;i<20;i++){
            mList.add(String.valueOf(i));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SocialHolder(mInflater.inflate(R.layout.social_fragment_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SocialHolder extends RecyclerView.ViewHolder{
        ImageView userImg;
        TextView userName;
        TextView time;
        TextView content;
        RelativeLayout shareBtn;
        RelativeLayout commentBtn;
        RelativeLayout likeBtn;
        TextView share;
        TextView comment;
        TextView like;

        public SocialHolder(View itemView) {
            super(itemView);
            userImg= (ImageView) itemView.findViewById(R.id.SocialFragment_list_item_User_img);
            userName= (TextView) itemView.findViewById(R.id.SocialFragment_list_item_User_name);
            time= (TextView) itemView.findViewById(R.id.SocialFragment_list_item_time);
            shareBtn= (RelativeLayout) itemView.findViewById(R.id.SocialFragment_list_item_share_btn);
            commentBtn= (RelativeLayout) itemView.findViewById(R.id.SocialFragment_list_item_comment_btn);
            likeBtn= (RelativeLayout) itemView.findViewById(R.id.SocialFragment_list_item_like_btn);
            share= (TextView) itemView.findViewById(R.id.SocialFragment_list_item_share);
            comment= (TextView) itemView.findViewById(R.id.SocialFragment_list_item_comment);
            like= (TextView) itemView.findViewById(R.id.SocialFragment_list_item_like);
        }
    }

}
