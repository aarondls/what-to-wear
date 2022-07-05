package com.example.whattowear.weatheranimation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.github.jinatonic.confetti.confetto.Confetto;

public class RaindropConfetto extends Confetto {
    private static final float RAINDROP_LENGTH_FACTOR = 10;

    public static final float DRIZZLE_RAINDROP_THICKNESS = 0.3f;
    public static final float RAIN_RAINDROP_THICKNESS = 0.6f;
    public static final int RAINDROP_COLOR = Color.WHITE;

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

    /**
     * Handles drawing a single raindrop onto the given canvas at the coordinates x and y using the given matrix and paint.
     * @param canvas the canvas where the raindrop is drawn
     * @param matrix matrix to help with drawing (can be used, for example, with rotation manipulation)
     * @param paint paint to help with drawing on the canvas
     * @param x the x coordinate of the center of the raindrop
     * @param y the y coordinate of the center of the raindrop
     * @param rotation the rotation angle in degrees of the current raindrop
     * @param percentAnimated the percentage of animation progress for this raindrop
     */
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
