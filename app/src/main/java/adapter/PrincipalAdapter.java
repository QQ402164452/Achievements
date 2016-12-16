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
import java.util.List;

import customView.GlideCircleTransform;
import interfaces.OnCustomItemClickListener;

/**
 * Created by Jason on 2016/12/10.
 */

public class PrincipalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<AVUser> mList;
    private int mCurrentPos=-1;
    private OnCustomItemClickListener mListener;

    public PrincipalAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
        mList=new ArrayList<>();
    }

    public void setDataSource(ArrayList<AVUser> user){
        this.mList=user;
    }

    public void setListener(OnCustomItemClickListener listener){
        this.mListener=listener;
    }

    public AVUser getCurrentSelect(){
        if(mCurrentPos==-1){
            return null;
        }else{
            return mList.get(mCurrentPos);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrincipalHolder(mInflater.inflate(R.layout.principal_recycler_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, List payLoads) {
        final PrincipalHolder pri= (PrincipalHolder) holder;
        if(payLoads.isEmpty()){
            AVUser user=mList.get(position);
            AVFile img=user.getAVFile("portrait");
            if(img!=null){
                Glide.with(mContext).load(img.getUrl())
                        .centerCrop()
                        .dontAnimate()
                        .bitmapTransform(new GlideCircleTransform(mContext))
                        .placeholder(R.drawable.dayhr_userphoto_def)
                        .into(pri.img);
            }else{
                pri.img.setImageResource(R.drawable.dayhr_userphoto_def);
            }
            pri.name.setText(user.getString("name"));
            if(mCurrentPos==position){
                pri.select.setVisibility(View.VISIBLE);
            }else{
                pri.select.setVisibility(View.GONE);
            }
        }else{
            pri.select.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentPos!=position){
                    if(mCurrentPos!=-1){
                        notifyItemChanged(mCurrentPos,0);
                    }
                    pri.select.setVisibility(View.VISIBLE);
                    mCurrentPos=position;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class PrincipalHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;
        ImageView select;

        public PrincipalHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.PrincipalActivity_list_item_img);
            name= (TextView) itemView.findViewById(R.id.PrincipalActivity_list_item_name);
            select= (ImageView) itemView.findViewById(R.id.PrincipalActivity_list_item_select);
        }
    }
}
