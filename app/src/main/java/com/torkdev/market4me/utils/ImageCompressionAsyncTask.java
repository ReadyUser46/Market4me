package com.torkdev.market4me.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public abstract class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {

    private WeakReference<Context> contextRef;

    public ImageCompressionAsyncTask(Context context) {
        contextRef = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        if (strings.length == 0 || strings[0] == null) return null;

        return CameraUtils.compressImage(contextRef.get(), strings[0]);
    }

    protected abstract void onPostExecute(String s);


}
