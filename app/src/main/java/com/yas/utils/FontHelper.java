package com.yas.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;


public class FontHelper {
    private static final Map<String, Typeface> TYPEFACES = new HashMap<>();

    public static Typeface get(Context context,String fontFileName){
        Typeface typeface = TYPEFACES.get(fontFileName);
        if(typeface == null){
            typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/"+fontFileName);
            TYPEFACES.put(fontFileName, typeface);
        }
        return typeface;
    }
}
