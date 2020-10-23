package com.example.motionexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;

    public VibrationEffect v;
    public float vibrateThreshold = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(MainActivity.this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;


        }else {
            //TODO: Let Student know there is not accelerometer and that the app must close
            }

            v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


    }//end onCreate

        //This is how you release functionality
        //This is good form to always have a onPause and onResume
        protected void onPause(){
            super.onPause();
            sensorManager.unregisterListener(this);
        }


        protected void onResume(){
            super.onResume();
            sensorManager.registerListener(MainActivity.this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
            //display clean values
            displayCleanValues();
            //
            displayCurrentValues();
            //display the max values
            displayMaxValues();
            //get the change of the x,y,z values of the accelerometer
            deltaX = Math.abs(lastX - sensorEvent.values[0]);
            deltaY = Math.abs(lastY - sensorEvent.values[1]);
            deltaZ = Math.abs(lastZ - sensorEvent.values[2]);


            //add vibration
        if(deltaX < 2){
            deltaX = 0;
        }

        if(deltaY < 2){
            deltaY = 0;
        }

        if(deltaZ < 2){
            deltaZ = 0;
        }

        if(deltaX > vibrateThreshold || deltaY > vibrateThreshold || deltaZ > vibrateThreshold){
           v.createOneShot(50,50);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //initialize views
    public void initializeViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);

        maxX = (TextView) findViewById(R.id.maxX);
        maxY = (TextView) findViewById(R.id.maxY);
        maxZ = (TextView) findViewById(R.id.maxZ);

    }// end initializeViews

        public void displayCleanValues(){
            currentX.setText("0.0");
            currentY.setText("0.0");
            currentZ.setText("0.0");
        }

        public void displayCurrentValues(){
            currentX.setText(Float.toString(deltaX));
            currentY.setText(Float.toString(deltaY));
            currentZ.setText(Float.toString(deltaZ));
        }

        public void displayMaxValues(){
            if(deltaX > deltaXMax){
                deltaXMax = deltaX;
                maxX.setText(Float.toString(deltaXMax));
            }

            if(deltaY > deltaYMax){
                deltaYMax = deltaY;
                maxY.setText(Float.toString(deltaYMax));
            }

            if(deltaZ > deltaZMax){
                deltaZMax = deltaZ;
                maxZ.setText(Float.toString(deltaZMax));
            }
        }



}//end MainActivity
