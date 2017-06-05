package com.example.jpet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Liran on 5/29/2017.
 */

public class Network {

    public interface ImageCallBack {
        void onSuccess(Bitmap bitmap);
        void onFailed();
    }

    public static void getImage(URL url, final ImageCallBack imageCallBack) {
        new AsyncTask<URL, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(URL... urls) {
                try {
                    return BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap == null) {
                    imageCallBack.onFailed();
                } else {
                    imageCallBack.onSuccess(bitmap);
                }
                super.onPostExecute(bitmap);
            }
        }.execute(url);
    }
}
