package com.example.hw4_devlife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.hw4_devlife.loaders.GifLoader;
import com.example.hw4_devlife.viewmodel.PostActivityViewModel;

import pl.droidsonroids.gif.GifImageView;

public class PostActivity extends AppCompatActivity {
    private GifImageView gifImageView;
    private ProgressBar progressBar;
    private TextView description;
    private PostActivityViewModel viewModel;

    public static final String GIF_URL_EXTRA_NAME = "gifUrl";
    public static final String POST_DESC_EXTRA_NAME = "postDescription";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        gifImageView = findViewById(R.id.gif_image_view);
        progressBar = findViewById(R.id.gif_progress);
        description = findViewById(R.id.gif_description);

        viewModel = new ViewModelProvider(this).get(PostActivityViewModel.class);

        if (viewModel.isLaunching()) {
            viewModel.setLaunching(false);
            Intent myIntent = getIntent();
            viewModel.setGifUrl(myIntent.getStringExtra(GIF_URL_EXTRA_NAME));
            viewModel.setDescription(myIntent.getStringExtra(POST_DESC_EXTRA_NAME));
            loadGif(viewModel.getGifUrl());
        } else if (viewModel.getGif() != null) {
            setPost();
            showGifImage();
        } else if (viewModel.isLoading()) {
            loadGif(viewModel.getGifUrl());
        }

    }

    private void loadGif(String url) {
        new GifLoader(this, viewModel).execute(url);
        viewModel.setLoading(true);
    }

    private void showProgressBar() {
        gifImageView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void postLoaded() {
        setPost();
        showGifImage();
    }

    private void showGifImage() {
        progressBar.setVisibility(View.GONE);
        gifImageView.setVisibility(View.VISIBLE);
    }

    private void setPost() {
        gifImageView.setImageDrawable(viewModel.getGif());
        description.setText(viewModel.getDescription());
    }

}
