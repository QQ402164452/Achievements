package adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.jason.achievements.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import interfaces.OnCustomItemClickListener;

/**
 * Created by Jason on 2016/12/13.
 */

public class MyTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<AVObject> mList;
    private OnCustomItemClickListener mListener;
    private SimpleDateFormat mFormat;

    public MyTaskAdapter(Context context,List<AVObject> list){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(mContext);
        mList=list;
        mFormat=new SimpleDateFormat("yy-MM-DD HH:mm", Locale.CHINA);
    }

    public void setListener(OnCustomItemClickListener listener){
        mListener=listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyTaskHolder(mInflater.inflate(R.layout.my_task_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MyTaskHolder myTask= (MyTaskHolder) holder;
        AVObject object=mList.get(position);
        int state=object.getInt("sign");
        String title=object.getString("title");
        switch (state){
            case 0:
                myTask.state.setEnabled(true);
                myTask.state.setChecked(false);
                myTask.title.setText(title);
                break;
            case 1:
                myTask.state.setEnabled(true);
                myTask.state.setChecked(true);
                myTask.title.setText(title);
                break;
            case 2:
                myTask.state.setEnabled(false);
                SpannableString span=new SpannableString(title);
                span.setSpan(new StrikethroughSpan(),0,title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                myTask.title.setText(span);
//                myTask.title.setPaintFlags(Paint. STRIKE_THRU_TEXT_FLAG);//设置中划线
                break;
        }
        myTask.time.setText(String.format("%s 截止",mFormat.format(object.getDate("endTime"))));
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

    class MyTaskHolder extends RecyclerView.ViewHolder{
        CheckBox state;
        TextView title;
        TextView time;

        public MyTaskHolder(View itemView) {
            super(itemView);
            state= (CheckBox) itemView.findViewById(R.id.MyTaskFragment_list_item_checkBox);
            title= (TextView) itemView.findViewById(R.id.MyTaskFragment_list_item_title);
            time= (TextView) itemView.findViewById(R.id.MyTaskFragment_list_item_time);
        }
    }
}
