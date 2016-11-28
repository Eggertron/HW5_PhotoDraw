package com.example.edgarhan.hw5_photodraw;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PhotoDraw extends AppCompatActivity {

    MyCanvas myCanvas;
    TouchHandler touchHandler;
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
    }

    private void takePicture() {
        Intent takePic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(takePic, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap thumbnail = (Bitmap) extras.get("data");
            //bitmap = (Bitmap)extras.get("data");
            //BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            myCanvas.setBitmap(thumbnail);
            //myCanvas.setBackground(bitmapDrawable);

        }
    }
}
