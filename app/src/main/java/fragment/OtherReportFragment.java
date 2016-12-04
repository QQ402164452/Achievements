package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.jason.achievements.R;

import java.util.ArrayList;

/**
 * Created by Jason on 2016/12/2.
 */

public class OtherReportFragment extends LazyFragment {
    private RadioGroup mRadioGroup;
    private RadioButton mDay;
    private Fragment mCurrent;
    private FragmentManager mFragmentManager;
    private OtherReportChildFragment mDayFragment;
    private OtherReportChildFragment mWeekFragment;
    private OtherReportChildFragment mMonthFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_report, parent, false);
        init(view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    public void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.OtherFragment_btn_day:
                        switchContent(mDayFragment);
                        break;
                    case R.id.OtherFragment_btn_week:
                        switchContent(mWeekFragment);
                        break;
                    case R.id.OtherFragment_btn_month:
                        switchContent(mMonthFragment);
                        break;
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if(isVisible&&isPrepared){
            mDay.setChecked(true);
        }
    }

    @Override
    public void initView(View view) {
        mRadioGroup = (RadioGroup) view.findViewById(R.id.OtherFragment_radioGroup);
        mDay = (RadioButton) view.findViewById(R.id.OtherFragment_btn_day);

        mFragmentManager = getChildFragmentManager();
        mDayFragment = OtherReportChildFragment.newInstance(0);
        mWeekFragment = OtherReportChildFragment.newInstance(1);
        mMonthFragment = OtherReportChildFragment.newInstance(2);
    }

    public void switchContent(Fragment to) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mCurrent != null) {
            if (!to.isAdded()) {
                transaction.hide(mCurrent).add(R.id.OtherFragment_fragmentContainer, to).commit();
            } else {
                transaction.hide(mCurrent).show(to).commit();
            }
        }else {
            transaction.add(R.id.OtherFragment_fragmentContainer,to).commit();
        }
        mCurrent=to;
    }

}
