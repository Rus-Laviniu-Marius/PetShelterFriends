package com.pet.shelter.friends.fragments.bottom_app_bar.pets.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pet.shelter.friends.R;

public class BottomAppBarPetsAbandonedTabFragment extends Fragment {

    public BottomAppBarPetsAbandonedTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_app_bar_pets_abandoned_tab, container, false);
    }
}