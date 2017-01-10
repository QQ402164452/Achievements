package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.Locale;

import bean.RealmUser;
import customView.GlideCircleTransform;
import interfaces.OnItemClickListener;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import utils.PinyinUtil;

/**
 * Created by Jason on 2017/1/9.
 */

public class RealmContactAdapter extends RealmRecyclerViewAdapter<RealmUser,RecyclerView.ViewHolder> {
    private OnItemClickListener mListener;

    public RealmContactAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<RealmUser> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new RealmContactAdapter.ContactHolder(inflater.inflate(R.layout.contact_list_no_head_item, parent, false));
                break;
            case 1:
                holder = new RealmContactAdapter.ContactHeadHolder(inflater.inflate(R.layout.contact_list_head_item, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        RealmUser user = getData().get(position);
        ImageView userImg;
        TextView userName;
        TextView userDepart;

        switch (getItemViewType(position)) {
            case 1:
                RealmContactAdapter.ContactHeadHolder headItem= (RealmContactAdapter.ContactHeadHolder) holder;
                userImg=headItem.img;
                userName=headItem.name;
                userDepart=headItem.job;
                headItem.indicate.setText(String.valueOf(Character.toUpperCase(PinyinUtil.getPinyinHeadChar(user.getName()))));
                break;
            case 0:
            default:
                RealmContactAdapter.ContactHolder item = (RealmContactAdapter.ContactHolder) holder;
                userImg=item.img;
                userName=item.name;
                userDepart=item.job;
                break;
        }

        userName.setText(user.getName());
        userDepart.setText(String.format(Locale.CHINA, "部门:%s", user.getDepartment()));
        String imgUrl = user.getPortrait();
        if (!imgUrl.equals("null")) {
            Glide.with(context).load(imgUrl)
                    .centerCrop()
                    .transform(new GlideCircleTransform(context))
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
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            char last = PinyinUtil.getPinyinHeadChar(getData().get(position - 1).getName());
            char current = PinyinUtil.getPinyinHeadChar(getData().get(position).getName());
            if (current != last) {
                return 1;
            }
        }
        return 0;
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
