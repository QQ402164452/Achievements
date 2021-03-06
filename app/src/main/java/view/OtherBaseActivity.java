package view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/12/12.
 */

public abstract class OtherBaseActivity extends AppCompatActivity{

    public PopupWindow mBasePopup;
    public View mBase;

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

    public void showBasePopup(View view,View parent,
                              int layoutWidth,int layoutHeight,
                              int gravity,int x,int y){
        if(mBasePopup==null||!mBasePopup.isShowing()){
            setWindowAlpha(0.5f);
            mBasePopup=new PopupWindow(view,layoutWidth,
                    layoutHeight,true);
            mBasePopup.setFocusable(false);
            mBasePopup.setOutsideTouchable(false);
            mBasePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setWindowAlpha(1.0f);
                }
            });
            mBasePopup.showAtLocation(parent, gravity,x,y);
        }
    }

    public void showAsDropDownPopup(View view,View parent,
                                    int layoutWidth,int layoutHeight){
        if(mBasePopup==null||!mBasePopup.isShowing()){
            setWindowAlpha(0.5f);
            mBasePopup=new PopupWindow(view,layoutWidth,
                    layoutHeight,true);
            mBasePopup.setFocusable(false);
            mBasePopup.setOutsideTouchable(false);
            mBasePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setWindowAlpha(1.0f);
                }
            });
            mBasePopup.showAsDropDown(parent);
        }
    }

    public void hideBasePopup(){
        if(mBasePopup!=null&&mBasePopup.isShowing()){
            mBasePopup.dismiss();
        }
    }

    public void showLoading(View root){
        View view= getLayoutInflater().inflate(R.layout.base_loading_popupwindow,null);
        ImageView close= (ImageView) view.findViewById(R.id.Loading_popupwindow_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBasePopup.dismiss();
            }
        });
        showBasePopup(view,root, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER,0,0);
    }

    public void setWindowAlpha(float alpha){
        WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
        layoutParams.alpha=alpha;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mBasePopup!=null&&mBasePopup.isShowing()){
                mBasePopup.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }

    //拦截toolbar 返回事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    public void onError(String error) {
        showToast(error);
    }

    public void showLoading(){
        showLoading(mBase);
    }

    public abstract void initView();
    public abstract void initListener();
    public abstract void initData();
}
