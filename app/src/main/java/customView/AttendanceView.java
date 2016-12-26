package customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.jason.achievements.R;

/**
 * Created by Jason on 2016/12/18.
 */

public class AttendanceView extends View {
    private String startTime = "09:00";
    private String endTime = "18:00";
    private int beginStateColor = 0xff99cc00;//0xff99cc00 ffff4444
    private int endStateColor = 0xff99cc00;
    private int defaultColor = 0xffFF7223;
    private String beginStateText = "正常";
    private String endStateText = "正常";
    private Paint paint;
    private int textSize = 30;
    private Rect bound;
    private int textHeight;
    private int textWidth;

    private int radius = 20;
    private int durationWidth = radius * 16;
    private int durationHeight = radius;

    private int textPadding = 10;
    private double leftPercentage = 0;
    private double rightPercentage = 0;

    public AttendanceView(Context context) {
        this(context, null);
    }

    public AttendanceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AttendanceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AttendanceView);//获取在Xml设置的属性里面的自定义样式属性
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.AttendanceView_beginTime:
                    startTime = array.getString(attr);
                    break;
                case R.styleable.AttendanceView_endTime:
                    endTime = array.getString(attr);
                    break;
                case R.styleable.AttendanceView_textSize:
                    textSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.AttendanceView_beginStateColor:
                    beginStateColor = array.getColor(attr, 0xffff4444);
                    break;
                case R.styleable.AttendanceView_endStateColor:
                    endStateColor = array.getColor(attr, 0xffff4444);
                    break;
            }
        }
        array.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        bound = new Rect();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        paint.setTextSize(textSize);
        paint.getTextBounds(startTime, 0, startTime.length(), bound);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        textHeight = bound.height();
        textWidth = bound.width();

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getPaddingLeft() + getPaddingRight() + textWidth * 2 + radius * 5 + durationWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) (getPaddingTop() + getPaddingBottom() + textHeight * 2 + textPadding + fontMetrics.descent);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setTextSize(textSize);
        paint.setColor(beginStateColor);
//        Paint.FontMetrics fontMetrics=paint.getFontMetrics();
        canvas.drawText(startTime, getPaddingLeft(), getPaddingTop() + textHeight, paint);
        canvas.drawText(beginStateText, getPaddingLeft(), getPaddingTop() + textHeight * 2 + textPadding, paint);

        paint.setColor(defaultColor);
        canvas.drawRect(getPaddingLeft() + textWidth + radius * 2, (getMeasuredHeight() - durationHeight) / 2, getPaddingLeft() + textWidth + radius * 2 + durationWidth, getMeasuredHeight() / 2 + durationHeight / 2, paint);
        //异常绘制
        paint.setColor(beginStateColor);
        canvas.drawRect(getPaddingLeft() + textWidth + radius * 2, (getMeasuredHeight() - durationHeight) / 2, (float) (getPaddingLeft() + textWidth + radius * 2 + durationWidth * leftPercentage), getMeasuredHeight() / 2 + durationHeight / 2, paint);
        paint.setColor(endStateColor);
        canvas.drawRect((float) (getPaddingLeft() + textWidth + radius * 2 + durationWidth * (1 - rightPercentage)), (getMeasuredHeight() - durationHeight) / 2, getPaddingLeft() + textWidth + radius * 2 + durationWidth, getMeasuredHeight() / 2 + durationHeight / 2, paint);

        paint.setColor(beginStateColor);
        canvas.drawCircle(getPaddingLeft() + textWidth + radius * 2, getMeasuredHeight() / 2, radius, paint);
        paint.setColor(endStateColor);
        canvas.drawCircle(getPaddingLeft() + textWidth + radius * 2 + durationWidth, getMeasuredHeight() / 2, radius, paint);

        paint.setTextSize(textSize);
        paint.setColor(endStateColor);
        canvas.drawText(endTime, getPaddingLeft() + textWidth + radius * 4 + durationWidth, getPaddingTop() + textHeight, paint);
        canvas.drawText(endStateText, getPaddingLeft() + textWidth + radius * 4 + durationWidth, getPaddingTop() + textHeight * 2 + textPadding, paint);

    }

    public int getTextSp(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, getResources().getDisplayMetrics());
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getBeginStateColor() {
        return beginStateColor;
    }

    public void setBeginStateColor(int beginStateColor) {
        this.beginStateColor = beginStateColor;
    }

    public int getEndStateColor() {
        return endStateColor;
    }

    public void setEndStateColor(int endStateColor) {
        this.endStateColor = endStateColor;
    }

    public String getBeginStateText() {
        return beginStateText;
    }

    public void setBeginStateText(String beginStateText) {
        this.beginStateText = beginStateText;
    }

    public String getEndStateText() {
        return endStateText;
    }

    public void setEndStateText(String endStateText) {
        this.endStateText = endStateText;
    }


    public int getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public double getRightPercentage() {
        return rightPercentage;
    }

    public void setRightPercentage(double rightPercentage) {
        this.rightPercentage = rightPercentage;
    }

    public double getLeftPercentage() {
        return leftPercentage;
    }

    public void setLeftPercentage(double leftPercentage) {
        this.leftPercentage = leftPercentage;
    }
}
