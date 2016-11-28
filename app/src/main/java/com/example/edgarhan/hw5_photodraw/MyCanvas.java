package com.example.edgarhan.hw5_photodraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;

import static android.R.attr.angle;
import static android.R.attr.left;
import static com.example.edgarhan.hw5_photodraw.R.id.top;

/**
 * Created by eggertron on 11/28/16.
 */

public class MyCanvas extends View {
    HashMap<Integer, Path> activePaths;
    Paint pathPaint;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    MyCanvas myCanvas;

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        activePaths = new HashMap<>();
        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(Color.BLACK);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(70);
        myCanvas = (MyCanvas)findViewById(R.id.myCanvas);

    }

    public void addPath(int id, float x, float y) {
        Path path = new Path();
        path.moveTo(x, y);
        activePaths.put(id, path);
        invalidate();
    }

    public void updatePath(int id, float x, float y) {
        Path path = activePaths.get(id);
        if (path != null) {
            path.lineTo(x, y);
        }
        invalidate();
    }

    public void removePath(int id) {
        if(activePaths.containsKey(id)){
            activePaths.remove(id);
        }
        invalidate();
    }

    public void upSideDown() {
        myCanvas.setRotationY(90);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Path path : activePaths.values()) {
            canvas.drawPath(path, pathPaint);
        }
        canvas.save(Canvas.MATRIX_SAVE_FLAG); //Saving the canvas and later restoring it so only this image will be rotated.
        canvas.rotate(-angle);
        //canvas.drawBitmap(bitmap, left, top, null);
        canvas.restore();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void rightSideUp(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG); //Saving the canvas and later restoring it so only this image will be rotated.
        canvas.rotate(-angle * 2);
        canvas.drawBitmap(bitmap, left, top, null);
        canvas.restore();

        invalidate();
    }
}
