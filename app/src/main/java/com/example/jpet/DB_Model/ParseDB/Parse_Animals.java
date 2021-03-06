package com.example.jpet.DB_Model.ParseDB;

import android.graphics.Bitmap;

import com.example.jpet.Contract;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.DEBUG;
import com.example.jpet.helpers.CameraHelper;
import com.example.jpet.objects.Animal;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yakov on 6/12/2017.
 */

public class Parse_Animals {

    public static boolean updateAnimal(Animal animal) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Contract.Animal.QUERY_NAME_STRING);

        DEBUG.trace(5);
        DEBUG.MSG(Parse_Animals.class, "animal.getAnimalId() = " + animal.getAnimalId());

        query.whereEqualTo(Contract.Animal.ANIMAL_OBJECT_ID, animal.getAnimalId());
        ParseObject animalObject = null;
        try {
            animalObject = query.getFirst();

            if (animalObject == null) {
                return false;
            }

            animalObject.put(Contract.Animal.USER_EMAIL_STRING, Parse_model.getInstance().getUserClass().get_email());

            animalObject.put(Contract.Animal.NAME_STRING, animal.getPetName());
            animalObject.put(Contract.Animal.SEX_STRING, animal.getSex());
            animalObject.put(Contract.Animal.TYPE_STRING, animal.getPetType());
            animalObject.put(Contract.Animal.BREED_STRING, animal.getBreed());
            animalObject.put(Contract.Animal.SUB_BREED_STRING, animal.getSubBreed());
            animalObject.put(Contract.Animal.WEIGHT_INT, animal.getWeight());
            animalObject.put(Contract.Animal.HEIGHT_INT, animal.getHeight());
            animalObject.put(Contract.Animal.BIRTHDAY_DATE_LONG, animal.getBirthdayInMilliSec());

            addAnimalPicture(animalObject, Contract.Animal.PROFILE_PICTURE_FILE, animal.getPhoto());

            animalObject.put(Contract.Animal.IS_PEDIGREE_BOOLEAN, animal.isPedigree());
            if (animal.isPedigree()) {
                addAnimalPicture(animalObject, Contract.Animal.PEDIGREE_CERTIFICATE_PICTURE_FILE, animal.getPedigreeCertificatePicture());
            }

            animalObject.put(Contract.Animal.IS_TRAINED_BOOLEAN, animal.isTrained());
            if (animal.isTrained()) {
                addAnimalPicture(animalObject, Contract.Animal.TRAINED_CERTIFICATE_PICTUE_FILE, animal.getTrainingCertificatePicture());
            }

            animalObject.put(Contract.Animal.IS_CHAMPION_BOOLEAN, animal.isChampion());
            if (animal.isChampion()) {
                addAnimalPicture(animalObject, Contract.Animal.CHAMPION_CERTIFICATE_PICTURE_FILE, animal.getChampionCertificatePicture());
            }

            animalObject.put(Contract.Animal.IS_NEUTER_BOOLEAN, animal.isNeutered());
            animalObject.put(Contract.Animal.SHOULD_SEND_BREEDING_OFFERS_BOOLEAN, animal.isShouldSendBreedingOffers());

            animalObject.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;

    }

    public static boolean addAnimalPicture(ParseObject animalParseObject, String columnName, Bitmap pic) {

        if (pic == null) {
            return false;
        }

        try {
            byte[] data = CameraHelper.encodeToBase64(pic).getBytes();
            ParseFile file = new ParseFile("pic.txt", data);
            animalParseObject.put(columnName, file);
            animalParseObject.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Bitmap getAnimalPicture(String animalId, String columnName) {

        ParseQuery query = new ParseQuery(Contract.Animal.QUERY_NAME_STRING);
        query = query.whereEqualTo(Contract.Animal.ANIMAL_OBJECT_ID, animalId);

        try {
            ParseFile parsePictureFile;
            ParseObject currPostObject = query.getFirst();
            parsePictureFile = (ParseFile) currPostObject.get(columnName);
            String data = new String(parsePictureFile.getData());
            return CameraHelper.decodeBase64(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addAnimal(Animal animal) {
        ParseObject animalObject = new ParseObject(Contract.Animal.QUERY_NAME_STRING);

        DEBUG.trace(5);
        DEBUG.MSG(Parse_Animals.class, "animalObject.getObjectId() = " + animalObject.getObjectId());

        animalObject.put(Contract.Animal.USER_EMAIL_STRING, Parse_model.getInstance().getUserClass().get_email());
        animalObject.put(Contract.Animal.NAME_STRING, animal.getPetName());
        animalObject.put(Contract.Animal.SEX_STRING, animal.getSex());
        animalObject.put(Contract.Animal.TYPE_STRING, animal.getPetType());
        animalObject.put(Contract.Animal.BREED_STRING, animal.getBreed());
        animalObject.put(Contract.Animal.SUB_BREED_STRING, animal.getSubBreed());
        animalObject.put(Contract.Animal.WEIGHT_INT, animal.getWeight());
        animalObject.put(Contract.Animal.HEIGHT_INT, animal.getHeight());
        animalObject.put(Contract.Animal.BIRTHDAY_DATE_LONG, animal.getBirthdayInMilliSec());

        addAnimalPicture(animalObject, Contract.Animal.PROFILE_PICTURE_FILE, animal.getPhoto());

        animalObject.put(Contract.Animal.IS_PEDIGREE_BOOLEAN, animal.isPedigree());
        if (animal.isPedigree()) {
            addAnimalPicture(animalObject, Contract.Animal.PEDIGREE_CERTIFICATE_PICTURE_FILE, animal.getPedigreeCertificatePicture());
        }

        animalObject.put(Contract.Animal.IS_TRAINED_BOOLEAN, animal.isTrained());
        if (animal.isTrained()) {
            addAnimalPicture(animalObject, Contract.Animal.TRAINED_CERTIFICATE_PICTUE_FILE, animal.getTrainingCertificatePicture());
        }

        animalObject.put(Contract.Animal.IS_CHAMPION_BOOLEAN, animal.isChampion());
        if (animal.isChampion()) {
            addAnimalPicture(animalObject, Contract.Animal.CHAMPION_CERTIFICATE_PICTURE_FILE, animal.getChampionCertificatePicture());
        }

        animalObject.put(Contract.Animal.IS_NEUTER_BOOLEAN, animal.isNeutered());
        animalObject.put(Contract.Animal.SHOULD_SEND_BREEDING_OFFERS_BOOLEAN, animal.isShouldSendBreedingOffers());


        try {
            animalObject.save();
            animal.setAnimalId(animalObject.getObjectId());
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ArrayList<Animal> getAllAnimals() {
        ArrayList<Animal> animals = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Contract.Animal.QUERY_NAME_STRING);
        try {
            List<ParseObject> animalsObjects = query.find();
            for (ParseObject animalObject : animalsObjects) {

                Animal animal = new Animal(animalObject.getObjectId());

                if (animalObject.containsKey(Contract.Animal.NAME_STRING)) {
                    String petName = animalObject.getString(Contract.Animal.NAME_STRING);
                    DEBUG.MSG(Parse_Animals.class, "animal name exists = " + petName);
                    animal.setPetName(petName);
                } else {
                    DEBUG.MSG(Parse_Animals.class, "animal name NOT exists");
                }

                if (animalObject.containsKey(Contract.Animal.USER_EMAIL_STRING)) {
                    animal.setEmail(animalObject.getString(Contract.Animal.USER_EMAIL_STRING));
                }

                if (animalObject.containsKey(Contract.Animal.SEX_STRING)) {
                    animal.setSex(animalObject.getString(Contract.Animal.SEX_STRING));
                }

                if (animalObject.containsKey(Contract.Animal.TYPE_STRING)) {
                    animal.setPetType(animalObject.getString(Contract.Animal.TYPE_STRING));
                }

                if (animalObject.containsKey(Contract.Animal.BREED_STRING)) {
                    animal.setBreed(animalObject.getString(Contract.Animal.BREED_STRING));
                }

                if (animalObject.containsKey(Contract.Animal.SUB_BREED_STRING)) {
                    animal.setSubBreed(animalObject.getString(Contract.Animal.SUB_BREED_STRING));
                }

                if (animalObject.containsKey(Contract.Animal.WEIGHT_INT)) {
                    animal.setWeight(animalObject.getInt(Contract.Animal.WEIGHT_INT));
                }

                if (animalObject.containsKey(Contract.Animal.HEIGHT_INT)) {
                    animal.setHeight(animalObject.getInt(Contract.Animal.HEIGHT_INT));
                }

                if (animalObject.containsKey(Contract.Animal.BIRTHDAY_DATE_LONG)) {
                    animal.setBirthdayInMilliSec(animalObject.getLong(Contract.Animal.BIRTHDAY_DATE_LONG));
                }

                if (animalObject.containsKey(Contract.Animal.IS_PEDIGREE_BOOLEAN)) {
                    animal.setPedigree(animalObject.getBoolean(Contract.Animal.IS_PEDIGREE_BOOLEAN));
                }
//                if (animalObject.containsKey(Contract.Animal.PEDIGREE_CERTIFICATE_PICTURE_FILE)) {
//                    animal.setPetName(animalObject.getString(Contract.Animal.PEDIGREE_CERTIFICATE_PICTURE_FILE));
//                }

                if (animalObject.containsKey(Contract.Animal.IS_TRAINED_BOOLEAN)) {
                    animal.setTrained(animalObject.getBoolean(Contract.Animal.IS_TRAINED_BOOLEAN));
                }
//                if (animalObject.containsKey(Contract.Animal.TRAINED_CERTIFICATE_PICTUE_FILE)) {
//                    animal.setPetName(animalObject.getString(Contract.Animal.TRAINED_CERTIFICATE_PICTUE_FILE));
//                }

                if (animalObject.containsKey(Contract.Animal.IS_CHAMPION_BOOLEAN)) {
                    animal.setChampion(animalObject.getBoolean(Contract.Animal.IS_CHAMPION_BOOLEAN));
                }
//                if (animalObject.containsKey(Contract.Animal.CHAMPION_CERTIFICATE_PICTURE_FILE)) {
//                    animal.setPetName(animalObject.getString(Contract.Animal.CHAMPION_CERTIFICATE_PICTURE_FILE));
//                }

                if (animalObject.containsKey(Contract.Animal.IS_NEUTER_BOOLEAN)) {
                    animal.setNeutered(animalObject.getBoolean(Contract.Animal.IS_NEUTER_BOOLEAN));
                }

                if (animalObject.containsKey(Contract.Animal.SHOULD_SEND_BREEDING_OFFERS_BOOLEAN)) {
                    animal.setShouldSendBreedingOffers(animalObject.getBoolean(Contract.Animal.SHOULD_SEND_BREEDING_OFFERS_BOOLEAN));
                }

                if (animalObject.containsKey(Contract.Animal.PROFILE_PICTURE_FILE)) {
                    Bitmap photo = getAnimalPicture(animalObject.getObjectId(), Contract.Animal.PROFILE_PICTURE_FILE);
                    animal.setPhoto(photo);
                }

                if (animalObject.containsKey(Contract.Animal.PEDIGREE_CERTIFICATE_PICTURE_FILE)) {
                    Bitmap pedigreePhoto = getAnimalPicture(animalObject.getObjectId(), Contract.Animal.PEDIGREE_CERTIFICATE_PICTURE_FILE);
                    animal.setPedigreeCertificatePicture(pedigreePhoto);
                }

                if (animalObject.containsKey(Contract.Animal.CHAMPION_CERTIFICATE_PICTURE_FILE)) {
                    Bitmap championPhoto = getAnimalPicture(animalObject.getObjectId(), Contract.Animal.CHAMPION_CERTIFICATE_PICTURE_FILE);
                    animal.setPhoto(championPhoto);
                }

                if (animalObject.containsKey(Contract.Animal.TRAINED_CERTIFICATE_PICTUE_FILE)) {
                    Bitmap trainedPhoto = getAnimalPicture(animalObject.getObjectId(), Contract.Animal.TRAINED_CERTIFICATE_PICTUE_FILE);
                    animal.setTrainingCertificatePicture(trainedPhoto);
                }

                animals.add(animal);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return animals;
    }

    public static ArrayList<Animal> getAllAnimalsByEmail(String email) {
        ArrayList<Animal> animals = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Contract.Animal.QUERY_NAME_STRING);
        query.whereEqualTo(Contract.Animal.USER_EMAIL_STRING, email);
        try {
            List<ParseObject> animalsObjects = query.find();
            for (ParseObject animalObject : animalsObjects) {

                Animal animal = new Animal(animalObject.getObjectId());

                if (animalObject.containsKey(Contract.Animal.NAME_STRING)) {
                    String petName = animalObject.getString(Contract.Animal.NAME_STRING);
                    DEBUG.MSG(Parse_Animals.class, "animal name exists = " + petName);
                    animal.setPetName(petName);
                } else {
                    DEBUG.MSG(Parse_Animals.class, "animal name NOT exists");
                }

                if (animalObject.containsKey(Contract.Animal.USER_EMAIL_STRING)) {
                    animal.setEmail(animalObject.getString(Contract.Animal.USER_EMAIL_STRING));
                }

                if (animalObject.containsKey(Contract.Animal.SEX_STRING)) {
                    animal.setSex(animalObject.getString(Contract.Animal.SEX_STRING));
                }

                if (animalObject.containsKey(Contract.Animal.TYPE_STRING)) {
                    animal.setPetType(animalObject.getString(Contract.Animal.TYPE_STRING));
                }

                if (animalObject.containsKey(Contract.Animal.BREED_STRING)) {
                    animal.setBreed(animalObject.getString(Contract.Animal.BREED_STRING));
                }

                if (animalObject.containsKey(Contract.Animal.SUB_BREED_STRING)) {
                    animal.setSubBreed(animalObject.getString(Contract.Animal.SUB_BREED_STRING));
                }

                if (animalObject.containsKey(Contract.Animal.WEIGHT_INT)) {
                    animal.setWeight(animalObject.getInt(Contract.Animal.WEIGHT_INT));
                }

                if (animalObject.containsKey(Contract.Animal.HEIGHT_INT)) {
                    animal.setHeight(animalObject.getInt(Contract.Animal.HEIGHT_INT));
                }

                if (animalObject.containsKey(Contract.Animal.BIRTHDAY_DATE_LONG)) {
                    animal.setBirthdayInMilliSec(animalObject.getLong(Contract.Animal.BIRTHDAY_DATE_LONG));
                }

                if (animalObject.containsKey(Contract.Animal.IS_PEDIGREE_BOOLEAN)) {
                    animal.setPedigree(animalObject.getBoolean(Contract.Animal.IS_PEDIGREE_BOOLEAN));
                }
//                if (animalObject.containsKey(Contract.Animal.PEDIGREE_CERTIFICATE_PICTURE_FILE)) {
//                    animal.setPetName(animalObject.getString(Contract.Animal.PEDIGREE_CERTIFICATE_PICTURE_FILE));
//                }

                if (animalObject.containsKey(Contract.Animal.IS_TRAINED_BOOLEAN)) {
                    animal.setTrained(animalObject.getBoolean(Contract.Animal.IS_TRAINED_BOOLEAN));
                }
//                if (animalObject.containsKey(Contract.Animal.TRAINED_CERTIFICATE_PICTUE_FILE)) {
//                    animal.setPetName(animalObject.getString(Contract.Animal.TRAINED_CERTIFICATE_PICTUE_FILE));
//                }

                if (animalObject.containsKey(Contract.Animal.IS_CHAMPION_BOOLEAN)) {
                    animal.setChampion(animalObject.getBoolean(Contract.Animal.IS_CHAMPION_BOOLEAN));
                }
//                if (animalObject.containsKey(Contract.Animal.CHAMPION_CERTIFICATE_PICTURE_FILE)) {
//                    animal.setPetName(animalObject.getString(Contract.Animal.CHAMPION_CERTIFICATE_PICTURE_FILE));
//                }

                if (animalObject.containsKey(Contract.Animal.IS_NEUTER_BOOLEAN)) {
                    animal.setNeutered(animalObject.getBoolean(Contract.Animal.IS_NEUTER_BOOLEAN));
                }

                if (animalObject.containsKey(Contract.Animal.SHOULD_SEND_BREEDING_OFFERS_BOOLEAN)) {
                    animal.setShouldSendBreedingOffers(animalObject.getBoolean(Contract.Animal.SHOULD_SEND_BREEDING_OFFERS_BOOLEAN));
                }

                if (animalObject.containsKey(Contract.Animal.PROFILE_PICTURE_FILE)) {
                    Bitmap photo = getAnimalPicture(animalObject.getObjectId(), Contract.Animal.PROFILE_PICTURE_FILE);
                    animal.setPhoto(photo);
                }

                if (animalObject.containsKey(Contract.Animal.PEDIGREE_CERTIFICATE_PICTURE_FILE)) {
                    Bitmap pedigreePhoto = getAnimalPicture(animalObject.getObjectId(), Contract.Animal.PEDIGREE_CERTIFICATE_PICTURE_FILE);
                    animal.setPedigreeCertificatePicture(pedigreePhoto);
                }

                if (animalObject.containsKey(Contract.Animal.CHAMPION_CERTIFICATE_PICTURE_FILE)) {
                    Bitmap championPhoto = getAnimalPicture(animalObject.getObjectId(), Contract.Animal.CHAMPION_CERTIFICATE_PICTURE_FILE);
                    animal.setPhoto(championPhoto);
                }

                if (animalObject.containsKey(Contract.Animal.TRAINED_CERTIFICATE_PICTUE_FILE)) {
                    Bitmap trainedPhoto = getAnimalPicture(animalObject.getObjectId(), Contract.Animal.TRAINED_CERTIFICATE_PICTUE_FILE);
                    animal.setTrainingCertificatePicture(trainedPhoto);
                }

                animals.add(animal);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return animals;
    }
}
