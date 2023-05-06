package com.example.jirin.widget.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * @ClassName FloatViewTouchListener
 * @Author pengfan
 * @Date 2023/5/4
 * @Description 用来处理view一些自定义的监听事件
 */
public class FloatViewTouchListener implements View.OnTouchListener {
    /**
     * 默认判定View被拖拽移动的距离为900px(10dp)， 当view移动距离超过这个值的时候，判定view正在被拖拽
     */
    private static final int DEFAULT_START_DRAG_DISTANCE = 900;

    /**
     * 拖拽判定距离
     */
    private int startDragDistance;

    private boolean isDrag = false;

    /**
     * view的Layout相关的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * view未移动之前的x坐标
     */
    private int originX;

    /**
     * view未移动之前的y坐标
     */
    private int originY;

    /**
     * 触摸屏幕并按住屏幕开始move时的x坐标
     */
    private float startTouchX;

    /**
     * 触摸屏幕并按住屏幕开始move时的y坐标
     */
    private float startTouchY;

    private OnFloatViewOnDragListener mOnFloatViewOnDragListener;

    private OnFloatViewOnClickListener mOnFloatViewOnClickListener;

    public FloatViewTouchListener(WindowManager.LayoutParams params) {
        this(params, DEFAULT_START_DRAG_DISTANCE);
    }

    public FloatViewTouchListener(WindowManager.LayoutParams params, int startDragDistance) {
        this(params, startDragDistance, null, null);
    }

    public FloatViewTouchListener(WindowManager.LayoutParams params, int startDragDistance,
                                  OnFloatViewOnClickListener onFloatViewOnClickListener,
                                  OnFloatViewOnDragListener onFloatViewOnDragListener) {
        this.mParams = params;
        this.startDragDistance = startDragDistance;
        this.mOnFloatViewOnClickListener = onFloatViewOnClickListener;
        this.mOnFloatViewOnDragListener = onFloatViewOnDragListener;
    }

    public void setOnFloatViewOnClickListener(OnFloatViewOnClickListener floatViewOnClickListener) {
        this.mOnFloatViewOnClickListener = floatViewOnClickListener;
    }

    public void setOnFloatViewOnDragListener(OnFloatViewOnDragListener floatViewOnDragListener) {
        this.mOnFloatViewOnDragListener = floatViewOnDragListener;
    }


    /**
     * 采用勾股定理的方式判断view的移动距离是否超过了900px， 如果超过900px，则认为当前的view正在被拖拽移动
     * @param event 移动过程中当前手指触摸屏幕的位置
     * @return 如果view正在被拖拽返回true， 如果没有被拖拽，返回false
     */
    private boolean isDragging(MotionEvent event) {
        double xDistance = Math.pow(event.getRawX() - startTouchX, 2);
        double yDistance = Math.pow(event.getRawY() - startTouchY, 2);
        return xDistance + yDistance > startDragDistance;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            isDrag = false;
            originX = mParams.x;
            originY = mParams.y;
            startTouchX = motionEvent.getRawX();
            startTouchY = motionEvent.getRawY();
            return true;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            if (!isDrag && isDragging(motionEvent)) {
                isDrag = true;
            }
            if (!isDrag) {
                return true;
            }
            mParams.x = (int) (originX + (motionEvent.getRawX() - startTouchX));
            mParams.y = (int) (originY + (motionEvent.getRawY() - startTouchY));
            if (mOnFloatViewOnDragListener != null) {
                mOnFloatViewOnDragListener.onDrag(view, mParams, motionEvent);
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (!isDrag) {
                if (mOnFloatViewOnClickListener != null) {
                    mOnFloatViewOnClickListener.onClick(view);
                }
            }
        }
        return false;
    }

    public interface OnFloatViewOnDragListener {
        /**
         * 拖拽监听回调
         * @param view 被监听的view
         * @param params view的layout params
         * @param event 此时屏幕上的触摸事件
         */
        void onDrag(View view, WindowManager.LayoutParams params, MotionEvent event);
    }

    public interface OnFloatViewOnClickListener {
        /**
         * 点击监听回调
         * @param view 被监听的view
         */
        void onClick(View view);
    }
}
