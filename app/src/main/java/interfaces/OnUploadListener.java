package interfaces;

import com.avos.avoscloud.AVFile;

/**
 * Created by Jason on 2016/12/27.
 */

public interface OnUploadListener {//主线程回调
    void onAllSuccess();
    void onAllFailed();
    void onThreadProgressChange(int position, int percent);
    void onThreadFinish(int position, AVFile avFile);
    void onThreadInterrupted(int position);
}
