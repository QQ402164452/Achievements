package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import bean.AppBean;
import interfaces.OnCustomItemClickListener;

/**
 * Created by Jason on 2016/11/30.
 */

public class AppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<AppBean> mList;
    private OnCustomItemClickListener mListener;

    public AppAdapter(Context context,ArrayList<AppBean> list){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
        this.mList=list;
    }

    public void setListener(OnCustomItemClickListener OnCustomItemClickListener){
        mListener= OnCustomItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppItem holder=new AppItem(mInflater.inflate(R.layout.app_recyclerview_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        AppItem item= (AppItem) holder;
        item.img.setImageResource(mList.get(position).getImg());
        item.title.setText(mList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClickListener(holder.itemView,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class AppItem extends RecyclerView.ViewHolder{
        TextView title;
        ImageView img;

        public AppItem(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.AppFragment_app_item_title);
            img= (ImageView) itemView.findViewById(R.id.AppFragment_app_item_img);
        }
    }
}
