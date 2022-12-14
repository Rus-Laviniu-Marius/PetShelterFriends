package com.pet.shelter.friends.adoption.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pet.shelter.friends.R;

import java.util.Objects;

public class CompleteFamilyAndHouseholdInformationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference adoptionFormReference;
    private TextView adultsNumberTextView, childrenNumberTextView, homeTypeTextView,
            homeDescriptionTextView;
    private EditText adultsNumberEditText, childrenNumberEditText, homeTypeEditText,
            homeDescriptionEditText, rentingRulesRegardingPetOwnership;
    private RadioButton knownAllergy, notKnownAllergy, familyAgrees, familyDoesNotAgree,
        adequateLoveAndAttention, notAdequateLoveAndAttention;
    private ImageView backButton, nextButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_family_and_household_information);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        adoptionFormReference = firebaseDatabase.getReference("adoptionFormReference");

        adultsNumberTextView = findViewById(R.id.familyAndHousingAdultsNumber_textView);
        childrenNumberTextView = findViewById(R.id.familyAndHousingChildrenNumber_textView);
        homeTypeTextView = findViewById(R.id.familyAndHousingHomeType_textView);
        homeDescriptionTextView = findViewById(R.id.familyAndHousingHomeDescription_textView);

        adultsNumberEditText = findViewById(R.id.familyAndHousingAdultsNumber_editText);
        childrenNumberEditText = findViewById(R.id.familyAndHousingChildrenNumber_editText);
        homeTypeEditText = findViewById(R.id.familyAndHousingHomeType_editText);
        homeDescriptionEditText = findViewById(R.id.familyAndHousingHomeDescription_editText);
        rentingRulesRegardingPetOwnership = findViewById(R.id.rentingRulesRegardingPetOwnership_editText);

        knownAllergy = findViewById(R.id.knownAllergyYes_radioButton);
        notKnownAllergy = findViewById(R.id.knownAllergyNo_radioButton);
        familyAgrees = findViewById(R.id.agreementDecisionYes_radioButton);
        familyDoesNotAgree = findViewById(R.id.agreementDecisionNo_radioButton);
        adequateLoveAndAttention = findViewById(R.id.adequateLoveAndAttentionYes_radioButton);
        notAdequateLoveAndAttention = findViewById(R.id.adequateLoveAndAttentionNo_radioButton);

        backButton = findViewById(R.id.completeFamilyAndHousingInformationBack_button);
        nextButton = findViewById(R.id.completeFamilyAndHousingInformationNext_button);
        submitButton = findViewById(R.id.completeFamilyAndHousingInformationSubmit_button);

        setOnFocusChangeListeners();
        setOnClickListeners();
    }

    private void setOnFocusChangeListeners() {
        adultsNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                adultsNumberEditText.setHint("");
                adultsNumberTextView.setVisibility(View.VISIBLE);
            }
        });
        childrenNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                childrenNumberEditText.setHint("");
                childrenNumberTextView.setVisibility(View.VISIBLE);
            }
        });
        homeTypeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                homeTypeEditText.setHint("");
                homeTypeTextView.setVisibility(View.VISIBLE);
            }
        });
        homeDescriptionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                homeDescriptionEditText.setHint("");
                homeDescriptionTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setOnClickListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToDatabase();
                sendToNextActivity();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteFamilyAndHouseholdInformationActivity.this, CompleteContactInformationActivity.class);
                startActivity(intent);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToNextActivity();
            }
        });
    }

    private void writeToDatabase() {

        String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("adultsNumber").setValue(adultsNumberEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("childrenNumber").setValue(childrenNumberEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("homeType").setValue(homeTypeEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("homeDescription").setValue(homeDescriptionEditText.getText().toString());
        adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("rentingRulesRegardingPetOwnership").setValue(rentingRulesRegardingPetOwnership.getText().toString());

        if (knownAllergy.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("knownAllergy").setValue("yes");
        }
        if (notKnownAllergy.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("knownAllergy").setValue("no");
        }

        if (familyAgrees.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("familyAgreement").setValue("yes");
        }
        if (familyDoesNotAgree.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("familyAgreement").setValue("no");
        }

        if (adequateLoveAndAttention.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("provideAdequateLoveAndAttention").setValue("yes");
        }
        if (notAdequateLoveAndAttention.isChecked()) {
            adoptionFormReference.child(currentFirebaseUserUid).child("familyAndHousehold").child("provideAdequateLoveAndAttention").setValue("no");
        }

    }

    private void sendToNextActivity() {
        Intent intent = new Intent(CompleteFamilyAndHouseholdInformationActivity.this, CompleteOtherPetsInformationActivity.class);
        startActivity(intent);
    }
}