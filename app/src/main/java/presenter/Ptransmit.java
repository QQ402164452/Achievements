package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import interfaces.Itransmit;
import utils.NetworkUtil;

/**
 * Created by Jason on 2016/12/30.
 */

public class Ptransmit {
    private Itransmit mView;

    public Ptransmit(Itransmit itransmit) {
        this.mView = itransmit;
    }

    public void transmitBlog(AVObject avObject, String content) {
        if (NetworkUtil.isNewWorkAvailable()) {
            mView.showLoading();
            final AVObject blog = new AVObject("microblog");
            blog.put("user", AVUser.getCurrentUser());
            blog.put("type", 3);
            if(avObject.getInt("type")==3){
                blog.put("transmit",avObject.getAVObject("transmit"));
                AVObject tmObject=avObject.getAVObject("transmit");
                if(content.isEmpty()){
                    blog.put("content", "转发微博 /"+"@"+avObject.getAVUser("user").get("name")+" "+avObject.getString("content")+
                            " / @"+tmObject.getAVUser("user").get("name")+" "+tmObject.getString("content"));
                }else{
                    blog.put("content", content+" / @"+avObject.getAVUser("user").get("name")+" "+avObject.getString("content")+
                            " / @"+tmObject.getAVUser("user").get("name")+" "+tmObject.getString("content"));
                }
            }else{
                blog.put("transmit",avObject);
                if(content.isEmpty()){
                    blog.put("content", "转发微博");
                }else{
                    blog.put("content", content);
                }
            }
            blog.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        mView.onSuccess("发布成功");
                    } else {
                        mView.onError(e.getMessage());
                    }
                }
            });

        } else {
            mView.showToast(NetworkUtil.tip);
        }
    }
}
