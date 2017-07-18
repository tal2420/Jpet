package com.example.jpet.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.jpet.Contract;
import com.example.jpet.DB_Model.ParseDB.Parse_Settings;
import com.example.jpet.MainActivity;
import com.example.jpet.Network.Network;
import com.example.jpet.R;
import com.example.jpet.managers.AnimalSettingsManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {

    View mainLayout;

    Spinner sexOfAnimalSpinner;
    Spinner animalTypesSpinner;
    Spinner breedSpinner;
    Spinner subBreedSpinner;

    String[] animalSexList = {Contract.AnimalSettings.ANY, "Male", "Female"};

    CheckBox pedigreeCheckBox;
    CheckBox trainCheckBox;
    CheckBox championCheckBox;
    CheckBox neuteredCheckBox;
    CheckBox shouldSendBreedingOffersCheckBox;

    ArrayAdapter<String> animalSexListAdapter;
    ArrayAdapter<String> animalTypesAdapter;
    ArrayAdapter<String> breedAdapter;
    ArrayAdapter<String> subBreedAdapter;

    Button mFilterButton;

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_filter, container, false);

        mainLayout = root.findViewById(R.id.main_layout);
        mFilterButton = (Button) root.findViewById(R.id.filter);

        sexOfAnimalSpinner = (Spinner) root.findViewById(R.id.sex_input);
        animalTypesSpinner = (Spinner) root.findViewById(R.id.animal_type);
        breedSpinner = (Spinner) root.findViewById(R.id.breed);
        subBreedSpinner = (Spinner) root.findViewById(R.id.sub_breed);

        pedigreeCheckBox = (CheckBox) root.findViewById(R.id.pedigree);
        trainCheckBox = (CheckBox) root.findViewById(R.id.train);
        championCheckBox = (CheckBox) root.findViewById(R.id.champion);
        neuteredCheckBox = (CheckBox) root.findViewById(R.id.neutered);
        shouldSendBreedingOffersCheckBox = (CheckBox) root.findViewById(R.id.should_send_breeding_offers);

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

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                String sex = sexOfAnimalSpinner.getSelectedItem().toString();
                String animalType = animalTypesSpinner.getSelectedItem().toString();
                String breed = breedSpinner.getSelectedItem().toString();
                String subBreed = subBreedSpinner.getSelectedItem().toString();
                boolean isPedigree = pedigreeCheckBox.isChecked();
                boolean isChampion = championCheckBox.isChecked();
                boolean isTrained = trainCheckBox.isChecked();
                boolean isNeuter = neuteredCheckBox.isChecked();

                bundle.putString(Contract.Animal.SEX_STRING, sex);
                bundle.putString(Contract.Animal.TYPE_STRING, animalType);
                bundle.putString(Contract.Animal.BREED_STRING, breed);
                bundle.putString(Contract.Animal.SUB_BREED_STRING, subBreed);

                bundle.putBoolean(Contract.Animal.IS_PEDIGREE_BOOLEAN, isPedigree);
                bundle.putBoolean(Contract.Animal.IS_TRAINED_BOOLEAN, isChampion);
                bundle.putBoolean(Contract.Animal.IS_CHAMPION_BOOLEAN, isTrained);
                bundle.putBoolean(Contract.Animal.IS_NEUTER_BOOLEAN, isNeuter);

                FilteredAnimalsFragment filteredAnimalsFragment = new FilteredAnimalsFragment();
                filteredAnimalsFragment.setArguments(bundle);

                ((MainActivity)getActivity()).openNewFrag(filteredAnimalsFragment);
            }
        });
        return root;
    }

}
