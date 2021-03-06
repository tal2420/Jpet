package com.example.jpet.Camera;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jpet.Contract;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.DEBUG;
import com.example.jpet.MainActivity;
import com.example.jpet.Network.Network;
import com.example.jpet.R;
import com.example.jpet.managers.AnimalSettingsManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


/**
 * Created by eliyadaida on 3/22/15.
 */
public class CameraFragment extends Fragment {

    public CameraFragment() {
        // Required empty public constructor
    }

    int picWidth;
    int picHeight;


    Bitmap _thePic;
    ImageView viewImage;
    EditText mainString;
    EditText hashTag;
    Button addPost;
    Button getPics;

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    //keep track of gallery intent
    final int GALLERY = 3;
    //captured picture uri
    private Uri picUri;

    CheckBox pedigreeCheckBox;
    CheckBox trainCheckBox;
    CheckBox championCheckBox;
    CheckBox neuteredCheckBox;
    CheckBox shouldSendBreedingOffersCheckBox;

    ArrayAdapter<String> animalSexListAdapter;
    ArrayAdapter<String> animalTypesAdapter;
    ArrayAdapter<String> breedAdapter;
    ArrayAdapter<String> subBreedAdapter;

    Spinner sexOfAnimalSpinner;
    Spinner animalTypesSpinner;
    Spinner breedSpinner;
    Spinner subBreedSpinner;

    String[] animalSexList = {Contract.AnimalSettings.ANY, "Male", "Female"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);

         ((MainActivity)getActivity()).getSupportActionBar().setTitle("Adding New Post");

