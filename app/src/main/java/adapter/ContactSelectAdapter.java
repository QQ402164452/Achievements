package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.List;

import customView.GlideCircleTransform;
import interfaces.OnCustomItemClickListener;

/**
 * Created by Jason on 2016/12/3.
 */

public class ContactSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AVUser> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mSelectedPos=-1;//保存当前选中的position

    public ContactSelectAdapter(Context context){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(mContext);
        mList=new ArrayList<>();
    }

    public void setDataSource(List<AVUser> list){
        this.mList=list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ContactHolder holder=new ContactHolder(mInflater.inflate(R.layout.contact_select_recyclerview_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position,List payloads) {
        final ContactHolder contact= (ContactHolder) holder;
        if(payloads.isEmpty()){
            AVUser user=mList.get(position);
            contact.userName.setText(user.getString("name"));
            contact.userId.setText(String.format("工号ID:%d",user.getInt("employeeId")));
            AVFile img=user.getAVFile("portrait");
            if(img!=null){
                Glide.with(mContext).load(img.getUrl())
                        .centerCrop()
                        .transform(new GlideCircleTransform(mContext))
                        .dontAnimate()
                        .placeholder(R.drawable.dayhr_userphoto_def)
                        .into(contact.userImg);
            }else{
                contact.userImg.setImageResource(R.drawable.dayhr_userphoto_def);
            }
            contact.checkBox.setChecked(mSelectedPos==position);
        }else{
            contact.checkBox.setChecked(mSelectedPos==position);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedPos!=position){
                    contact.checkBox.setChecked(true);
                    if(mSelectedPos!=-1){
                        notifyItemChanged(mSelectedPos,0);
                    }
                    mSelectedPos=position;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int getSelectedPos(){
        return mSelectedPos;
    }

    class ContactHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView userName;
        TextView userId;
        ImageView userImg;

        ContactHolder(View itemView) {
            super(itemView);
            checkBox= (CheckBox) itemView.findViewById(R.id.ContactSelect_list_item_checkBox);
            userName= (TextView) itemView.findViewById(R.id.ContactSelect_list_item_user_name);
            userId= (TextView) itemView.findViewById(R.id.ContactSelect_list_item_user_employeeId);
            userImg= (ImageView) itemView.findViewById(R.id.ContactSelect_list_item_img);
        }
    }
}
