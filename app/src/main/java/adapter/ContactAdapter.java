package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import customView.GlideCircleTransform;
import interfaces.OnItemClickListener;
import utils.PinyinUtil;

/**
 * Created by Jason on 2017/1/3.
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<AVUser> mList;
    private OnItemClickListener mListener;

    public ContactAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        mList = new ArrayList<>();
    }

    public void setDataSource(List<AVUser> list) {
        this.mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            char last = PinyinUtil.getPinyinHeadChar(mList.get(position - 1).getString("name"));
            char current = PinyinUtil.getPinyinHeadChar(mList.get(position).getString("name"));
            if (current != last) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new ContactHolder(mInflater.inflate(R.layout.contact_list_no_head_item, parent, false));
                break;
            case 1:
                holder = new ContactHeadHolder(mInflater.inflate(R.layout.contact_list_head_item, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        AVUser user = mList.get(position);
        ImageView userImg;
        TextView userName;
        TextView userDepart;

        switch (getItemViewType(position)) {
            case 1:
                ContactHeadHolder headItem= (ContactHeadHolder) holder;
                userImg=headItem.img;
                userName=headItem.name;
                userDepart=headItem.job;
                headItem.indicate.setText(String.valueOf(Character.toUpperCase(PinyinUtil.getPinyinHeadChar(user.getString("name")))));
                break;
            case 0:
            default:
                ContactHolder item = (ContactHolder) holder;
                userImg=item.img;
                userName=item.name;
                userDepart=item.job;
                break;
        }

        userName.setText(user.getString("name"));
        userDepart.setText(String.format(Locale.CHINA, "部门:%s", user.getString("department")));
        AVFile img = user.getAVFile("portrait");
        if (img != null) {
            Glide.with(mContext).load(img.getUrl())
                    .centerCrop()
                    .transform(new GlideCircleTransform(mContext))
                    .dontAnimate()
                    .placeholder(R.drawable.dayhr_userphoto_def)
                    .into(userImg);
        } else {
            userImg.setImageResource(R.drawable.dayhr_userphoto_def);
        }

        if(mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private static final class ContactHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView job;

        private ContactHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.ContactFragment_List_No_Head_Item_img);
            name = (TextView) itemView.findViewById(R.id.ContactFragment_List_No_Head_Item_Name);
            job = (TextView) itemView.findViewById(R.id.ContactFragment_List_No_Head_Item_Job);
        }
    }

    private static final class ContactHeadHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView job;
        TextView indicate;

        private ContactHeadHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.ContactFragment_List_Head_Item_img);
            name = (TextView) itemView.findViewById(R.id.ContactFragment_List_Head_Item_Name);
            job = (TextView) itemView.findViewById(R.id.ContactFragment_List_Head_Item_Job);
            indicate = (TextView) itemView.findViewById(R.id.ContactFragment_List_Head_Item_Indicate);
        }
    }
}
