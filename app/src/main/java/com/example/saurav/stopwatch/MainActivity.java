package com.example.saurav.stopwatch;


import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button resetButton;
    private TextView viewSec;
    private TextView viewMin;
    private TextView viewMiliSec;

    private long startTime;
    private long timeInMili;
    private long timeSwapBuffer;
    private long updateTime = 0L;

    private boolean running = false;
    private Handler handler;
    Runnable updateTimerThread = new Runnable() {

        @Override
        public void run() {
            timeInMili = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuffer + timeInMili;
            int secs = (int) (updateTime / 1000);
            int min = secs / 60;
            secs %= 60;

            viewSec.setText(String.format("%02d", secs));
            viewMin.setText(String.format("%02d", min));
            viewMiliSec.setText(String.format("%03d", timeInMili));

            handler.postDelayed(this, 0);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        initializeViews();
    }

    public void initializeViews() {
        viewSec = findViewById(R.id.viewSec);
        viewMin = findViewById(R.id.viewMin);
        viewMiliSec = findViewById(R.id.viewMiliSec);

        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        resetButton.setVisibility(View.INVISIBLE);
    }

    public void onStartOnPause(View view) {
        running = !running;
        if (running) {
            startButton.setText(getString(R.string.reset));
            resetButton.setVisibility(View.INVISIBLE);
            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(updateTimerThread, 0);
        } else {
            startButton.setText(getString(R.string.resume));
            running = false;
            timeSwapBuffer = timeSwapBuffer + timeInMili;
            handler.removeCallbacks(updateTimerThread);
            resetButton.setVisibility(View.VISIBLE);
        }
    }

    public void onReset(View view) {
        running = false;
        handler.removeCallbacks(updateTimerThread);
        startTime = 0L;
        timeInMili = 0L;
        timeSwapBuffer = 0L;
        updateTime = 0L;
        viewSec.setText(String.format("%02d", startTime));
        viewMin.setText(String.format("%02d", startTime));
        viewMiliSec.setText(String.format("%03d", startTime));
        startButton.setText(getString(R.string.start));
    }
}