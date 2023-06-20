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
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_labyrinth);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",0);
        int color = 0;
        switch (pos){
            case 0:
                color = Color.GREEN;
                break;
            case 1:
                color = Color.RED;
                break;
            case 2:
                color = Color.BLUE;
                break;
        }

        getSupportActionBar().hide();
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        if (view.getState() == MapView.GAME_OVER){
            view.freeHandler();
            finish();
        }
        return true;
    }
}