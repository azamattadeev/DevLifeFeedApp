package com.example.hw4_devlife.viewmodel;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.ViewModel;

import com.example.hw4_devlife.loaders.listeners.OnLoadDrawableListener;

import lombok.Getter;
import lombok.Setter;

public class PostActivityViewModel extends ViewModel implements OnLoadDrawableListener {
    @Getter @Setter
    private Drawable gif;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private String gifUrl;
    @Getter @Setter
    private boolean isLaunching = true;

    @Override
    public void onLoadDrawable(Drawable drawable) {
        gif = drawable;
    }
}
