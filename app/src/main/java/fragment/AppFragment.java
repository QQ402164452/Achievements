package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/23.
 */

public class AppFragment extends BaseFragment {
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_app,viewGroup,false);
        init(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initView(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.AppFragment_recyclerView);
    }
}
