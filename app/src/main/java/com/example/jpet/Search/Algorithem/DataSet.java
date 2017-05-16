package com.example.jpet.Search.Algorithem;

/**
 * Created by Liran on 05-Jun-15.
 */
public class DataSet {

    String postID;
    int cluster;
    int numOfClusters;
    double[] postPoint = new double[6];

    public DataSet(String postID, int cluster, int numOfClusters, double[] postPoint) {
        this.postID = postID;
        this.cluster = cluster;
        this.numOfClusters = numOfClusters;
        this.postPoint = postPoint;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public int getNumOfClusters() {
        return numOfClusters;
    }

    public void setNumOfClusters(int numOfClusters) {
        this.numOfClusters = numOfClusters;
    }

    public double[] getPostPoint() {
        return postPoint;
    }

    public void setPostPoint(double[] postPoint) {
        this.postPoint = postPoint;
    }
}
