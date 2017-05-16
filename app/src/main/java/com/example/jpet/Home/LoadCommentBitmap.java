package com.example.jpet.Home;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

import com.example.jpet.ApplicationContextProvider;
import com.example.jpet.DB_Model.Parse_model;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Liran on 19-May-15.
 */
public class LoadCommentBitmap {

    AtomicReference<CommentClass> currComment;
    ImageView commentImageView;
    Bitmap placeHolderCommentImage;

    /* whichCommentTask means which parse function needed to activate:
    1 - getting comment picture and changing by reference

     */
    int whichCommentTask;

    public LoadCommentBitmap(AtomicReference<CommentClass> currComment, ImageView commentImageView, Bitmap placeHolderCommentImage, int whichCommentTask) {
        this.currComment = currComment;
        this.commentImageView = commentImageView;
        this.placeHolderCommentImage = placeHolderCommentImage;
        this.whichCommentTask = whichCommentTask;

        loadBitmap(currComment, commentImageView, placeHolderCommentImage, whichCommentTask);
    }

    //pics functions
//***************************************************************************

    //loading bitmap function
    //mPlaceHolderBitmap displays bitmap until the tasks completes
    public void loadBitmap(AtomicReference<CommentClass> currComment, ImageView commentImageView,Bitmap placeHolderCommentImage,int whichCommentTask) {
        if (cancelPotentialWork(currComment.get().get_commentID(), commentImageView)) {
            //comment image task
            final BitmapWorkerTask commentTask = new BitmapWorkerTask(commentImageView, whichCommentTask);
            final AsyncDrawable commentAsyncDrawable = new AsyncDrawable(ApplicationContextProvider.getContext().getResources(), placeHolderCommentImage, commentTask);
            commentImageView.setImageDrawable(commentAsyncDrawable);
            commentTask.execute(currComment);
//            StartAsyncTask(commentTask);
        }
    }

    //loading image from parse
    class BitmapWorkerTask extends AsyncTask<AtomicReference<CommentClass>, Void, Bitmap> {
        private final AtomicReference<ImageView> imageViewReference;
        private String commentID = null;
        int whichCommentTask;

        public BitmapWorkerTask(ImageView imageView, int whichCommentTask) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new AtomicReference<ImageView>(imageView);
            this.whichCommentTask = whichCommentTask;
        }

        // Getting image in background.
        @Override
        protected Bitmap doInBackground(AtomicReference<CommentClass>... params) {
            AtomicReference<CommentClass> currObject = params[0];

            switch (whichCommentTask){
                case 1:
                    //taking comment image and changing by reference
                    return Parse_model.getInstance().getCommentPictureAndChange(currObject);

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
    public static boolean cancelPotentialWork(String commentID, ImageView imageView) {
        //checking ImageView task
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final String bitmapId = bitmapWorkerTask.commentID;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapId == null || bitmapId != commentID) {
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void StartAsyncTask(BitmapWorkerTask task, CommentClass currComment) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        else
            task.execute();
    }

    //***************************************************************************

}
