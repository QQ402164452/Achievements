package interfaces;

/**
 * Created by Jason on 2016/12/3.
 */

public interface Ibase {
    void showToast(String str);
    void onError(String error);
    void onSuccess(String str);
}
