package interfaces;

/**
 * Created by Jason on 2016/12/26.
 */

public interface OnAllThreadResultListener {
    void onSuccess();//所有线程执行完毕
    void onFailed();//所有线程执行出现问题
}
