package view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

import fragment.AppFragment;
import fragment.CenterFragment;
import fragment.ContactFragment;
import fragment.PersonalFragment;
import fragment.SocialFragment;
import utils.CleanLeakUtils;
import utils.PermissionCodes;

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
    private PersonalFragment mPersonalFragment;
    private SocialFragment mSocialFragment;
    private ContactFragment mContactFragment;

    private boolean isShowConfirn=false;//标志位 判断是否确认退出

    @Override
    protected void initPre() {
    }

    public void initView(){
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mAppBtn= (RadioButton) findViewById(R.id.MainActivity_btn_app);
        mRadioGroup= (RadioGroup) findViewById(R.id.MainActivity_radioGroup);
        mBase=mRadioGroup;
    }

    public void initData(){
        mFragmentManager=getSupportFragmentManager();
        mAppFragment=new AppFragment();
        mCenterFragment=new CenterFragment();
        mPersonalFragment=new PersonalFragment();
        mSocialFragment=new SocialFragment();
        mContactFragment=new ContactFragment();

        checkAllPermission(PermissionCodes.perArr,PermissionCodes.PERMISSIONS_REQUEST_ALL);
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
        mAppBtn.setChecked(true);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(!isShowConfirn){
                showConfirm();
                return true;
            }else {
                isShowConfirn=false;
                return super.onKeyDown(keyCode,event);
            }
        }
        return super.onKeyDown(keyCode,event);
    }

    public void showConfirm(){
        View view=getLayoutInflater().inflate(R.layout.popup__confirm,null);
        Button confirm= (Button) view.findViewById(R.id.popup_Warn_Confirm);
        Button cancel= (Button) view.findViewById(R.id.popup_Warn_Cancel);
        TextView title= (TextView) view.findViewById(R.id.popup_Warn_title);
        title.setText("确认要退出APP吗？");
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.popup_Warn_Confirm:
                        hideBasePopup();
                        finish();
                        break;
                    case R.id.popup_Warn_Cancel:
                        hideBasePopup();
                        isShowConfirn=false;
                        break;
                }
            }
        };
        confirm.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
        showBasePopup(view,mAppBtn, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.CENTER,0,0);
        isShowConfirn=true;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(AVUser.getCurrentUser()==null){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDestroy(){
        CleanLeakUtils.fixInputMethodManagerLeak(MainActivity.this);
        super.onDestroy();
    }

}
