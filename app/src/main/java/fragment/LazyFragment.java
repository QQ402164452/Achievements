package fragment;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Jason on 2016/12/3.
 */

public abstract class LazyFragment extends Fragment {
    protected boolean isVisible;
    protected boolean isPrepared;

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
