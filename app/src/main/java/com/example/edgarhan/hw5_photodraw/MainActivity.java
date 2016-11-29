package com.example.edgarhan.hw5_photodraw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    Button startBtn;
    static String msg = EXTRA_MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = (Button)findViewById(R.id.start);
    }

    /**
     * when start button is clicked
     * @param view
     */
    public void clickStart(View view) {
        // open camera, get image and send to intent
        Intent intent = new Intent(this, PhotoDraw.class);
        String message = "START";
        intent.putExtra(msg, message);
        startActivity(intent);
    }
}
