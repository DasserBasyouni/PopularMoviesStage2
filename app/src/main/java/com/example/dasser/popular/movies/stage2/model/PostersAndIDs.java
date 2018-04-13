package com.example.dasser.popular.movies.stage2.model;

import android.graphics.Bitmap;

public final class PostersAndIDs {
    private final int id;
    private final Bitmap bitmap;

    public PostersAndIDs(final int id, final Bitmap bitmap) {
        this.id = id;
        this.bitmap = bitmap;
    }

    public int getID() {
        return id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
