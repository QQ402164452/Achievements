package view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/24.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    public void setCustomToolbar(Toolbar toolbar){
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.tab_icon_back);
    }

    public void showProgress(View root){
        setWindow(0.4f);
        View view= getLayoutInflater().inflate(R.layout.base_loading_popupwindow,null);
        PopupWindow popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(root, Gravity.CENTER,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindow(1.0f);
            }
        });
    }

    public void setWindow(float alpha){
        WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
        layoutParams.alpha=alpha;
        getWindow().setAttributes(layoutParams);
    }


    public abstract void initView();
    public abstract void initListener();
    public abstract void initData();
}
