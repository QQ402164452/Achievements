package customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Jason on 2016/12/7.
 */

public class ImageTextView extends TextView {

    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas){
        Drawable[] drawables=getCompoundDrawables();
        if(drawables!=null){
            Drawable drawableLeft=drawables[0];
            if(drawableLeft!=null){
                float textWidth=getPaint().measureText(getText().toString());
                int drawablePadding=getCompoundDrawablePadding();
                int drawableWidth=0;
                drawableWidth=drawableLeft.getIntrinsicWidth();
                float bodyWidth=textWidth+drawableWidth+drawablePadding;
                canvas.translate((getWidth()-bodyWidth)/2-getPaddingLeft(),0);
            }
        }
        super.onDraw(canvas);
    }
}
