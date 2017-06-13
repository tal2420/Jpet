package com.example.jpet.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jpet.Contract;
import com.example.jpet.DB_Model.ParseDB.Parse_Settings;
import com.example.jpet.MainActivity;
import com.example.jpet.Network;
import com.example.jpet.R;
import com.example.jpet.managers.AnimalSettingsManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnimalsFragment extends Fragment {

    Button signUpAnimalButton;

    public AnimalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_animals, container, false);

        signUpAnimalButton = (Button) root.findViewById(R.id.sign_up_animal_button);

        signUpAnimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Network.runOnThreadWithProgressDialog(getActivity(), new Network.NetworkCallBack() {

                    @Override
                    public Object doInBackground() {
                        AnimalSettingsManager.breedTypes = Parse_Settings.getAnimalsSettingsByName(
                                Contract.AnimalSettings.BREED_TABLE_NAME,
                                Contract.AnimalSettings.BREED_COLUMN);

                        AnimalSettingsManager.subBreedTypes = Parse_Settings.getAnimalsSettingsByName(
                                Contract.AnimalSettings.SUB_BREED_TABLE_NAME,
                                Contract.AnimalSettings.SUB_BREED_COLUMN);

                        AnimalSettingsManager.animalTypes = Parse_Settings.getAnimalsSettingsByName(
                                Contract.AnimalSettings.ANIMALS_TYPES_TABLE_NAME,
                                Contract.AnimalSettings.ANIMALS_TYPES_COLUMN);

                        return null;
                    }

                    @Override
                    public void onPostExecute(Object object) {
                        super.onPostExecute(object);
                        ((MainActivity)getActivity()).openNewFrag(new AnimalRegistrationFragment());
                    }
                });
            }
        });


        return root;
    }
}
