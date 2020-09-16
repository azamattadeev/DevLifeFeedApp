package com.example.hw4_devlife.previewsrecycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw4_devlife.R;
import com.example.hw4_devlife.data.Post;

import java.util.List;

public class PreviewsAdapter extends RecyclerView.Adapter<PreviewsAdapter.PreviewsViewHolder> {
    private List<Post> postsList;
    private OnItemClickListener onItemClickListener;

    public PreviewsAdapter(List<Post> postsList, OnItemClickListener onItemClickListener) {
        this.postsList = postsList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public PreviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View previewItemView = inflater.inflate(R.layout.preview_item, parent, false);
        return new PreviewsViewHolder(previewItemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewsViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.description.setText(post.getDescription());
        holder.previewImage.setImageBitmap(post.getPreviewBitmap());
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public static class PreviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView description;
        public ImageView previewImage;
        private OnItemClickListener onItemClickListener;

        public PreviewsViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            description = itemView.findViewById(R.id.preview_description);
            previewImage = itemView.findViewById(R.id.preview_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

}
