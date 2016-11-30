package presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.jason.achievements.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import bean.CityBean;
import bean.EditListBean;
import interfaces.Iedit;
import utils.NetworkUtil;
import utils.StringUtil;

/**
 * Created by Jason on 2016/11/25.
 */

public class Pedit {
    private ArrayList<EditListBean> mList=new ArrayList<>();
    private Iedit editView;
    private byte[] mImgByte=null;

    public Pedit(Iedit iedit){
        this.editView=iedit;
    }

    public void setData(){
        AVUser user=AVUser.getCurrentUser();
        mList.add(new EditListBean(0));
        AVFile f=user.getAVFile("portrait");
        if(f!=null){
            mList.add(new EditListBean(1,"头像",f.getUrl()));
        }else {
            mList.add(new EditListBean(1,"头像",null));
        }
        mList.add(new EditListBean(2,"姓名",user.getString("name"),"请输入姓名"));
        mList.add(new EditListBean(3,"性别",user.getString("gender"),"请选择性别"));
        mList.add(new EditListBean(3,"简介",user.getString("introduce"),"进入个人简介编辑"));
        mList.add(new EditListBean(0));
        mList.add(new EditListBean(3,"城市",user.getString("position"),"请选择城市"));
        mList.add(new EditListBean(2,"公司",user.getString("company"),"请输入公司名称"));
        mList.add(new EditListBean(2,"部门",user.getString("department"),"请输入公司部门"));
        mList.add(new EditListBean(2,"职位",user.getString("job"),"请输入公司职位"));
        mList.add(new EditListBean(0));
        mList.add(new EditListBean(2,"电子邮箱",user.getEmail(),"请输入电子邮箱"));
        editView.setDataSource(mList);

    }

    public void saveUser(){
        if(NetworkUtil.isNewWorkAvailable()){
            editView.showLoading();
            try {
                final AVUser user=AVUser.getCurrentUser();
                if(mImgByte!=null){
                    AVFile img=new AVFile(user.getMobilePhoneNumber()+"_img",mImgByte);
                    user.put("portrait",img);
                }
                user.put("name",mList.get(2).getValue());
                user.put("gender",mList.get(3).getValue());
                user.put("introduce",mList.get(4).getValue());
                user.put("position",mList.get(6).getValue());
                user.put("company",mList.get(7).getValue());
                user.put("department",mList.get(8).getValue());
                user.put("job",mList.get(9).getValue());
                user.setEmail((String)mList.get(11).getValue());
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            user.fetchInBackground(new GetCallback<AVObject>() {
                                @Override
                                public void done(AVObject avObject, AVException e) {
                                    if(e==null){
                                        editView.onSaveResult(true,"保存成功！");
                                    }else{
                                        editView.onSaveResult(false,e.getMessage());
                                    }
                                }
                            });
                        }else{
                            editView.onSaveResult(false,e.getMessage());
                        }
                    }
                });
            }catch (Exception e){
                editView.showToast("网络出现问题了!");
                Log.e("save",e.getMessage());
            }
        }else {
            editView.showToast(NetworkUtil.tip);
        }
    }


    public CityBean getCityBean(Context context){
        try {
            InputStream is=context.getAssets().open("province_city.txt");
            String text= StringUtil.getInstance().readText(is);
            CityBean citys= JSON.parseObject(text,CityBean.class);
            return citys;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getGender(){
        ArrayList<String> list=new ArrayList<>();
        list.add("保密");
        list.add("男");
        list.add("女");
        return list;
    }

    public String getListValue(int pos){
        return (String) mList.get(pos).getValue();
    }

    public void updateRecyclerView(int pos,String str){
        mList.get(pos).setValue(str);
        editView.updateRecyclerView(pos);
    }

    public void setList(int pos,Uri uri,Context context){
        try {
            byte[] imgBytes=getBytes(context.getContentResolver().openInputStream(uri));
            mImgByte=imgBytes;
            mList.get(pos).setValue(imgBytes);
            editView.updateRecyclerView(pos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setList(int pos,String str){
        mList.get(pos).setValue(str);
        Log.e(String.format("%d is ---->",pos),str);
    }

    public byte[] getBytes(InputStream inputStream) throws  IOException{
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        int bufferSize=1024;
        byte[] buffer=new byte[bufferSize];
        int len;
        while((len=inputStream.read(buffer))!=-1){
            byteArrayOutputStream.write(buffer,0,len);
        }
        return byteArrayOutputStream.toByteArray();
    }


}
