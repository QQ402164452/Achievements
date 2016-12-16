package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.jason.achievements.R;

import java.util.ArrayList;

import bean.WageBean;

/**
 * Created by Jason on 2016/12/8.
 */

public class WageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private AVObject mObject;
    private ArrayList<WageBean> mList;
    private double realWage=0;
    private double totalWage=0;

    public WageAdapter(Context context){
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mList=new ArrayList<>();
    }

    public void setDataSource(AVObject avObject){
        if(avObject!=null){
            this.mObject=avObject;
            mList.clear();

            double persion=avObject.getDouble("persion");//养老保险
            double medicare=avObject.getDouble("medicare");//医疗保险
            double unemployment=avObject.getDouble("unemployment");//失业保险
            double housing=avObject.getDouble("housing");//住房公积金
            double insurance=avObject.getDouble("insurance");//其他保险

            totalWage=avObject.getDouble("total");//应发工资
            double tax=avObject.getDouble("tax");//个人所得税
            double deduct=avObject.getDouble("deduct");//应扣工资

            double base=avObject.getDouble("base");//基本工资
            double subsidy=avObject.getDouble("subsidy");//补贴
            double performance=avObject.getDouble("performance");//绩效工资
            double bonus=avObject.getDouble("bonus");//月度奖金
            double overtime=avObject.getDouble("overtime");//加班费

            double socialTotal=persion+medicare+unemployment+housing+insurance;//社保合计
            realWage=totalWage-tax-socialTotal-deduct;//实发工资

            mList.add(new WageBean(0,"收入项目"));
            mList.add(new WageBean(1,"基本工资",String.valueOf(base)));
            mList.add(new WageBean(1,"补贴",String.valueOf(subsidy)));
            mList.add(new WageBean(1,"绩效工资",String.valueOf(performance)));
            mList.add(new WageBean(1,"月度奖金",String.valueOf(bonus)));
            mList.add(new WageBean(1,"加班费",String.valueOf(overtime)));
            mList.add(new WageBean(0,"支出项目"));
            mList.add(new WageBean(2,"养老保险",String.valueOf(persion)));
            mList.add(new WageBean(2,"医疗保险",String.valueOf(medicare)));
            mList.add(new WageBean(2,"失业保险",String.valueOf(unemployment)));
            mList.add(new WageBean(2,"住房公积金",String.valueOf(housing)));
            mList.add(new WageBean(2,"其他保险",String.valueOf(insurance)));
            mList.add(new WageBean(0,"其他项目"));
            mList.add(new WageBean(3,"个人所得税",String.valueOf(tax)));
            mList.add(new WageBean(3,"社保合计",String.valueOf(socialTotal)));
            mList.add(new WageBean(3,"应扣工资",String.valueOf(deduct)));
            mList.add(new WageBean(3,"应发工资",String.valueOf(totalWage)));
            mList.add(new WageBean(3,"实发工资",String.valueOf(realWage)));
        }
    }

    public double getRealWage(){
        return realWage;
    }

    public double getTotalWage(){
        return totalWage;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        switch (viewType){
            case 0:
                holder=new WageTitleHolder(mInflater.inflate(R.layout.wage_recycler_item_title,parent,false));
                break;
            case 1:
                holder=new WageNormalHolder(mInflater.inflate(R.layout.wage_recycler_item_normal,parent,false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WageBean wage=mList.get(position);
        switch (getItemViewType(position)){
            case 0:
                WageTitleHolder wageTitle= (WageTitleHolder) holder;
                wageTitle.title.setText(wage.getTitle());
                break;
            case 1:
                WageNormalHolder wageNor= (WageNormalHolder) holder;
                wageNor.title.setText(wage.getTitle());
                wageNor.content.setText(wage.getContent());
                switch (wage.getType()){
                    case 1:
                        wageNor.content.setTextColor(Color.RED);
                        break;
                    case 2:
                        wageNor.content.setTextColor(Color.GREEN);
                        break;
                    case 3:
                        wageNor.content.setTextColor(Color.BLACK);
                        break;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position){
        switch (mList.get(position).getType()){
            case 0:
                return 0;
            case 1:
            case 2:
            case 3:
                return 1;
        }
        return 0;
    }

    private class WageNormalHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;

        public WageNormalHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.WageActivity_item_normal_title);
            content= (TextView) itemView.findViewById(R.id.WageActivity_item_normal_content);
        }
    }

    private class WageTitleHolder extends RecyclerView.ViewHolder{
        TextView title;

        public WageTitleHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.WageActivity_item_title_text);
        }
    }

}
