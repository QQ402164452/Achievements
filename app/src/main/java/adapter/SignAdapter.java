package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import bean.SignBean;

/**
 * Created by Jason on 2016/12/1.
 */

public class SignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<SignBean> mList;
    private LayoutInflater mInflater;

    public SignAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
    }

    public void setList(ArrayList<SignBean> list){
        this.mList=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SignViewHolder holder=new SignViewHolder(mInflater.inflate(R.layout.sign_recyclerview_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SignViewHolder view= (SignViewHolder) holder;
        view.date.setText(mList.get(position).getDate());
        view.position.setText(mList.get(position).getLocation());
        switch (mList.get(position).getSign()){
            case 0:
                view.sign.setTextColor(Color.GREEN);
                view.sign.setText("准时");
                break;
            case 1:
                view.sign.setTextColor(Color.RED);
                view.sign.setText("迟到");
                break;
            case 2:
                view.sign.setTextColor(Color.RED);
                view.sign.setText("早退");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SignViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView position;
        TextView sign;

        public SignViewHolder(View itemView) {
            super(itemView);
            date= (TextView) itemView.findViewById(R.id.SignActivity_list_item_time_text);
            position= (TextView) itemView.findViewById(R.id.SignActivity_list_item_position_text);
            sign= (TextView) itemView.findViewById(R.id.SignActivity_list_item_sign);
        }
    }
}
