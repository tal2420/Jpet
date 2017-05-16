package com.example.jpet;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.Home.CommentClass;
import com.example.jpet.LikeAndFollowing.NotificationsOfPost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Liran on 14-Apr-15.
 */
public class CurrentDateTime {
    Calendar c = Calendar.getInstance();

    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss ,dd-MM-yyyy");
    String formattedDate = df.format(c.getTime());
    Boolean isTrue = false;


    public ArrayList<PostClass> sortPostsArrayByDate(ArrayList<PostClass> postsArray) {
        ArrayList<PostClass> newPostsArray = new ArrayList<PostClass>();
        int arraySize1 = postsArray.size();
        int arraySize2 = postsArray.size();

        for (int i = 0; i < arraySize1; i++) {
            int thePost = 0;
            for (int j = 1; j < arraySize2; j++) {
                Date date1 = new CurrentDateTime().getDateByString(postsArray.get(thePost).get_date());
                Date date2 = new CurrentDateTime().getDateByString(postsArray.get(j).get_date());
                if (!new CurrentDateTime().IsDateBigger(date1, date2)) {
                    thePost = j;
                }
            }
            arraySize2--;
            newPostsArray.add(postsArray.get(thePost));
            postsArray.remove(thePost);
        }
        return newPostsArray;
    }

    public ArrayList<CommentClass> sortCommentsArrayByDate(ArrayList<CommentClass> commentsArray) {
        ArrayList<CommentClass> newCommentsArray = new ArrayList<CommentClass>();
        int arraySize1 = commentsArray.size();
        int arraySize2 = commentsArray.size();

        for (int i = 0; i < arraySize1; i++) {
            int thePost = 0;
            for (int j = 1; j < arraySize2; j++) {
                Date date1 = new CurrentDateTime().getDateByString(commentsArray.get(thePost).get_date());
                Date date2 = new CurrentDateTime().getDateByString(commentsArray.get(j).get_date());
                if (new CurrentDateTime().IsDateBigger(date1, date2)) {
                    thePost = j;
                }
            }
            arraySize2--;
            newCommentsArray.add(commentsArray.get(thePost));
            commentsArray.remove(thePost);
        }
        return newCommentsArray;
    }


    public ArrayList<NotificationsOfPost> sortNotificationsArrayByDate(ArrayList<NotificationsOfPost> notificationsArray) {
        ArrayList<NotificationsOfPost> newNotificationArray = new ArrayList<NotificationsOfPost>();
        int arraySize1 = notificationsArray.size();
        int arraySize2 = notificationsArray.size();

        for (int i = 0; i < arraySize1; i++) {
            int thePost = 0;
            for (int j = 1; j < arraySize2; j++) {
                Date date1 = new CurrentDateTime().getDateByString(notificationsArray.get(thePost).get_date());
                Date date2 = new CurrentDateTime().getDateByString(notificationsArray.get(j).get_date());
                if (!(new CurrentDateTime().IsDateBigger(date1, date2))) {
                    thePost = j;
                }
            }
            arraySize2--;
            newNotificationArray.add(notificationsArray.get(thePost));
            notificationsArray.remove(thePost);
        }
        return newNotificationArray;
    }

    public String getDateTime() {
        return formattedDate;
    }

    public Boolean IsDateBigger(Date date1, Date date2) {
        if (date1.getYear() > date2.getYear()) {
            return true;
        } else if (date1.getYear() < date2.getYear()) {
            return false;
        } else {
            //checking months
            if (date1.getMonth() > date2.getMonth()) {
                return true;
            } else if (date1.getMonth() < date2.getMonth()) {
                return false;
            } else {
                //checking days
                if (date1.getDay() > date2.getDay()) {
                    return true;
                } else if (date1.getDay() < date2.getDay()) {
                    return false;
                } else {
                    //checking hours
                    if (date1.getHour() > date2.getHour()) {
                        return true;
                    } else if (date1.getHour() < date2.getHour()) {
                        return false;
                    } else {
                        //checking minutes
                        if (date1.getMinute() > date2.getMinute()) {
                            return true;
                        } else if (date1.getMinute() < date2.getMinute()) {
                            return false;
                        } else {
                            //checking seconds
                            if (date1.getSecond() > date2.getSecond()) {
                                return true;
                            } else if (date1.getSecond() < date2.getSecond()) {
                                return false;
                            } else {
//                                Log.e("Comparing dates", "dates are equals");
                            }

                        }
                    }
                }
            }
        }
//        Log.e("Comparing dates", "failed");
        return false;
    }

    public Date getDateByString(String date) {
        String[] dates = date.split(",");

        String[] HHmmss = dates[0].split(":");
        String[] ddMMyyyy = dates[1].split("-");

        String[] HHmmssZero = HHmmss[0].split(" ");

        String[] HHmmssTwo = HHmmss[2].split(" ");
        return new Date(
                Integer.valueOf(ddMMyyyy[2]),
                Integer.valueOf(ddMMyyyy[1]),
                Integer.valueOf(ddMMyyyy[0]),
                Integer.valueOf(HHmmssZero[0]),
                Integer.valueOf(HHmmss[1]),
                Integer.valueOf(HHmmssTwo[0]));
    }

//    String dateNow = new CurrentDateTime().getDateTime();
//    Date dgsdg = new CurrentDateTime().getDateByString(dateNow);

    public class Date  {
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;

        public Date(int year, int month, int day, int hour, int minute, int second) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }
    }


}
