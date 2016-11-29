package adapter;

import android.content.Context;

import com.example.wheelview.adapters.AbstractWheelTextAdapter;

import bean.CityBean;

/**
 * Created by Jason on 2016/11/27.
 */

public class ProvinceAdapter extends AbstractWheelTextAdapter {
    private CityBean cityBean;

    public ProvinceAdapter(Context context,CityBean cityBean) {
        super(context);
        this.cityBean=cityBean;
    }

    @Override
    protected CharSequence getItemText(int index) {
        if(index>=0&&index<cityBean.getData().size()){
            return cityBean.getData().get(index).getP();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return cityBean.getData().size();
    }
}
