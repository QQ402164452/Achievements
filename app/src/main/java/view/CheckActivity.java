package view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import customView.SlidingTabLayout;
import fragment.CheckFragment;

/**
 * Created by Jason on 2016/12/5.
 */

public class CheckActivity extends BaseActivity {
    private SlidingTabLayout mTab;
    private ViewPager mViewPager;
    private FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragment;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_check);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.CheckActivity_toolbar);
        setCustomToolbar(toolbar);

        mTab= (SlidingTabLayout) findViewById(R.id.CheckActivity_slideTablayout);
        mViewPager= (ViewPager) findViewById(R.id.CheckActivity_viewpager);
        mFragmentManager=getSupportFragmentManager();
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        final ArrayList<String> title=new ArrayList<>();
        title.add("请假");
        title.add("加班");
        title.add("出差");
        mFragment=new ArrayList<>();
        mFragment.add(CheckFragment.newInstance(0));
        mFragment.add(CheckFragment.newInstance(1));
        mFragment.add(CheckFragment.newInstance(2));

        mViewPager.setAdapter(new FragmentStatePagerAdapter(mFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Override
            public CharSequence getPageTitle(int position){
                return title.get(position);
            }
        });
        mTab.setTabStripIndicatorPadding(50);
        mTab.setCustomTabView(R.layout.slidingtablayout_custom_view,0);
        mTab.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mTab.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            //每个选项卡下划线颜色
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
            //分割线颜色
            @Override
            public int getDividerColor(int position) {
                return Color.TRANSPARENT;
            }
        });
        mTab.setViewPager(mViewPager);
    }
}
