package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import adapter.SelectAdapter;
import interfaces.InewBlog;
import interfaces.OnSelectItemClickListener;
import presenter.PnewBlog;

/**
 * Created by Jason on 2016/12/20.
 */

public class NewBlogActivity extends BaseActivity implements View.OnClickListener, InewBlog{
    private TextView mCancel;
    private TextView mConfirm;
    private EditText mContent;
    private ArrayList<String> mSelect;//当前选择的图片数组
    private RecyclerView mRecyclerView;
    private SelectAdapter mAdapter;
    private PnewBlog mPresenter;

    public final static int PHOTO_PICK_REQUEST = 300;
    public final static int PREVIEW_PHOTO_REQUEST = 301;

    @Override
    protected void initPre() {
        mPresenter=new PnewBlog(this);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_new_blog);
        mCancel= (TextView) findViewById(R.id.NewBlogActivity_cancel);
        mConfirm= (TextView) findViewById(R.id.NewBlogActivity_confirm);
        mRecyclerView = (RecyclerView) findViewById(R.id.NewBlogActivity_RecyclerView);
        mContent= (EditText) findViewById(R.id.NewBlogActivity_content);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mBase=mConfirm;
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemClickListener(new OnSelectItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position, boolean isAddBtn) {
                if (isAddBtn) {
                    Intent intent = new Intent(NewBlogActivity.this, PhotoPickActivity.class);
                    ArrayList<String> temp = new ArrayList<>();
                    temp.addAll(mSelect.subList(0, mSelect.size() - 1));//去掉 加载更多的图片的 标志位
                    intent.putStringArrayListExtra(PhotoPickActivity.PHOTO_SELECT_IMG_ARRAYLIST, temp);
                    startActivityForResult(intent, PHOTO_PICK_REQUEST);
                } else {
                    Intent intent = new Intent(NewBlogActivity.this, PreviewActivity.class);
                    if (mSelect.size() < 9) {//如果图片不满9张 显示 加载更多图片的 标志
                        ArrayList<String> temp = new ArrayList<>();
                        temp.addAll(mSelect.subList(0, mSelect.size() - 1));//去掉 加载更多的图片的 标志位
                        intent.putStringArrayListExtra(PreviewActivity.PREVIEW_SELECT_IMG_ARRAYLIST, temp);
                    } else {//如果图片满9张 数组中没有 显示 加载更多图片的 标志位
                        intent.putStringArrayListExtra(PreviewActivity.PREVIEW_SELECT_IMG_ARRAYLIST, mSelect);
                    }
                    intent.putExtra(PreviewActivity.PREVIEW_SELECT_CURRENT_POSITION, position);
                    startActivityForResult(intent, PREVIEW_PHOTO_REQUEST);
                }
            }
        });
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mSelect = new ArrayList<>();
        mSelect.add("_Add_Btn");//添加 加载更多的图片的 标志位
        mAdapter = new SelectAdapter(this, mSelect);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onNewIntent(Intent intent) {//当前Activity的启动模式为SingleTask 从ACtivity栈上该Activity上的Activity启动该Activity会回调onNewIntent
        super.onNewIntent(intent);
        if (intent != null) {
            ArrayList<String> selects = intent.getStringArrayListExtra(PhotoSeeActivity.PHOTO_SEE_ARRAYLIST_SELECT_RESULT);
            mSelect.clear();
            mSelect.addAll(selects);
            if (selects.size() < 9) {//不满9张 显示加载更多的图标
                mSelect.add("_Add_Btn");
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestId, int resultId, Intent data) {
        super.onActivityResult(requestId, resultId, data);
        switch (requestId) {
            case PHOTO_PICK_REQUEST:
                switch (resultId) {
                    case PhotoPickActivity.PHOTO_TAKE_RESULT:
                        if (data != null) {
                            String imgPath = data.getStringExtra(PhotoPickActivity.PHOTO_TAKE_IMG);
                            mSelect.add(0, imgPath);
                            if (mSelect.size() > 9) {//满9张 不显示加载更多的图标
                                mSelect.remove(mSelect.size() - 1);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    case PhotoPickActivity.PHOTO_PICK_RESULT:
                        if (data != null) {
                            ArrayList<String> selects = data.getStringArrayListExtra(PhotoPickActivity.PHOTO_SELECT_IMG_ARRAYLIST);
                            mSelect.clear();
                            mSelect.addAll(selects);
                            if (mSelect.size() < 9) {//不满9张 显示加载更多的图标
                                mSelect.add("_Add_Btn");
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                break;
            case PREVIEW_PHOTO_REQUEST:
                switch (resultId) {
                    case PreviewActivity.PREVIEW_SELECT_RESULT:
                        if (data != null) {
                            ArrayList<String> selects = data.getStringArrayListExtra(PreviewActivity.PREVIEW_SELECT_IMG_ARRAYLIST);
                            mSelect.clear();
                            mSelect.addAll(selects);
                            if (mSelect.size() < 9) {//不满9张 显示加载更多的图标
                                mSelect.add("_Add_Btn");
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.NewBlogActivity_cancel:
                finish();
            break;
            case R.id.NewBlogActivity_confirm:
                if(mSelect.size()==9){
                    mPresenter.submitBlog(mContent.getText().toString(),mSelect);
                }else{
                    ArrayList<String> temp=new ArrayList<>();
                    temp.addAll(mSelect.subList(0,mSelect.size()-1));
                    mPresenter.submitBlog(mContent.getText().toString(),temp);
                }
            break;
        }
    }

    @Override
    public void onSuccess(final String str) {
        hideBasePopup();
        showToast(str);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onError(final String str){
        hideBasePopup();
        showToast(str);
    }

    @Override
    public void onLoadingDismiss(){
        mPresenter.cancel();
    }
}
