package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import interfaces.OnCustomItemClickListener;

/**
 * Created by Jason on 2016/12/2.
 */

public class SelectTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnCustomItemClickListener onClickListener;

    public SelectTimeAdapter(Context context){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(mContext);
    }

    public void setDataSoure(ArrayList<String> list){
        this.mList=list;
    }

    public void setOnClickListener(OnCustomItemClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextHolder holder=new TextHolder(mInflater.inflate(R.layout.report_popup_select_list_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final TextHolder textHolder= (TextHolder) holder;
        textHolder.text.setText(mList.get(position));
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

    class TextHolder extends RecyclerView.ViewHolder{
        TextView text;

        public TextHolder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.ReportActivity_select_list_item_text);
        }
    }
}
