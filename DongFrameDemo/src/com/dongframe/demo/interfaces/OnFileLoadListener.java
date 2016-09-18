package com.dongframe.demo.interfaces;

import android.widget.ImageView;

public interface OnFileLoadListener
{
    public void onFileLoad(int t, Object o, ImageView imageView, String imageUrl);

    public void onError(int t, ImageView imageView, String imageUrl);
    
}


