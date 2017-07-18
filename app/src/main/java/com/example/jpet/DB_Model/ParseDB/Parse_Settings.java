package com.example.jpet.DB_Model.ParseDB;

import com.example.jpet.Contract;
import com.example.jpet.managers.AnimalSettingsManager;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yakov on 6/13/2017.
 */

public class Parse_Settings {

    public static ArrayList<String> getAnimalsSettingsByName(String tableName, String columnName) {
        ArrayList<String> arrayList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        try {
            List<ParseObject> parseObjectList = query.find();

            for (ParseObject parseObject : parseObjectList) {
                arrayList.add(parseObject.getString(columnName));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static HashMap<String, String> getAnimalsSettingsHash(String tableName, String columnName, String fatherTypeColumnName) {
        HashMap<String, String> map = new HashMap<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        try {
            List<ParseObject> parseObjectList = query.find();
            for (ParseObject parseObject : parseObjectList) {
                map.put(parseObject.getString(columnName), parseObject.getString(fatherTypeColumnName));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

//    public static ArrayList<AnimalSettingsManager.SubBreed> getAnimalsSettingsSubBreed(String tableName, String columnName, String fatherTypeColumnName) {
//        ArrayList<AnimalSettingsManager.SubBreed> arrayList = new ArrayList<>();
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
//        try {
//            List<ParseObject> parseObjectList = query.find();
//
//            for (ParseObject parseObject : parseObjectList) {
//                AnimalSettingsManager.SubBreed subBreed = new AnimalSettingsManager.SubBreed(
//                        parseObject.getString(columnName),
//                        parseObject.getString(fatherTypeColumnName)
//                );
//                arrayList.add(subBreed);
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return arrayList;
//    }

    public static void setAnimalSettings(String tableName, String columnName, String... items) {
        if (items != null && items.length > 0) {
            ParseObject parseObject = new ParseObject(tableName);
            for (int i = 0; i < items.length; i++) {
                parseObject.put(columnName, items[i]);
            }
            try {
                parseObject.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
