package presenter;

import com.example.jason.achievements.R;

import java.util.ArrayList;

import bean.EditListBean;
import interfaces.Iedit;

/**
 * Created by Jason on 2016/11/25.
 */

public class Pedit {
    private ArrayList<EditListBean> mList;
    private Iedit mIedit;

    public Pedit(Iedit iedit){
        this.mIedit=iedit;
    }

    public void setData(){
        mList=new ArrayList<>();
        mList.add(new EditListBean(0));
        mList.add(new EditListBean(1,"头像",String.valueOf(R.drawable.dayhr_userphoto_def)));
        mList.add(new EditListBean(2,"姓名","陈嘉生","请输入姓名"));
        mList.add(new EditListBean(3,"性别","保密"));
        mList.add(new EditListBean(3,"简介",""));
        mList.add(new EditListBean(0));
        mList.add(new EditListBean(3,"国家城市","中国广州"));
        mList.add(new EditListBean(2,"公司","腾讯","请输入公司名称"));
        mList.add(new EditListBean(2,"部门","","请输入公司部门"));
        mList.add(new EditListBean(2,"职位","CEO","请输入公司职位"));
        mList.add(new EditListBean(0));
        mList.add(new EditListBean(2,"电子邮箱","402164452@qq.com","请输入电子邮箱"));
        mIedit.setDataSource(mList);
    }

}
