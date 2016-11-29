package com.example.edgarhan.hw5_photodraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


/**
 * Created by eggertron on 11/28/16.
 */

public class MyCanvas extends View {
    //HashMap<Integer, Path> activePaths;
    HashMap<Integer, Brush> activePaths;
    List<HashMap<Integer, Brush>> listPaths;
    //Paint redPaint, bluePaint, greenPaint;
    //Path path;
    Brush brush;
    int paintColor;
    boolean star;
    float lastX, lastY;

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        activePaths = new HashMap<>();
        listPaths = new LinkedList<>();
        paintColor = 0;
        brush = new Brush();
        star = false;
    }

    public void setColor(int i) {
        paintColor = i;
    }

    public void addPath(int id, float x, float y) {
        activePaths = new HashMap<>();
        brush = new Brush();
        brush.path.moveTo(x, y);
        if (paintColor == 0) brush.setRed();
        else if (paintColor == 1) brush.setBlue();
        else if (paintColor == 2) brush.setGreen();
        activePaths.put(id, brush);
        listPaths.add(activePaths);
        invalidate();
    }

    public void updatePath(int id, float x, float y) {
        Brush brush = activePaths.get(id);
        if (brush != null) {
            brush.path.lineTo(x, y);
        }
        invalidate();
    }

    public void removePath() {
        if (!listPaths.isEmpty()) {
            listPaths.remove(listPaths.size() - 1);
        }
        System.out.println("list size: " + listPaths.size());
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (HashMap<Integer, Brush> activePaths : listPaths) {
            for (Brush brush : activePaths.values()) {
                canvas.drawPath(brush.path, brush.pathPaint);
            }
        }
        addStar(canvas);
    }

    private void addStar(Canvas canvas) {
        if (star) {
            Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.star);
            canvas.drawBitmap(b, lastX, lastY, null);
            star = false;
        }
    }

    public void clearPath() {
        listPaths.clear();
        invalidate();
    }

    public class Brush {
        Paint redPaint, bluePaint, greenPaint, pathPaint;
        Path path;

        public Brush() {
            redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            redPaint.setColor(Color.RED);
            redPaint.setStyle(Paint.Style.STROKE);
            redPaint.setStrokeWidth(70);

            bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bluePaint.setColor(Color.BLUE);
            bluePaint.setStyle(Paint.Style.STROKE);
            bluePaint.setStrokeWidth(70);

            greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            greenPaint.setColor(Color.GREEN);
            greenPaint.setStyle(Paint.Style.STROKE);
            greenPaint.setStrokeWidth(70);
            path = new Path();
        }

        public void setRed() {
            pathPaint = redPaint;
        }

        public void setBlue() {
            pathPaint = bluePaint;
        }

        public void setGreen() {
            pathPaint = greenPaint;
        }
    }
}
