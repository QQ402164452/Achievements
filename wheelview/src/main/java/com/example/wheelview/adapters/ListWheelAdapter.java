package com.example.wheelview.adapters;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2016/11/26.
 */

public class ListWheelAdapter<T> extends AbstractWheelTextAdapter {
    private List<T> mList;

    public ListWheelAdapter(Context context, List<T> list) {
        super(context);
        this.mList=list;
    }


    @Override
    protected CharSequence getItemText(int index) {
        if(index>=0&&index<mList.size()){
            T item=mList.get(index);
            if(item instanceof CharSequence){
                return (CharSequence)item;
            }
            return item.toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return mList.size();
    }
}
