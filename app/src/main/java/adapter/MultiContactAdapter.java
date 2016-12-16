package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import customView.GlideCircleTransform;
import other.CustomComparator;

/**
 * Created by Jason on 2016/12/10.
 */

public class MultiContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AVUser> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private TreeSet<Integer> mTreeSet;

    public MultiContactAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        CustomComparator comparator=new CustomComparator();//使用实现序列化接口的comparator
        mTreeSet = new TreeSet<>(comparator);
        mList = new ArrayList<>();
    }

    public void setDataSource(List<AVUser> list) {
        this.mList = list;
    }

    public void setSelectPos(ArrayList<AVUser> exist){
        mTreeSet.clear();
        for(int i=0;i<mList.size();i++){
            if(exist.contains(mList.get(i))){
                mTreeSet.add(i);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MultiContactAdapter.ContactHolder holder = new MultiContactAdapter.ContactHolder(mInflater.inflate(R.layout.contact_select_recyclerview_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, List payloads) {
        final MultiContactAdapter.ContactHolder contact = (MultiContactAdapter.ContactHolder) holder;
        AVUser user = mList.get(position);
        contact.userName.setText(user.getString("name"));
        contact.userId.setText(String.format("工号ID:%d", user.getInt("employeeId")));
        AVFile img = user.getAVFile("portrait");
        if (img != null) {
            Glide.with(mContext).load(img.getUrl())
                    .centerCrop()
                    .transform(new GlideCircleTransform(mContext))
                    .dontAnimate()
                    .placeholder(R.drawable.dayhr_userphoto_def)
                    .into(contact.userImg);
        } else {
            contact.userImg.setImageResource(R.drawable.dayhr_userphoto_def);
        }
        contact.checkBox.setChecked(mTreeSet.contains(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTreeSet.contains(position)) {
                    contact.checkBox.setChecked(false);
                    mTreeSet.remove(position);
                } else {
                    contact.checkBox.setChecked(true);
                    mTreeSet.add(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public TreeSet<Integer> getTreeSet(){
        return mTreeSet;
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView userName;
        TextView userId;
        ImageView userImg;

        ContactHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.ContactSelect_list_item_checkBox);
            userName = (TextView) itemView.findViewById(R.id.ContactSelect_list_item_user_name);
            userId = (TextView) itemView.findViewById(R.id.ContactSelect_list_item_user_employeeId);
            userImg = (ImageView) itemView.findViewById(R.id.ContactSelect_list_item_img);
        }
    }
}
