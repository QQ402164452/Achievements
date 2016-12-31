package interfaces;

import android.content.Intent;

/**
 * Created by Jason on 2016/12/28.
 */

public interface OnSocialItemClickListener {
    void OnShareClickListener(Intent intent);
    void OnCommentClickListener(Intent intent);
    void OnItemClickListener(Intent intent);
}
