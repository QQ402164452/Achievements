package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import adapter.SocialAdapter;
import customView.GlideCircleTransform;
import view.MainActivity;
import view.NewBlogActivity;

/**
 * Created by Jason on 2016/11/23.
 */

public class SocialFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private MainActivity mParent;
    private SocialAdapter mAdapter;
    private ImageView mUserImg;
    private ImageView mNewBtn;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mParent= (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view=layoutInflater.inflate(R.layout.fragment_social,container,false);
        init(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        AVUser user=AVUser.getCurrentUser();
        if(user!=null){
            AVFile img=user.getAVFile("portrait");
            if(img!=null){
                Glide.with(mParent).load(img.getUrl())
                        .dontAnimate()
                        .centerCrop()
                        .placeholder(R.drawable.dayhr_userphoto_def)
                        .bitmapTransform(new GlideCircleTransform(mParent))
                        .into(mUserImg);

            }else{
                mUserImg.setImageResource(R.drawable.dayhr_userphoto_def);
            }
        }
        mAdapter=new SocialAdapter(mParent);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mParent, NewBlogActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initView(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.SocialFragment_recyclerView);
        mUserImg= (ImageView) view.findViewById(R.id.SocialFragment_User_img);
        mNewBtn= (ImageView) view.findViewById(R.id.SocialFragment_new_Item);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mParent));
    }
}
