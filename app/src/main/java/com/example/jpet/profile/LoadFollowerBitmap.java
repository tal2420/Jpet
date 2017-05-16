package com.example.jpet.profile;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.jpet.ApplicationContextProvider;
import com.example.jpet.DB_Model.Parse_model;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Liran on 17-May-15.
 */
public class LoadFollowerBitmap {

    AtomicReference<FollowersClass> currFollower;
    ImageView postImageView;
    Bitmap placeHolderPostImage;

    /* whichPostTask means which parse function needed to activate:
    1 - getting followers list
    2 - getting following list
     */

    public LoadFollowerBitmap(AtomicReference<FollowersClass> currFollower, ImageView postImageView, Bitmap placeHolderPostImage) {
        this.currFollower = currFollower;
        this.postImageView = postImageView;
        this.placeHolderPostImage = placeHolderPostImage;

        loadBitmap(currFollower, postImageView, placeHolderPostImage);
    }

    //pics functions
//***************************************************************************

    //loading bitmap function
    //mPlaceHolderBitmap displays bitmap until the tasks completes
    public void loadBitmap(AtomicReference<FollowersClass> currFollower, ImageView followerImage, Bitmap placeHolderPostImage) {
        if (cancelPotentialWork(currFollower.get().getUserNameThatFollowMe(), followerImage)) {
            //post image task
            final BitmapWorkerTask postTask = new BitmapWorkerTask(followerImage);
            final AsyncDrawable postAsyncDrawable = new AsyncDrawable(ApplicationContextProvider.getContext().getResources(), placeHolderPostImage, postTask);
            followerImage.setImageDrawable(postAsyncDrawable);
            postTask.execute(currFollower);
        }
    }

    //loading image from parse
    class BitmapWorkerTask extends AsyncTask<AtomicReference<FollowersClass>, Void, Bitmap> {
        private final AtomicReference<ImageView> imageViewReference;
        private String postId = null;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new AtomicReference<ImageView>(imageView);
        }

        // Getting image in background.
        @Override
        protected Bitmap doInBackground(AtomicReference<FollowersClass>... params) {
            AtomicReference<FollowersClass> currObject = params[0];

            //taking profile image and changing by reference

            Bitmap profilePicture = Parse_model.getInstance().getProfilePictureByUserName(currObject.get().getUserNameThatFollowMe());

            currObject.get().setUserThatFollowMePic(profilePicture);
            return profilePicture;

        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference.get() != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    // A helper method, getBitmapWorkerTask(), is used above to retrieve the task associated with a particular ImageView
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    // Creates a dedicated Drawable subclass to store a reference back to the worker task.
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    // The cancelPotentialWork method referenced in the code sample above checks if another running task is already associated with the ImageView.
    public static boolean cancelPotentialWork(String postId, ImageView imageView) {
        //checking ImageView task
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final String bitmapId = bitmapWorkerTask.postId;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapId == null || bitmapId != postId) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    //***************************************************************************

}
