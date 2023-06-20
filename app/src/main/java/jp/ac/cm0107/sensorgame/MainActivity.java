package jp.ac.cm0107.sensorgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Spinner spnColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,LabyrinthActivity.class);

        ImageButton imgButton = findViewById(R.id.imgStart);
        spnColor = findViewById(R.id.spnColor);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = spnColor.getSelectedItemPosition();
                intent.putExtra("pos",pos);
                startActivity(intent);
            }
        });


    }
}