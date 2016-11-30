package view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/24.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected PopupWindow mLoading;

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

    public void showLoading(View root){
        if(mLoading==null||!mLoading.isShowing()){
            setWindow(0.5f);
            View view= getLayoutInflater().inflate(R.layout.base_loading_popupwindow,null);
            ImageView close= (ImageView) view.findViewById(R.id.Loading_popupwindow_close);
            mLoading=new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,true);
            mLoading.setFocusable(false);
            mLoading.setOutsideTouchable(false);
            mLoading.showAtLocation(root, Gravity.CENTER,0,0);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLoading.dismiss();
                }
            });
            mLoading.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setWindow(1.0f);
                }
            });
        }
    }

    public void hideLoading(){
        if(mLoading!=null&&mLoading.isShowing()){
            mLoading.dismiss();
        }
    }

    public void setWindow(float alpha){
        WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
        layoutParams.alpha=alpha;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e){
        if(mLoading!=null&&mLoading.isShowing()){
            return false;
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mLoading!=null&&mLoading.isShowing()){
                mLoading.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }


    public abstract void initView();
    public abstract void initListener();
    public abstract void initData();
}
