package view;

import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.jason.achievements.R;

import utils.Constants;

/**
 * Created by Jason on 2017/1/11.
 */

public class SysSettingActivity extends BaseActivity {
    private ImageView notificationSwitch;

    @Override
    protected void initPre() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_sys_setting);
        Toolbar toolbar= (Toolbar) findViewById(R.id.SysSettingActivity_toolbar);
        setCustomToolbar(toolbar);

        notificationSwitch= (ImageView) findViewById(R.id.SysSettingActivity_Notification_Switch);
    }

    @Override
    protected void initData() {
        SharedPreferences sysSettingInfo=getSharedPreferences(Constants.SYS_SETTING_SHAREPREFERENCE,MODE_PRIVATE);
        notificationSwitch.setSelected(sysSettingInfo.getBoolean(Constants.NOTIFICATION_SWITCH,true));
    }

    @Override
    protected void initListener() {
        notificationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sysSettingInfo=getSharedPreferences(Constants.SYS_SETTING_SHAREPREFERENCE,MODE_PRIVATE);
                SharedPreferences.Editor editor=sysSettingInfo.edit();
                if(notificationSwitch.isSelected()){
                    notificationSwitch.setSelected(false);
                    editor.putBoolean(Constants.NOTIFICATION_SWITCH,false);
                }else {
                    notificationSwitch.setSelected(true);
                    editor.putBoolean(Constants.NOTIFICATION_SWITCH,true);
                }
                editor.apply();
            }
        });
    }
}
