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
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostsLoader extends AsyncTask<Integer, Void, Void> {
    private OnLoadPostsListener listener;
    private WeakReference<PreviewsActivity> activityReference;
    private static final OkHttpClient client = getUnsafeOkHttpClient();

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
                "http://developerslife.ru/latest/%d?json=true",
                page);
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
