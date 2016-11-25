package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;

/**
 * Created by Jason on 2016/11/25.
 */

public class SettingListAdapter extends RecyclerView.Adapter<SettingListAdapter.SviewHolder> {
    private Context mContext;
    private ArrayList<String> mList;
    private LayoutInflater mInflater;

    public SettingListAdapter(Context context){
        this.mContext=context;
        mList=new ArrayList<>();
        mList.add("新消息通知");
        mList.add("检查版本更新");
        mList.add("关于我们");
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public SviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SviewHolder holder=new SviewHolder(mInflater.inflate(R.layout.setting_recyclerview_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(SviewHolder holder, int position) {
        holder.title.setText(mList.get(position));
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
