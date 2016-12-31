package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import customView.GlideCircleTransform;
import interfaces.OnSocialItemClickListener;
import utils.DateUtil;
import view.CommentActivity;
import view.PhotoActivity;
import view.SocialDetailActivity;
import view.TransmitActivity;

/**
 * Created by Jason on 2016/12/20.
 */

public class SocialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AVObject> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnSocialItemClickListener mListener;

    public SocialAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mList = new ArrayList<>();
    }

    public void setDataSource(List<AVObject> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void setListener(OnSocialItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new ZeroHolder(mInflater.inflate(R.layout.social_fragment_list_item_zero, parent, false));
                break;
            case 1:
                holder = new OneHolder(mInflater.inflate(R.layout.social_fragment_list_item_one, parent, false));
                break;
            case 2:
                holder = new TwoHolder(mInflater.inflate(R.layout.social_fragment_list_item_two, parent, false));
                break;
            case 3:
                holder=new ThreeHolder(mInflater.inflate(R.layout.social_fragment_list_item_three,parent,false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position, List payLoad) {
        final AVObject avObject = mList.get(position);
        AVUser user = avObject.getAVUser("user");
        AVFile img = user.getAVFile("portrait");
        String content = avObject.getString("content");
        List<AVUser> likes = avObject.getList("likes", AVUser.class);

        TextView userName;
        ImageView userImg;
        TextView time;
        TextView share;
        TextView comment;
        TextView like;

        switch (getItemViewType(position)) {
            case 1:
                OneHolder one = (OneHolder) holder;
                userName = one.userName;
                userImg = one.userImg;
                time = one.time;
                share = one.share;
                comment = one.comment;
                like = one.like;
                break;
            case 2:
                TwoHolder two = (TwoHolder) holder;
                userName = two.userName;
                userImg = two.userImg;
                time = two.time;
                share = two.share;
                comment = two.comment;
                like = two.like;
                break;
            case 3:
                ThreeHolder three= (ThreeHolder) holder;
                userName = three.userName;
                userImg = three.userImg;
                time = three.time;
                share = three.share;
                comment = three.comment;
                like = three.like;
                break;
            case 0:
            default:
                ZeroHolder zero = (ZeroHolder) holder;
                zero.content.setText(content);
                userName = zero.userName;
                userImg = zero.userImg;
                time = zero.time;
                share = zero.share;
                comment = zero.comment;
                like = zero.like;
                break;
        }
        if (payLoad == null || payLoad.isEmpty()) {
            switch (getItemViewType(position)) {
                case 1:
                    final OneHolder one = (OneHolder) holder;
                    one.content.setText(content);
                    final List<String> singleImg = avObject.getList("photos");
                    if (singleImg != null && singleImg.size() > 0) {
                        Glide.with(mContext).load(singleImg.get(0) + "?imageView/1/w/400/h/400/q/30/format/jpeg")
                                .dontAnimate()
                                .into(one.img);
                    }
                    one.img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PhotoActivity.class);
                            intent.putStringArrayListExtra(PhotoActivity.PHOTO_URL_LIST, new ArrayList<>(singleImg));
                            intent.putExtra(PhotoActivity.PHOTO_CURRENT_POSITION, 0);
                            mContext.startActivity(intent);
                        }
                    });
                    break;

                case 2:
                    final TwoHolder two = (TwoHolder) holder;
                    two.content.setText(content);
                    List<String> multiImg = avObject.getList("photos");
                    if (multiImg != null && multiImg.size() > 0) {
                        SocialChildAdapter socialAdapter = new SocialChildAdapter(mContext, multiImg);
                        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
                        layoutManager.setAutoMeasureEnabled(true);//交由layoutManager管理测量 自适应高度
                        two.recyclerView.setLayoutManager(layoutManager);
                        two.recyclerView.setAdapter(socialAdapter);
                    }
                    break;
                case 3:
                    ThreeHolder three= (ThreeHolder) holder;
                    AVObject tmObject=avObject.getAVObject("transmit");
                    three.content.setText(content);
                    switch (tmObject.getInt("type")){
                        case 1:
                        case 2:
                            three.tmReyclcerView.setVisibility(View.VISIBLE);
                            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
                            layoutManager.setAutoMeasureEnabled(true);//交由layoutManager管理测量 自适应高度
                            three.tmReyclcerView.setLayoutManager(layoutManager);
                            List<String> imgList = tmObject.getList("photos");
                            if(imgList!=null&&imgList.size()>0){
                                SocialChildAdapter socialAdapter = new SocialChildAdapter(mContext, imgList);
                                three.tmReyclcerView.setAdapter(socialAdapter);
                            }
                            three.tmTitle.setText("@"+tmObject.getAVUser("user").get("name")+" "+tmObject.getString("content"));
                            break;
                        case 0:
                        case 3:
                            three.tmReyclcerView.setVisibility(View.GONE);
                            three.tmTitle.setText("@"+tmObject.getAVUser("user").get("name")+" "+tmObject.getString("content"));
                            break;
                    }
                    break;
                case 0:
                default:
                    final ZeroHolder zero = (ZeroHolder) holder;
                    zero.content.setText(content);
                    break;
            }

            if (img != null) {
                Glide.with(mContext).load(img.getUrl())
                        .centerCrop()
                        .transform(new GlideCircleTransform(mContext))
                        .dontAnimate()
                        .into(userImg);
            } else {
                userImg.setImageResource(R.drawable.dayhr_userphoto_def);
            }
            userName.setText(user.getString("name"));
            time.setText(DateUtil.calTime(avObject.getCreatedAt()));

            if (likes!=null&&likes.size() > 0) {
                if (likes.contains(AVUser.getCurrentUser())) {
                    like.setSelected(true);
                } else {
                    like.setSelected(false);
                }
            } else {
                like.setSelected(false);
            }

            final List<AVUser> finalLikes = likes;
            final TextView finalLike = like;
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeControl(finalLike, finalLikes, avObject);
                }
            });

            if (avObject.getInt("commentNum") > 0) {
                comment.setText(String.valueOf(avObject.getInt("commentNum")));
            } else {
                comment.setText("评论");
            }

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TransmitActivity.class);
                    intent.putExtra(TransmitActivity.TRANSMIT_BLOG, avObject.toString());
                    mListener.OnShareClickListener(intent);
                }
            });

            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    intent.putExtra(CommentActivity.COMMENT_BLOG_AVOBJECT, avObject.toString());
                    intent.putExtra(CommentActivity.COMMENT_BLOG_POSITION, position);
