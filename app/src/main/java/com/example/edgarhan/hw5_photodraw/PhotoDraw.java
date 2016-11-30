package com.example.edgarhan.hw5_photodraw;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PhotoDraw extends AppCompatActivity {

    MyCanvas myCanvas;
    TouchHandler touchHandler;
    Bitmap bitmap;
    final String tmpImageStore = Environment.getExternalStorageDirectory()
            .toString() + "/imageStore.jpg";

    MediaPlayer mp;

    //initial color
    private int paintColor = 0xFF660000;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_draw);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.msg);
        if (message.equals("START")) {
            takePicture();
            //dispatchTakePictureIntent();
        }
        myCanvas = (MyCanvas) findViewById(R.id.myCanvas);
        touchHandler = new TouchHandler(this);
        myCanvas.setOnTouchListener(touchHandler);

        //loadFromFile();
    }

    public void addNewPath(int id, float x, float y) {     myCanvas.addPath(id, x, y); }

    public void updatePath(int id, float x, float y) {     myCanvas.updatePath(id, x, y); }

    public void removePath(int id) {     myCanvas.removePath(); }

    private void takePicture() {
        Intent takePic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //takePic.putExtra(MediaStore.EXTRA_OUTPUT, tmpImageStore); // save to file.
        if(takePic.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(takePic, REQUEST_IMAGE_CAPTURE);
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void loadFromFile() {
        // load from file.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(tmpImageStore, options);
        myCanvas.setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            myCanvas.setBackground(new BitmapDrawable(getResources(), bitmap));
            //loadFromFile();
        }
    }

    /**
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void onLongPress(float x, float y) {
        //toastMe("Long Press: " + x + ": " + y);
        // add a sticker to the bitmap
        myCanvas.addStar((int)x, (int)y);
    }

    /**
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void onDoubleTap(float x, float y) {
        //toastMe("Double Tap: " + x + ": " + y);
        // add a sticker to the bitmap
        myCanvas.addVT((int)x, (int)y);
    }

    public void clickRed(View v) {
        paintColor = Color.RED;
        myCanvas.setColor(0);
        toastMe("Clicked Red");
    }

    public void clickBlue(View v) {
        paintColor = Color.BLUE;
        myCanvas.setColor(1);
        toastMe("Clicked Blue");
    }

    public void clickGreen(View v) {
        paintColor = Color.GREEN;
        myCanvas.setColor(2);
        toastMe("Clicked Green");
    }

    /**
     * save to file
     */
    public void clickDone(View v) {
        toastMe("Saved to photo gallery!");

        FileOutputStream out = null;
        try {
            //String filename = new SimpleDateFormat("HHmmss").toString();
            String filename = Environment.getExternalStorageDirectory()
                    .toString() + "/DCIM/Camera/PicDraw" +
                    new SimpleDateFormat("HHmmss").toString() +
                    ".png";
            out = new FileOutputStream(filename);
            myCanvas.setDrawingCacheEnabled(true);
            myCanvas.buildDrawingCache();
            Bitmap bm = myCanvas.getDrawingCache();
            //bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            bm.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
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

        super.onBackPressed();
    }

    public void toastMe(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void clickUndo(View v) {
        toastMe("Clicked Undo");
        myCanvas.removePath();
    }

    public void clickClear(View v) {
        toastMe("Clicked Clear");
        myCanvas.clearPath();
    }

    public void startSound() {
        if (mp != null) mp.stop();
        mp = MediaPlayer.create(this, R.raw.pencil);
        mp.start();
    }

    public void stopSound() {
        if (mp != null) mp.stop();
        mp = null;
    }
}
