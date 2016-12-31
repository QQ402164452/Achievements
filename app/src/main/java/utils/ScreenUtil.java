package utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import application.MyApplication;

/**
 * Created by Jason on 2016/12/27.
 */

public class ScreenUtil {
    private int displayWidth;
    private int displayHeight;

    private ScreenUtil() {
        DisplayMetrics metric = new DisplayMetrics();//获取系统屏幕信息
        WindowManager windowManager = (WindowManager) MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        this.displayWidth = metric.widthPixels;     // 屏幕宽度（像素）
        this.displayHeight = metric.heightPixels;   // 屏幕高度（像素）
    }

    private static class ScreenHolder {
        private final static ScreenUtil screenUtil = new ScreenUtil();
    }

    public static ScreenUtil getInstance() {
        return ScreenHolder.screenUtil;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }
}
