package view;

import android.widget.TextView;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/29.
 */

public abstract class UserBaseActivity extends BaseActivity {

    public void setUserNameDrawable(String gender,TextView view){
        switch (gender){
            case "保密":
                view.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                break;
            case "男":
                view.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.content_icon_male_def),null);
                break;
            case "女":
                view.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.content_icon_female_def),null);
                break;
        }
    }

    public void setText(TextView view, String str){
        if(str!=null){
            if(!str.isEmpty()){
                view.setText(str);
            }else{
                view.setText("暂无信息");
            }
        }else{
            view.setText("暂无信息");
        }
    }
}
