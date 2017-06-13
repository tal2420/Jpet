package com.example.jpet;

/**
 * Created by yakov on 6/12/2017.
 */

public class Contract {

    public class AnimalSettings {

        public static final String ANIMALS_TYPES_TABLE_NAME = "AnimalTypes";
        public static final String ANIMALS_TYPES_COLUMN = "types";

        public static final String BREED_TABLE_NAME = "Breeds";
        public static final String BREED_COLUMN = "breed";

        public static final String SUB_BREED_TABLE_NAME = "SubBreeds";
        public static final String SUB_BREED_COLUMN = "subBreed";
    }

    public class Animal {
        public static final String QUERY_NAME_STRING = "animals";

        public static final String USER_EMAIL_STRING = "user_email";
        public static final String NAME_STRING = "name";
        public static final String SEX_STRING = "sex";
        public static final String TYPE_STRING = "type";

        public static final String BREED_STRING = "breed";
        public static final String SUB_BREED_STRING = "sub_breed";

        public static final String WEIGHT_INT = "weight";
        public static final String HEIGHT_INT = "height";

        public static final String BIRTHDAY_DATE_LONG = "birth_date";

        public static final String IS_PEDIGREE_BOOLEAN = "is_pedigree";
        public static final String PEDIGREE_CERTIFICATE_PICTUE_FILE = "pedigree_certificate_picture";

        public static final String IS_TRAINED_BOOLEAN = "is_trained";
        public static final String TRAINED_CERTIFICATE_PICTUE_FILE = "trained_certificate_picture";

        public static final String IS_CHAMPION_BOOLEAN = "is_champion";
        public static final String CHAMPION_CERTIFICATE_PICTURE_FILE = "champion_certificate_picture";

        public static final String IS_NEUTER_BOOLEAN = "is_neuter";
        public static final String SHOULD_SEND_BREEDING_OFFERS_BOOLEAN = "should_send_breeding_offers";

        public static final String PHOTO_FILE = "photo";
    }
}
