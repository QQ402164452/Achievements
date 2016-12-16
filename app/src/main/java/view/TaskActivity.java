package view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.Arrays;

import customView.SlidingTabLayout;
import fragment.MyTaskFragment;
import fragment.TodoFragment;

/**
 * Created by Jason on 2016/12/9.
 */

public class TaskActivity  extends OtherBaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private SlidingTabLayout mTab;
    private ImageView mNewTask;
    private ImageView mBack;
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitle;
    private FragmentManager mManager;
    public Toolbar mToolbar;
    private TodoFragment mTodoFragmnet;
    private MyTaskFragment mMyTaskFragment;

    public final static int NEW_TASK_RESULT_REQUEST=303;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_task);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mToolbar= (Toolbar) findViewById(R.id.TaskActivity_toolbar);
        mViewPager= (ViewPager) findViewById(R.id.TaskActivity_ViewPager);
        mTab= (SlidingTabLayout) findViewById(R.id.TaskActivity_slidingTab);
        mNewTask= (ImageView) findViewById(R.id.TaskActivity_new_Task);
        mBack= (ImageView) findViewById(R.id.TaskActivity_back);
        mManager=getSupportFragmentManager();
    }

    @Override
    public void initListener() {
        mNewTask.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideBasePopup();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
        mTitle=new ArrayList<>(Arrays.asList("待办任务","我发布的"));
        mFragments=new ArrayList<>();
        mTodoFragmnet=new TodoFragment();
        mMyTaskFragment=new MyTaskFragment();
        mFragments.add(mTodoFragmnet);
        mFragments.add(mMyTaskFragment);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(mManager) {
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
                return mTitle.get(position);
            }
        });
        mTab.setCustomTabView(R.layout.task_toolbar_custom_view,0);
        mTab.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mTab.setTabStripIndicatorPadding(50);
        mTab.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }

            @Override
            public int getDividerColor(int position) {
                return Color.TRANSPARENT;
            }
        });
        mTab.setViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        hideBasePopup();
        switch (v.getId()){
            case R.id.TaskActivity_back:
                finish();
                break;
            case R.id.TaskActivity_new_Task:
                Intent intent=new Intent(this,NewTaskActivity.class);
                startActivityForResult(intent,NEW_TASK_RESULT_REQUEST);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestId,int resultId,Intent data){
        super.onActivityResult(requestId,resultId,data);
        if(resultId==RESULT_OK){
            refresh();
        }
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        refresh();
    }

    public void refresh(){
        mMyTaskFragment.isFirst=true;
        mMyTaskFragment.lazyLoad();
        mTodoFragmnet.isFirst=true;
        mTodoFragmnet.lazyLoad();
    }
}
