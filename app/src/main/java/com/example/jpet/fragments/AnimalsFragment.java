package com.example.jpet.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jpet.Constant;
import com.example.jpet.Contract;
import com.example.jpet.DB_Model.ParseDB.Parse_Settings;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.MainActivity;
import com.example.jpet.Network.Network;
import com.example.jpet.R;
import com.example.jpet.managers.AnimalSettingsManager;
import com.example.jpet.objects.Animal;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnimalsFragment extends Fragment {

    RecyclerView mRecyclerView;
    AnimalsAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    Button signUpAnimalButton;


    public AnimalsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_animals, container, false);
        signUpAnimalButton = (Button) root.findViewById(R.id.sign_up_animal_button);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.animals_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AnimalsAdapter(Parse_model.getInstance().getUserClass().getAnimals());
        mRecyclerView.setAdapter(mAdapter);

        signUpAnimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Network.runOnThreadWithProgressDialog(getActivity(), new Network.NetworkCallBack() {

                    @Override
                    public Object doInBackground() {
                        AnimalSettingsManager.downloadAndSetSettings();
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

    private class AnimalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<Animal> animals = new ArrayList<>();

        public AnimalsAdapter(ArrayList<Animal> animals) {
            this.animals = animals;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.animal_item, parent, false);
            return new AnimalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            AnimalViewHolder animalViewHolder = (AnimalViewHolder) holder;

            Animal animal = animals.get(position);

            animalViewHolder.name.setText(animal.getPetName());
            animalViewHolder.type.setText(animal.getPetType());
            animalViewHolder.sex.setText(animal.getSex());
            animalViewHolder.profilePicture.setImageBitmap(animal.getPhoto());

            animalViewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Network.runOnThreadWithProgressDialog(getActivity(), new Network.NetworkCallBack() {

                        @Override
                        public Object doInBackground() {
                            AnimalSettingsManager.downloadAndSetSettings();
                            return null;
                        }

                        @Override
                        public void onPostExecute(Object object) {
                            super.onPostExecute(object);
                            AnimalRegistrationFragment animalRegistrationFragment = new AnimalRegistrationFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.BundleKeys.ANIMAL_POSITION_IN_ARRAY, holder.getAdapterPosition());
                            animalRegistrationFragment.setArguments(bundle);
                            ((MainActivity)getActivity()).openNewFrag(animalRegistrationFragment);
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return animals.size();
        }
        private class AnimalViewHolder extends RecyclerView.ViewHolder {

            private View mainLayout;
            private TextView name;
            private TextView type;
            private TextView sex;
            private ImageView profilePicture;

            private AnimalViewHolder(View itemView) {
                super(itemView);
                mainLayout = itemView.findViewById(R.id.main_layout);
                name = (TextView) itemView.findViewById(R.id.animal_name);
                type = (TextView) itemView.findViewById(R.id.animal_type);
                sex = (TextView) itemView.findViewById(R.id.animal_sex);
                profilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
            }
        }

    }


}
