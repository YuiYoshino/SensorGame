package jp.ac.cm0107.sensorgame;


import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {
    //ボールの表示情報
    private int mX = 0;
    // X座標
    private int mY = 0;
    // Y座標
    private int mRadius = 0;
    //ボール描画用
    private Paint mBallPaint;
    public Ball(int color) {
        mBallPaint = new Paint();
        mBallPaint.setColor(color);
        mBallPaint.setAntiAlias (true);
    }
    public void setPosition(int x, int y) {
        mX = x;
        mY = y;
    }

    public int getX() {
        return mX;
    }
    public int getY() {
        return mY;
    }
    public void setRadius (int r) {
        mRadius = r;
    }
    public int getRadius() {
        return mRadius;
    }
    // 描画
    public void draw(Canvas canvas) {
        canvas.drawCircle (mX, mY, mRadius, mBallPaint);
    }

    // 移動
    public void move(int dx, int dy) {
        mX += dx;
        mY += dy;

    }
}
