package fragment;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Jason on 2016/11/30.
 */

public abstract class BaseFragment extends Fragment {

    public abstract void initData();
    public abstract void initListener();
    public abstract void initView(View view);

    public void init(View view){
        initView(view);
        initData();
        initListener();
    }

}
