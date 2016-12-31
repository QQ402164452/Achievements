package view;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapter.SocComAdapter;
import adapter.SocLikeAdapter;
import adapter.SocialChildAdapter;
import customView.DividerItemDecoration;
import customView.DividerItemExceptLastDecoration;
import customView.GlideCircleTransform;
import interfaces.IsocDetail;
import presenter.PsocDetail;
import utils.DateUtil;

/**
 * Created by Jason on 2016/12/28.
 */

public class SocialDetailActivity extends BaseActivity implements View.OnClickListener, IsocDetail{
    private LinearLayout mTmViewGroup;
    private TextView mTmContent;
    private RecyclerView mTmReyclerView;

    private TextView mComment;
    private TextView mLike;
    private RecyclerView mRecyclerView;
    private RecyclerView mPhotoRecy;
    private ImageView mSinglePhoto;
    private TextView mContent;
    private SocComAdapter mComAdapter;
    private SocLikeAdapter mLikeAdapter;
    private AVObject mBlog;
    private PsocDetail mPresenter;
    private int mType;

    private TextView mBtmComm;
    private TextView mBtmLike;

    private ImageView mUserImg;
    private TextView mUserName;
    private TextView mTime;

    private List<AVUser> likes;
    private int mPos=0;
    private Intent mResultIntent;

    public final static String SOCIAL_DETAIL_AVOBJECT="SOCIAL_DETAIL_AVOBJECT";
    public final static String SOCIAL_DETAIL_POSITION="SOCIAL_DETAIL_POSITION";
    public final static String SOCIAL_DETAIL_LIKE_SING="SOCIAL_DETAIL_LIKE_SING";
    public final static String SOCIAL_DETAIL_COMMENT_Num="SOCIAL_DETAIL_COMMENT_Num";
    public final static int COMMENT_REQUEST=400;

    @Override
    protected void initPre() {
        Intent intent=getIntent();
        if(intent!=null){
            try {
                mBlog=AVObject.parseAVObject(intent.getStringExtra(SOCIAL_DETAIL_AVOBJECT));
                mPos=intent.getIntExtra(SOCIAL_DETAIL_POSITION,0);
                mType=mBlog.getInt("type");
                likes = mBlog.getList("likes", AVUser.class);
                if (likes == null) {
                    likes = new ArrayList<>();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_social_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.SocialDetailActivity_toolbar);
        setCustomToolbar(toolbar);

        mUserImg= (ImageView) findViewById(R.id.SocialDetailActivity_User_Img);
        mUserName= (TextView) findViewById(R.id.SocialDetailActivity_User_Name);
        mTime= (TextView) findViewById(R.id.SocialDetailActivity_Time);
        mComment= (TextView) findViewById(R.id.SocialDetailActivity_Indicate_Comment);
        mPhotoRecy= (RecyclerView) findViewById(R.id.SocialDetailActivity_Photo_RecyclerView);
        mSinglePhoto= (ImageView) findViewById(R.id.SocialDetailActivity_Photo);
        mContent= (TextView) findViewById(R.id.SocialDetailActivity_Content);
        mLike= (TextView) findViewById(R.id.SocialDetailActivity_Indicate_Like);
        mRecyclerView= (RecyclerView) findViewById(R.id.SocialDetailActivity_RecyclerView);
        mBtmComm= (TextView) findViewById(R.id.SocialDetailActivity_Bottom_comment);
        mBtmLike= (TextView) findViewById(R.id.SocialDetailActivity_Bottom_like);
        mTmViewGroup= (LinearLayout) findViewById(R.id.SocialDetailActivity_TransmitItem);
        mTmContent= (TextView) findViewById(R.id.SocialDetailActivity_TransmitItem_Content);
        mTmReyclerView= (RecyclerView) findViewById(R.id.SocialDetailActivity_TransmitItem_RecyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        mComment.setSelected(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        mResultIntent=new Intent();
        mResultIntent.putExtra(SOCIAL_DETAIL_POSITION,mPos);

        AVUser user=mBlog.getAVUser("user");
        AVFile userImg=user.getAVFile("portrait");
        if(userImg!=null){
            Glide.with(this).load(userImg.getUrl())
                    .centerCrop()
                    .transform(new GlideCircleTransform(this))
                    .dontAnimate()
                    .into(mUserImg);
        }else {
            mUserImg.setImageResource(R.drawable.dayhr_userphoto_def);
        }
        mUserName.setText(user.getString("name"));
        mTime.setText(DateUtil.calTime(mBlog.getCreatedAt()));

        String content = mBlog.getString("content");
        switch (mType){
            case 0:
                mContent.setVisibility(View.VISIBLE);
                mContent.setText(content);
                break;
            case 1:
                if(content!=null&&!content.isEmpty()){
                    mContent.setVisibility(View.VISIBLE);
                    mContent.setText(content);
                }
                mSinglePhoto.setVisibility(View.VISIBLE);
                final List<String> singleImg = mBlog.getList("photos");
                if (singleImg != null && singleImg.size() > 0) {
                    Glide.with(this).load(singleImg.get(0) + "?imageView/1/w/400/h/400/q/30/format/jpeg")
                            .dontAnimate()
                            .into(mSinglePhoto);
                }
                mSinglePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SocialDetailActivity.this, PhotoActivity.class);
                        intent.putStringArrayListExtra(PhotoActivity.PHOTO_URL_LIST, new ArrayList<>(singleImg));
                        intent.putExtra(PhotoActivity.PHOTO_CURRENT_POSITION, 0);
                        startActivity(intent);
                    }
                });
                break;
            case 2:
                if(content!=null&&!content.isEmpty()){
                    mContent.setVisibility(View.VISIBLE);
                    mContent.setText(content);
                }
                mPhotoRecy.setVisibility(View.VISIBLE);
                List<String> multiImg = mBlog.getList("photos");
                if (multiImg != null && multiImg.size() > 0) {
                    SocialChildAdapter socialAdapter = new SocialChildAdapter(this, multiImg);
                    GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
                    layoutManager.setAutoMeasureEnabled(true);//交由layoutManager管理测量 自适应高度
                    mPhotoRecy.setLayoutManager(layoutManager);
                    mPhotoRecy.setAdapter(socialAdapter);
                }
                break;
            case 3:
                if(content!=null&&!content.isEmpty()){
                    mContent.setVisibility(View.VISIBLE);
                    mContent.setText(content);
                }
                mTmViewGroup.setVisibility(View.VISIBLE);
                AVObject tmObject=mBlog.getAVObject("transmit");
                mTmContent.setText("@"+tmObject.getAVUser("user").get("name")+" "+tmObject.getString("content"));
                switch (tmObject.getInt("type")){
                    case 1:
                    case 2:
                        mTmReyclerView.setVisibility(View.VISIBLE);
                        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
                        layoutManager.setAutoMeasureEnabled(true);//交由layoutManager管理测量 自适应高度
                        mTmReyclerView.setLayoutManager(layoutManager);
                        List<String> imgList = tmObject.getList("photos");
                        if(imgList!=null&&imgList.size()>0){
                            SocialChildAdapter socialAdapter = new SocialChildAdapter(this, imgList);
                            mTmReyclerView.setAdapter(socialAdapter);
                        }
                        break;
                }
                break;
        }

        if(likes.size()>0){
            if(likes.contains(AVUser.getCurrentUser())){
                mBtmLike.setSelected(true);
            }else {
                mBtmLike.setSelected(false);
            }
        }else {
            mBtmLike.setSelected(false);
        }
        mResultIntent.putExtra(SOCIAL_DETAIL_LIKE_SING,mBtmLike.isSelected());

        mPresenter=new PsocDetail(this);
        mComAdapter=new SocComAdapter(this);
        mLikeAdapter=new SocLikeAdapter(this);

        mRecyclerView.setAdapter(mComAdapter);

        mPresenter.getCommentList(mBlog);
        mPresenter.getLikeList(mBlog);
    }

