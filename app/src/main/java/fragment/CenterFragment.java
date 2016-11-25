package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.CenterListAdapter;
import bean.CenterListBean;
import view.LoginActivity;
import view.MyActivity;
import view.MyCardActivity;
import view.SettingActivity;

/**
 * Created by Jason on 2016/11/23.
 */

public class CenterFragment extends Fragment {
    private ListView mListView;
    private CenterListAdapter mAdapter;
    private ImageView mUserImg;
    private TextView mUserName;
    private TextView mUserId;
    private RelativeLayout mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.centerFragment_ListView);
        mUserImg = (ImageView) view.findViewById(R.id.centerFragment_user_img);
        mUserName = (TextView) view.findViewById(R.id.centerFragment_user_name);
        mUserId = (TextView) view.findViewById(R.id.centerFragment_user_id);
        mUser= (RelativeLayout) view.findViewById(R.id.centerFragment_user);

        mAdapter = new CenterListAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser user=AVUser.getCurrentUser();
                if(user==null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }else{
                    Intent intent=new Intent(getActivity(), MyActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), MyCardActivity.class);
                        getActivity().startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 3:
                        break;
                    case 4:
                        intent = new Intent(getActivity(), SettingActivity.class);
                        getActivity().startActivity(intent);
                        break;
                }
            }
        });
    }

    private void initData() {
        AVUser user = AVUser.getCurrentUser();
        if (user != null) {
            mUserImg.setImageResource(R.drawable.dayhr_userphoto_def);
            mUserName.setText(user.get("name").toString());
            if(user.get("portrait")!=null){

            }else{
                mUserId.setText(String.format("员工ID:%s",user.get("employeeId").toString()));
            }
        } else {
            mUserImg.setImageResource(R.drawable.dayhr_userphoto_def);
            mUserName.setText("未登录");
            mUserId.setText("点击登录");
        }
    }
}
