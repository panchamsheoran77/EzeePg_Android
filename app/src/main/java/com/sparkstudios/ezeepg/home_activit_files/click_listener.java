package com.sparkstudios.ezeepg.home_activit_files;

import android.view.View;
import android.widget.ImageView;

public interface click_listener  {
    void onItemClick(View view, int position);
    void onImageViewRemove(View view, int position);
    void onImageViewClick(View v, int position, ImageView shorter);
}
