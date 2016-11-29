package presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetDataCallback;

import java.util.ArrayList;

import bean.MyDataBean;
import interfaces.Imy;

/**
 * Created by Jason on 2016/11/25.
 */

public class Pmy {
    private Imy mImy;
    private ArrayList<MyDataBean> mList;

    public Pmy(Imy imy){
        this.mImy=imy;
    }

    public void setData(AVUser user){
        mList=new ArrayList<>();
        mList.add(new MyDataBean(0));
        mList.add(new MyDataBean(1,"简介",user.getString("introduce")));
        mList.add(new MyDataBean(1,"员工ID",String.valueOf(user.getInt("employeeId"))));
        mList.add(new MyDataBean(0));
        mList.add(new MyDataBean(1,"国家城市",user.getString("position")));
        mList.add(new MyDataBean(1,"公司",user.getString("company")));
        mList.add(new MyDataBean(1,"部门",user.getString("department")));
        mList.add(new MyDataBean(1,"职位",user.getString("job")));
        mList.add(new MyDataBean(0));
        mList.add(new MyDataBean(1,"手机号码",user.getMobilePhoneNumber()));
        mList.add(new MyDataBean(1,"电子邮箱",user.getEmail()));
        mList.add(new MyDataBean(0));
        mImy.setDataSource(mList);
    }

}
