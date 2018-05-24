package com.example.saurav.stopwatch;


import android.content.res.Resources;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    int width;
    int height;
    Button startButton;
    Button resetButton;
    TextView viewSec, viewMin, viewMiliSec, minuteText, secondText, milisecondText, signature;
    View lineView;
    boolean running = false;
    Handler handler = new Handler();
    long startTime = 0L, timeInMili = 0L, updateTime = 0L, timeSwapBuffer = 0L;

    //converting pixels into device independent pixels
    public int dipConvertion(int value){
        Resources metrices = this.getResources();
        return (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, value, metrices.getDisplayMetrics());
    }
    //----------------------------->


    Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMili = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuffer + timeInMili;
            int secs = (int) updateTime / 1000;
            int min = secs / 60;
            secs %= 60;
            int milliseconds = (int) (updateTime % 1000);
            System.out.println(min);

            viewSec.setText(String.format("%02d", secs));
            viewMin.setText(String.format("%02d", min));
            viewMiliSec.setText(String.format("%03d", milliseconds));

            handler.postDelayed(this, 0);
        }
    };

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        RelativeLayout parent = new RelativeLayout(this);
        setContentView(parent);

        //get display details of current device
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        System.out.println("height:" + height);
        //----------------------------->

        setContentView(R.layout.activity_main);
        //time view code
        viewSec = (TextView) findViewById(R.id.viewSec);
        viewSec.bringToFront();

        viewMin = (TextView) findViewById(R.id.viewMin);
        viewMin.bringToFront();

        viewMiliSec = (TextView) findViewById(R.id.viewMiliSec);
        viewMiliSec.bringToFront();

        signature = (TextView) findViewById(R.id.signature);
        signature.bringToFront();

        minuteText = (TextView) findViewById(R.id.minuteText);
        minuteText.bringToFront();
        secondText = (TextView) findViewById(R.id.secondText);
        secondText.bringToFront();
        milisecondText = (TextView) findViewById(R.id.milisecondText);
        milisecondText.bringToFront();
        lineView = (View) findViewById(R.id.lineView);
        lineView.bringToFront();

        //----------------------------->

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                running = !running;
                if(running == true) {
                    startButton.setText("PAUSE");
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimerThread, 0);
                    resetButton.setVisibility(View.INVISIBLE);
                }
                else {
                    startButton.setText("START");
                    running = false;
                    timeSwapBuffer = timeSwapBuffer + timeInMili;
                    handler.removeCallbacks(updateTimerThread);
                    resetButton.setVisibility(View.VISIBLE);
                }
            }
        });

        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setVisibility(View.INVISIBLE);
        resetButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                running = false;
                handler.removeCallbacks(updateTimerThread);
                startTime = 0L;
                timeInMili = 0L;
                updateTime = 0L;
                timeSwapBuffer = 0L;
                viewSec.setText(String.format("%02d", startTime));
                viewMin.setText(String.format("%02d", startTime));
                viewMiliSec.setText(String.format("%03d", startTime));
                startButton.setText("START");
            }
        });

    }
}