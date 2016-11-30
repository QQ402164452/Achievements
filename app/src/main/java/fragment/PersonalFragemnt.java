package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/23.
 */

public class PersonalFragemnt extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view=layoutInflater.inflate(R.layout.fragment_personal,container,false);
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

    }
}
