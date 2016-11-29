package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import bean.MyDataBean;

/**
 * Created by Jason on 2016/11/25.
 */

public class MyDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MyDataBean> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MyDataAdapter(Context context,ArrayList<MyDataBean> mList){
        this.mContext=context;
        this.mList=mList;
        mInflater=LayoutInflater.from(mContext);
    }

    public MyDataAdapter(Context context){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(mContext);
    }

    public void setData(ArrayList<MyDataBean> mList){
        this.mList=mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        switch (viewType){
            case 0:
                holder=new divider(mInflater.inflate(R.layout.my_recycleview_divider,parent,false));
                break;
            case 1:
                holder=new viewHolder(mInflater.inflate(R.layout.my_recycleview_item,parent,false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 1:
                viewHolder h=(viewHolder)holder;
                h.title.setText(mList.get(position).getTitle());
                h.content.setText(mList.get(position).getContent());
                h.content.setHint("暂无信息");
                break;
            case 0:
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position){
        return mList.get(position).getType();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;

        public viewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.MyActivity_RecyclerView_title);
            content= (TextView) itemView.findViewById(R.id.MyActivity_RecyclerView_content);
        }
    }

    class divider extends RecyclerView.ViewHolder {

        public divider(View itemView) {
            super(itemView);
        }

    }
}
