package com.example.vinayak.hw07;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FillSurvey extends AppCompatActivity {

    EditText etNation;
    RadioGroup rgRmType;
    RadioGroup rgRmmtNum;
    EditText etLsStartTime;
    EditText etLsEndTime;
    EditText etRentLow;
    EditText etRentHigh;
    RadioGroup rgSmoking;
    RadioGroup rgPet;
    RadioGroup rgCook;
    RadioGroup rgParty;
    RadioGroup rgSameNation;
    Button btnBack;
    Button btnUpdate;

    FirebaseAuth refAuth;
    FirebaseDatabase refDatabase;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageref;

    String uuid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        etNation = (EditText) findViewById(R.id.etNation);
        rgRmType = (RadioGroup) findViewById(R.id.surveyroomtype);
        rgRmmtNum = (RadioGroup) findViewById(R.id.surveyroommatenum);
        etLsStartTime = (EditText) findViewById(R.id.surveyLsStartTime);
        etLsEndTime = (EditText) findViewById(R.id.surveyLsEndTime);
        etRentLow = (EditText) findViewById(R.id.surveyRentLow);
        etRentHigh = (EditText) findViewById(R.id.surveyRentHigh);
        rgSmoking = (RadioGroup) findViewById(R.id.surveysmoking);
        rgPet = (RadioGroup) findViewById(R.id.surveypet);
        rgCook = (RadioGroup) findViewById(R.id.surveycooking);
        rgParty = (RadioGroup) findViewById(R.id.surveyparty);
        rgSameNation = (RadioGroup) findViewById(R.id.surveySameNation);
        btnBack = (Button) findViewById(R.id.surveyback);
        btnUpdate = (Button) findViewById(R.id.surveyupdate);

        refAuth = FirebaseAuth.getInstance();
        refDatabase = FirebaseDatabase.getInstance();

        storageref = storage.getReferenceFromUrl("gs://fir-test-ff77a.appspot.com/");


        DatabaseReference mrefcheckprofile = FirebaseDatabase.getInstance().getReference().child("users");

        mrefcheckprofile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserProfile tempProfile = postSnapshot.getValue(UserProfile.class);
                    if(tempProfile.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        uuid = tempProfile.getUuid();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSurvey survey = new UserSurvey();

                survey.setNation(etNation.getText().toString().trim());
                survey.setRmType(getSelectedRadioButtonText(rgRmType));
                survey.setRmmtNum(getSelectedRadioButtonText(rgRmmtNum));
                survey.setLsStartTime(etLsStartTime.getText().toString().trim());
                survey.setLsEndTime(etLsEndTime.getText().toString().trim());
                survey.setRentLow(etRentLow.getText().toString().trim());
                survey.setRentHigh(etRentHigh.getText().toString().trim());
                survey.setSmoke(getSelectedRadioButtonText(rgSmoking));
                survey.setPet(getSelectedRadioButtonText(rgPet));
                survey.setCook(getSelectedRadioButtonText(rgCook));
                survey.setParty(getSelectedRadioButtonText(rgParty));
                survey.setSameNation(getSelectedRadioButtonText(rgSameNation));

                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                final DatabaseReference pushid = refDatabase.getReference().child("surveys").child(uuid);
                pushid.setValue(survey).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Survey created successfully",Toast.LENGTH_LONG).show();
                    }
                });

                Intent i = new Intent(getApplicationContext(),Messages.class);
                finish();
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Messages.class);
                finish();
                startActivity(i);
            }
        });
    }

    private String getSelectedRadioButtonText(RadioGroup rg) {
        return ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
    }
}
