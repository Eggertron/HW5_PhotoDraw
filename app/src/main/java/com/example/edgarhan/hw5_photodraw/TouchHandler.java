package com.example.edgarhan.hw5_photodraw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by eggertron on 11/28/16.
 */

public class TouchHandler implements View.OnTouchListener {
    MainActivity mainActivity;
    PhotoDraw photoDraw;

    GestureDetectorCompat gestureDetectorCompat;

    //public TouchHandler(MainActivity mainActivity) {
    public TouchHandler(PhotoDraw pd) {
        //this.mainActivity = mainActivity;
        photoDraw = pd;
        //gestureDetectorCompat = new GestureDetectorCompat(this.mainActivity, new MyGestureListener());
        gestureDetectorCompat = new GestureDetectorCompat(this.photoDraw, new MyGestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        int maskedAction = event.getActionMasked();
        gestureDetectorCompat.onTouchEvent(event);
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
                //drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                for (int i = 0, size = event.getPointerCount(); i < size; i++) {
                    int id = event.getPointerId(i);
                    photoDraw.addNewPath(id, event.getX(i), event.getY(i));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //drawPath.lineTo(touchX, touchY);
                for (int i = 0, size = event.getPointerCount(); i < size; i++) {
                    int id = event.getPointerId(i);
                    photoDraw.updatePath(id, event.getX(i), event.getY(i));
                }
                break;
            case MotionEvent.ACTION_UP:
                //canvas.drawPath(drawPath, drawPaint);
                //drawPath.reset();
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                for (int i = 0, size = event.getPointerCount(); i < size; i++) {
                    int id = event.getPointerId(i);
                    photoDraw.removePath(id);
                }
                break;
        }
        photoDraw.onDraw();
        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            //mainActivity.onLongPress();
            photoDraw.onLongPress(e.getX(), e.getY());
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //mainActivity.onDoubleTap();
            photoDraw.onDoubleTap(e.getX(), e.getY());
            return super.onDoubleTap(e);
        }
    }
}