package com.example.whattowear;

import android.view.Window;
import android.view.WindowManager;

/**
 * Used to hide both the top and bottom bars
 */
public class FullscreenLayout {
    public static void hideTopBottomBars(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
