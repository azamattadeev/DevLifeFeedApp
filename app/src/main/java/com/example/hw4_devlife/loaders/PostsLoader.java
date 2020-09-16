package com.example.hw4_devlife.loaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.hw4_devlife.PreviewsActivity;
import com.example.hw4_devlife.data.Post;
import com.example.hw4_devlife.loaders.listeners.OnLoadPostsListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostsLoader extends AsyncTask<Integer, Void, Void> {
    private OnLoadPostsListener listener;
    private WeakReference<PreviewsActivity> activityReference;
    private static final OkHttpClient client = new OkHttpClient();

    public static final int POSTS_NUMBER_AT_ONE_TIME = 5;

    public PostsLoader(PreviewsActivity activity, OnLoadPostsListener listener) {
        activityReference = new WeakReference<>(activity);
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        try {
            List<Post> posts = parsePostsJson(getRequestResult(getUrl(integers[0])));
            loadPreviewImagesForPosts(posts);
            listener.onLoadPosts(posts);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        PreviewsActivity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) return;
        activity.notifyRecycler();
    }

    private List<Post> parsePostsJson(String postsJson) throws JSONException{
        String resultJson = new JSONObject(postsJson).getJSONArray("result").toString();
        Post[] posts = new Gson().fromJson(resultJson, Post[].class);
        return Arrays.asList(posts);
    }

    private void loadPreviewImagesForPosts(List<Post> posts) throws IOException {
        for (Post post : posts) {
            InputStream is = new URL(post.getPreviewUrl())
                    .openConnection()
                    .getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(is));
            post.setPreviewBitmap(bitmap);
        }
    }

    private String getRequestResult(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private String getUrl(int page) {
        return String.format(
                Locale.getDefault(),
                "https://developerslife.ru/latest/%d?json=true",
                page);
    }

}
