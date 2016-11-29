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
    PhotoDraw photoDraw;

    GestureDetectorCompat gestureDetectorCompat;

    public TouchHandler(PhotoDraw pd) {
        photoDraw = pd;
        gestureDetectorCompat = new GestureDetectorCompat(this.photoDraw, new MyGestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        int maskedAction = event.getActionMasked();
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                createPath(event);
                break;
            case MotionEvent.ACTION_MOVE:
                updatePath(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                //removePath(event);
        }
        //photoDraw.onDraw();
        return true;
    }

    private void createPath(MotionEvent event) {
        for (int i = 0, size = event.getPointerCount(); i < size; i++) {
            int id = event.getPointerId(i);
            photoDraw.addNewPath(id, event.getX(i), event.getY(i));
        }
    }

    private void updatePath(MotionEvent event) {
        for (int i = 0, size = event.getPointerCount(); i < size; i++) {
            int id = event.getPointerId(i);
            photoDraw.updatePath(id, event.getX(i), event.getY(i));
        }
    }

    private void removePath(MotionEvent event) {
        for (int i = 0, size = event.getPointerCount(); i < size; i++) {
            int id = event.getPointerId(i);
            photoDraw.removePath(id);
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            photoDraw.onLongPress(e.getX(), e.getY());
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            photoDraw.onDoubleTap(e.getX(), e.getY());
            return super.onDoubleTap(e);
        }
    }
}