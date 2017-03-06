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

import java.text.SimpleDateFormat;
import java.util.Locale;

import bean.RealmConversation;
import bean.RealmMessage;
import bean.RealmUser;
import customView.GlideCircleTransform;
import interfaces.OnItemClickListener;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Jason on 2017/1/10.
 */

public class PersonalAdapter extends RealmRecyclerViewAdapter<RealmConversation, RecyclerView.ViewHolder> {
    private SimpleDateFormat simpleDateFormat;
    private OnItemClickListener onItemClickListener;

    public PersonalAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<RealmConversation> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        simpleDateFormat=new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public PersonalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonalViewHolder(inflater.inflate(R.layout.personal_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        PersonalViewHolder item= (PersonalViewHolder) holder;
        RealmConversation conversation=getData().get(position);
        RealmMessage realmMessage=conversation.getMsgList().last();
        RealmUser toUser=conversation.getToTarget();
        if(!toUser.getPortrait().equals("null")){
            Glide.with(context).load(toUser.getPortrait())
                    .dontAnimate()
                    .transform(new GlideCircleTransform(context))
                    .into(item.img);
        }else{
            item.img.setImageResource(R.drawable.dayhr_userphoto_def);
        }
        item.name.setText(toUser.getName());
        item.content.setText(realmMessage.getContent());
        item.time.setText(simpleDateFormat.format(realmMessage.getDate()));
        item.newIndicator.setVisibility(conversation.isNew()?View.VISIBLE:View.GONE);

        if(onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }
    }

    private static class PersonalViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView content;
        TextView time;
        ImageView newIndicator;

        private PersonalViewHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.PersonalFragment_List_Item_Img);
            name= (TextView) itemView.findViewById(R.id.PersonalFragment_List_Item_Name);
            content= (TextView) itemView.findViewById(R.id.PersonalFragment_List_Item_Content);
            time= (TextView) itemView.findViewById(R.id.PersonalFragment_List_Item_Time);
            newIndicator= (ImageView) itemView.findViewById(R.id.PersonalFragment_List_Item_NewIndicator);
        }
    }
}
