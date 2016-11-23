package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import bean.CenterListBean;

/**
 * Created by Jason on 2016/11/23.
 */

public class CenterListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<CenterListBean> mList;

    public CenterListAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
        mList=new ArrayList<>();
        mList.add(new CenterListBean(2));
        mList.add(new CenterListBean(0,"纳兹",R.drawable.dayhr_userphoto_def,123456));
        mList.add(new CenterListBean(2));
        mList.add(new CenterListBean(1,"我的名片",R.drawable.list_icon_card_def));
        mList.add(new CenterListBean(1,"账号设置",R.drawable.me_icon_saoyisao_def));
        mList.add(new CenterListBean(2));
        mList.add(new CenterListBean(1,"意见反馈",R.drawable.list_icon_collect_def));
        mList.add(new CenterListBean(1,"设置",R.drawable.list_icon_set_def));
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 根据数据源的position返回需要显示的的layout的type
     * type的值必须从0开始
     * */
    @Override
    public int getItemViewType(int position){
        switch (mList.get(position).getType()){
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 2;
        }
    }

    /**
     * 返回所有的layout的数量
     *
     * */
    @Override
    public int getViewTypeCount(){
        return 3;
    }

    /**
     * 当前行是否可以点击
     */
    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        switch (mList.get(position).getType()){
            case 0:
            case 1:
                return true;
            case 2:
            default:
                return false;

        }
//        return super.isEnabled(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1=null;
        ViewHolder2 holder2=null;

        switch (getItemViewType(position)){
            case 0:
                if(convertView==null){
                    convertView=mInflater.inflate(R.layout.center_listview_item1,parent,false);
                    holder1=new ViewHolder1();
                    holder1.img= (ImageView) convertView.findViewById(R.id.centerFragment_user_img);
                    holder1.name= (TextView) convertView.findViewById(R.id.centerFragment_user_name);
                    holder1.id= (TextView) convertView.findViewById(R.id.centerFragment_user_id);
                    convertView.setTag(holder1);
                }else{
                    holder1= (ViewHolder1) convertView.getTag();
                }
                Glide.with(mContext).load(mList.get(position).getResId()).into(holder1.img);
                holder1.name.setText(mList.get(position).getName());
                holder1.id.setText(String.format("KK号:%d",mList.get(position).getId()));
                return convertView;
            case 1:
                if(convertView==null){
                    convertView=mInflater.inflate(R.layout.center_listview_item2,parent,false);
                    holder2=new ViewHolder2();
                    holder2.img= (ImageView) convertView.findViewById(R.id.centerFragment_item_img);
                    holder2.name= (TextView) convertView.findViewById(R.id.centerFragment_item_title);
                    convertView.setTag(holder2);
                }else{
                    holder2= (ViewHolder2) convertView.getTag();
                }
                Glide.with(mContext).load(mList.get(position).getResId()).into(holder2.img);
                holder2.name.setText(mList.get(position).getName());
                return convertView;
            case 2:
                if(convertView==null){
                    convertView=mInflater.inflate(R.layout.center_listview_item3,parent,false);
                }
                return convertView;
        }
        return convertView;
    }

    class ViewHolder1{
        ImageView img;
        TextView name;
        TextView id;
    }

    class ViewHolder2{
        ImageView img;
        TextView name;
    }
}
