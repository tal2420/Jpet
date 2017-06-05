package com.example.jpet.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.jpet.Config;
import com.example.jpet.Constant;
import com.example.jpet.DEBUG;


public class ResourceHelper {
   public static Resources resources;
   private static Class<?> aClass = ResourceHelper.class;

   /**
    * Return application resources, generated once.
    */
   public static Resources getResources() {
      if (resources == null)
         resources = Config.getContext().getResources();
      return resources;
   }

   public static Drawable getDrawable(int id) {
      return ContextCompat.getDrawable(Config.getContext(), id);
   }

   public static String getString(int id){
      return getResources().getString(id);
   }

   public static int getColor(int id) {
      return ContextCompat.getColor(Config.getContext(), id);
   }

   /**
    * Return application shared preferences object.
    */
   public static SharedPreferences getSharedPreferences() {
      return Config.getContext().getSharedPreferences(Constant.PREFERENCES, Context.MODE_PRIVATE);
   }

   public static SharedPreferences getSharedPreferences(String preference) {
      return Config.getContext().getSharedPreferences(preference, Context.MODE_PRIVATE);
   }

   /**
    * Method is used to save preferences objects.
    * Supported common types: Integer, String, Boolean, Long, Float.
    */

   public static void savePreferenceObject(String sharedPreferenceName, String preferenceKey, Object preferenceObject) {
      if (preferenceObject instanceof Integer)
         getSharedPreferences(sharedPreferenceName).edit().putInt(preferenceKey, (Integer) preferenceObject).apply();
      else if (preferenceObject instanceof String)
         getSharedPreferences(sharedPreferenceName).edit().putString(preferenceKey, (String) preferenceObject).apply();
      else if (preferenceObject instanceof Boolean)
         getSharedPreferences(sharedPreferenceName).edit().putBoolean(preferenceKey, (Boolean) preferenceObject).apply();
      else if (preferenceObject instanceof Long)
         getSharedPreferences(sharedPreferenceName).edit().putLong(preferenceKey, (Long) preferenceObject).apply();
      else if (preferenceObject instanceof Float)
         getSharedPreferences(sharedPreferenceName).edit().putFloat(preferenceKey, (Float) preferenceObject).apply();
      else DEBUG.MSG(aClass, "Saving preference object is failed.");
   }


   public static void savePreferenceObject(String preferenceKey, Object preferenceObject) {
      if (preferenceObject instanceof Integer)
         getSharedPreferences().edit().putInt(preferenceKey, (Integer) preferenceObject).apply();
      else if (preferenceObject instanceof String)
         getSharedPreferences().edit().putString(preferenceKey, (String) preferenceObject).apply();
      else if (preferenceObject instanceof Boolean)
         getSharedPreferences().edit().putBoolean(preferenceKey, (Boolean) preferenceObject).apply();
      else if (preferenceObject instanceof Long)
         getSharedPreferences().edit().putLong(preferenceKey, (Long) preferenceObject).apply();
      else if (preferenceObject instanceof Float)
         getSharedPreferences().edit().putFloat(preferenceKey, (Float) preferenceObject).apply();
      else DEBUG.MSG(aClass, "Saving preference object is failed.");
   }


}