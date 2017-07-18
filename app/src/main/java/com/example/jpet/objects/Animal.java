package com.example.jpet.objects;

import android.graphics.Bitmap;

import com.example.jpet.Camera.PostClass;
import com.parse.ParseObject;

/**
 * Created by yakov on 6/6/2017.
 */

public class Animal {

    String email;

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

    public Animal(){}

    public Animal(String animalId) {
        this.animalId = animalId;
    }

    public boolean shouldSeePost(PostClass post) {
        return post.isChampion() == isChampion() && post.isChampion() ||
                post.isPedigree() == isPedigree() && post.isPedigree() ||
                post.isTrained() == isTrained() && post.isTrained() ||
                post.isNeutered() == isNeutered() && post.isNeutered() ||
                post.getSex().equals(sex) ||
                post.getPetType().equals(petType)  ||
                post.getBreed().equals(breed) ||
                post.getSubBreed().equals(subBreed);
    }

    public String getEmail() {
        return email;
    }

    public Animal setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAnimalId() {
        return animalId;
    }

    public Animal setAnimalId(String animalId) {
        this.animalId = animalId;
        return this;
    }

    public String getPetName() {
        return petName;
    }

    public Animal setPetName(String petName) {
        this.petName = petName;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public Animal setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getPetType() {
        return petType;
    }

    public Animal setPetType(String petType) {
        this.petType = petType;
        return this;
    }

    public String getBreed() {
        return breed;
    }

    public Animal setBreed(String breed) {
        this.breed = breed;
        return this;
    }

    public String getSubBreed() {
        return subBreed;
    }

    public Animal setSubBreed(String subBreed) {
        this.subBreed = subBreed;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public Animal setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Animal setHeight(int height) {
        this.height = height;
        return this;
    }

    public long getBirthdayInMilliSec() {
        return birthdayInMilliSec;
    }

    public Animal setBirthdayInMilliSec(long birthdayInMilliSec) {
        this.birthdayInMilliSec = birthdayInMilliSec;
        return this;
    }

    public boolean isPedigree() {
        return isPedigree;
    }

    public Animal setPedigree(boolean pedigree) {
        isPedigree = pedigree;
        return this;
    }

    public Bitmap getPedigreeCertificatePicture() {
        return pedigreeCertificatePicture;
    }

    public Animal setPedigreeCertificatePicture(Bitmap pedigreeCertificatePicture) {
        this.pedigreeCertificatePicture = pedigreeCertificatePicture;
        return this;
    }

    public boolean isTrained() {
        return isTrained;
    }

    public Animal setTrained(boolean trained) {
        isTrained = trained;
        return this;
    }

    public Bitmap getTrainingCertificatePicture() {
        return trainingCertificatePicture;
    }

    public Animal setTrainingCertificatePicture(Bitmap trainingCertificatePicture) {
        this.trainingCertificatePicture = trainingCertificatePicture;
        return this;
    }

    public boolean isChampion() {
        return isChampion;
    }

    public Animal setChampion(boolean champion) {
        isChampion = champion;
        return this;
    }

    public Bitmap getChampionCertificatePicture() {
        return championCertificatePicture;
    }

    public Animal setChampionCertificatePicture(Bitmap championCertificatePicture) {
        this.championCertificatePicture = championCertificatePicture;
        return this;
    }

    public boolean isNeutered() {
        return isNeutered;
    }

    public Animal setNeutered(boolean neutered) {
        isNeutered = neutered;
        return this;
    }

    public boolean isShouldSendBreedingOffers() {
        return shouldSendBreedingOffers;
    }

    public Animal setShouldSendBreedingOffers(boolean shouldSendBreedingOffers) {
        this.shouldSendBreedingOffers = shouldSendBreedingOffers;
        return this;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public Animal setPhoto(Bitmap photo) {
        this.photo = photo;
        return this;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "email='" + email + '\'' +
                ", animalId='" + animalId + '\'' +
                ", petName='" + petName + '\'' +
                ", sex='" + sex + '\'' +
                ", petType='" + petType + '\'' +
                ", breed='" + breed + '\'' +
                ", subBreed='" + subBreed + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", birthdayInMilliSec=" + birthdayInMilliSec +
                ", isPedigree=" + isPedigree +
                ", pedigreeCertificatePicture=" + pedigreeCertificatePicture +
                ", isTrained=" + isTrained +
                ", trainingCertificatePicture=" + trainingCertificatePicture +
                ", isChampion=" + isChampion +
                ", championCertificatePicture=" + championCertificatePicture +
                ", isNeutered=" + isNeutered +
                ", shouldSendBreedingOffers=" + shouldSendBreedingOffers +
                ", photo=" + photo +
                '}';
    }
}
