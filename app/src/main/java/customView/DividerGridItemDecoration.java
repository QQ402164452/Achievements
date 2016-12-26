package customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/12/21.
 * RecyclerView 中GridLayoutManager布局的 分割线绘制
 */

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration{

    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerWidth;

    public DividerGridItemDecoration(Context context,int resId){
        mDivider=context.getResources().getDrawable(resId);
        mDividerHeight=mDivider.getIntrinsicHeight();
        mDividerWidth=mDivider.getIntrinsicWidth();
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state){
        drawHorizontal(canvas,parent);
        drawVertical(canvas,parent);
    }

    private int getSpanCount(RecyclerView parent){
        int spanCount=-1;//列数
        RecyclerView.LayoutManager layoutManager=parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            spanCount=((GridLayoutManager) layoutManager).getSpanCount();
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            spanCount=((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas canvas,RecyclerView parent){
        int childCount=parent.getChildCount();
        for(int i=0;i<childCount;i++){
            final View child=parent.getChildAt(i);
            final RecyclerView.LayoutParams layoutParams= (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left=child.getLeft()-layoutParams.leftMargin;
            final int right=child.getRight()+layoutParams.rightMargin+mDivider.getIntrinsicWidth();
            final int top=child.getBottom()+layoutParams.bottomMargin;
            final int bottom=top+mDividerHeight;
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(canvas);
        }
    }

    public void drawVertical(Canvas canvas,RecyclerView parent){
        int childCount=parent.getChildCount();
        for(int i=0;i<childCount;i++){
            View child=parent.getChildAt(i);
            final RecyclerView.LayoutParams layoutParams= (RecyclerView.LayoutParams) child.getLayoutParams();
            int top=child.getTop()-layoutParams.topMargin;
            int bottom=child.getBottom()+layoutParams.bottomMargin;
            int left=child.getRight()+layoutParams.rightMargin;
            int right=left+mDividerWidth;
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(canvas);
        }
    }

    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount){
        RecyclerView.LayoutManager layoutManager=parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            if((pos+1)%spanCount==0){
                return true;
            }
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            int  orientation=((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if(orientation== StaggeredGridLayoutManager.VERTICAL){
                if((pos+1)%spanCount==0){
                    return true;
                }
            }else{
                childCount = childCount % spanCount == 0 ? childCount - spanCount : childCount - childCount % spanCount;
                if(pos>=childCount){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount){
        RecyclerView.LayoutManager layoutManager=parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            childCount = childCount % spanCount == 0 ? childCount - spanCount : childCount - childCount % spanCount;
            if(pos>=childCount){
                return true;
            }
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            int orientation=((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if(orientation== StaggeredGridLayoutManager.VERTICAL){//纵向滚动
                childCount = childCount % spanCount == 0 ? childCount - spanCount : childCount - childCount % spanCount;
                if(pos>=childCount){
                    return true;
                }
            }else{//横向滚动
                if((pos+1)%spanCount==0){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        int spanCount=getSpanCount(parent);
        int childCount=parent.getAdapter().getItemCount();
        int position=parent.getChildAdapterPosition(view);
        if(isLastRaw(parent,position,spanCount,childCount)){
            outRect.set(0,0,mDividerWidth,0);
        }else if(isLastColumn(parent,position,spanCount,childCount)){
            outRect.set(0,0,0,mDividerHeight);
        }else{
            outRect.set(0,0,mDividerWidth,mDivider.getIntrinsicHeight());
        }
    }
}
