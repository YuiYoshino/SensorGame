package jp.ac.cm0107.sensorgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

public class LabyrinthActivity extends AppCompatActivity {
    private MapView view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_labyrinth);

        getSupportActionBar().hide();
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = new MapView(this);
        setContentView(view);
    }
}