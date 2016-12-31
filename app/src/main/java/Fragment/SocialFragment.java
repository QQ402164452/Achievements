package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.SocialAdapter;
import customView.GlideCircleTransform;
import interfaces.Irecy;
import interfaces.Isocial;
import interfaces.OnSocialItemClickListener;
import presenter.Psocial;
import utils.WeakHandler;
import view.CommentActivity;
import view.MainActivity;
import view.NewBlogActivity;
import view.SocialDetailActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jason on 2016/11/23.
 */

public class SocialFragment extends BaseFragment implements Isocial {
    private XRecyclerView mRecyclerView;
    private MainActivity mParent;
    private SocialAdapter mAdapter;
    private ImageView mUserImg;
    private ImageView mNewBtn;
    private Psocial mPresenter;

    private int mSkip = 0;
    private List<AVObject> mList;

    public static final int COMMENT_REQUEST = 401;
    public static final int BLOG_DETAIL_REQUEST = 402;
    public static final int TRANSMIT_BLOG_REQUEST=403;
    public static final int NEW_BLOG_REQUEST=404;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParent = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_social, container, false);
        init(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new Psocial(this);
    }

    @Override
    public void initData() {
        AVUser user = AVUser.getCurrentUser();
        if (user != null) {
            AVFile img = user.getAVFile("portrait");
            if (img != null) {
                Glide.with(mParent).load(img.getUrl())
                        .dontAnimate()
                        .centerCrop()
                        .placeholder(R.drawable.dayhr_userphoto_def)
                        .bitmapTransform(new GlideCircleTransform(mParent))
                        .into(mUserImg);

            } else {
                mUserImg.setImageResource(R.drawable.dayhr_userphoto_def);
            }
        }
        mAdapter = new SocialAdapter(mParent);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getData(mSkip);
    }

    @Override
    public void initListener() {
        mNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mParent, NewBlogActivity.class);
                startActivityForResult(intent,NEW_BLOG_REQUEST);
            }
        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mSkip = 0;
                mPresenter.getData(mSkip);
            }

            @Override
            public void onLoadMore() {
                mPresenter.getData(mSkip);
            }
        });
        mAdapter.setListener(new OnSocialItemClickListener() {
            @Override
            public void OnShareClickListener(Intent intent) {
                startActivityForResult(intent,TRANSMIT_BLOG_REQUEST);
            }

            @Override
            public void OnCommentClickListener(Intent intent) {
                startActivityForResult(intent,COMMENT_REQUEST);
            }

            @Override
            public void OnItemClickListener(Intent intent) {
                startActivityForResult(intent,BLOG_DETAIL_REQUEST);
            }
        });
    }

    @Override
    public void initView(View view) {
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.SocialFragment_recyclerView);
        mUserImg = (ImageView) view.findViewById(R.id.SocialFragment_User_img);
        mNewBtn = (ImageView) view.findViewById(R.id.SocialFragment_new_Item);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mParent));
    }

    @Override
    public void setListData(List<AVObject> avObjects, int type) {
        switch (type) {
            case 0:
                mList=null;
                mList = avObjects;
                mSkip = mList.size();
                mAdapter.setDataSource(mList);
                mRecyclerView.refreshComplete();
                mRecyclerView.setNoMore(false);
                mRecyclerView.scrollToPosition(0);
                break;
            case 1:
                mRecyclerView.loadMoreComplete();
                if (avObjects.size() > 0) {
                    mList.addAll(avObjects);
                    mAdapter.notifyDataSetChanged();
                    mSkip = mList.size();
                } else {
                    mRecyclerView.setNoMore(true);
                }
                break;
        }

    }

    @Override
    public void showToast(String str) {
        mParent.showToast(str);
    }

    @Override
    public void onError(String error) {
        mParent.onError(error);
    }

    @Override
    public void onSuccess(String str) {

    }

    @Override
    public void showLoading() {
        mParent.showLoading();
    }

    @Override
    public void onActivityResult(int requestId, int resultId, Intent data) {
        super.onActivityResult(requestId, resultId, data);
        switch (requestId) {
            case COMMENT_REQUEST:
                if(resultId==RESULT_OK){
                    int position=data.getIntExtra(CommentActivity.COMMENT_BLOG_POSITION,0);
                    mAdapter.increaseComment(position);
                }
                break;
            case BLOG_DETAIL_REQUEST:
                if(resultId==RESULT_OK){
                    if(data!=null){
                        int position=data.getIntExtra(SocialDetailActivity.SOCIAL_DETAIL_POSITION,0);
                        int commNum=data.getIntExtra(SocialDetailActivity.SOCIAL_DETAIL_COMMENT_Num,0);
                        boolean isLike=data.getBooleanExtra(SocialDetailActivity.SOCIAL_DETAIL_LIKE_SING,false);
                        mAdapter.updateComNumAndLike(position,commNum,isLike);
                    }
                }
                break;
            case NEW_BLOG_REQUEST:
                if(resultId==RESULT_OK){
                    mRecyclerView.refresh();
                }
                break;
            case TRANSMIT_BLOG_REQUEST:
                if(resultId==RESULT_OK){
                    mRecyclerView.refresh();
                }
                break;
        }
    }
}
