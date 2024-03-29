package com.pet.shelter.friends.fragments.services;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
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

public class ActiveServicesVeterinariansTabFragment extends Fragment {
    private DatabaseReference veterinariansActiveServicesReference, roles;
    private String loggedUserId;
    private SearchBar searchBar;
    private SearchView searchView;
    private ListView veterinarianServicesListView;
    private RelativeLayout addActiveServicesVeterinarianRelativeLayout;
    private MaterialTextView materialTextView, nothingFound;

    private final ArrayList<ActiveServiceData> activeVeterinariansServicesList = new ArrayList<>();
    private final ArrayList<ActiveServiceData> originalActiveVeterinariansServicesList = new ArrayList<>();
    private ArrayList<ActiveServiceData> textSearchedActiveVeterinariansServicesList = new ArrayList<>();

    private ActiveServicesCustomAdapter activeServicesCustomAdapter;

    public ActiveServicesVeterinariansTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_active_services_veterinarians_tab, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        roles = firebaseDatabase.getReference("roles");
        veterinariansActiveServicesReference = firebaseDatabase.getReference("activeServices");
        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        searchBar = layout.findViewById(R.id.activeServicesVeterinarians_searchBar);
        searchView = layout.findViewById(R.id.activeServicesVeterinarians_searchView);
        nothingFound = layout.findViewById(R.id.activeServicesVeterinariansNothingFound_materialTextView);
        veterinarianServicesListView = layout.findViewById(R.id.activeServicesVeterinarians_listView);
        addActiveServicesVeterinarianRelativeLayout = layout.findViewById(R.id.activeServicesAddVeterinarians_relativeLayout);
        ExtendedFloatingActionButton addServiceFloatingActionButton = layout.findViewById(R.id.activeServicesAddVeterinarians_floatingActionButton);
        materialTextView = layout.findViewById(R.id.activeServicesVeterinariansNoServices_materialTextView);


        getDataFromDatabase();

        addServiceFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddServiceActivity.class));
                requireActivity().finish();
            }
        });

        searchBar.setHint("Search by provider name");
        searchView.setupWithSearchBar(searchBar);
        searchView.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchBar.setText(searchView.getText());
                filterListBySearchedName();
                searchView.hide();
                return false;
            }
        });

        veterinarianServicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        veterinariansActiveServicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Veterinarian")) {
                    addActiveServicesVeterinarianRelativeLayout.setVisibility(View.VISIBLE);
                    veterinarianServicesListView.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);

                    originalActiveVeterinariansServicesList.clear();
                    activeVeterinariansServicesList.clear();
                    for (DataSnapshot activeServiceSnapshot : snapshot.child("Veterinarian").getChildren()) {
                        ActiveServiceData activeServiceData = activeServiceSnapshot.getValue(ActiveServiceData.class);
                        activeVeterinariansServicesList.add(activeServiceData);
                        originalActiveVeterinariansServicesList.add(activeServiceData);
                    }

                    activeServicesCustomAdapter = new ActiveServicesCustomAdapter(getApplicationContext(),
                            R.layout.active_service,
                            activeVeterinariansServicesList);
                    activeServicesCustomAdapter.notifyDataSetChanged();
                    refresh();
                    veterinarianServicesListView.setAdapter(activeServicesCustomAdapter);
                } else {
                    veterinarianServicesListView.setVisibility(View.GONE);
                    addActiveServicesVeterinarianRelativeLayout.setVisibility(View.VISIBLE);
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
                    addActiveServicesVeterinarianRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    addActiveServicesVeterinarianRelativeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterListBySearchedName() {
        ArrayList<ActiveServiceData> filteredList = new ArrayList<>();
        String searchBarText = String.valueOf(searchBar.getText());
        if (searchBarText.isEmpty()) {
            activeServicesCustomAdapter.clear();
            activeServicesCustomAdapter.addAll(originalActiveVeterinariansServicesList);
            activeServicesCustomAdapter.notifyDataSetChanged();
            veterinarianServicesListView.setAdapter(activeServicesCustomAdapter);
            veterinarianServicesListView.setVisibility(View.VISIBLE);
            nothingFound.setVisibility(View.GONE);
        } else {
            for (ActiveServiceData activeServiceData : activeVeterinariansServicesList) {
                if (activeServiceData.getName().toLowerCase().contains(searchBarText.toLowerCase())) {
                    filteredList.add(activeServiceData);
                    nothingFound.setVisibility(View.GONE);
                } else {
                    nothingFound.setVisibility(View.VISIBLE);
                }
            }

            activeServicesCustomAdapter.clear();
            activeServicesCustomAdapter.addAll(filteredList);
            activeServicesCustomAdapter.notifyDataSetChanged();
            textSearchedActiveVeterinariansServicesList.clear();
            textSearchedActiveVeterinariansServicesList = filteredList;
            refresh();
        }
    }


    public void refresh() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                activeServicesCustomAdapter.notifyDataSetChanged();
                veterinarianServicesListView.invalidate();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}