package com.example.edgarhan.hw5_photodraw;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStoragePublicDirectory;

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
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_draw);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.msg);
        if (message.equals("START")) {
            captureFull();
        }
        myCanvas = (MyCanvas) findViewById(R.id.myCanvas);
        touchHandler = new TouchHandler(this);
        myCanvas.setOnTouchListener(touchHandler);
    }

    public void addNewPath(int id, float x, float y) {     myCanvas.addPath(id, x, y); }

    public void updatePath(int id, float x, float y) {     myCanvas.updatePath(id, x, y); }

    public void removePath(int id) {     myCanvas.removePath(); }

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

    private void captureFull() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
    }

    private Bitmap rotateBitmap(Bitmap b) {
        Matrix matrix = new Matrix();
        matrix.postRotate(-90); // anti-clockwise by 90 degrees

        // create a new bitmap from the original using the matrix to transform the result
        Bitmap rotatedBitmap = Bitmap.createBitmap(b , 0, 0, b .getWidth(), b .getHeight(), matrix, true);

        return rotatedBitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            //myCanvas.setBackground(new BitmapDrawable(getResources(), bitmap));
            //loadFromFile();
        }
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
            Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
            bitmap = rotateBitmap(bitmap);
            myCanvas.setBackground(new BitmapDrawable(getResources(), bitmap));
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

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

}
