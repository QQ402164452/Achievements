package presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import interfaces.InewBlog;
import interfaces.OnUploadListener;
import utils.NetworkUtil;
import utils.UploadUtil;

/**
 * Created by Jason on 2016/12/26.
 */

public class PnewBlog {
    private InewBlog mView;
    private ArrayList<String> avFiles;
    private UploadUtil uploadUtil;

    public PnewBlog(InewBlog inewBlog) {
        this.mView = inewBlog;
        uploadUtil=new UploadUtil(4);
    }

    public void submitBlog(String content, final ArrayList<String> photos) {
        if (content.isEmpty()&&photos.size()==0) {
            mView.showToast("内容或图片不能为空");
        } else if (NetworkUtil.isNewWorkAvailable()) {
            mView.showLoading();
            final AVObject blog = new AVObject("microblog");
            blog.put("user", AVUser.getCurrentUser());
            blog.put("content", content);
            int photoSize = photos.size();
            int type;
            switch (photoSize){
                case 0:
                    type = 0;
                    break;
                case 1:
                    type = 1;
                    break;
                default:
                    type = 2;
                    break;
            }
            blog.put("type", type);
            if (photoSize > 0) {
                avFiles = new ArrayList<>();
                uploadUtil.setOnUploadListener(new OnUploadListener() {
                    @Override
                    public void onAllSuccess() {
                        blog.put("photos", avFiles);
                        blog.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if(e==null){
                                    mView.onSuccess("发布成功");
                                }else{
                                    mView.onError(e.getMessage());
                                }
                            }
                        });
                    }

                    @Override
                    public void onAllFailed() {
                        mView.onError("发布中断");
                    }

                    @Override
                    public void onThreadProgressChange(int position, int percent) {

                    }

                    @Override
                    public void onThreadFinish(int position, AVFile avFile) {
                        avFiles.add(avFile.getUrl());
                    }

                    @Override
                    public void onThreadInterrupted(int position) {

                    }
                });
                uploadUtil.submitAll(photos);
            }else {
                blog.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            mView.onSuccess("发布成功");
                        }else{
                            mView.onError(e.getMessage());
                        }
                    }
                });
            }
        } else {
            mView.showToast(NetworkUtil.tip);
        }
    }

    public void cancel(){
        if(uploadUtil!=null){
            uploadUtil.shutDownNow();
        }
    }
}

