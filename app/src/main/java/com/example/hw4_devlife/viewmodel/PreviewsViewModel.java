package com.example.hw4_devlife.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.hw4_devlife.data.Post;
import com.example.hw4_devlife.loaders.listeners.OnLoadPostsListener;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

public class PreviewsViewModel extends ViewModel implements OnLoadPostsListener {
    @Getter @Setter
    private List<Post> postList;
    @Getter @Setter
    private boolean isLaunching = true;
    @Getter
    private AtomicInteger apiPageNumber = new AtomicInteger(0);
    @Getter @Setter
    private boolean isLoading = false;

    @Override
    public void onLoadPosts(List<Post> posts) {
        postList.addAll(posts);
        isLoading = false;
    }
}
