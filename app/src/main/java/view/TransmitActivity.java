package view;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.jason.achievements.R;

import java.util.List;

import adapter.SocialChildAdapter;
import interfaces.Itransmit;
import presenter.Ptransmit;

/**
 * Created by Jason on 2016/12/30.
 */

public class TransmitActivity extends BaseActivity implements View.OnClickListener, Itransmit {
    private TextView mCancel;
    private TextView mConfirm;
    private EditText mContent;
    private Ptransmit mPresenter;
    private TextView mTmTitle;
    private RecyclerView mRecyclerView;

    private AVObject mBlog;
    private int mType;

    public static final String TRANSMIT_BLOG="TRANSMIT_BLOG";

    @Override
    protected void initPre() {
        Intent intent=getIntent();
        if(intent!=null){
            try {
                mBlog=AVObject.parseAVObject(intent.getStringExtra(TRANSMIT_BLOG));
                mType=mBlog.getInt("type");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mPresenter=new Ptransmit(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_transmit);

        mCancel= (TextView) findViewById(R.id.TransmitActivity_cancel);
        mConfirm= (TextView) findViewById(R.id.TransmitActivity_confirm);
        mContent= (EditText) findViewById(R.id.TransmitActivity_content);
        mTmTitle= (TextView) findViewById(R.id.TransmitActivity_Other_Content);
        mRecyclerView= (RecyclerView) findViewById(R.id.TransmitActivity_Other_RecyclerView);
        mBase=mContent;
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setAutoMeasureEnabled(true);//交由layoutManager管理测量 自适应高度
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        switch (mType){
            case 1:
            case 2:
                mRecyclerView.setVisibility(View.VISIBLE);
                List<String> imgList = mBlog.getList("photos");
                if(imgList!=null&&imgList.size()>0){
                    SocialChildAdapter socialAdapter = new SocialChildAdapter(this, imgList);
                    mRecyclerView.setAdapter(socialAdapter);
                }
                mTmTitle.setText("@"+mBlog.getAVUser("user").get("name")+" "+mBlog.getString("content"));
                break;
            case 0:
                mTmTitle.setText("@"+mBlog.getAVUser("user").get("name")+" "+mBlog.getString("content"));
                break;
            case 3:
                AVObject tmObject=mBlog.getAVObject("transmit");
                mTmTitle.setText("@"+mBlog.getAVUser("user").get("name")+" "+mBlog.getString("content")+
                        " / @"+tmObject.getAVUser("user").get("name")+" "+tmObject.getString("content"));
                switch (tmObject.getInt("type")){
                    case 1:
                    case 2:
                        mRecyclerView.setVisibility(View.VISIBLE);
                        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
                        layoutManager.setAutoMeasureEnabled(true);//交由layoutManager管理测量 自适应高度
                        mRecyclerView.setLayoutManager(layoutManager);
                        List<String> imgLists = tmObject.getList("photos");
                        if(imgLists!=null&&imgLists.size()>0){
                            SocialChildAdapter socialAdapter = new SocialChildAdapter(this, imgLists);
                            mRecyclerView.setAdapter(socialAdapter);
                        }
                        break;
                }
                break;
        }
    }

    @Override
    protected void initListener() {
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onSuccess(String str) {
        hideBasePopup();
        showToast(str);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onError(String str){
        hideBasePopup();
        showToast(str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.TransmitActivity_cancel:
                finish();
                break;
            case R.id.TransmitActivity_confirm:
                mPresenter.transmitBlog(mBlog,mContent.getText().toString());
                break;
        }
    }
}
