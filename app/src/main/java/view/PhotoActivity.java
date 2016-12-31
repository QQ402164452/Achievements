package view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.List;

import fragment.ImageFragment;

/**
 * Created by Jason on 2016/12/28.
 */

public class PhotoActivity extends PreviewBaseActivity {
    private ViewPager mViewPager;
    private List<String> mList;
    private int mSize;
    private int mCurPos;
    private TextView mPosText;

    public final static String PHOTO_CURRENT_POSITION="PHOTO_CURRENT_POSITION";
    public final static String PHOTO_URL_LIST="PHOTO_URL_LIST";

    @Override
    public void setResultBack() {

    }

    @Override
    protected void initPre() {
        Intent intent=getIntent();
        if(intent!=null){
            mCurPos=intent.getIntExtra(PHOTO_CURRENT_POSITION,0);
            mList=intent.getStringArrayListExtra(PHOTO_URL_LIST);
            mSize=mList.size();
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_photo);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mToolbar= (Toolbar) findViewById(R.id.PhotoActivity_Toolbar);
        setCustomToolbar(mToolbar);
        mToolbar.setPadding(0,getStatusBarHeight(),0,0);//设置状态栏高度的padding

        mViewPager= (ViewPager) findViewById(R.id.PhotoActivity_ViewPager);
        mPosText= (TextView) findViewById(R.id.PhotoActivity_Current_Position);
    }

    @Override
    protected void initData() {
        mPosText.setText((mCurPos+1)+"/"+mSize);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ImageFragment.newInstance(mList.get(position));
            }

            @Override
            public int getCount() {
                return mList.size();
            }
        });
        mViewPager.setCurrentItem(mCurPos);
    }

    @Override
    protected void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurPos=position;
                mPosText.setText((mCurPos+1)+"/"+mSize);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
