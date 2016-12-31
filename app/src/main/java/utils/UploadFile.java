package utils;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import interfaces.OnThreadResultListener;

/**
 * Created by Jason on 2016/12/26.
 */

public class UploadFile implements Runnable {
    private CountDownLatch downLatch;//计数器
    private String filePath;//文件名
    private OnThreadResultListener listener;//任务线程回调接口
    private AVFile avFile=null;

    public UploadFile(CountDownLatch downLatch, String filePath, OnThreadResultListener listener) {
        this.downLatch = downLatch;
        this.filePath = filePath;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            File outFile=ImageUtil.compressImage(filePath,
                    ScreenUtil.getInstance().getDisplayWidth(),
                    ScreenUtil.getInstance().getDisplayHeight());
            if(outFile!=null){
                avFile=AVFile.withAbsoluteLocalPath(outFile.getName(), outFile.getAbsolutePath());
                avFile.save();
            }
            this.downLatch.countDown();
            listener.onFinish(avFile);//顺利完成
        } catch (Exception e) {
            avFile.cancel();
            listener.onInterrupted();//被中断
        }
    }
}
