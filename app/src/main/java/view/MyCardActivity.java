package view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import customView.GlideCircleTransform;

/**
 * Created by Jason on 2016/11/24.
 */

public class MyCardActivity extends UserBaseActivity{
    private ImageView mUserImg;
    private TextView mUserName;
    private TextView mUserCp;
    private TextView mUserJob;
    private TextView mPhone;
    private TextView mEmail;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_mycard);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public void initView(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.MyCardActivity_toolbar);
        setCustomToolbar(toolbar);

        mUserImg= (ImageView) findViewById(R.id.MyCardActivity_user_img);
        mUserName= (TextView) findViewById(R.id.MyCardActivity_user_name);
        mUserCp= (TextView) findViewById(R.id.MyCardActivity_user_company);
        mUserJob= (TextView) findViewById(R.id.MyCardActivity_user_job);
        mPhone= (TextView) findViewById(R.id.MyCardActivity_phone);
        mEmail= (TextView) findViewById(R.id.MyCardActivity_email);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        AVUser user=AVUser.getCurrentUser();
        setText(mUserName,user.getString("name"));
        setText(mUserCp,user.getString("company"));
        setText(mUserJob,user.getString("job"));
        setText(mPhone,user.getMobilePhoneNumber());
        setText(mEmail,user.getEmail());
        setUserNameDrawable(user.getString("gender"),mUserName);
        AVFile file=user.getAVFile("portrait");
        if(file!=null){
            Glide.with(MyCardActivity.this).load(file.getUrl())
                    .centerCrop()
                    .transform(new GlideCircleTransform(MyCardActivity.this))
                    .dontAnimate()
                    .placeholder(R.drawable.dayhr_userphoto_def)
                    .into(mUserImg);
        }
    }


}
