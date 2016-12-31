package presenter;

import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import interfaces.IsocDetail;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/29.
 */

public class PsocDetail {
    private IsocDetail mView;

    public PsocDetail(IsocDetail isocDetail){
        this.mView=isocDetail;
    }

    public void getCommentList(AVObject blog){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("comment");
            query.include("user");
            query.whereEqualTo("blog",blog);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        mView.setCommentList(list);
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }

    public void getLikeList(AVObject blog){
        if(NetworkUtil.isNewWorkAvailable()){
            AVQuery<AVObject> query=new AVQuery<>("like");
            query.include("user");
            query.whereEqualTo("blog",blog);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        mView.setLikeList(list);
                    }else{
                        mView.onError(e.getMessage());
                    }
                }
            });
        }else{
            mView.showToast(NetworkUtil.tip);
        }
    }


    public void likeControl(boolean isSelected, final List<AVUser> likesList, final AVObject avObject) {
        final AVUser user = AVUser.getCurrentUser();
        if (isSelected) {
            AVQuery<AVObject> likeQuery=new AVQuery<>("like");
            likeQuery.whereEqualTo("blog",avObject);
            likeQuery.whereEqualTo("user",user);
            likeQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if(e==null){
                        if(list!=null&&list.size()>0){
                            final AVObject like=list.get(0);
                            avObject.removeAll("likes", new ArrayList<Object>(Collections.singletonList(user)));
                            avObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        likesList.remove(user);
                                        mView.setBottomLike(false);
                                        mView.showToast("取消喜欢成功");
                                        avObject.put("likes",likesList);
                                    } else {
                                        mView.showToast("失败");
                                    }
                                }
                            });
                            like.deleteInBackground();
                        }else{
                            mView.showToast("失败");
                        }
                    }else{
                        mView.showToast("失败");
                    }
                }
            });

        } else {
            final AVObject likeObject=new AVObject("like");
            likeObject.put("blog",avObject);
            likeObject.put("user",user);
            likeObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e==null){
                        avObject.addUnique("likes", user);
                        avObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    likesList.add(user);
                                    mView.setBottomLike(true);
                                    mView.showToast("设置喜欢成功");
                                    avObject.put("likes",likesList);
                                } else {
                                    mView.showToast("失败");
                                }
                            }
                        });
                    }else{
                        mView.showToast("失败");
                    }
                }
            });

        }
    }
}
