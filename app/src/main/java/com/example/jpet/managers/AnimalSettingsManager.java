package com.example.jpet.managers;

import com.example.jpet.Contract;
import com.example.jpet.DB_Model.ParseDB.Parse_Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yakov on 6/13/2017.
 */

public class AnimalSettingsManager {

    public static ArrayList<String> animalTypes = new ArrayList<>();
    public static ArrayList<String> breedTypes = new ArrayList<>();
    public static ArrayList<String> subBreedTypes = new ArrayList<>();

    private static Map<String, String> breedMap = new HashMap<>();
    private static Map<String, String> subBreedMap = new HashMap<>();

    public static void downloadAndSetSettings() {
        breedMap = Parse_Settings.getAnimalsSettingsHash(Contract.AnimalSettings.BREED_TABLE_NAME
        ,Contract.AnimalSettings.BREED_COLUMN,Contract.AnimalSettings.ANIMALS_TYPES_COLUMN);
        for (Map.Entry<String,String> entry : breedMap.entrySet()) {
            breedTypes.add(entry.getKey());
        }

        subBreedMap = Parse_Settings.getAnimalsSettingsHash(Contract.AnimalSettings.SUB_BREED_TABLE_NAME,
                Contract.AnimalSettings.SUB_BREED_COLUMN, Contract.AnimalSettings.BREED_COLUMN);
        for (Map.Entry<String,String> entry : subBreedMap.entrySet()) {
            subBreedTypes.add(entry.getKey());
        }

        animalTypes = Parse_Settings.getAnimalsSettingsByName(
                Contract.AnimalSettings.ANIMALS_TYPES_TABLE_NAME,
                Contract.AnimalSettings.ANIMALS_TYPES_COLUMN);
    }

    public static ArrayList<String> getBreedsByFather(String type) {
        ArrayList<String> breeds = new ArrayList<>();
        for (Map.Entry<String,String> entry : breedMap.entrySet()) {
            if (type.equals(entry.getValue())) {
                breeds.add(entry.getKey());
            }
        }
        return breeds;
    }

    public static ArrayList<String> getSubBreedsByFather(String breed) {
        ArrayList<String> subBreeds = new ArrayList<>();
        for (Map.Entry<String,String> entry : subBreedMap.entrySet()) {
            if (breed.equals(entry.getValue())) {
                subBreeds.add(entry.getKey());
            }
        }
        return subBreeds;
    }
}
