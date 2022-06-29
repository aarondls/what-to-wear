package com.example.whattowear.weatheranimation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.github.jinatonic.confetti.confetto.Confetto;

public class RaindropConfetto extends Confetto {
    private static final float RAINDROP_LENGTH_FACTOR = 10;

    private float raindropThickness;
    private int raindropColor;


    public RaindropConfetto(float raindropThickness, int raindropColor) {
        this.raindropThickness = raindropThickness;
        this.raindropColor = raindropColor;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    protected void drawInternal(Canvas canvas, Matrix matrix, Paint paint, float x, float y, float rotation, float percentAnimated) {
        // Draw single raindrop centered at x, y

        // model as a line from (startX, startY) to (stopX, stopY), centered at (x, y)
        // use velocity to determine length of line
        float xVel = currentVelocityX;
        float yVel = currentVelocityY;

        float startX = x-xVel*RAINDROP_LENGTH_FACTOR;
        float stopX = x+xVel*RAINDROP_LENGTH_FACTOR;

        float startY = y-yVel*RAINDROP_LENGTH_FACTOR;
        float stopY = y+yVel*RAINDROP_LENGTH_FACTOR;

        paint.setColor(raindropColor);
        paint.setStrokeWidth(raindropThickness);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }
}
