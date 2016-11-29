package interfaces;

/**
 * Created by Jason on 2016/11/28.
 */

public interface Iverify {
    void showToast(String str);
    void showErrorMessage(String str);
    void setVerifyBtn(boolean isEnable);
    void setGetVerifyBtn(boolean isEnable);
    void onVerifyResult(boolean isSuccess,String message);
    void setPhone(String phone);
}
