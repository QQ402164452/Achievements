package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2015/10/15.
 */
public abstract class BaseIndicatorController {


    private View mTarget;

    protected List<Animator> mAnimators;


    public void setTarget(View target){
        this.mTarget=target;
    }

    public View getTarget(){
        return mTarget;
    }


    public int getWidth(){
        return mTarget.getWidth();
    }

    public int getHeight(){
        return mTarget.getHeight();
    }

    public void postInvalidate(){
        mTarget.invalidate();//直接刷新 11
    }

    /**
     * draw indicator
     * @param canvas
     * @param paint
     */
    public abstract void draw(Canvas canvas,Paint paint);

    /**
     * create animation or animations
     */
    public abstract void createAnimation();

    public void initAnimation(){
        createAnimation();
    }

    /**
     * make animation to start or end when target
     * view was be Visible or Gone or Invisible.
     * make animation to cancel when target view
     * be onDetachedFromWindow.
     * @param animStatus
     */
    public void setAnimationStatus(AnimStatus animStatus){
        if (mAnimators==null){
            return;
        }
        int count=mAnimators.size();
        switch (animStatus){
            case START:
                addAllListener();
                for (int i = 0; i < count; i++) {
                    Animator animator=mAnimators.get(i);
                    boolean isRunning=animator.isRunning();
                    if (!isRunning){
                        animator.start();
                    }
                }
                break;
            case END:
                removeAllListener();
                for (int i = 0; i < count; i++) {
                    Animator animator=mAnimators.get(i);
                    boolean isRunning=animator.isRunning();
                    if (isRunning){
                        animator.end();
                    }
                }
                break;
            case CANCEL:
                removeAllListener();
                for (int i = 0; i < count; i++) {
                    Animator animator=mAnimators.get(i);
                    boolean isRunning=animator.isRunning();
                    if (isRunning){
                        animator.cancel();
                    }
                }
                break;
        }
    }


    public enum AnimStatus{
        START,END,CANCEL
    }

    public abstract void addAllListener();
    public abstract void removeAllListener();




}
