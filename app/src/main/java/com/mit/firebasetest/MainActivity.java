package com.mit.firebasetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //below is writing to database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();





    }


    private void postSurvey(String name, String nation, String rmSize, String gender){
        DatabaseReference cur = myRef.child("Surveys").push();
        cur.child("name").setValue(name);
        cur.child("nation").setValue(nation);
        cur.child("rmSize").setValue(rmSize);
        cur.child("gender").setValue(gender);
    }
}
