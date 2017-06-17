package com.example.jpet.Network;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Liran on 5/29/2017.
 */

public class Network {

    public abstract static class NetworkCallBack {
        public abstract Object doInBackground();
        public void onPostExecute(Object object) {

        }
    }

    public interface ImageCallBack {
        void onSuccess(Bitmap bitmap);
        void onFailed();
    }

    public static void runOnThreadWithProgressDialog(final Context context, final NetworkCallBack networkCallBack) {
        new AsyncTask<Object , Void, Object>() {
            ProgressDialog progressDialog;

            @Override
            protected Object doInBackground(Object... params) {
                return networkCallBack.doInBackground();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context, "", "Loading, please wait...", true);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);
                progressDialog.dismiss();
                networkCallBack.onPostExecute(object);
            }
        }.execute();
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
