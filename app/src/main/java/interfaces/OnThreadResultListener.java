package interfaces;

import com.avos.avoscloud.AVFile;

/**
 * Created by Jason on 2016/12/26.
 */

public interface OnThreadResultListener {
    void onProgressChange(int percent);//进度变化回调
    void onFinish(AVFile avFile);//线程完成时回调
    void onInterrupted();//线程被中断回调
}
