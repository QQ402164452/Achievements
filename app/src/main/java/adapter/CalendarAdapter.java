package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.AppBean;
import interfaces.OnCustomItemClickListener;

/**
 * Created by Jason on 2016/12/8.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarHolder>{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mList;
    private OnCustomItemClickListener mListener;
    private int mCurrentSelect=-1;

    public CalendarAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mList=new ArrayList<>(Arrays.asList("1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"));
    }

    public void setCurrent(int position){
        mCurrentSelect=position;
    }

    @Override
    public CalendarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CalendarHolder holder=new CalendarHolder(mInflater.inflate(R.layout.wage_calendar_list_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CalendarHolder holder, int position) {

    }

    public void setListener(OnCustomItemClickListener OnCustomItemClickListener){
        mListener= OnCustomItemClickListener;
    }

    @Override
    public void onBindViewHolder(final CalendarHolder holder, final int position, List payLoads) {
        holder.month.setText(mList.get(position));
        if(mCurrentSelect==position){
            holder.month.setSelected(true);
        }
        if(mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.month.setSelected(true);
                    mCurrentSelect=position;
                    mListener.onItemClickListener(holder.itemView,position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class CalendarHolder extends RecyclerView.ViewHolder{
        TextView month;

        public CalendarHolder(View itemView) {
            super(itemView);
            month= (TextView) itemView.findViewById(R.id.WageActivity_calendar_list_item_text);
        }
    }
}
