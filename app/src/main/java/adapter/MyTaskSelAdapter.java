package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jason.achievements.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import interfaces.OnCustomItemClickListener;

/**
 * Created by Jason on 2016/12/11.
 */

public class MyTaskSelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mList;
    private int mCurrent=0;
    private OnCustomItemClickListener mListener;

    public MyTaskSelAdapter(Context context){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(mContext);
        mList=new ArrayList<>();
    }

    public void setDataSource(ArrayList<String> list){
        this.mList=list;
    }

    public void setCurrent(int current){
        this.mCurrent=current;
    }

    public void setListener(OnCustomItemClickListener listener){
        this.mListener=listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelHolder(mInflater.inflate(R.layout.my_task_fragment_select_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        SelHolder sel= (SelHolder) holder;
        sel.title.setText(mList.get(position));
        if(mCurrent==position){
            sel.img.setVisibility(View.VISIBLE);
        }else{
            sel.img.setVisibility(View.GONE);
        }
        if(mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClickListener(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SelHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView img;

        public SelHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.MyTaskFragment_select_list_item_title);
            img= (ImageView) itemView.findViewById(R.id.MyTaskFragment_select_list_item_img);
        }
    }
}
