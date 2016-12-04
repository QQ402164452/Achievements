package view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.achievements.R;
import com.example.wheelview.OnWheelChangedListener;
import com.example.wheelview.WheelView;
import com.example.wheelview.adapters.ListWheelAdapter;


import java.util.ArrayList;

import adapter.EditListAdapter;
import adapter.ProvinceAdapter;
import bean.CityBean;
import bean.EditListBean;
import customView.DividerItemDecoration;
import interfaces.Iedit;
import interfaces.OnCustomItemClickListener;
import interfaces.onTextChangeListener;
import presenter.Pedit;
import utils.ErrorUtil;

/**
 * Created by Jason on 2016/11/25.
 */

public class EditActivity extends BaseActivity implements Iedit{
    private TextView mSave;
    private Pedit mPedit;
    private RecyclerView mRecyclerView;
    private EditListAdapter mAdapter;
    private CityBean mCitys;
    private ArrayList<String> mGender;

    public static int INTRO_REQUEST_CODE=100;
    public static int INTRO_RESULT_CODE=101;
    public static String INTRO_REQUEST_STRING="intro_request_string";
    public static String INTRO_RESULT_STRING="intro_result_string";
    public static int EDIT_USER_REQUEST_CODE=100;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_edit);
        super.onCreate(savedInstanceState);
    }

    public void initView(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.EditActivity_Toolbar);
        setCustomToolbar(toolbar);
        mSave= (TextView) findViewById(R.id.EditActivity_btn_save);
        mRecyclerView= (RecyclerView) findViewById(R.id.EditActivity_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mAdapter=new EditListAdapter(this);
        mPedit=new Pedit(this);
    }

    public void initListener(){
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPedit.saveUser();
            }
        });
        mAdapter.setOnItemClickListener(new OnCustomItemClickListener(){
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent;
                switch (position){
                    case 1:
                        intent=new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent,EDIT_USER_REQUEST_CODE);
                        break;
                    case 3:
                        setGenderPopup();
                        break;
                    case 4:
                        intent=new Intent(EditActivity.this,IntroductionActivity.class);
                        intent.putExtra(INTRO_REQUEST_STRING,mPedit.getListValue(4));
                        startActivityForResult(intent,INTRO_REQUEST_CODE);
                        break;
                    case 6:
                        setCityPopup();
                        break;
                    default:
                        break;
                }
            }
        });
        mAdapter.setOnTextChangeListener(new onTextChangeListener() {
            @Override
            public void onTextChanged(int pos, String str) {
                mPedit.setList(pos,str);
            }
        });
    }

    @Override
    public void initData() {
        mPedit.setData();
        mCitys=mPedit.getCityBean(this);
        mGender=mPedit.getGender();
    }

    @Override
    public void setDataSource(ArrayList<EditListBean> list) {
        mAdapter.setData(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void showPopupWindow(View view){
        clearFocus();
        showBasePopup(view,mRecyclerView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM,0,0);
    }

    public void setCityPopup() {
        View selectView=getLayoutInflater().inflate(R.layout.edit_popupwindow_select_double,null);
        TextView cancel= (TextView) selectView.findViewById(R.id.EditActivity_popup_double_cancel);
        TextView confirm= (TextView) selectView.findViewById(R.id.EditActivity_popup_double_confirm);
        final WheelView left= (WheelView) selectView.findViewById(R.id.EditActivity_popup_double_left);
        final WheelView right= (WheelView) selectView.findViewById(R.id.EditActivity_popup_double_right);
        left.setViewAdapter(new ProvinceAdapter(this,mCitys));
        right.setViewAdapter(new ListWheelAdapter<>(EditActivity.this,mCitys.getData().get(0).getC()));
        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBasePopup.isShowing()){
                    mBasePopup.dismiss();
                }
                switch (v.getId()){
                    case R.id.EditActivity_popup_double_confirm:
                        mPedit.updateRecyclerView(6,
                                mCitys.getData().get(left.getCurrentItem()).getP()+
                                        mCitys.getData().get(left.getCurrentItem()).getC().get(right.getCurrentItem()));
                }
            }
        };
        cancel.setOnClickListener(clickListener);
        confirm.setOnClickListener(clickListener);

        left.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                right.setViewAdapter(new ListWheelAdapter<>(EditActivity.this,mCitys.getData().get(newValue).getC()));
                right.setCurrentItem(0);
            }
        });
        showPopupWindow(selectView);
    }

    public void setGenderPopup(){
        View selectView=getLayoutInflater().inflate(R.layout.edit_popupwindow_select_single,null);
        TextView cancel= (TextView) selectView.findViewById(R.id.EditActivity_popup_single_cancel);
        TextView confirm= (TextView) selectView.findViewById(R.id.EditActivity_popup_single_confirm);
        final WheelView wheelView= (WheelView) selectView.findViewById(R.id.EditActivity_popup_single_wheel);
        wheelView.setViewAdapter(new ListWheelAdapter<>(this,mGender));
        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBasePopup.isShowing()){
                    mBasePopup.dismiss();
                }
                switch (v.getId()){
                    case R.id.EditActivity_popup_single_confirm:
                        mPedit.updateRecyclerView(3,mGender.get(wheelView.getCurrentItem()));
                }
            }
        };
        cancel.setOnClickListener(clickListener);
        confirm.setOnClickListener(clickListener);
        showPopupWindow(selectView);
    }

    @Override
    public void updateRecyclerView(int pos) {
        mAdapter.notifyItemChanged(pos);
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveResult(boolean isSuccess, String str) {
        hideBasePopup();
        if(isSuccess){
            showToast(str);
            finish();
        }else {
            showToast(ErrorUtil.getErrorMessage(str));
        }
    }

    @Override
    public void showLoading() {
        super.showLoading(mRecyclerView);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        clearFocus();
        if(requestCode==INTRO_REQUEST_CODE&&resultCode==INTRO_RESULT_CODE){
            if(data!=null){
                mPedit.updateRecyclerView(4,data.getStringExtra(INTRO_RESULT_STRING));
            }
        }else if(requestCode==EDIT_USER_REQUEST_CODE&&resultCode== Activity.RESULT_OK){
            if(data!=null){
                Uri imgUri=data.getData();
//                String[] filePathColumns={MediaStore.Images.Media.DATA};
//                Cursor c=getContentResolver().query(imgUri,filePathColumns,null,null,null);
//                c.moveToFirst();
//                int columnIndex=c.getColumnIndex(filePathColumns[0]);
//                String imgPath=c.getString(columnIndex);
//                c.close();
                mPedit.setList(1,imgUri,this);
            }
        }
    }

    public void clearFocus(){
        mRecyclerView.clearFocus();//清除ViewGroup中的焦点
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//强制关闭输入法
        inputMethodManager.hideSoftInputFromWindow(mRecyclerView.getWindowToken(),0);
    }

}
