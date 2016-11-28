package com.example.edgarhan.hw5_photodraw;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by eggertron on 11/28/16.
 */

public class TouchHandler implements View.OnTouchListener {
    MainActivity mainActivity;

    GestureDetectorCompat gestureDetectorCompat;

    public TouchHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        gestureDetectorCompat = new GestureDetectorCompat(this.mainActivity, new MyGestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int maskedAction = event.getActionMasked();
        gestureDetectorCompat.onTouchEvent(event);
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
                // make it upside down
                //mainActivity.upSideDown();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                for (int i = 0, size = event.getPointerCount(); i < size; i++) {
                    int id = event.getPointerId(i);
                    //mainActivity.addNewPath(id, event.getX(i), event.getY(i));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0, size = event.getPointerCount(); i < size; i++) {
                    int id = event.getPointerId(i);
                    //mainActivity.updatePath(id, event.getX(i), event.getY(i));
                }
                break;
            case MotionEvent.ACTION_UP:
                // make it rightside up
                //mainActivity.rightSideUp();
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                for (int i = 0, size = event.getPointerCount(); i < size; i++) {
                    int id = event.getPointerId(i);
                    //mainActivity.removePath(id);
                }
                break;
        }
        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            //mainActivity.onLongPress();
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //mainActivity.onDoubleTap();
            return super.onDoubleTap(e);
        }
    }
}