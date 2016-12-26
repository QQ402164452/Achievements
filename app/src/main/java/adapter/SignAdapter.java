package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.jason.achievements.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bean.SignBean;

/**
 * Created by Jason on 2016/12/1.
 */

public class SignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<SignBean> mList;
    private LayoutInflater mInflater;
    private SimpleDateFormat simpleDateFormat;

    public SignAdapter(Context context,List<SignBean> list){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mList=list;
        simpleDateFormat=new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SignViewHolder(mInflater.inflate(R.layout.sign_recyclerview_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SignViewHolder view= (SignViewHolder) holder;
        SignBean sign=mList.get(position);
        final String typeStr=sign.getType()==0?"上班":"下班";
        view.date.setText(simpleDateFormat.format(sign.getDate())+"  "+typeStr);
        view.position.setText(sign.getLocation());
        switch (sign.getType()){
            case 0:
                switch (sign.getSign()){
                    case 1:
                        view.sign.setTextColor(Color.GREEN);
                        view.sign.setText("准时");
                        break;
                    case 2:
                        view.sign.setTextColor(Color.RED);
                        view.sign.setText("迟到");
                        break;
                }
                break;
            case 1:
                switch (sign.getSign()){
                    case 1:
                        view.sign.setTextColor(Color.GREEN);
                        view.sign.setText("准时");
                        break;
                    case 2:
                        view.sign.setTextColor(Color.RED);
                        view.sign.setText("早退");
                        break;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class SignViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView position;
        TextView sign;

         SignViewHolder(View itemView) {
            super(itemView);
            date= (TextView) itemView.findViewById(R.id.SignActivity_list_item_time_text);
            position= (TextView) itemView.findViewById(R.id.SignActivity_list_item_position_text);
            sign= (TextView) itemView.findViewById(R.id.SignActivity_list_item_sign);
        }
    }
}
