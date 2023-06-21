package jp.ac.cm0107.sensorgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

    public class ColorPickerActivity extends AppCompatActivity implements ColorPicker.OnColorChangedListener {

        private ColorPicker picker;
        private SVBar svBar;
        private OpacityBar opacityBar;
        private Button button;
        //private TextView text;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_color_picker);
            getSupportActionBar().hide();
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Intent intent = new Intent();


            picker = (ColorPicker) findViewById(R.id.picker);
            svBar = (SVBar) findViewById(R.id.svbar);
            opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
            button = (Button) findViewById(R.id.button1);
            //text = (TextView) findViewById(R.id.textView1);

            picker.addSVBar(svBar);
            picker.addOpacityBar(opacityBar);
            picker.setOnColorChangedListener(this);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //text.setTextColor(picker.getColor());
                    picker.setOldCenterColor(picker.getColor());
                    intent.putExtra("INPUT_STRING",picker.getColor());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        }

        @Override
        public void onColorChanged(int color) {
            //gives the color when it's changed.
        }
    }