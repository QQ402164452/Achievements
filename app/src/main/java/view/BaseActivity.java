package view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.avos.avoscloud.AVQuery;
import com.example.jason.achievements.R;

import java.util.ArrayList;
import java.util.List;

import utils.PermissionCodes;

/**
 * Created by Jason on 2016/11/24.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public PopupWindow mBasePopup;
    public View mBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPre();//初始化前的准备 例如获取之前Activity传递过来的数据 getIntent()
        initView();//初始化视图
        initData();//初始化数据
        initListener();//初始化监听器
    }

    public void setCustomToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.tab_icon_back);
    }

    public void showBasePopup(View view, View parent,
                              int layoutWidth, int layoutHeight,
                              int gravity, int x, int y) {
        if (mBasePopup == null || !mBasePopup.isShowing()) {
            setWindowAlpha(0.5f);
            mBasePopup = new PopupWindow(view, layoutWidth,
                    layoutHeight);
            mBasePopup.setFocusable(false);
            mBasePopup.setOutsideTouchable(false);
            mBasePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setWindowAlpha(1.0f);
                }
            });
            mBasePopup.showAtLocation(parent, gravity, x, y);
        }
    }

    public void showAsDropDownPopup(View view, View parent,
                                    int layoutWidth, int layoutHeight) {
        if (mBasePopup == null || !mBasePopup.isShowing()) {
            setWindowAlpha(0.5f);
            mBasePopup = new PopupWindow(view, layoutWidth,
                    layoutHeight);
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

    public void hideBasePopup() {
        if (mBasePopup != null && mBasePopup.isShowing()) {
            mBasePopup.dismiss();
        }
    }

    public void showLoading(View root) {
        View view = getLayoutInflater().inflate(R.layout.base_loading_popupwindow, null);
        ImageView close = (ImageView) view.findViewById(R.id.Loading_popupwindow_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBasePopup.dismiss();
                onLoadingDismiss();
            }
        });
        showBasePopup(view, root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER, 0, 0);
    }

    public void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = alpha;
        getWindow().setAttributes(layoutParams);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (mBasePopup != null && mBasePopup.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBasePopup != null && mBasePopup.isShowing()) {
                mBasePopup.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //拦截toolbar 返回事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void onError(String error) {
        showToast(error);
    }

    public void showLoading() {
        showLoading(mBase);
    }

    public void startAnimation(View view, int AnimationId) {//开始动画
        if (view != null && AnimationId != 0) {
            Animation animation = AnimationUtils.loadAnimation(this, AnimationId);
            view.startAnimation(animation);
        }
    }

    protected abstract void initPre();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected void checkPermission(String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    showAlertDialog("部分权限被禁止，将导致程序无法正常运行。是否开启部分权限？(步骤：应用信息->权限->'勾选')");
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
                }
            }
        }
    }

    protected void checkAllPermission(String[] perArray, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> deniedPers = new ArrayList<>();
            for (int i = 0; i < perArray.length; i++) {//获取批量请求中 被拒绝的权限列表
                if (ContextCompat.checkSelfPermission(this, perArray[i]) != PackageManager.PERMISSION_GRANTED) {
                    deniedPers.add(perArray[i]);
                }
            }
            int deniedSize = deniedPers.size();
            if (deniedSize != 0) {//进行批量请求
                ActivityCompat.requestPermissions(this, deniedPers.toArray(new String[deniedSize]), requestCode);
            }
        }
    }

    protected void onRequestAllResult(String[] permission, int[] grantResults) {
        int grantedNum = 0;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedNum++;
            }
        }
        if (grantedNum != permission.length) {
            showAlertDialog("部分权限被禁止，将导致程序无法正常运行。是否开启部分权限？(步骤：应用信息->权限->'勾选')");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        switch (requestCode) {
            case PermissionCodes.PERMISSIONS_REQUEST_ALL:
                onRequestAllResult(permission, grantResults);
                break;
            case PermissionCodes.PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionSuccess();
                }else{
                    showAlertDialog("位置信息权限被禁止，将导致定位失败。是否开启该权限？(步骤：应用信息->权限->'勾选'位置信息)");
                }
                break;
            case PermissionCodes.PERMISSIONS_REQUEST_PHONE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionSuccess();
                }else {
                    showAlertDialog("拨打电话权限被禁止，无法使用拨打电话功能。是否开启该权限？(步骤：应用信息->权限->'勾选'电话)");
                }
                break;
            case PermissionCodes.PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionSuccess();
                }else {
                    showAlertDialog("存储空间权限被禁止，无法使用读取存储空间。是否开启该权限？(步骤：应用信息->权限->'勾选'存储空间)");
                }
                break;
        }
    }

    protected void showAlertDialog(String content) {
        new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage(content)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri packageUri = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }).create().show();
    }

    public void onPermissionSuccess(){

    }

    public void onLoadingDismiss(){

    }
}
