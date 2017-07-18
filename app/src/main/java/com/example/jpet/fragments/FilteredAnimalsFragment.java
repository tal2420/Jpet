package com.example.jpet.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jpet.Contract;
import com.example.jpet.DB_Model.ParseDB.Parse_Animals;
import com.example.jpet.DEBUG;
import com.example.jpet.Network.Network;
import com.example.jpet.R;
import com.example.jpet.objects.Animal;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilteredAnimalsFragment extends Fragment {

    RecyclerView mRecyclerView;
    FilteredAnimalAdapter mAdapter;
    LinearLayoutManager mLinearLayoutManager;

    private String mSexFilter;
    private String mAnimalTypeFilter;
    private String mBreedFilter;
    private String mSubBreedFilter;

    private boolean mPedigreeFilter;
    private boolean mTrainFilter;
    private boolean mChampionFilter;
    private boolean mNeuteredFilter;

    ArrayAdapter<String> animalSexListAdapter;
    ArrayAdapter<String> animalTypesAdapter;
    ArrayAdapter<String> breedAdapter;
    ArrayAdapter<String> subBreedAdapter;

    Spinner sexOfAnimalSpinner;
    Spinner animalTypesSpinner;
    Spinner breedSpinner;
    Spinner subBreedSpinner;

    String[] animalSexList = {"Male", "Female"};
    public FilteredAnimalsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_filtered_animals, container, false);

        if (getArguments() != null) {
            Bundle bundle = getArguments();

            mSexFilter = bundle.getString(Contract.Animal.SEX_STRING);
            mAnimalTypeFilter = bundle.getString(Contract.Animal.TYPE_STRING);
            mBreedFilter = bundle.getString(Contract.Animal.BREED_STRING);
            mSubBreedFilter = bundle.getString(Contract.Animal.SUB_BREED_STRING);

            mPedigreeFilter = bundle.getBoolean(Contract.Animal.IS_PEDIGREE_BOOLEAN);
            mTrainFilter = bundle.getBoolean(Contract.Animal.IS_TRAINED_BOOLEAN);
            mChampionFilter = bundle.getBoolean(Contract.Animal.IS_CHAMPION_BOOLEAN);
            mNeuteredFilter = bundle.getBoolean(Contract.Animal.IS_NEUTER_BOOLEAN);

            DEBUG.MSG(getClass(), "mSexFilter: " + mSexFilter);
            DEBUG.MSG(getClass(), "mAnimalTypeFilter: " + mAnimalTypeFilter);
            DEBUG.MSG(getClass(), "mBreedFilter: " + mBreedFilter);
            DEBUG.MSG(getClass(), "mSubBreedFilter: " + mSubBreedFilter);

            DEBUG.MSG(getClass(), "mPedigreeFilter: " + mPedigreeFilter);
            DEBUG.MSG(getClass(), "mTrainFilter: " + mTrainFilter);
            DEBUG.MSG(getClass(), "mChampionFilter: " + mChampionFilter);
            DEBUG.MSG(getClass(), "mNeuteredFilter: " + mNeuteredFilter);
        }

        mRecyclerView = (RecyclerView) root.findViewById(R.id.animals_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        Network.runOnThreadWithProgressDialog(getActivity(), new Network.NetworkCallBack() {
            @Override
            public Object doInBackground() {
                return Parse_Animals.getAllAnimals();
            }

            @Override
            public void onPostExecute(Object object) {

                ArrayList<Animal> animals = (ArrayList<Animal>) object;
                filterAnimals(animals);
                mAdapter = new FilteredAnimalAdapter(animals);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        return root;
    }

    public void filterAnimals(ArrayList<Animal> animals) {
        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();

            DEBUG.MSG(getClass(), animal.toString());

            if (!Contract.AnimalSettings.ANY.equals(mSexFilter)) {
                if (!mSexFilter.equals(animal.getSex())) {
                    iterator.remove();
                    continue;
                }
            }

            if (!Contract.AnimalSettings.ANY.equals(mAnimalTypeFilter)) {
                if (!mAnimalTypeFilter.equals(animal.getPetType())) {
                    iterator.remove();
                    continue;
                }
            }

            if (!Contract.AnimalSettings.ANY.equals(mBreedFilter)) {
                if (!mBreedFilter.equals(animal.getBreed())) {
                    iterator.remove();
                    continue;
                }
            }

            if (!Contract.AnimalSettings.ANY.equals(mSubBreedFilter)) {
                if (!mSubBreedFilter.equals(animal.getSubBreed())) {
                    iterator.remove();
                    continue;
                }
            }

            if (animal.isPedigree() != mPedigreeFilter) {
                iterator.remove();
                continue;
            }

            if (animal.isTrained() != mTrainFilter) {
                iterator.remove();
                continue;
            }

            if (animal.isChampion() != mChampionFilter) {
                iterator.remove();
                continue;
            }

            if (animal.isNeutered() != mNeuteredFilter) {
                iterator.remove();
            }
        }
    }

    private class FilteredAnimalAdapter extends RecyclerView.Adapter<FilteredAnimalAdapter.FilteredAnimalViewHolder> {

        private ArrayList<Animal> animals;

        public FilteredAnimalAdapter(ArrayList<Animal> animals) {
            this.animals = animals;
        }

        @Override
        public FilteredAnimalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filtered_animal, parent, false);
            return new FilteredAnimalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FilteredAnimalViewHolder holder, int position) {
            Animal animal = animals.get(position);

            holder.email.setText("Owner email: " + animal.getEmail());
            holder.petName.setText("Pet name: " + animal.getPetName());
            holder.sex.setText("Pet sex: " + animal.getSex());
            holder.petType.setText("Pet type: " + animal.getPetType());
            holder.breed.setText("Pet breed: " + animal.getBreed());
        }

        @Override
        public int getItemCount() {
            return animals.size();
        }

        public class FilteredAnimalViewHolder extends RecyclerView.ViewHolder {

            public TextView email;
            public TextView petName;
            public TextView sex;
            public TextView petType;
            public TextView breed;

            public FilteredAnimalViewHolder(View itemView) {
                super(itemView);

                email = (TextView) itemView.findViewById(R.id.owner_email);
                petName = (TextView) itemView.findViewById(R.id.pet_name);
                sex = (TextView) itemView.findViewById(R.id.pet_sex);
                petType = (TextView) itemView.findViewById(R.id.pet_type);
                breed = (TextView) itemView.findViewById(R.id.pet_breed);
            }
        }
    }

}
