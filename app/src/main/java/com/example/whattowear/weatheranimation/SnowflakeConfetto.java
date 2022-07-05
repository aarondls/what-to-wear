package com.example.whattowear.weatheranimation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.github.jinatonic.confetti.confetto.Confetto;

public class SnowflakeConfetto extends Confetto {
    public static final float SNOWFLAKE_RADIUS = 10;
    public static final float SLEET_RADIUS = 7;
    public static final int SNOWFLAKE_ALPHA = 40;
    public static final int SLEET_ALPHA = 80;
    public static final int SNOWFLAKE_COLOR = Color.WHITE;
    public static final int SLEET_COLOR = 0xFFb9bbbf;

    private float snowflakeRadius;
    private int snowflakeColor;
    private int snowflakeAlpha;

    public SnowflakeConfetto(float snowflakeRadius, int snowflakeColor, int snowflakeAlpha) {
        this.snowflakeRadius = snowflakeRadius;
        this.snowflakeColor = snowflakeColor;
        this.snowflakeAlpha = snowflakeAlpha;
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
     * Handles drawing a single snowflake onto the given canvas at the coordinates x and y using the given matrix and paint.
     * @param canvas the canvas where the snowflake is drawn
     * @param matrix matrix to help with drawing (can be used, for example, with rotation manipulation)
     * @param paint paint to help with drawing on the canvas
     * @param x the x coordinate of the center of the snowflake
     * @param y the y coordinate of the center of the snowflake
     * @param rotation the rotation angle in degrees of the current snowflake
     * @param percentAnimated the percentage of animation progress for this snowflake
     */
    @Override
    protected void drawInternal(Canvas canvas, Matrix matrix, Paint paint, float x, float y, float rotation, float percentAnimated) {
        // draw a single snowflake centered at x, y

        paint.setColor(snowflakeColor);
        paint.setAlpha(snowflakeAlpha);
        canvas.drawCircle(x, y, snowflakeRadius, paint);
    }
}
