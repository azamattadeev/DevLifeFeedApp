package com.example.hw4_devlife.loaders;

import android.os.AsyncTask;

import com.example.hw4_devlife.PostActivity;
import com.example.hw4_devlife.loaders.listeners.OnLoadDrawableListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import okhttp3.OkHttpClient;
import pl.droidsonroids.gif.GifDrawable;

public class GifLoader extends AsyncTask<String, Void, Void> {
    private OnLoadDrawableListener listener;
    private WeakReference<PostActivity> activityRef;
    private static final OkHttpClient client = new OkHttpClient();

    public GifLoader(PostActivity postActivity, OnLoadDrawableListener listener) {
        this.activityRef = new WeakReference<>(postActivity);
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            InputStream is = new URL(strings[0]).openConnection().getInputStream();
            GifDrawable drawable = new GifDrawable(new BufferedInputStream(is));
            listener.onLoadDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        PostActivity activity = activityRef.get();
        if (activity == null || activity.isFinishing()) return;
        activity.postLoaded();
    }
}
