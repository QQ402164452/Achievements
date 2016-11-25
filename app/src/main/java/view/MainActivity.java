package view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.jason.achievements.R;

import fragment.AppFragment;
import fragment.CenterFragment;
import fragment.ContactFragment;
import fragment.PersonalFragemnt;
import fragment.SocialFragment;

/**
 * Created by Jason on 2016/11/22.
 */

public class MainActivity extends BaseActivity{
    private RadioButton mAppBtn;
    private Fragment mContent;
    private FragmentManager mFragmentManager;
    private RadioGroup mRadioGroup;

    private AppFragment mAppFragment;
    private CenterFragment mCenterFragment;
    private PersonalFragemnt mPersonalFragment;
    private SocialFragment mSocialFragment;
    private ContactFragment mContactFragment;

    @Override
    public void onCreate(Bundle savedInstancedState){
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstancedState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public void initView(){
        mAppBtn= (RadioButton) findViewById(R.id.MainActivity_btn_app);
        mRadioGroup= (RadioGroup) findViewById(R.id.MainActivity_radioGroup);
    }

    public void initData(){
        mFragmentManager=getSupportFragmentManager();
        mAppFragment=new AppFragment();
        mCenterFragment=new CenterFragment();
        mPersonalFragment=new PersonalFragemnt();
        mSocialFragment=new SocialFragment();
        mContactFragment=new ContactFragment();

        initListener();
        mAppBtn.setChecked(true);
    }

    public void initListener(){
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.MainActivity_btn_app:
                        switchContent(mAppFragment);
                        break;
                    case R.id.MainActivity_btn_personal:
                        switchContent(mPersonalFragment);
                        break;
                    case R.id.MainActivity_btn_social:
                        switchContent(mSocialFragment);
                        break;
                    case R.id.MainActivity_btn_contact:
                        switchContent(mContactFragment);
                        break;
                    case R.id.MainActivity_btn_center:
                        switchContent(mCenterFragment);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void switchContent(Fragment to){
        FragmentTransaction transaction=mFragmentManager.beginTransaction();
        if(mContent!=null){
            if(!to.isAdded()){
                transaction.hide(mContent).add(R.id.MainActivity_fragmentContainer,to).commit();
            }else{
                transaction.hide(mContent).show(to).commit();
            }
            mContent=to;
        }else{
            transaction.add(R.id.MainActivity_fragmentContainer,to).commit();
            mContent=to;
        }
    }
}
