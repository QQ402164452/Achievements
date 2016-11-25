package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import bean.EditListBean;

/**
 * Created by Jason on 2016/11/25.
 */

public class EditListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<EditListBean> mList;

    public EditListAdapter(Context context, ArrayList<EditListBean> list){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
        this.mList=list;
    }

    public EditListAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
    }

    public void setData(ArrayList<EditListBean> list){
        this.mList=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        switch (viewType){
            case 0:
                holder=new itemHolder4(mInflater.inflate(R.layout.my_recycleview_divider,parent,false));
                break;
            case 1:
                holder=new itemHolder1(mInflater.inflate(R.layout.edit_recyclerview_item1,parent,false));
                break;
            case 2:
                holder=new itemHolder2(mInflater.inflate(R.layout.edit_recyclerview_item2,parent,false));
                break;
            case 3:
                holder=new itemHolder3(mInflater.inflate(R.layout.edit_recyclerview_item3,parent,false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 1:
                itemHolder1 holder1= (itemHolder1) holder;
                holder1.title.setText(mList.get(position).getKey());
                holder1.img.setImageResource(Integer.parseInt(mList.get(position).getValue()));
                break;
            case 2:
                itemHolder2 holder2= (itemHolder2) holder;
                holder2.title.setText(mList.get(position).getKey());
                holder2.input.setText(mList.get(position).getValue());
                holder2.input.setHint(mList.get(position).getHint());
                break;
            case 3:
                itemHolder3 holder3= (itemHolder3) holder;
                holder3.title.setText(mList.get(position).getKey());
                holder3.input.setText(mList.get(position).getValue());
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

    class itemHolder1 extends RecyclerView.ViewHolder{
        TextView title;
        ImageView img;

        public itemHolder1(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.EditActivity_item1_title);
            img= (ImageView) itemView.findViewById(R.id.EditActivity_item1_user_img);
        }
    }

    class itemHolder2 extends RecyclerView.ViewHolder{
        TextView title;
        EditText input;

        public itemHolder2(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.EditActivity_item2_title);
            input= (EditText) itemView.findViewById(R.id.EditActivity_item2_text);
        }
    }

    class itemHolder3 extends RecyclerView.ViewHolder{
        TextView title;
        TextView input;

        public itemHolder3(View itemView) {
            super(itemView);

            title= (TextView) itemView.findViewById(R.id.EditActivity_item3_title);
            input= (TextView) itemView.findViewById(R.id.EditActivity_item3_text);
        }
    }

    class itemHolder4 extends RecyclerView.ViewHolder{

        public itemHolder4(View itemView) {
            super(itemView);
        }
    }
}
