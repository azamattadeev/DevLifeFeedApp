package com.example.hw4_devlife.data;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Post {
    @SerializedName("id")
    private int id;
    @SerializedName("description")
    private String description;
    @SerializedName("previewURL")
    private String previewUrl;
    @SerializedName("gifURL")
    private String gifUrl;
    @SerializedName("gifSize")
    private int gifSize;
    private Bitmap previewBitmap;
}
