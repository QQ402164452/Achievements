package adapter;

import android.content.Context;
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
 * Created by Jason on 2016/12/29.
 */

public class SocComAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<AVObject> mList;
    private SimpleDateFormat mFormat;

    public SocComAdapter(Context context){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(mContext);
        mFormat=new SimpleDateFormat("MM月dd日HH:mm", Locale.CHINA);
        mList=new ArrayList<>();
    }

    public void setDataSource(List<AVObject> list){
        this.mList=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SocComHolder(mInflater.inflate(R.layout.social_list_comment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SocComHolder item= (SocComHolder) holder;
        AVObject avObject=mList.get(position);
        AVUser user=avObject.getAVUser("user");
        AVFile imgFile=user.getAVFile("portrait");
        if(imgFile!=null){
            Glide.with(mContext).load(imgFile.getUrl())
                    .dontAnimate()
                    .transform(new GlideCircleTransform(mContext))
                    .into(item.userImg);
        }else{
            item.userImg.setImageResource(R.drawable.dayhr_userphoto_def);
        }
        item.userName.setText(user.getString("name"));
        item.time.setText(mFormat.format(avObject.getCreatedAt()));
        item.content.setText(avObject.getString("content"));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private static class SocComHolder extends RecyclerView.ViewHolder{
        ImageView userImg;
        TextView userName;
        TextView time;
        TextView content;

        private SocComHolder(View itemView) {
            super(itemView);
            userImg= (ImageView) itemView.findViewById(R.id.SocialDetailActivity_Comment_User_img);
            userName= (TextView) itemView.findViewById(R.id.SocialDetailActivity_Comment_User_Name);
            time= (TextView) itemView.findViewById(R.id.SocialDetailActivity_Comment_Time);
            content= (TextView) itemView.findViewById(R.id.SocialDetailActivity_Comment_Content);
        }
    }
}