        viewImage = (ImageView) root.findViewById(R.id.viewImage);
        viewImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.noimage));
        mainString = (EditText) root.findViewById(R.id.mainString_txt);
        hashTag = (EditText) root.findViewById(R.id.hash_tag);

        sexOfAnimalSpinner = (Spinner) root.findViewById(R.id.sex_input);
        animalTypesSpinner = (Spinner) root.findViewById(R.id.animal_type);
        breedSpinner = (Spinner) root.findViewById(R.id.breed);
        subBreedSpinner = (Spinner) root.findViewById(R.id.sub_breed);

        pedigreeCheckBox = (CheckBox) root.findViewById(R.id.pedigree);
        trainCheckBox = (CheckBox) root.findViewById(R.id.train);
        championCheckBox = (CheckBox) root.findViewById(R.id.champion);
        neuteredCheckBox = (CheckBox) root.findViewById(R.id.neutered);
        shouldSendBreedingOffersCheckBox = (CheckBox) root.findViewById(R.id.should_send_breeding_offers);

        if (Parse_model.getInstance().getUserClass().isAdmin()) {
            root.findViewById(R.id.admin_form).setVisibility(View.VISIBLE);
        }

        Button selectPhotoButton = (Button) root.findViewById(R.id.btnSelectPhoto);
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).confirmPermissions(new MainActivity.PermissionRequestCallback() {
                    @Override
                    public void onRequestResults(boolean hasPermissions, ArrayList<String> rejectedPermissions) {
                        if (hasPermissions) {
                            selectImage();
                        }
                    }
                }, android.Manifest.permission.CAMERA);
            }
        });


        addPost = (Button) root.findViewById(R.id.addPost_btn);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_thePic != null) {
                    DEBUG.MSG(getClass(), "addPost has clicked");
                    new postPic().execute();

                    getActivity().findViewById(R.id.camera1).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.camera).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.home_page).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.home_page1).setVisibility(View.GONE);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("You must upload a picture before!");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.show();
                }
            }
        });

        animalTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> breeds = AnimalSettingsManager.getBreedsByFather((String)animalTypesSpinner.getSelectedItem());
                breeds.add(Contract.AnimalSettings.ANY);
                breedAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        breeds.toArray(new String[breeds.size()])
                );
                breedSpinner.setAdapter(breedAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ArrayList<String> breeds = AnimalSettingsManager.getBreedsByFather((String)animalTypesSpinner.getSelectedItem());
                breeds.add(Contract.AnimalSettings.ANY);
                breedAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        breeds.toArray(new String[breeds.size()])
                );
                breedSpinner.setAdapter(breedAdapter);
            }
        });

        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> subBreeds = AnimalSettingsManager.getSubBreedsByFather((String)breedSpinner.getSelectedItem());
                subBreeds.add(Contract.AnimalSettings.ANY);
                subBreedAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        subBreeds.toArray(new String[subBreeds.size()])
                );
                subBreedSpinner.setAdapter(subBreedAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ArrayList<String> subBreeds = AnimalSettingsManager.getSubBreedsByFather((String)breedSpinner.getSelectedItem());
                subBreeds.add(Contract.AnimalSettings.ANY);
                subBreedAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        subBreeds.toArray(new String[subBreeds.size()])
                );
                subBreedSpinner.setAdapter(subBreedAdapter);
            }
        });

        Network.runOnThreadWithProgressDialog(getActivity(), new Network.NetworkCallBack() {
            @Override
            public Object doInBackground() {
                AnimalSettingsManager.downloadAndSetSettings();
                return null;
            }

            @Override
            public void onPostExecute(Object object) {
                super.onPostExecute(object);

                animalSexListAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        animalSexList);
                sexOfAnimalSpinner.setAdapter(animalSexListAdapter);

                ArrayList<String> types = AnimalSettingsManager.animalTypes;
                types.add(Contract.AnimalSettings.ANY);
                animalTypesAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        types.toArray(new String[types.size()])
                );
                animalTypesSpinner.setAdapter(animalTypesAdapter);

                ArrayList<String> breeds = AnimalSettingsManager.animalTypes;
                breeds.add(Contract.AnimalSettings.ANY);
                breedAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        breeds.toArray(new String[breeds.size()])
                );
                breedSpinner.setAdapter(breedAdapter);

                ArrayList<String> subBreeds = AnimalSettingsManager.animalTypes;
                subBreeds.add(Contract.AnimalSettings.ANY);
                subBreedAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        subBreeds.toArray(new String[subBreeds.size()])
                );
                subBreedSpinner.setAdapter(subBreedAdapter);
            }
        });

        return root;
    }

    private class postPic extends AsyncTask<Void, Void, Boolean> {

        ProgressDialog dialog;
        PostClass currPost;

        protected void onPreExecute() {

            DEBUG.MSG(getClass(), "postPic AsyncTask is working");
            dialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
            //super.onPreExecute();
            currPost = new PostClass();

            currPost.set_userName(Parse_model.getInstance().getUserClass().get_userName());
            currPost.set_mainString(mainString.getText().toString());
            currPost.set_hashTag(hashTag.getText().toString());
            currPost.set_postPicture(_thePic);
            currPost.setPicWidth(picWidth);
            currPost.setPicHeight(picHeight);

            if (Parse_model.getInstance().getUserClass().isAdmin()) {
                String sex = sexOfAnimalSpinner.getSelectedItem().toString();
                String animalType = animalTypesSpinner.getSelectedItem().toString();
                String breed = breedSpinner.getSelectedItem().toString();
                String subBreed = subBreedSpinner.getSelectedItem().toString();

                boolean isPedigree = pedigreeCheckBox.isChecked();
                boolean isChampion = championCheckBox.isChecked();
                boolean isTrained = trainCheckBox.isChecked();
                boolean isNeuter = neuteredCheckBox.isChecked();

                currPost.setSex(sex)
                        .setPetType(animalType)
                        .setBreed(breed)
                        .setSubBreed(subBreed)
                        .setPedigree(isPedigree)
                        .setTrained(isTrained)
                        .setChampion(isChampion)
                        .setNeutered(isNeuter)
                        .setShouldSendBreedingOffers(true);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return Parse_model.getInstance().addPost(currPost);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean) {
                dialog.hide();
                errorDialog();
            } else {
                mainString.setText("");
                viewImage.setImageBitmap(null);
                if (cameraToHomePageDelegate != null) {
                    cameraToHomePageDelegate.GoToHomePage();
                }
//                Home_Model.getInstance().setPostsArrayList(4, null, null);
//                Home_Model.getInstance().setPostsArrayList(2, Parse_model.getInstance().getUserClass().get_userName(), null);
                dialog.hide();
            }
        }
    }


    private void selectImage() {

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


    /**
     * Handle user returning from both capturing and cropping the image
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            //user is returning from capturing an image using the camera
            if (requestCode == CAMERA_CAPTURE || requestCode == GALLERY) {
                //get the Uri for the captured image
//                picUri = data.getData();

                if (data == null || data.getExtras() == null) return;

                _thePic = data.getExtras().getParcelable("data");

                if (_thePic == null) {
                    Toast.makeText(getActivity(), "Failed taking picture", Toast.LENGTH_SHORT).show();
                }

                picWidth = _thePic.getWidth();
                picHeight = _thePic.getHeight();

                //display the returned cropped image
                viewImage.setImageBitmap(_thePic);
                //carry out the crop operation
//                performCrop();
            }
            //user is returning from cropping the image
            else if (requestCode == PIC_CROP) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                _thePic = extras.getParcelable("data");

//                _thePic = decodeSampledBitmapFromPath(picturePath, 100, 100);
                picWidth = _thePic.getWidth();
                picHeight = _thePic.getHeight();

                //display the returned cropped image
                viewImage.setImageBitmap(_thePic);

            }
        }
    }


    public void errorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Failed connecting with parse. please try again later.")
                .setCancelable(false)
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    //****************************************************************************
    public static String encodeToBase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);


        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        if (input != null) {
            Log.d("decode pic:", "is NOT null, which means it got the pics");

            byte[] decodedByte = Base64.decode(input, 0);
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        } else {
            // if(dialogDelegate!=null){
            //    dialogDelegate.alert();
            //  }
            return null;

        }

    }

    //***************************************************************************
    // resizing the resolution of the picture
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Helper method to carry out crop operation
     */
    private void performCrop() {
        //take care of exceptions
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setClassName("com.android.camera", "com.android.camera.CropImage");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("noFaceDetection", true);
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        //respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromPath(String pathName, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }


    //****************************************************************************
//camera to home page delegate
    public interface cameraToHomePageInterface {
        public void GoToHomePage();

    }

    cameraToHomePageInterface cameraToHomePageDelegate;

    public void setCameraToHomePageDelegate(cameraToHomePageInterface cameraToHomePageDelegate) {
        this.cameraToHomePageDelegate = cameraToHomePageDelegate;
    }

    //****************************************************************************

}

