package jp.ac.cm0107.sensorgame;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;

public class MapView extends View implements SensorEventListener {
    private static final float FILTER_FACTOR = 0.2f;

    private float mAccelX = 0.0f;
    private float mAccelY = 0.0f;

    private float mVectorX = 0.0f;
    private float mVectorY = 0.0f;

    private GameMap map = null;
    private Ball ball = null;
    // 画面サイズ
    private int mWidth;
    private int mHeight;

    public MapView(Context context){
        super(context);
        init();
    }
    private void init(){
        map = new GameMap();
        ball = new Ball();

        int[][] data = makeTestData();
        map.setData(data);
    }
    // テストデータ生成
    private int[][] makeTestData(){
        int[][] data = new int[32][20];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if( i == 0 || i == data.length - 1 ||
                        j == 0 || j == data[i].length - 1){
                    data[i][j] = 1; // 壁
                }else{
                    data[i][j] = 0; // 通路
                }
            }
        }
        return data;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        map.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        map.setSize(w, h);
        ball.setRadius(w/(2*GameMap.MAP_COLS));
        initGame();
    }
    public void initGame(){
        ball.setPosition(ball.getRadius()*6, ball.getRadius()*6);
        invalidate();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this){
            float[] values = sensorEvent.values.clone();

            mAccelX= (mAccelX*FILTER_FACTOR)+(values[0]*(1.0f-FILTER_FACTOR));
            mAccelY= (mAccelY*FILTER_FACTOR)+(values[1]*(1.0f-FILTER_FACTOR));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}