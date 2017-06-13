package com.example.jpet.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.jpet.MainActivity;
import com.example.jpet.R;
import com.example.jpet.SoftKeyboard;
import com.example.jpet.managers.AnimalSettingsManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AnimalRegistrationFragment extends Fragment {

    View mainLayout;

    Spinner sexOfAnimalSpinner;
    Spinner animalTypesSpinner;
    Spinner breedSpinner;
    Spinner subBreedSpinner;
    Button registerButton;

    EditText nameOfPet;
    EditText height;
    EditText weight;
    EditText birthday;

    Calendar animalBirthDayCalendar = Calendar.getInstance();

    String[] animalSexList = {"Sex", "Male", "Female"};

    CheckBox pedigreeCheckBox;
    CheckBox trainCheckBox;
    CheckBox championCheckBox;
    CheckBox neuteredCheckBox;
    CheckBox shouldSendBreedingOffersCheckBox;

    View pedigreeCertificateUploadLayout;
    View trainedCertificateUploadLayout;
    View championCertificateUploadLayout;

    Button pedigreeTakePictureButton;
    Button trainedTakePictureButton;
    Button championTakePictureButton;

    ImageView pedigreeCertificateImageView;
    ImageView trainedCertificateImageView;
    ImageView championCertificateImageView;

    SoftKeyboard softKeyboard;

    Bitmap pedigreeCertificateImage;
    Bitmap trainedCertificateImage;
    Bitmap championCertificateImage;

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    //keep track of gallery intent
    final int GALLERY = 3;

    ImageType currentImageType;
    private enum ImageType {
        Pedigree, champion, Trained
    }

    public AnimalRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_animal_registration, container, false);

        mainLayout = root.findViewById(R.id.main_layout);
        registerButton = (Button) root.findViewById(R.id.register);

        sexOfAnimalSpinner = (Spinner) root.findViewById(R.id.sex_input);
        animalTypesSpinner = (Spinner) root.findViewById(R.id.animal_type);
        breedSpinner = (Spinner) root.findViewById(R.id.breed);
        subBreedSpinner = (Spinner) root.findViewById(R.id.sub_breed);

        pedigreeCheckBox = (CheckBox) root.findViewById(R.id.pedigree);
        trainCheckBox = (CheckBox) root.findViewById(R.id.train);
        championCheckBox = (CheckBox) root.findViewById(R.id.champion);
        neuteredCheckBox = (CheckBox) root.findViewById(R.id.neutered);
        shouldSendBreedingOffersCheckBox = (CheckBox) root.findViewById(R.id.should_send_breeding_offers);

        pedigreeTakePictureButton = (Button) root.findViewById(R.id.pedigree_button);
        trainedTakePictureButton = (Button) root.findViewById(R.id.trained_button);
        championTakePictureButton = (Button) root.findViewById(R.id.champion_button);

        pedigreeCertificateImageView = (ImageView) root.findViewById(R.id.pedigree_image_view);
        trainedCertificateImageView = (ImageView) root.findViewById(R.id.trained_image_view);
        championCertificateImageView = (ImageView) root.findViewById(R.id.champion_image_view);

        pedigreeCertificateUploadLayout = root.findViewById(R.id.pedigree_certificate_upload_layout);
        trainedCertificateUploadLayout = root.findViewById(R.id.trained_certificate_upload_layout);
        championCertificateUploadLayout = root.findViewById(R.id.champion_certificate_upload_layout);

        nameOfPet = (EditText) root.findViewById(R.id.pet_name_input);
        height = (EditText) root.findViewById(R.id.height);
        weight = (EditText) root.findViewById(R.id.weight);

        birthday = (EditText) root.findViewById(R.id.birthday);

        ArrayAdapter<String> animalSexListAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                animalSexList);
        sexOfAnimalSpinner.setAdapter(animalSexListAdapter);

        ArrayAdapter<String> animalTypesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                AnimalSettingsManager.animalTypes.toArray(new String[AnimalSettingsManager.animalTypes.size()])
        );
        animalTypesSpinner.setAdapter(animalTypesAdapter);

        ArrayAdapter<String> breedAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                AnimalSettingsManager.breedTypes.toArray(new String[AnimalSettingsManager.breedTypes.size()])
        );
        breedSpinner.setAdapter(breedAdapter);

        ArrayAdapter<String> subBreedAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                AnimalSettingsManager.subBreedTypes.toArray(new String[AnimalSettingsManager.subBreedTypes.size()])
        );
        subBreedSpinner.setAdapter(subBreedAdapter);

        pedigreeTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(ImageType.Pedigree);
            }
        });

        trainedTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(ImageType.Trained);
            }
        });

        championTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(ImageType.champion);
            }
        });

        pedigreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pedigreeCertificateUploadLayout.setVisibility(View.VISIBLE);
                    pedigreeCheckBox.setAlpha(1f);
                } else {
                    pedigreeCertificateUploadLayout.setVisibility(View.GONE);
                    pedigreeCheckBox.setAlpha(0.5f);
                }
            }
        });

        trainCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    trainedCertificateUploadLayout.setVisibility(View.VISIBLE);
                    trainCheckBox.setAlpha(1f);
                } else {
                    trainedCertificateUploadLayout.setVisibility(View.GONE);
                    trainCheckBox.setAlpha(0.5f);
                }
            }
        });

        championCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    championCertificateUploadLayout.setVisibility(View.VISIBLE);
                    championCheckBox.setAlpha(1f);
                } else {
                    championCertificateUploadLayout.setVisibility(View.GONE);
                    championCheckBox.setAlpha(0.5f);
                }
            }
        });

        neuteredCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    neuteredCheckBox.setAlpha(1f);
                } else {
                    neuteredCheckBox.setAlpha(0.5f);
                }
            }
        });

        shouldSendBreedingOffersCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shouldSendBreedingOffersCheckBox.setAlpha(1f);
                } else {
                    shouldSendBreedingOffersCheckBox.setAlpha(0.5f);
                }
            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                softKeyboard.closeSoftKeyboard();
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        animalBirthDayCalendar.set(Calendar.YEAR, year);
                        animalBirthDayCalendar.set(Calendar.MONTH, month);
                        animalBirthDayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        birthday.setText(sdf.format(animalBirthDayCalendar.getTime()));
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateListener, animalBirthDayCalendar
                        .get(Calendar.YEAR), animalBirthDayCalendar.get(Calendar.MONTH),
                        animalBirthDayCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfPetString = nameOfPet.getText().toString();
                String heightOfPet = height.getText().toString();
                String weightOfPet = weight.getText().toString();

                long dateOfBirth = animalBirthDayCalendar.getTimeInMillis();

                String sex = sexOfAnimalSpinner.getSelectedItem().toString();
                String animalType = animalTypesSpinner.getSelectedItem().toString();
                String breed = breedSpinner.getSelectedItem().toString();
                String subBreed = subBreedSpinner.getSelectedItem().toString();

                boolean isPedigree = pedigreeCheckBox.isChecked();
                boolean isChampion = championCheckBox.isChecked();
                boolean isTrained = trainCheckBox.isChecked();
                boolean isNeuter = neuteredCheckBox.isChecked();
                boolean shouldSendOffers = shouldSendBreedingOffersCheckBox.isChecked();


            }
        });

        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);

        softKeyboard = new SoftKeyboard((ViewGroup) mainLayout, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {

            @Override
            public void onSoftKeyboardHide() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        registerButton.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onSoftKeyboardShow() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        registerButton.setVisibility(View.GONE);
                    }
                });
            }
        });

        ((MainActivity) getActivity()).hidePanelBar();
        return root;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        softKeyboard.unRegisterSoftKeyboardCallback();
    }

    private void selectImage(ImageType imageType) {
        currentImageType = imageType;
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    try {
                        //use standard intent to capture an image
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        captureIntent.putExtra("imageType", "12312");
                        //we will handle the returned data in onActivityResult
                        startActivityForResult(captureIntent, CAMERA_CAPTURE);
                    } catch (ActivityNotFoundException anfe) {
                        //display an error message
                        String errorMessage = "Whoops - your device doesn't support capturing images!";
//                            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//                            toast.show();
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            //user is returning from capturing an image using the camera
            if (requestCode == CAMERA_CAPTURE) {// || requestCode == GALLERY) {

                Bundle extras = data.getExtras();

                switch (currentImageType) {
                    case Pedigree:
                        pedigreeCertificateImage = extras.getParcelable("data");
                        pedigreeCertificateImageView.setImageBitmap(pedigreeCertificateImage);
                        break;

                    case champion:
                        championCertificateImage = extras.getParcelable("data");
                        championCertificateImageView.setImageBitmap(championCertificateImage);
                        break;

                    case Trained:
                        trainedCertificateImage = extras.getParcelable("data");
                        trainedCertificateImageView.setImageBitmap(trainedCertificateImage);
                        break;
                }
            }

            if (requestCode == GALLERY) {
                final Uri imageUri = data.getData();
                final InputStream imageStream;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    switch (currentImageType) {
                        case Pedigree:
                            pedigreeCertificateImage = selectedImage;
                            pedigreeCertificateImageView.setImageBitmap(pedigreeCertificateImage);
                            break;

                        case champion:
                            championCertificateImage = selectedImage;
                            championCertificateImageView.setImageBitmap(championCertificateImage);
                            break;

                        case Trained:
                            trainedCertificateImage = selectedImage;
                            trainedCertificateImageView.setImageBitmap(trainedCertificateImage);
                            break;
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
