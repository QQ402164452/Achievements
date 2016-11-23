package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jason.achievements.R;

import adapter.CenterListAdapter;

/**
 * Created by Jason on 2016/11/23.
 */

public class CenterFragment extends Fragment{
    private ListView mListView;
    private CenterListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_center,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    private void initView(View view){
        mListView= (ListView) view.findViewById(R.id.centerFragment_ListView);
        mAdapter=new CenterListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
    }
}
