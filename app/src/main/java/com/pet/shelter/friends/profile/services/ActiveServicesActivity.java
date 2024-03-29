package com.pet.shelter.friends.profile.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.search.SearchView;
import com.google.android.material.tabs.TabLayout;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.profile.ViewProfileActivity;

import java.util.Objects;

public class ActiveServicesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_services);

        tabLayout = findViewById(R.id.activeServices_tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Veterinarians"));
        tabLayout.addTab(tabLayout.newTab().setText("Pet trainers"));
        tabLayout.addTab(tabLayout.newTab().setText("Pet walkers"));

        MaterialToolbar materialToolbar = findViewById(R.id.activeServices_materialToolbar);

        viewPager2 = findViewById(R.id.activeServices_viewPager2);
        ActiveServicesViewPager2Adapter activeServicesViewPager2Adapter = new ActiveServicesViewPager2Adapter(this);
        viewPager2.setAdapter(activeServicesViewPager2Adapter);
        viewPager2.setSaveEnabled(false);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActiveServicesActivity.this, ViewProfileActivity.class));
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return true;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        Objects.requireNonNull(tabLayout.getTabAt(position)).select();
                }
                super.onPageSelected(position);
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