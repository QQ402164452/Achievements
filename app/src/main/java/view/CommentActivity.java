package view;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/12/29.
 */

public class CommentActivity extends BaseActivity implements View.OnClickListener{
    private TextView mCancel;
    private TextView mConfirm;
    private EditText mInput;
    private AVObject mBlog;
    private int mPos=0;

    public final static String COMMENT_BLOG_AVOBJECT="COMMENT_BLOG_AVOBJECT";
    public final static String COMMENT_BLOG_POSITION="COMMENT_BLOG_POSITION";

    @Override
    protected void initPre() {
        Intent intent=getIntent();
        if(intent!=null){
            try {
                mBlog=AVObject.parseAVObject(intent.getStringExtra(COMMENT_BLOG_AVOBJECT));
                mPos=intent.getIntExtra(COMMENT_BLOG_POSITION,0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_comment);

        mCancel= (TextView) findViewById(R.id.CommentActivity_cancel);
        mConfirm= (TextView) findViewById(R.id.CommentActivity_confirm);
        mInput= (EditText) findViewById(R.id.CommentActivity_Input_Comment);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.CommentActivity_confirm:
                AVObject comment=new AVObject("comment");
                comment.put("user", AVUser.getCurrentUser());
                comment.put("blog",mBlog);
                comment.put("content",mInput.getText().toString());
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            int commentSize=mBlog.getInt("commentNum")+1;
                            mBlog.put("commentNum",commentSize);
                            mBlog.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if(e==null){
                                        Intent intent=new Intent();
                                        intent.putExtra(COMMENT_BLOG_POSITION,mPos);
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    }else{
                                        onError("发布失败");
                                    }
                                }
                            });
                        }else{
                            onError("发布失败");
                        }
                    }
                });
                break;
            case R.id.CommentActivity_cancel:
                finish();
                break;
        }
    }
}
