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

public class SocLikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<AVObject> mList;
    private SimpleDateFormat mFormat;

    public SocLikeAdapter(Context context){
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
        return new SocLikeHolder(mInflater.inflate(R.layout.social_list_like_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SocLikeHolder item= (SocLikeHolder) holder;
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private static class SocLikeHolder extends RecyclerView.ViewHolder{
        ImageView userImg;
        TextView userName;
        TextView time;

        public SocLikeHolder(View itemView) {
            super(itemView);
            userImg= (ImageView) itemView.findViewById(R.id.SocialDetailActivity_Like_User_img);
            userName= (TextView) itemView.findViewById(R.id.SocialDetailActivity_Like_User_Name);
            time= (TextView) itemView.findViewById(R.id.SocialDetailActivity_Like_Time);
        }
    }
}
