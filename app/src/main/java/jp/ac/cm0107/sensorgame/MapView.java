package jp.ac.cm0107.sensorgame;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.view.View;

public class MapView extends View implements SensorEventListener , Runnable{
    private static final float FILTER_FACTOR = 0.2f;
    private Handler mHandler = null;

    private static final int WALL = GameMap.WALL_TILE;
    private static final float REBOUND = 0.5f;
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
        mHandler = new Handler();
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
        data[3][8] = 2;
        return data;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        map.draw(canvas);
        ball.draw(canvas);
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
    public void startGame(){
        mHandler.post(this);
        ball.setPosition(ball.getRadius()*6,ball.getRadius()*6);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this){
            float[] values = sensorEvent.values.clone();

            mAccelX= (mAccelX*FILTER_FACTOR)+
                    (values[0]*(1.0f-FILTER_FACTOR));
            mAccelY= (mAccelY*FILTER_FACTOR)+
                    (values[1]*(1.0f-FILTER_FACTOR));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void run() {
        gameLoop();
    }

    private void gameLoop() {
        mVectorX = mVectorX -mAccelX;
        mVectorY = mVectorY +mAccelY;

        int nextX = (int) (ball.getX() + mVectorX);
        int nextY = (int) (ball.getY() + mVectorY);
        int radius = ball.getRadius();

        if ((nextX -radius)<0){
            mVectorX *= -0.5f;
        } else if ((nextX -radius)>mWidth) {
            mVectorX *= -0.5f;
        }
        if ((nextY -radius)<0){
            mVectorY *= -0.5f;
        } else if ((nextY -radius)>mHeight) {
            mVectorX *= -0.5f;
        }
        if (radius < nextX && nextX < mWidth - radius && radius < nextY && nextY < mHeight - radius) {
            // 壁の当たり判定
            int ul = map.getCellType(nextX - radius, nextY - radius);
            int ur = map.getCellType(nextX + radius, nextY - radius);
            int dl = map.getCellType(nextX - radius, nextY + radius);
            int dr = map.getCellType(nextX + radius, nextY + radius);
            if (ul != WALL && ur != WALL && dl != WALL && dr != WALL) {
            } else if (ul != WALL && ur == WALL && dl != WALL && dr == WALL) {
                mVectorX *= -REBOUND;
            } else if (ul == WALL && ur != WALL && dl == WALL && dr != WALL) {
                mVectorX *= -REBOUND;
            } else if (ul == WALL && ur == WALL && dl != WALL && dr != WALL) {
                mVectorY *= -REBOUND;
            } else if (ul != WALL && ur != WALL && dl == WALL && dr == WALL) {
                mVectorY *= -REBOUND;
            } else if (ul == WALL && ur != WALL && dl != WALL && dr != WALL) {
                if (mVectorX < 0.0f && mVectorY > 0.0f) {
                    mVectorX *= -REBOUND;
                } else if (mVectorX > 0.0f && mVectorY < 0.0f) {
                    mVectorY *= -REBOUND;
                } else {
                    mVectorX *= -REBOUND;
                    mVectorY *= -REBOUND;
                }
            } else if (ul != WALL && ur == WALL && dl != WALL && dr != WALL) {
                if (mVectorX > 0.0f && mVectorY > 0.0f) {
                    mVectorX *= -REBOUND;
                } else if (mVectorX < 0.0f && mVectorY < 0.0f) {
                    mVectorY *= -REBOUND;
                } else {
                    mVectorX *= -REBOUND;
                    mVectorY *= -REBOUND;
                }
            } else if (ul != WALL && ur != WALL && dl == WALL && dr != WALL) {
                if (mVectorX > 0.0f && mVectorY > 0.0f) {
                    mVectorY *= -REBOUND;
                } else if (mVectorX < 0.0f && mVectorY < 0.0f) {
                    mVectorX *= -REBOUND;
                } else {
                    mVectorX *= -REBOUND;
                    mVectorY *= -REBOUND;
                }
            } else if (ul != WALL && ur != WALL && dl != WALL && dr == WALL) {
                if (mVectorX < 0.0f && mVectorY > 0.0f) {
                    mVectorY *= -REBOUND;
                } else if (mVectorX > 0.0f && mVectorY < 0.0f) {
                    mVectorX *= -REBOUND;
                } else {
                    mVectorX *= -REBOUND;
                    mVectorY *= -REBOUND;
                }
            } else {
                mVectorX *= -REBOUND;
                mVectorY *= -REBOUND;
            }
        }
        ball.move((int)mVectorX, (int) mVectorY);
        invalidate();
        mHandler.removeCallbacks(this);
        mHandler.postDelayed(this,30);
    }
}