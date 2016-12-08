package fragment;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/12/3.
 */

public abstract class LazyFragment extends Fragment {
    protected boolean isVisible;
    protected boolean isPrepared;
    protected boolean isFirst=true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){//判断Fragment可见
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            this.isVisible=true;
            lazyLoad();
        }else{
            this.isVisible=false;
        }
    }

    protected abstract void lazyLoad();

    public void init(View view){
        initView(view);
        initListener();
    }

    protected abstract void initView(View view);
    protected abstract void initListener();

}
