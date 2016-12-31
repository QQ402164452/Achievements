package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.List;

import view.PhotoActivity;

/**
 * Created by Jason on 2016/12/28.
 */

public class SocialChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mList;

    public SocialChildAdapter(Context context, List<String> list){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(mContext);
        this.mList=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SocialListHolder(mInflater.inflate(R.layout.social_child_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SocialListHolder item= (SocialListHolder) holder;
        final String imgUrl=mList.get(position);
        if(imgUrl!=null&&!imgUrl.isEmpty()){
            Glide.with(mContext).load(imgUrl+"?imageView/1/w/400/h/400/q/30/format/jpeg")
                    .dontAnimate()
                    .into(item.img);
        }else{
            item.img.setImageResource(R.drawable.temp);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PhotoActivity.class);
                intent.putStringArrayListExtra(PhotoActivity.PHOTO_URL_LIST,new ArrayList<>(mList));
                intent.putExtra(PhotoActivity.PHOTO_CURRENT_POSITION,position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private static class SocialListHolder extends RecyclerView.ViewHolder{
        ImageView img;

        public SocialListHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.SocialFragment_Child_List_Item_Img);
        }
    }


}
