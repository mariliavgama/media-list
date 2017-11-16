package com.example.mariliavgama.medialist.util;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Utility class for layout interactions
 */

public class LayoutUtils {

    /** Convert a value in dp (density independent pixels) to pixels
     *
     * @param valueInDP value in dp
     * @return value in pixels
     */
    public static int DPToPixels(float valueInDP) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDP, Resources.getSystem().getDisplayMetrics());
    }
}
