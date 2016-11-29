package interfaces;

/**
 * Created by Jason on 2016/11/28.
 */

public interface Ipassword {

    void setConfirmBtn(boolean isEnable);
    void setVerifyBtn(boolean isEnable);
    void showToast(String str);
    void showErrorMessage(String str);
    void onPasswordResult(boolean isSuccess,String str);
}
