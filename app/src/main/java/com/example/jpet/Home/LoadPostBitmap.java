package com.example.jpet.Home;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.jpet.ApplicationContextProvider;
import com.example.jpet.Camera.PostClass;
import com.example.jpet.DB_Model.ModelSql;
import com.example.jpet.DB_Model.Parse_model;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Liran on 17-May-15.
 */
public class LoadPostBitmap {

    AtomicReference<PostClass> currPost;
    ImageView postImageView;
    Bitmap placeHolderPostImage;

    /* whichPostTask means which parse function needed to activate:
    1 - getting post picture and changing by reference
    2 - getting profile picture and changing by reference
    3 - getting profile picture for comments fragment and changing by reference
     */
    int whichPostTask;

    public LoadPostBitmap(AtomicReference<PostClass> currPost, ImageView postImageView, Bitmap placeHolderPostImage, int whichPostTask) {
        this.currPost = currPost;
        this.postImageView = postImageView;
        this.placeHolderPostImage = placeHolderPostImage;
        this.whichPostTask = whichPostTask;

        loadBitmap(currPost, postImageView, placeHolderPostImage, whichPostTask);
    }

    //pics functions
//***************************************************************************

    //loading bitmap function
    //mPlaceHolderBitmap displays bitmap until the tasks completes
    public void loadBitmap(AtomicReference<PostClass> currPost, ImageView postImageView,Bitmap placeHolderPostImage,int whichPostTask) {
        if (cancelPotentialWork(currPost.get().getObjectID(), postImageView)) {
            //post image task
            final BitmapWorkerTask postTask = new BitmapWorkerTask(postImageView, whichPostTask);
            final AsyncDrawable postAsyncDrawable = new AsyncDrawable(ApplicationContextProvider.getContext().getResources(), placeHolderPostImage, postTask);
            postImageView.setImageDrawable(postAsyncDrawable);
            postTask.execute(currPost);
        }
    }

    //loading image from parse
    class BitmapWorkerTask extends AsyncTask<AtomicReference<PostClass>, Void, Bitmap> {
        private final AtomicReference<ImageView> imageViewReference;
        private String postId = null;
        int whichPostTask;

        public BitmapWorkerTask(ImageView imageView, int whichPostTask) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new AtomicReference<ImageView>(imageView);
            this.whichPostTask = whichPostTask;
        }

        // Getting image in background.
        @Override
        protected Bitmap doInBackground(AtomicReference<PostClass>... params) {
            AtomicReference<PostClass> currObject = params[0];

            switch (whichPostTask){
                case 1:
                    //taking post image and changing by reference
                    Bitmap postPicture = ModelSql.getInstance().getPostPictureByID(currObject.get().getObjectID());
                    if(postPicture == null){
                        postPicture = Parse_model.getInstance().getPictureByPostAndChangeNOGOOD(currObject);
                        ModelSql.getInstance().updatePostPicture(postPicture, currObject.get().getObjectID());
                        Log.e("postImageDB","NULL");
                    }
                    currObject.get().set_postPicture(postPicture);
                    return postPicture;

                case 2:
                    //taking profile image and changing by reference

                    Bitmap profilePicture = ModelSql.getInstance().getProfilePictureByID(currObject.get().getObjectID());
                    if(profilePicture == null){
                        profilePicture = Parse_model.getInstance().getProfilePictureByPostAndChange(currObject);
                        ModelSql.getInstance().updateProfilePicture(profilePicture, currObject.get().getObjectID());
                        Log.e("profileImageDB", "NULL");
                    }
                    currObject.get().set_userProfilePicture(profilePicture);
                    return profilePicture;
                case 3:
                    //taking profile image for comments fragment and changing by reference
//                    Parse_model.getInstance().getCommentPicture(currObject);

            }
            return null;
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
