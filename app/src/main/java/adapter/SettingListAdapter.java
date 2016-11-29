package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import interfaces.onCustomItemClickListener;

/**
 * Created by Jason on 2016/11/25.
 */

public class SettingListAdapter extends RecyclerView.Adapter<SettingListAdapter.SviewHolder> {
    private Context mContext;
    private ArrayList<String> mList;
    private LayoutInflater mInflater;
    private onCustomItemClickListener onClickListener;

    public SettingListAdapter(Context context,ArrayList<String> list){
        this.mContext=context;
        this.mList=list;
        mInflater=LayoutInflater.from(mContext);
    }

    public void setOnClickListener(onCustomItemClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    @Override
    public SviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SviewHolder holder=new SviewHolder(mInflater.inflate(R.layout.setting_recyclerview_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final SviewHolder holder, final int position) {
        holder.title.setText(mList.get(position));
        if(onClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClickListener(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SviewHolder extends RecyclerView.ViewHolder{

        TextView title;

        public SviewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.SettingActivity_List_item_title);
        }
    }
}
