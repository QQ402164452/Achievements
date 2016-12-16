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
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import customView.GlideCircleTransform;

/**
 * Created by Jason on 2016/12/10.
 */

public class NewTaskParAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<AVUser> mList;

    public NewTaskParAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
        mList=new ArrayList<>();
    }

    public void setDataSource(ArrayList<AVUser> user){
        this.mList=user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewTaskParHolder(mInflater.inflate(R.layout.new_task_participant_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewTaskParHolder par= (NewTaskParHolder) holder;
        AVUser user=mList.get(position);
        AVFile img=user.getAVFile("portrait");
        if(img!=null){
            Glide.with(mContext).load(img.getUrl())
                    .centerCrop()
                    .dontAnimate()
                    .bitmapTransform(new GlideCircleTransform(mContext))
                    .placeholder(R.drawable.dayhr_userphoto_def)
                    .into(par.img);
        }else{
            par.img.setImageResource(R.drawable.dayhr_userphoto_def);
        }
        par.name.setText(user.getString("name"));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class NewTaskParHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;

        public NewTaskParHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.NewTaskActivity_parList_item_img);
            name= (TextView) itemView.findViewById(R.id.NewTaskActivity_parList_item_name);
        }
    }
}
