package jp.ac.cm0107.sensorgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    public static final int GAME_RUN = 1;
    public static final int GAME_OVER =2;
    private long mStartTime = 0;
    private long mTotalTime = 0;
    private int state = 0;
    Paint fullScr = new Paint();
    Paint message = new Paint();

    public MapView(Context context, int color){
        super(context);
        init(color);
    }
    private void init(int color){
        mHandler = new Handler();
        map = new GameMap();
        ball = new Ball(color);

        int[][] data = loadLabyrinth(1);
        map.setData(data);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        map.draw(canvas);
        ball.draw(canvas);
        if (state == GAME_OVER){
            fullScr.setColor(0xDD000000);
            canvas.drawRect(0f,0f,(float) mWidth,(float) mHeight,fullScr);

            message.setColor(Color.GREEN);
            message.setAntiAlias(true);
            message.setTextSize(40);
            message.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("やった！",mWidth/2,mHeight/2,message);
        }
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
        mTotalTime = 0;
        ball.setPosition(ball.getRadius()*6, ball.getRadius()*6);
        invalidate();
    }
    public void startGame(){
        mHandler.post(this);
        ball.setPosition(ball.getRadius()*6,ball.getRadius()*6);
        mStartTime = System.currentTimeMillis();
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
        if(map.getCellType(nextX,nextY)==GameMap.EXIT_TILE){
            stopGame();
        }
        ball.move((int)mVectorX, (int) mVectorY);
        invalidate();
        mHandler.removeCallbacks(this);
        mHandler.postDelayed(this,30);
    }
    public void stopGame(){
        state = GAME_OVER;
        freeHandler();
        mTotalTime = System.currentTimeMillis() - mStartTime;
    }
    public void freeHandler(){
        if(mHandler != null){
            mHandler.removeCallbacks(this);
            mHandler = null;
        }
    }
    public int getState(){
        return state;
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
    private int[][]loadLabyrinth(int level){
        final String fileName = "stage" + level +".csv";
        final int MAZE_ROWS = GameMap.MAP_ROWS;
        final int MAZE_COLS = GameMap.MAP_COLS;
        int [][]data = new int[MAZE_ROWS][MAZE_COLS];
        InputStream is = null;
        try {
            is = getContext().getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            int i = 0;
            while((line = reader.readLine())!=null &&i<MAZE_ROWS){
                String[] lineD = line.split(",");
                for (int j = 0; j < lineD.length;j++){
                    data[i][j] = Integer.parseInt(lineD[j].trim());
                }
                i++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (is != null){
                try {
                    is.close();
                }catch (IOException e){}
            }
        }
        return data;
    }
}
