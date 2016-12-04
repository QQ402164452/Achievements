package view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import customView.SlidingTabLayout;
import fragment.AppFragment;
import fragment.MyReportFragment;
import fragment.OtherReportFragment;

/**
 * Created by Jason on 2016/12/2.
 */

public class ReportActivity extends BaseActivity {
    private ImageView mAdd;
    private Toolbar mToolbar;
    private SlidingTabLayout mTab;
    private ViewPager mViewPager;

    public static String REPORT_TYPE="REPORT_TYPE";

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_report);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mToolbar= (Toolbar) findViewById(R.id.ReportActivity_toolbar);
        setCustomToolbar(mToolbar);
        mAdd= (ImageView) findViewById(R.id.ReportActivity_btn_add);
        mTab= (SlidingTabLayout) findViewById(R.id.ReportActivity_slideTablayout);
        mViewPager= (ViewPager) findViewById(R.id.ReportActivity_viewpager);
    }

    @Override
    public void initListener() {
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPopup();
            }
        });
    }

    public void showAddPopup(){
        View view=getLayoutInflater().inflate(R.layout.report_popup_add,null);
        LinearLayout mDay= (LinearLayout) view.findViewById(R.id.ReportActivity_report_day);
        LinearLayout mWeek= (LinearLayout) view.findViewById(R.id.ReportActivity_report_week);
        LinearLayout mMonth= (LinearLayout) view.findViewById(R.id.ReportActivity_report_month);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportActivity.this,WriteReportActivity.class);
                int type=0;
                switch (v.getId()){
                    case R.id.ReportActivity_report_day:
                        type=0;
                        break;
                    case R.id.ReportActivity_report_week:
                        type=1;
                        break;
                    case R.id.ReportActivity_report_month:
                        type=2;
                        break;
                }
                intent.putExtra(REPORT_TYPE,type);
                startActivity(intent);
            }
        };
        mDay.setOnClickListener(onClickListener);
        mWeek.setOnClickListener(onClickListener);
        mMonth.setOnClickListener(onClickListener);

        mBasePopup=new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        setWindowAlpha(0.5f);
        mBasePopup.showAsDropDown(mToolbar);
        mBasePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(1.0f);
            }
        });
    }

    @Override
    public void initData() {
        final ArrayList<String> tabList=new ArrayList<>();
        final ArrayList<Fragment> fragments=new ArrayList<>();
        tabList.add("我汇报的");
        tabList.add("我收到的");
        fragments.add(new MyReportFragment());
        fragments.add(new OtherReportFragment());

        FragmentManager fragmentManager=getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position){
                return tabList.get(position);
            }
        });
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

    @Override
    public void onPause(){
        super.onPause();
        if(mBasePopup!=null&&mBasePopup.isShowing()){
            mBasePopup.dismiss();
        }
    }
}
