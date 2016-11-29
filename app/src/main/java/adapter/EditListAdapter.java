package adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.jason.achievements.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

import bean.EditListBean;
import customView.GlideCircleTransform;
import interfaces.onCustomItemClickListener;
import interfaces.onTextChangeListener;

/**
 * Created by Jason on 2016/11/25.
 */

public class EditListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<EditListBean> mList;
    private onCustomItemClickListener mListener;
    private onTextChangeListener mTextListener;

    public EditListAdapter(Context context, ArrayList<EditListBean> list){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
        this.mList=list;
    }

    public EditListAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(onCustomItemClickListener onItemClickListener){
        this.mListener=onItemClickListener;
    }

    public void setOnTextChangeListener(onTextChangeListener onTextChangeListener){
        this.mTextListener=onTextChangeListener;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)){
            case 1:
                final itemHolder1 holder1= (itemHolder1) holder;
                holder1.title.setText(mList.get(position).getKey());

                Object object=mList.get(position).getValue();
                if(object==null){
                    Glide.with(mContext).
                            load(R.drawable.dayhr_userphoto_def).
                            centerCrop().
                            transform(new GlideCircleTransform(mContext)).
                            dontAnimate().
                            into(holder1.img);
                }else {
                    if(object instanceof byte[]){
                        Glide.with(mContext).
                                load((byte[]) mList.get(position).getValue()).
                                centerCrop().
                                transform(new GlideCircleTransform(mContext)).
                                placeholder(R.drawable.dayhr_userphoto_def).
                                dontAnimate().
                                into(holder1.img);
                    }
                    if(object instanceof String){
                        Glide.with(mContext).
                                load((String) mList.get(position).getValue()).
                                centerCrop().
                                transform(new GlideCircleTransform(mContext)).
                                placeholder(R.drawable.dayhr_userphoto_def).
                                dontAnimate().
                                into(holder1.img);
                    }
                }

                if(mListener!=null){
                    holder1.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos=holder1.getLayoutPosition();
                            mListener.onItemClickListener(holder1.itemView,pos);
                        }
                    });
                }
                break;
            case 2:
                final itemHolder2 holder2= (itemHolder2) holder;
                holder2.title.setText(mList.get(position).getKey());
                holder2.input.setText((String) mList.get(position).getValue());
                holder2.input.setHint(mList.get(position).getHint());

//                try {
//                    Class clazz=TextView.class;
//                    Field field=clazz.getDeclaredField("mListeners");
//                    field.setAccessible(true);
//                    field.set(holder2.input,null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } catch (Error e) {
//                    e.printStackTrace();
//                }

                TextWatcher textWatcher=new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mTextListener.onTextChanged(position,holder2.input.getText().toString());
                    }
                };
                holder2.input.addTextChangedListener(textWatcher);
                break;
            case 3:
                final itemHolder3 holder3= (itemHolder3) holder;
                holder3.title.setText(mList.get(position).getKey());
                holder3.input.setText((String)mList.get(position).getValue());
                holder3.input.setHint(mList.get(position).getHint());
                if(mListener!=null){
                    holder3.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos=holder3.getLayoutPosition();
                            mListener.onItemClickListener(holder3.itemView,pos);
                        }
                    });
                }
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
