package com.example.jpet.objects;

import android.graphics.Bitmap;

import com.parse.ParseObject;

/**
 * Created by yakov on 6/6/2017.
 */

public class Animal {

    String animalId;

    String petName;

    String sex;
    String petType;
    String breed;
    String subBreed;
    int weight;
    int height;
    long birthdayInMilliSec;

    boolean isPedigree;
    Bitmap pedigreeCertificatePicture;

    boolean isTrained;
    Bitmap trainingCertificatePicture;

    boolean isChampion;
    Bitmap championCertificatePicture;

    boolean isNeutered;
    boolean shouldSendBreedingOffers;

    Bitmap photo;


    public Animal(String animalId) {
        this.animalId = animalId;
    }

    public enum PetType {
        Dog, Cat, Bird;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSubBreed() {
        return subBreed;
    }

    public void setSubBreed(String subBreed) {
        this.subBreed = subBreed;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getBirthdayInMilliSec() {
        return birthdayInMilliSec;
    }

    public void setBirthdayInMilliSec(long birthdayInMilliSec) {
        this.birthdayInMilliSec = birthdayInMilliSec;
    }

    public boolean isPedigree() {
        return isPedigree;
    }

    public void setPedigree(boolean pedigree) {
        isPedigree = pedigree;
    }

    public Bitmap getPedigreeCertificatePicture() {
        return pedigreeCertificatePicture;
    }

    public void setPedigreeCertificatePicture(Bitmap pedigreeCertificatePicture) {
        this.pedigreeCertificatePicture = pedigreeCertificatePicture;
    }

    public boolean isTrained() {
        return isTrained;
    }

    public void setTrained(boolean trained) {
        isTrained = trained;
    }

    public Bitmap getTrainingCertificatePicture() {
        return trainingCertificatePicture;
    }

    public void setTrainingCertificatePicture(Bitmap trainingCertificatePicture) {
        this.trainingCertificatePicture = trainingCertificatePicture;
    }

    public boolean isChampion() {
        return isChampion;
    }

    public void setChampion(boolean champion) {
        isChampion = champion;
    }

    public Bitmap getChampionCertificatePicture() {
        return championCertificatePicture;
    }

    public void setChampionCertificatePicture(Bitmap championCertificatePicture) {
        this.championCertificatePicture = championCertificatePicture;
    }

    public boolean isNeutered() {
        return isNeutered;
    }

    public void setNeutered(boolean neutered) {
        isNeutered = neutered;
    }

    public boolean isShouldSendBreedingOffers() {
        return shouldSendBreedingOffers;
    }

    public void setShouldSendBreedingOffers(boolean shouldSendBreedingOffers) {
        this.shouldSendBreedingOffers = shouldSendBreedingOffers;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
