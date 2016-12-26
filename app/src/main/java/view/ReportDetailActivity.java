package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Locale;

import customView.GlideCircleTransform;

/**
 * Created by Jason on 2016/12/3.
 */

public class ReportDetailActivity extends BaseActivity {
    private ImageView mUserImg;
    private TextView mUserName;
    private TextView mUpTime;
    private TextView mTime;
    private TextView mContent;
    private ImageView mArImg;
    private TextView mArText;

    @Override
    protected void initPre() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_report_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.ReportDetailActivity_toolbar);
        setCustomToolbar(toolbar);

        mUserImg= (ImageView) findViewById(R.id.ReportDetailActivity_user_img);
        mUserName= (TextView) findViewById(R.id.ReportDetailActivity_user_name);
        mUpTime= (TextView) findViewById(R.id.ReportDetailActivity_user_upTime);
        mTime= (TextView) findViewById(R.id.ReportDetailActivity_time);
        mContent= (TextView) findViewById(R.id.ReportDetailActivity_content);
        mArImg= (ImageView) findViewById(R.id.ReportDetailActivity_bottom_img);
        mArText= (TextView) findViewById(R.id.ReportDetailActivity_bottom_name);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Intent intent=getIntent();
        if(intent!=null){
            String serializedString=intent.getStringExtra("REPORT_DETAIL_STRING");
            try {
                AVObject object=AVObject.parseAVObject(serializedString);
                AVUser arUser=object.getAVUser("approver");
                AVFile arImg=arUser.getAVFile("portrait");
                if(arImg!=null){
                    Glide.with(this).load(arImg.getUrl())
                            .centerCrop()
                            .transform(new GlideCircleTransform(this))
                            .dontAnimate()
                            .placeholder(R.drawable.dayhr_userphoto_def)
                            .into(mArImg);
                }else {
                    mArImg.setImageResource(R.drawable.dayhr_userphoto_def);
                }
                mArText.setText(arUser.getString("name"));
                AVUser user=object.getAVUser("reporter");
                AVFile img=user.getAVFile("portrait");
                if(img!=null){
                    Glide.with(this).load(img.getUrl())
                            .centerCrop()
                            .transform(new GlideCircleTransform(this))
                            .dontAnimate()
                            .placeholder(R.drawable.dayhr_userphoto_def)
                            .into(mUserImg);
                }else {
                    mUserImg.setImageResource(R.drawable.dayhr_userphoto_def);
                }
                mUserName.setText(user.getString("name"));
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                mUpTime.setText(simpleDateFormat.format(object.getCreatedAt()));
                switch (object.getInt("type")){
                    case 0:
                        mTime.setText(String.format("日报(%s %s)",object.getString("firstday"),object.getString("lastday")));
                        mContent.setText(String.format("今日工作总结\n%s\n明日工作计划:\n%s",object.getString("summary"),object.getString("plan")));
                        break;
                    case 1:
                        mTime.setText(String.format("周报(%s 第%s周)",object.getString("firstday"),object.getString("lastday")));
                        mContent.setText(String.format("本周工作总结\n%s\n下周工作计划:\n%s",object.getString("summary"),object.getString("plan")));
                        break;
                    case 2:
                        mTime.setText(String.format("月报(%s年%s月)",object.getString("firstday"),object.getString("lastday")));
                        mContent.setText(String.format("本月工作总结\n%s\n下月工作计划:\n%s",object.getString("summary"),object.getString("plan")));
                        break;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
