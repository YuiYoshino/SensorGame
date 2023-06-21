package jp.ac.cm0107.sensorgame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private int color = Color.RED;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TypefaceProvider.registerDefaultIconSets();

        Intent intent = new Intent(this,LabyrinthActivity.class);
        Intent intentC = new Intent(this,ColorPickerActivity.class);

        ImageButton imgButton = findViewById(R.id.imgStart);
        Button btnColor = findViewById(R.id.btnColorAct);


        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent.putExtra("color",color);
                startActivity(intent);
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intentC,REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case(REQUEST_CODE):
                if (resultCode == RESULT_OK){
                    color = data.getIntExtra("INPUT_STRING",color);
                } else if (resultCode == RESULT_CANCELED){
                    
                }else{

                }
                break;
            default:
                break;
        }
    }
}