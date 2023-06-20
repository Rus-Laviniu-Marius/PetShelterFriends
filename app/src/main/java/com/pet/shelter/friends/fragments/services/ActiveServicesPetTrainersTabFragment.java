package com.pet.shelter.friends.fragments.services;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.profile.services.ActiveServiceData;
import com.pet.shelter.friends.profile.services.ActiveServiceDetailsActivity;
import com.pet.shelter.friends.profile.services.ActiveServicesCustomAdapter;
import com.pet.shelter.friends.profile.services.AddServiceActivity;

import java.util.ArrayList;
import java.util.Objects;

public class ActiveServicesPetTrainersTabFragment extends Fragment {

    private DatabaseReference petTrainersActiveServicesReference, roles;
    private String loggedUserId;
    private ListView petTrainerServicesListView;
    private RelativeLayout addActiveServicesPetTrainerRelativeLayout;
    private MaterialTextView materialTextView;

    private final ArrayList<ActiveServiceData> activePetTrainersServicesList = new ArrayList<>();
    private ActiveServicesCustomAdapter activeServicesCustomAdapter;

    public ActiveServicesPetTrainersTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_active_services_pet_trainers_tab, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        roles = firebaseDatabase.getReference("roles");
        petTrainersActiveServicesReference = firebaseDatabase.getReference("activeServices");

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        petTrainerServicesListView = layout.findViewById(R.id.activeServicesPetTrainers_listView);
        addActiveServicesPetTrainerRelativeLayout = layout.findViewById(R.id.activeServicesAddPetTrainers_relativeLayout);
        ExtendedFloatingActionButton addServiceFloatingActionButton = layout.findViewById(R.id.activeServicesAddPetTrainers_floatingActionButton);
        materialTextView = layout.findViewById(R.id.activeServicesPetTrainersNoServices_materialTextView);

        getDataFromDatabase();

        addServiceFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddServiceActivity.class));
                requireActivity().finish();
            }
        });

        petTrainerServicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActiveServiceData activeServiceData = activeServicesCustomAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ActiveServiceDetailsActivity.class);
                intent.putExtra("providerUserProfileImage", activeServiceData.getProviderUserProfileImage());
                intent.putExtra("name", activeServiceData.getName());
                intent.putExtra("email", activeServiceData.getEmail());
                intent.putExtra("phoneNumber", activeServiceData.getPhoneNumber());
                intent.putExtra("webpageLink", activeServiceData.getWebpageLink());
                intent.putExtra("cityStateCountry", activeServiceData.getCityStateCountry());
                intent.putExtra("address", activeServiceData.getAddress());
                intent.putExtra("description", activeServiceData.getDescription());
                intent.putExtra("serviceType", activeServiceData.getServiceType());
                startActivity(intent);
            }
        });

        return layout;
    }

    private void getDataFromDatabase() {
        petTrainersActiveServicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Pet trainer")) {
                    addActiveServicesPetTrainerRelativeLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);
                    petTrainerServicesListView.setVisibility(View.VISIBLE);

                    activePetTrainersServicesList.clear();
                    for (DataSnapshot activeServiceSnapshot : snapshot.child("Pet trainer").getChildren()) {
                        ActiveServiceData activeServiceData = activeServiceSnapshot.getValue(ActiveServiceData.class);
                        activePetTrainersServicesList.add(activeServiceData);
                    }

                    activeServicesCustomAdapter = new ActiveServicesCustomAdapter(getApplicationContext(),
                            R.layout.active_service,
                            activePetTrainersServicesList);
                    activeServicesCustomAdapter.notifyDataSetChanged();
                    refresh();
                    petTrainerServicesListView.setAdapter(activeServicesCustomAdapter);
                } else {
                    petTrainerServicesListView.setVisibility(View.GONE);
                    addActiveServicesPetTrainerRelativeLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });

        roles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(loggedUserId).hasChild("user")) {
                    addActiveServicesPetTrainerRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    addActiveServicesPetTrainerRelativeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void refresh() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                activeServicesCustomAdapter.notifyDataSetChanged();
                petTrainerServicesListView.invalidate();
            }
        });
    }
}