//                    Log.e("comment",""+position);
                    mListener.OnCommentClickListener(intent);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SocialDetailActivity.class);
                    intent.putExtra(SocialDetailActivity.SOCIAL_DETAIL_AVOBJECT,avObject.toString());
                    intent.putExtra(SocialDetailActivity.SOCIAL_DETAIL_POSITION,position);
                    mListener.OnItemClickListener(intent);
                }
            });
        } else {
            int sign = (int) payLoad.get(0);
            switch (sign) {
                case 0:
                    if (avObject.getInt("commentNum") > 0) {
                        comment.setText(String.valueOf(avObject.getInt("commentNum")));
                    } else {
                        comment.setText("评论");
                    }
                    break;
                case 1:
                    if (avObject.getInt("commentNum") > 0) {
                        comment.setText(String.valueOf(avObject.getInt("commentNum")));
                    } else {
                        comment.setText("评论");
                    }

                    if (likes!=null&&likes.size() > 0) {
                        if (likes.contains(AVUser.getCurrentUser())) {
                            like.setSelected(true);
                        } else {
                            like.setSelected(false);
                        }
                    } else {
                        like.setSelected(false);
                    }
                    break;
            }
        }
    }

    public void increaseComment(int position) {
        AVObject blog = mList.get(position);
        int curNum = blog.getInt("commentNum");
        mList.get(position).put("commentNum", curNum + 1);
        notifyItemChanged(position+1, 0);//刷新的位置要加1 多了个刷新的头部 所以要加1
    }

    public void updateComNumAndLike(int position,int commNum,boolean isLike){
        mList.get(position).put("commentNum",commNum);
        List<AVUser> likes= mList.get(position).getList("likes", AVUser.class);
        if (likes == null) {
            likes = new ArrayList<>();
        }
        if(isLike){
            likes.add(AVUser.getCurrentUser());
        }else{
            likes.remove(AVUser.getCurrentUser());
        }
        mList.get(position).put("likes",likes);
        notifyItemChanged(position+1, 1);//刷新的位置要加1 多了个刷新的头部 所以要加1
    }


    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getInt("type");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void likeControl(final TextView textView,List<AVUser> likes, final AVObject avObject) {
        if(likes==null){
            likes=new ArrayList<>();
        }
        final List<AVUser> likesList=likes;
        final AVUser user = AVUser.getCurrentUser();
        if (textView.isSelected()) {
            AVQuery<AVObject> likeQuery = new AVQuery<>("like");
            likeQuery.whereEqualTo("blog", avObject);
            likeQuery.whereEqualTo("user", user);
            likeQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        if (list != null && list.size() > 0) {
                            final AVObject like = list.get(0);
                            avObject.removeAll("likes", new ArrayList<Object>(Collections.singletonList(user)));
                            avObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        likesList.remove(user);
                                        textView.setSelected(false);
                                        showToast("取消喜欢成功");
                                        avObject.put("likes", likesList);
                                    } else {
                                        showToast("失败");
                                    }
                                }
                            });
                            like.deleteInBackground();
                        } else {
                            showToast("失败");
                        }
                    } else {
                        showToast("失败");
                    }
                }
            });

        } else {
            final AVObject likeObject = new AVObject("like");
            likeObject.put("blog", avObject);
            likeObject.put("user", user);
            likeObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        avObject.addUnique("likes", user);
                        avObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    likesList.add(user);
                                    textView.setSelected(true);
                                    showToast("设置喜欢成功");
                                    avObject.put("likes", likesList);
                                } else {
                                    showToast("失败");
                                }
                            }
                        });
                    } else {
                        showToast("失败");
                    }
                }
            });

        }
    }

    public void showToast(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }

    private static class ZeroHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView userName;
        TextView time;
        TextView content;
        TextView share;
        TextView comment;
        TextView like;

        private ZeroHolder(View itemView) {
            super(itemView);
            userImg = (ImageView) itemView.findViewById(R.id.SocialFragment_list_item_User_img);
            userName = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_User_name);
            time = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_time);
            share = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_share);
            comment = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_comment);
            like = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_like);
            content = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_Zero_content);
        }
    }

    private static class OneHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView userName;
        TextView time;
        TextView content;
        TextView share;
        TextView comment;
        TextView like;
        ImageView img;

        private OneHolder(View itemView) {
            super(itemView);
            userImg = (ImageView) itemView.findViewById(R.id.SocialFragment_list_item_User_img);
            userName = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_User_name);
            time = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_time);
            share = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_share);
            comment = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_comment);
            like = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_like);
            content = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_One_content);
            img = (ImageView) itemView.findViewById(R.id.SocialFragment_list_item_One_pic);
        }
    }

    private static class TwoHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView userName;
        TextView time;
        TextView content;
        TextView share;
        TextView comment;
        TextView like;
        RecyclerView recyclerView;

        private TwoHolder(View itemView) {
            super(itemView);
            userImg = (ImageView) itemView.findViewById(R.id.SocialFragment_list_item_User_img);
            userName = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_User_name);
            time = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_time);
            share = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_share);
            comment = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_comment);
            like = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_like);
            content = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_Two_content);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.SocialFragment_list_item_Two_RecyclerView);
        }
    }

    private static class ThreeHolder extends RecyclerView.ViewHolder{
        ImageView userImg;
        TextView userName;
        TextView time;
        TextView content;
        TextView share;
        TextView comment;
        TextView like;
        RecyclerView tmReyclcerView;
        TextView tmTitle;

        public ThreeHolder(View itemView) {
            super(itemView);
            userImg = (ImageView) itemView.findViewById(R.id.SocialFragment_list_item_User_img);
            userName = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_User_name);
            time = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_time);
            share = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_share);
            comment = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_comment);
            like = (TextView) itemView.findViewById(R.id.SocialFragment_list_item_like);
            content= (TextView) itemView.findViewById(R.id.SocialFragment_list_item_Three_content);
            tmReyclcerView= (RecyclerView) itemView.findViewById(R.id.SocialFragment_list_item_Three_Other_RecyclerView);
            tmTitle= (TextView) itemView.findViewById(R.id.SocialFragment_list_item_Three_Other_Content);
        }
    }


}
