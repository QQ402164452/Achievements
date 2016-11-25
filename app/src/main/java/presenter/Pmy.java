package presenter;

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

    public void setData(){
        mList=new ArrayList<>();
        mList.add(new MyDataBean(0));
        mList.add(new MyDataBean(1,"简介","我就是我，不一样的烟火！"));
        mList.add(new MyDataBean(1,"员工ID","12306"));
        mList.add(new MyDataBean(0));
        mList.add(new MyDataBean(1,"国家城市","中国广州"));
        mList.add(new MyDataBean(1,"公司","康佳集团"));
        mList.add(new MyDataBean(1,"部门","多媒体研发中心"));
        mList.add(new MyDataBean(1,"职位","CEO"));
        mList.add(new MyDataBean(0));
        mList.add(new MyDataBean(1,"手机号码","15692012612"));
        mList.add(new MyDataBean(1,"电子邮箱","402164452@qq.com"));
        mList.add(new MyDataBean(0));
        mImy.setDataSource(mList);
    }

}
