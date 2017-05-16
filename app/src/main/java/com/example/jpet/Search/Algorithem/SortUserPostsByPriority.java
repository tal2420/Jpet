package com.example.jpet.Search.Algorithem;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.DB_Model.CommentsLikes;
import com.example.jpet.Home.CommentClass;
import com.example.jpet.Home.LikeClass;

import java.util.ArrayList;

/**
 * Created by Liran on 05-Jun-15.
 */
public class SortUserPostsByPriority {

    ArrayList<DataSet> postsClustersArray;
    ArrayList<PostClass> postsArray;
    CommentsLikes userCommentsAndLikes;

    public SortUserPostsByPriority(ArrayList<DataSet> postsClustersArray, ArrayList<PostClass> postsArray, CommentsLikes userCommentsAndLikes) {
        this.postsClustersArray = postsClustersArray;
        this.postsArray = postsArray;
        this.userCommentsAndLikes = userCommentsAndLikes;
    }

    ArrayList<PostIdValuability> postIdValuabilitiesArray;

    public ArrayList<PostClass> getSortedPostsByPriority() {

//        int numOfClusters = postsClustersArray.get(0).getNumOfClusters();

        setPostsValueByLikeComments();

        //array which sets points to each cluster by the number of posts it has
        ArrayList<ClusterValue> clusterValueArray = setPostsValueClustersArray();

        ArrayList<ClusterValue> clusterValueArrayBySize = new ArrayList<>();
        //arrange cluster by value size
        for (int currClusterIndex = 0; currClusterIndex < clusterValueArray.size() - 1; currClusterIndex++) {
            for (int otherClusterIndex = currClusterIndex + 1; otherClusterIndex < clusterValueArray.size(); otherClusterIndex++) {
                if(clusterValueArray.get(currClusterIndex).getClusterValue() < clusterValueArray.get(otherClusterIndex).getClusterValue()){
                    clusterValueArray = replaceIndexesInArray(clusterValueArray, currClusterIndex, otherClusterIndex);
                }
            }
        }
        ArrayList<PostClass> newPostArray = new ArrayList<>();
        for(ClusterValue currClusterValue : clusterValueArray){
            for(DataSet currDataSet : postsClustersArray){
                if(currDataSet.getCluster() == currClusterValue.getClusterNum())
                    for(PostClass currPost : postsArray)
                        if(currPost.getObjectID().equals(currDataSet.getPostID()))
                            newPostArray.add(currPost);
            }
        }

        return newPostArray;
    }

    public ArrayList<ClusterValue> replaceIndexesInArray(ArrayList<ClusterValue> clusterValueArray, int index1, int index2) {
        ClusterValue clusterValue = new ClusterValue();

        clusterValue.setClusterNum(clusterValueArray.get(index1).getClusterNum());
        clusterValue.setClusterValue(clusterValueArray.get(index1).getClusterValue());

        clusterValueArray.get(index1).setClusterNum(clusterValueArray.get(index2).getClusterNum());
        clusterValueArray.get(index1).setClusterValue(clusterValueArray.get(index2).getClusterValue());

        clusterValueArray.get(index2).setClusterNum(clusterValue.getClusterNum());
        clusterValueArray.get(index2).setClusterValue(clusterValue.getClusterValue());

        return clusterValueArray;
    }

    class ClusterValue {
        int clusterNum;
        int clusterValue;

        public ClusterValue() {
        }

        public ClusterValue(int clusterNum, int clusterValue) {
            this.clusterNum = clusterNum;
            this.clusterValue = clusterValue;
        }

        public int getClusterNum() {
            return clusterNum;
        }

        public void setClusterNum(int clusterNum) {
            this.clusterNum = clusterNum;
        }

        public int getClusterValue() {
            return clusterValue;
        }

        public void setClusterValue(int clusterValue) {
            this.clusterValue = clusterValue;
        }
    }

    public ArrayList<ClusterValue> setPostsValueClustersArray() {
        int numOfClusters = postsClustersArray.get(0).getNumOfClusters();
        int[] clustersArrayPoints = new int[numOfClusters];

        // sets clusters to each post's value
        for (PostIdValuability currPostValue : postIdValuabilitiesArray) {
            for (DataSet currDataSet : postsClustersArray) {
                if (currPostValue.getPostID().equals(currDataSet.getPostID())) {
                    currPostValue.setPostCluster(currDataSet.getCluster());
                }
            }
        }

        ArrayList<ClusterValue> clusterValueArray = new ArrayList<>();

        //sets how much value each cluster has by post's value
        for (int i = 0; i < postIdValuabilitiesArray.size(); i++) {
            clustersArrayPoints[postIdValuabilitiesArray.get(i).getPostCluster()] += postIdValuabilitiesArray.get(i).getPoints();
        }

        for (int i = 0; i < clustersArrayPoints.length; i++) {
            clusterValueArray.add(new ClusterValue(i, clustersArrayPoints[i]));
        }
        return clusterValueArray;
    }

    public void setPostsValueByLikeComments() {

        postIdValuabilitiesArray = new ArrayList<>();

        ArrayList<CommentClass> userCommentsArray = userCommentsAndLikes.getCommentsArray();
        ArrayList<LikeClass> userLikesArray = userCommentsAndLikes.getLikesArray();

        //set each user's posts value by how much comments and like the user did by each post

        //set posts value by comments
        for (CommentClass currComment : userCommentsArray) {
            if (postIdValuabilitiesArray.size() == 0) {
                //if posts value array's size is 0, so add the first comment
                postIdValuabilitiesArray.add(new PostIdValuability(
                        currComment.get_postId(),
                        1
                ));
            } else {
                //posts value array's size != 0
                boolean isPostExsist = false;
                for (PostIdValuability currPostValuable : postIdValuabilitiesArray) {
                    //if post already exists
                    if (currPostValuable.getPostID().equals(currComment.get_postId())) {
                        //if post value exist so ++ post's value
                        currPostValuable.setPoints(currPostValuable.getPoints() + 1);
                        isPostExsist = true;
                    }
                }
                if (!isPostExsist) {
                    //else, add the post value
                    postIdValuabilitiesArray.add(new PostIdValuability(
                            currComment.get_postId(),
                            1
                    ));
                }
            }
        }
        //sets posts value by likes
        for (LikeClass currLike : userLikesArray) {
            if (postIdValuabilitiesArray.size() == 0) {
                //if posts value array's size is 0, so add the first comment
                postIdValuabilitiesArray.add(new PostIdValuability(
                        currLike.getPostID(),
                        1
                ));
            } else {
                //posts value array's size != 0
                boolean isPostExist = false;
                for (PostIdValuability currPostValuable : postIdValuabilitiesArray) {
                    //if post already exists
                    if (currPostValuable.getPostID().equals(currLike.getPostID())) {
                        //if post value exist so ++ post's value
                        currPostValuable.setPoints(currPostValuable.getPoints() + 1);
                        isPostExist = true;
                    }
                }
                if (!isPostExist) {
                    //else, add the post value
                    postIdValuabilitiesArray.add(new PostIdValuability(
                            currLike.getPostID(),
                            1
                    ));
                }
            }
        }


    }

    class PostIdValuability {
        String postID;
        int points = 0;
        int postCluster;


        public PostIdValuability(String postID, int points) {
            this.postID = postID;
            this.points = points;
        }

        public int getPostCluster() {
            return postCluster;
        }

        public void setPostCluster(int postCluster) {
            this.postCluster = postCluster;
        }

        public String getPostID() {
            return postID;
        }

        public void setPostID(String postID) {
            this.postID = postID;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }
    }


}
