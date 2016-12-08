package view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.Arrays;

import customView.SlidingTabLayout;
import fragment.MyExamineFragment;
import fragment.OtherExamineFragment;

/**
 * Created by Jason on 2016/12/6.
 */

public class ExamineActivity extends BaseActivity {
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_examine);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.ExamineActivity_toolbar);
        setCustomToolbar(toolbar);

        mTabLayout= (SlidingTabLayout) findViewById(R.id.ExamineActivity_slidingTabLayout);
        mViewPager= (ViewPager) findViewById(R.id.ExamineActivity_viewPager);
        mFragmentManager=getSupportFragmentManager();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        final ArrayList<String> mTabText=new ArrayList<>(Arrays.asList("我的流程","待处理的"));
        mFragments=new ArrayList<>();
        mFragments.add(new MyExamineFragment());
        mFragments.add(new OtherExamineFragment());
        mViewPager.setAdapter(new FragmentStatePagerAdapter(mFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position){
                return mTabText.get(position);
            }
        });
        mTabLayout.setCustomTabView(R.layout.slidingtablayout_custom_view,0);
        mTabLayout.setTabStripIndicatorPadding(100);
        mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }

            @Override
            public int getDividerColor(int position) {
                return Color.TRANSPARENT;
            }
        });
        mTabLayout.setViewPager(mViewPager);
    }
}
