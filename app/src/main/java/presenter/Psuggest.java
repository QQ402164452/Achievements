package presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bean.ChatBean;
import interfaces.Isuggest;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/11/30.
 */

public class Psuggest {
    private Isuggest suggestView;
    private ArrayList<ChatBean> list;
    private String imgUrl;

    public Psuggest(Isuggest isuggest){
        AVFile img=AVUser.getCurrentUser().getAVFile("portrait");
        if(img!=null){
            imgUrl=img.getUrl();
        }else{
            imgUrl="";
        }
        suggestView=isuggest;
    }

    public void getListData(){
        list=new ArrayList<>();
        list.add(new ChatBean(1,true,"请留下您的意见，感谢您的反馈！",""));
        suggestView.setRecyclerView(list);
    }

    public void addListData(final String content){
        if(NetworkUtil.isNewWorkAvailable()){
            AVObject feedback=new AVObject("feedback");
            feedback.put("content",content);
            feedback.put("user_id",AVUser.getCurrentUser());
            feedback.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e==null){
                        list.add(new ChatBean(0,true,content,imgUrl));
                        suggestView.notifyRecyclerView();
                    }else{
                        list.add(new ChatBean(0,false,content,imgUrl));
                        suggestView.notifyRecyclerView();
                        suggestView.showToast("发送失败，请稍后再试！");
                    }
                }
            });
        }else{
            list.add(new ChatBean(0,false,content,imgUrl));
            suggestView.notifyRecyclerView();
            suggestView.showToast(NetworkUtil.tip);
        }
    }

}
