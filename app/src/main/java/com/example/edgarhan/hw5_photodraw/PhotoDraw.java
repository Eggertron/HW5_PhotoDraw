package com.example.edgarhan.hw5_photodraw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PhotoDraw extends AppCompatActivity {

    MyCanvas myCanvas;
    TouchHandler touchHandler;
    Bitmap bitmap;
    Canvas canvas;
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_draw);

        /*
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
        */

        takePicture();
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        touchHandler = new TouchHandler(this, canvas, drawPath, drawPaint);
    }

    private void takePicture() {
        Intent takePic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(takePic, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onDraw() {
        //draw view
        canvas.drawBitmap(bitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            canvas = new Canvas(bitmap);
            //bitmap = (Bitmap)extras.get("data");
            //BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            myCanvas.setBitmap(bitmap);
            //myCanvas.setBackground(bitmapDrawable);

        }
    }

    /**
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void onLongPress(float x, float y) {
        // add a sticker to the bitmap
    }

    /**
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void onDoubleTap(float x, float y) {
        // add a sticker to the bitmap
    }

    public void clickRed() {
        paintColor = 0xFF000000;
        drawPaint.setColor(paintColor);
        toastMe("Clicked Red");
    }

    public void clickBlue() {
        paintColor = 0x0000FF00;
        drawPaint.setColor(paintColor);
        toastMe("Clicked Blue");
    }

    public void clickGreen() {
        paintColor = 0x00FF0000;
        drawPaint.setColor(paintColor);
        toastMe("Clicked Green");
    }

    /**
     * save to file
     */
    public void clickDone() {
        toastMe("Clicked Done");
        FileOutputStream out = null;
        try {
            String filename = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").toString();
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void toastMe(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void clickUndo() {
        toastMe("Clicked Undo");
    }

    public void clickClear() {
        toastMe("Clicked Clear");
    }
}
