package jp.ac.cm0107.sensorgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

public class LabyrinthActivity extends AppCompatActivity {
    private MapView view = null;
    private SensorManager manager;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_labyrinth);
        getSupportActionBar().hide();
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        color = intent.getIntExtra("color",Color.RED);

        view = new MapView(this,color);
        setContentView(view);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(view,sensor,SensorManager.SENSOR_DELAY_GAME);
        view.startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (view.getState() == MapView.GAME_OVER || view.getState() == MapView.GAME_VOID){
            view.freeHandler();
            finish();
        }
        return true;
    }
}