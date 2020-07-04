package com.example.market4me.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraUtils {


    public static File onCreateFile(Context context, String subDir) {

        // file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.US).format(new Date());
        String pictureName = "IMG_" + timeStamp + ".jpg";

        // file path
        File picturePath;
        if (subDir != null) {
            picturePath = new File(context.getFilesDir(), subDir);
            if (!picturePath.exists()) picturePath.mkdir();
        } else {
            picturePath = context.getFilesDir();
        }

        // file
        return new File(picturePath, pictureName);

    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {

        // Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;


        // Figure out how much to scale down by
        int scaleFactor = Math.min(srcHeight / destHeight, srcWidth / destWidth);


        // Decode the image file into a Bitmap sized to fill the View
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;

        // Create final bitmap
        return BitmapFactory.decodeFile(path, options);
    }


}
