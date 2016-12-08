package view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.MyDataAdapter;
import bean.MyDataBean;
import customView.DividerItemDecoration;
import customView.GlideCircleTransform;
import interfaces.Imy;
import presenter.Pmy;

/**
 * Created by Jason on 2016/11/25.
 */

public class MyActivity extends UserBaseActivity implements Imy{
    private RecyclerView mRecyclerView;
    private MyDataAdapter mAdapter;
    private Pmy mPmy;
    private TextView mEditBtn;
    private TextView mUserName;
    private ImageView mUserImg;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_my);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void onStart(){
        super.onStart();

        AVUser user=AVUser.getCurrentUser();
        setText(mUserName,user.getString("name"));
        setUserNameDrawable(user.getString("gender"),mUserName);
        AVFile file=user.getAVFile("portrait");
        if(file!=null){
            Glide.with(this).load(file.getUrl())
                    .centerCrop()
                    .transform(new GlideCircleTransform(this))
                    .dontAnimate()
                    .placeholder(R.drawable.dayhr_userphoto_def)
                    .into(mUserImg);
        }else {
            mUserImg.setImageResource(R.drawable.dayhr_userphoto_def);
        }

        mPmy.setData(user);
    }

    public void initView(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.MyActivity_toolbar);
        setCustomToolbar(toolbar);

        mRecyclerView= (RecyclerView) findViewById(R.id.MyActivity_RecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mEditBtn= (TextView) findViewById(R.id.MyActivity_btn_edit);
        mUserName= (TextView) findViewById(R.id.MyActivity_user_name);
        mUserImg= (ImageView) findViewById(R.id.MyActivity_user_img);
    }

    public void initData(){
        mPmy=new Pmy(this);
        mAdapter=new MyDataAdapter(this);
    }

    public void initListener(){
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyActivity.this,EditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setDataSource(ArrayList<MyDataBean> list) {
        mAdapter.setData(list);
        mRecyclerView.setAdapter(mAdapter);
    }

}
