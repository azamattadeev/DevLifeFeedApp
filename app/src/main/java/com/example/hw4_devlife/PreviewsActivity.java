package com.example.hw4_devlife;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw4_devlife.data.Post;
import com.example.hw4_devlife.loaders.PostsLoader;
import com.example.hw4_devlife.previewsrecycler.PreviewsAdapter;
import com.example.hw4_devlife.viewmodel.PreviewsViewModel;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.hw4_devlife.PostActivity.GIF_URL_EXTRA_NAME;
import static com.example.hw4_devlife.PostActivity.POST_DESC_EXTRA_NAME;
import static com.example.hw4_devlife.loaders.PostsLoader.POSTS_NUMBER_AT_ONE_TIME;

public class PreviewsActivity extends AppCompatActivity implements PreviewsAdapter.OnItemClickListener {
    private PreviewsViewModel previewsViewModel;
    private RecyclerView previewRecycler;
    private PreviewsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previews);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        initViewModel();
        buildRecycler();

        if (previewsViewModel.isLaunching()) {
            previewsViewModel.setLaunching(false);
            loadPostsNTimes(3);
        }

        previewRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!previewsViewModel.isLoading()) {
                    previewsViewModel.setLoading(true);
                    loadPostsNTimes(1);
                }
            }
        });
    }

    private void initViewModel() {
        previewsViewModel = new ViewModelProvider(this).get(PreviewsViewModel.class);
        if (previewsViewModel.getPostList() == null) previewsViewModel.setPostList(new CopyOnWriteArrayList<Post>());
    }

    private void buildRecycler() {
        previewRecycler = findViewById(R.id.previews_recycler);
        previewRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PreviewsAdapter(previewsViewModel.getPostList(), this);
        previewRecycler.setAdapter(adapter);
    }

    private void loadPostsNTimes(int n) {
        for(int i = 0; i < n; i++) loadPosts();
    }

    private void loadPosts() {
        new PostsLoader(this, previewsViewModel).execute(previewsViewModel.getApiPageNumber().getAndIncrement());
    }

    public void notifyRecycler(){
        int postsCount = previewsViewModel.getPostList().size();
        adapter.notifyItemRangeInserted(postsCount - POSTS_NUMBER_AT_ONE_TIME, POSTS_NUMBER_AT_ONE_TIME);
    }

    @Override
    public void onItemClicked(int position) {
        Post post = previewsViewModel.getPostList().get(position);
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(GIF_URL_EXTRA_NAME, post.getGifUrl());
        intent.putExtra(POST_DESC_EXTRA_NAME, post.getDescription());
        startActivity(intent);
    }

}
