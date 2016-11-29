package com.example.edgarhan.hw5_photodraw;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class PhotoDraw extends AppCompatActivity {

    MyCanvas myCanvas;
    TouchHandler touchHandler;
    Bitmap bitmap;
    Canvas canvas;

    //initial color
    private int paintColor = 0xFF660000;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_draw);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.msg);
        if (message.equals("START")) {
            takePicture();
        }

        touchHandler = new TouchHandler(this);
        myCanvas = (MyCanvas) findViewById(R.id.myCanvas);
        myCanvas.setOnTouchListener(touchHandler);
    }

    public void addNewPath(int id, float x, float y) {     myCanvas.addPath(id, x, y); }

    public void updatePath(int id, float x, float y) {     myCanvas.updatePath(id, x, y); }

    public void removePath(int id) {     myCanvas.removePath(id); }

    private void takePicture() {
        Intent takePic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(takePic, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onDraw() {
        //draw view
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            //canvas = new Canvas(bitmap);
            //bitmap = (Bitmap)extras.get("data");
            //BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            //myCanvas.setBitmap(bitmap);
            myCanvas.setBackground(new BitmapDrawable(getResources(), bitmap));
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
        Random rd = new Random();
        myCanvas.setBackgroundColor(Color.rgb(rd.nextInt(255), rd.nextInt(255), rd.nextInt(255)));
    }

    public void clickRed(View v) {
        paintColor = 0xFF000000;
        myCanvas.setColor(paintColor);
        toastMe("Clicked Red");
    }

    public void clickBlue(View v) {
        paintColor = 0x0000FF00;
        myCanvas.setColor(paintColor);
        toastMe("Clicked Blue");
    }

    public void clickGreen(View v) {
        paintColor = 0x00FF0000;
        myCanvas.setColor(paintColor);
        toastMe("Clicked Green");
    }

    /**
     * save to file
     */
    public void clickDone(View v) {
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

    public void clickUndo(View v) {
        toastMe("Clicked Undo");
    }

    public void clickClear(View v) {
        toastMe("Clicked Clear");
    }
}