    @Override
    protected void initListener() {
        mComment.setOnClickListener(this);
        mLike.setOnClickListener(this);
        mBtmComm.setOnClickListener(this);
        mBtmLike.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.SocialDetailActivity_Indicate_Comment:
                if(!mComment.isSelected()){
                    mComment.setSelected(true);
                    mLike.setSelected(false);
                    mRecyclerView.setAdapter(mComAdapter);
                }
                break;
            case R.id.SocialDetailActivity_Indicate_Like:
                if(!mLike.isSelected()){
                    mLike.setSelected(true);
                    mComment.setSelected(false);
                    mRecyclerView.setAdapter(mLikeAdapter);
                }
                break;
            case R.id.SocialDetailActivity_Bottom_comment:
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra(CommentActivity.COMMENT_BLOG_AVOBJECT,mBlog.toString());
                startActivityForResult(intent,COMMENT_REQUEST);
                break;
            case R.id.SocialDetailActivity_Bottom_like:
                mPresenter.likeControl(mBtmLike.isSelected(),likes,mBlog);
                break;
        }
    }

    @Override
    public void setCommentList(List<AVObject> list) {
        mComAdapter.setDataSource(list);
        mComAdapter.notifyDataSetChanged();
        mComment.setText(String.format(Locale.CHINA,"%d评论",list.size()));

        mResultIntent.removeExtra(SOCIAL_DETAIL_COMMENT_Num);
        mResultIntent.putExtra(SOCIAL_DETAIL_COMMENT_Num,list.size());
        setResult(RESULT_OK,mResultIntent);
    }

    @Override
    public void setLikeList(List<AVObject> list) {
        mLikeAdapter.setDataSource(list);
        mLikeAdapter.notifyDataSetChanged();
        mLike.setText(String.format(Locale.CHINA,"%d喜欢",list.size()));
    }

    @Override
    public void setBottomLike(boolean isSelected) {
        mBtmLike.setSelected(isSelected);
        mPresenter.getLikeList(mBlog);//获取最新的喜欢列表

        mResultIntent.removeExtra(SOCIAL_DETAIL_LIKE_SING);
        mResultIntent.putExtra(SOCIAL_DETAIL_LIKE_SING,isSelected);
    }

    @Override
    public void onSuccess(String str) {

    }

    @Override
    public void onActivityResult(int requestId,int resultId,Intent data){
        super.onActivityResult(requestId,resultId,data);
        if(resultId==RESULT_OK){
            if(data!=null){
                mPresenter.getCommentList(mBlog);//获取最新的评论列表
            }
        }
    }


}
