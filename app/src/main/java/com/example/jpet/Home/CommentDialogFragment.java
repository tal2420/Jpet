package com.example.jpet.Home;


import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jpet.CurrentDateTime;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.LikeAndFollowing.NotificationsOfPost;
import com.example.jpet.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

//import com.parse.Parse;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentDialogFragment extends DialogFragment {

    public static CommentDialogFragment newInstance(View homeView ,String _postID) {
        CommentDialogFragment commentDialogFragment = new CommentDialogFragment();
        commentDialogFragment.set_postID(_postID);
        commentDialogFragment.setHomePageView(homeView);

        return commentDialogFragment;
    }
    View homePageView;
    Bitmap noProfilePictureBitmap;
    String _postID;

    public void set_postID(String _postID) {
        this._postID = _postID;
    }

    // public CommentDialogFragment(){}


    ArrayList<CommentClass> commentsArray = new ArrayList<>();
    ListAdapter listAdapter;
    View root;
    EditText comment_editText;
    Button comment_sendButton;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_comment_dialog, container, false);
        //set the title of the dialog
        getDialog().setTitle("Comments");

        noProfilePictureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_profile_picture);

        comment_sendButton = (Button) root.findViewById(R.id.comment_send_button);


        comment_editText = (EditText) root.findViewById(R.id.coment_editText);
        //comment_sendButton = (Button) root.findViewById(R.id.comment_send_button);

        //comment_sendButton.setClickable(true);

        new getAllComments(root).execute();


        comment_sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("comment button:", "has pressed");

                String commentContent = comment_editText.getText().toString();

                if (commentContent.equals("")) {

                } else {
                    CommentClass newComment = new CommentClass(
                            Parse_model.getInstance().getUserClass().get_userPic(),
                            Parse_model.getInstance().getUserClass().get_userName(),
                            new CurrentDateTime().getDateTime(),
                            commentContent,
                            _postID
                    );
                    new addComment(root, newComment).execute();
                }


                String _currUserName = Parse_model.getInstance().getUserClass().get_userName();
                new setCommentNotification().execute(_postID, _currUserName);

                Button commentTextView = (Button)homePageView.findViewById(R.id.commentButton);

                new getNumOfComments(_postID, commentTextView).execute();

            }
        });


        return root;
    }


    public class setCommentNotification extends AsyncTask<String, Void, String> {

        int commentsCounter;

        public setCommentNotification() {

            this.commentsCounter = 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            getDialog().dismiss();
        }

        @Override
        protected String doInBackground(String... params) {

            NotificationsOfPost notification = new NotificationsOfPost();
            notification.setUserName_getNotification(String.valueOf(Parse_model.getInstance().getUserNameByPostId(params[0])));
            notification.setRead(false);
            notification.setRecentAction("C");
            notification.setUserName_doAction(params[1]);
            notification.setProfilePicture(Parse_model.getInstance().getUserClass().get_userPic());
            Bitmap pic = (Bitmap) Parse_model.getInstance().getPictureByPostId(params[0]);
            notification.setPostPicture(pic);
            notification.setPostIdLiked(params[0]);


            Parse_model.getInstance().addCommentNotification(notification);


            commentsCounter = Parse_model.getInstance().getNumOfComments(_postID);

            return String.valueOf(commentsCounter);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


        }
    }


    public class ListAdapter extends BaseAdapter {
        private Context mContext;

        View mainView;

        public ListAdapter(Context mContext, View mainView) {
            this.mContext = mContext;
            this.mainView = mainView;
        }

        public int getCount() {
            return commentsArray.size();
        }

        public Object getItem(int position) {
            return commentsArray.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.comment_item, null);
            }

            ImageView _profilePicture = (ImageView) convertView.findViewById(R.id.comment_profilePicture);
            TextView _userName = (TextView) convertView.findViewById(R.id.comment_userName);
            TextView _commentContent = (TextView) convertView.findViewById(R.id.comment_content);
            TextView _date = (TextView) convertView.findViewById(R.id.comment_date);

            // sets profile picture
            if(commentsArray.get(position).get_profilePicture()==null){
                AtomicReference<CommentClass> currComment = new AtomicReference<>();
                currComment.set(commentsArray.get(position));
                new LoadCommentBitmap(currComment, _profilePicture, noProfilePictureBitmap, 1);
                commentsArray.get(position).set_profilePicture(currComment.get().get_profilePicture());
            }else{
                _profilePicture.setImageBitmap(commentsArray.get(position).get_profilePicture());
            }

            // sets user name
            _userName.setText(commentsArray.get(position).get_userName());

            // sets comment's content
            _commentContent.setText(commentsArray.get(position).get_comment());

            // sets the date
            _date.setText(commentsArray.get(position).get_date());

            return convertView;
        }
    }

    protected class addComment extends AsyncTask<String, Void, Boolean> {
        View view;
        CommentClass currComment;

        public addComment(View view, CommentClass currComment) {
            this.view = view;
            this.currComment = currComment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.findViewById(R.id.noCommentsMessage).setVisibility(View.GONE);
            EditText editText = (EditText) view.findViewById(R.id.coment_editText);
            editText.setText("");

            commentsArray.add(currComment);
            if(commentsArray.size() == 1){
                listView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
            }{
                listAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
            }


        }

        @Override
        protected Boolean doInBackground(String... params) {
//            commentsArray = Parse_model.getInstance().getCommentsByPost(_postID);
            return Parse_model.getInstance().addCommentToPost(currComment);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                // succeed adding comment to parse
                //new getAllComments(view).execute();
                listAdapter.notifyDataSetChanged();
            } else {
                // failed adding comment to parse
                view.findViewById(R.id.failedCommentMessage).setVisibility(View.VISIBLE);
            }

        }
    }

    protected class getAllComments extends AsyncTask<String, String, String> {

        View currView;

        public getAllComments(View view) {
            currView = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            commentsArray = Parse_model.getInstance().getCommentsByPost(_postID);

            // sorts all comments by date
            commentsArray = new CurrentDateTime().sortCommentsArrayByDate(commentsArray);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // makes the loading animation invisible
            currView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            listView = (ListView) currView.findViewById(R.id.commentListView);
            listAdapter = new ListAdapter(getActivity(), getView());
            if (commentsArray.size() != 0) {
                listView.setAdapter(listAdapter);
                scrollMyListViewToBottom();
            } else
                // showing message that there are no comments to display
                currView.findViewById(R.id.noCommentsMessage).setVisibility(View.VISIBLE);
            listAdapter.notifyDataSetChanged();
            super.onPostExecute(s);
        }
    }

    protected class getNumOfComments extends AsyncTask<Void, Void, Integer> {
        String objectId;
        Button numOfCommentsButton;

        public getNumOfComments(String objectId, Button numOfLikesButton) {
            this.objectId = objectId;
            this.numOfCommentsButton = numOfLikesButton;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return Parse_model.getInstance().getNumOfComments(objectId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            numOfCommentsButton.setText(integer + " Comments");

            Log.e("setCommentInDialog",integer + " Comments");
        }
    }

    private void scrollMyListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(listAdapter.getCount());
            }
        });
    }

    public void setHomePageView(View homePageView1){
        this.homePageView = homePageView1;
    }
//***************************************************************************************


}
