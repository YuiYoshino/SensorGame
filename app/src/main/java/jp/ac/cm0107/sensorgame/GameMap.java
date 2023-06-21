package jp.ac.cm0107.sensorgame;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameMap {
    public final static int PATH_TILE = 0;
    public final static int WALL_TILE = 1;
    public final static int EXIT_TILE = 2;
    public final static int VOID_TILE = 3;
    public final static int IN_first_TILE = 4;
    public final static int OUT_first_TILE = 5;
    public final static int IN_second_TILE = 6;
    public final static int OUT_second_TILE = 7;


    public final static int MAP_ROWS = 32;
    public final static int MAP_COLS = 20;
    private int [][] mData;
    private int mTileWidth;

    public int getmTileWidth() {
        return mTileWidth;
    }

    public int getmTileHeight() {
        return mTileHeight;
    }

    private int mTileHeight;
    private Paint mPathPaint = new Paint();
    private Paint mWallPaint = new Paint();
    private Paint mExitPaint = new Paint();
    private Paint mVoidPaint = new Paint();


    private Paint mFirstINPaint = new Paint();
    private Paint mFirstOUTPaint = new Paint();
    private Paint mSecondINPaint = new Paint();
    private Paint mSecondOUTPaint = new Paint();


    public GameMap() {
        mData = new int[MAP_ROWS][MAP_COLS];
        mPathPaint.setColor(Color.BLACK);
        mWallPaint.setColor(Color.WHITE);
        mExitPaint.setColor(Color.CYAN);
        mVoidPaint.setColor(Color.YELLOW);
        mFirstINPaint.setColor(Color.GRAY);
        mFirstOUTPaint.setColor(Color.GRAY);
        mSecondINPaint.setColor(Color.MAGENTA);
        mSecondOUTPaint.setColor(Color.MAGENTA);
    }
    public void setData(int [][] data){
        mData = data;
    }
    public void setSize(int w, int h){
        mTileWidth = w / MAP_COLS;
        mTileHeight = h / MAP_ROWS;
    }
    public void draw(Canvas canvas){
        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) {
                int x = j * mTileWidth; //表示位置(x 座標)
                int y = i * mTileHeight; //表示位置(y 座標)
                int width = (j + 1) * mTileWidth; //表示幅
                int height = (i + 1) * mTileHeight; //表示高さ

                switch (mData[i][j]){
                    case PATH_TILE:
                        canvas.drawRect(x, y, width, height, mPathPaint);
                        break;
                    case WALL_TILE:
                        canvas.drawRect(x, y, width, height, mWallPaint);
                        break;
                    case EXIT_TILE:
                        canvas.drawRect(x, y, width, height, mExitPaint);
                        break;
                    case VOID_TILE:
                        canvas.drawRect(x, y, width, height, mVoidPaint);
                        break;
                    case IN_first_TILE:
                        canvas.drawRect(x, y, width, height, mFirstINPaint);
                        break;
                    case OUT_first_TILE:
                        canvas.drawRect(x, y, width, height, mFirstOUTPaint);
                        break;
                    case IN_second_TILE:
                        canvas.drawRect(x, y, width, height, mSecondINPaint);
                        break;
                    case OUT_second_TILE:
                        canvas.drawRect(x, y, width, height, mSecondOUTPaint);
                        break;
                }
            }
        }
    }
    public int getCellType(int x, int y){
        if (mTileHeight == 0||mTileWidth ==0)
            return PATH_TILE;
        int j = x / mTileWidth;
        int i = y / mTileHeight;
        if(i < MAP_ROWS && j < MAP_COLS){
            return mData[i][j];
        }
        return PATH_TILE;
    }
}

