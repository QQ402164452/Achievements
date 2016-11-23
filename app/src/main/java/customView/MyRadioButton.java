package customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/11/23.
 */

public class MyRadioButton extends RadioButton {
    private int mDrawableSize;

    public MyRadioButton(Context context) {
        this(context,null);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Drawable drawableLeft=null;
        Drawable drawableRight=null;
        Drawable drawableBottom=null;
        Drawable drawableTop=null;

        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.MyRadioButton);
        int n=a.getIndexCount();
        for(int i=0;i<n;i++){
            int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.MyRadioButton_drawableSizes:
                    mDrawableSize=a.getDimensionPixelSize(R.styleable.MyRadioButton_drawableSizes,50);
                    break;
                case R.styleable.MyRadioButton_drawableTop:
                    drawableTop=a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableBottom:
                    drawableBottom=a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableRight:
                    drawableRight=a.getDrawable(attr);
                    break;
                case R.styleable.MyRadioButton_drawableLeft:
                    drawableLeft=a.getDrawable(attr);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        setCompoundDrawablesWithIntrinsicBounds(drawableTop,drawableBottom,drawableLeft,drawableRight);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable top,Drawable bottom,Drawable left,Drawable right){
        if(left!=null){
            left.setBounds(0,0,mDrawableSize,mDrawableSize);
        }
        if(right!=null){
            right.setBounds(0,0,mDrawableSize,mDrawableSize);
        }
        if(top!=null){
            top.setBounds(0,0,mDrawableSize,mDrawableSize);
        }
        if(bottom!=null){
            bottom.setBounds(0,0,mDrawableSize,mDrawableSize);
        }
        setCompoundDrawables(left,top,right,bottom);
    }
}
