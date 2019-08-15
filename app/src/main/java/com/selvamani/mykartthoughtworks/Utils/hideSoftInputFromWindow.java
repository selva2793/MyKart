package com.selvamani.mykartthoughtworks.Utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ${Selva} on 13/08/19.
 */


public class hideSoftInputFromWindow {

    public void hideSoftInputmWindow(Context context, View v) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
