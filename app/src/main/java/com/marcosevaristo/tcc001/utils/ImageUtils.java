package com.marcosevaristo.tcc001.utils;

import android.content.res.Resources;
import android.util.TypedValue;

public class ImageUtils {

  public static int dpToPx(float dp, Resources resources) {
    float px =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    return (int) px;
  }
}
