package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        mList.add(new CenterListBean(1,"我的名片",R.drawable.list_icon_card_def));
        mList.add(new CenterListBean(1,"账号设置",R.drawable.me_icon_saoyisao_def));
        mList.add(new CenterListBean(0));
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
        return mList.get(position).getType();
    }

    /**
     * 返回所有的layout的数量
     *
     * */
    @Override
    public int getViewTypeCount(){
        return 2;
    }

    /**
     * 当前行是否可以点击
     */
    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        switch (mList.get(position).getType()){
            case 0:
                return false;
            case 1:
            default:
                return true;

        }
//        return super.isEnabled(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;

        switch (getItemViewType(position)){
            case 0:
                if(convertView==null){
                    convertView=mInflater.inflate(R.layout.center_listview_item_divider,parent,false);
                }
                return convertView;
            case 1:
                if(convertView==null){
                    convertView=mInflater.inflate(R.layout.center_listview_item,parent,false);
                    holder=new ViewHolder();
                    holder.img= (ImageView) convertView.findViewById(R.id.centerFragment_item_img);
                    holder.name= (TextView) convertView.findViewById(R.id.centerFragment_item_title);
                    convertView.setTag(holder);
                }else{
                    holder= (ViewHolder) convertView.getTag();
                }
                holder.img.setImageResource(mList.get(position).getResId());
                holder.name.setText(mList.get(position).getName());
                return convertView;
        }
        return convertView;
    }

    class ViewHolder{
        ImageView img;
        TextView name;
    }
}
