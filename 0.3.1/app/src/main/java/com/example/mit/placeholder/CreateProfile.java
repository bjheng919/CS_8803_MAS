package com.example.mit.placeholder;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.UUID;

public class CreateProfile extends AppCompatActivity {

    ImageView ibgallery;
    Button btncreate;
    EditText etfname;
    EditText etlname;
    EditText etemail;

    String imgurl=null;
    static String myuuid = null;
    static String myname = null;
    boolean hasProfile = false;
    boolean hasSurvey = false;

    FirebaseAuth refAuth;
    FirebaseDatabase refDatabase;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        // ivimage = (ImageView) findViewById(R.id.cpivimage);
        ibgallery = (ImageView) findViewById(R.id.cpibgallery);
        btncreate = (Button) findViewById(R.id.cpbtncreate);
        etfname = (EditText) findViewById(R.id.cpetfname);
        etlname = (EditText) findViewById(R.id.cpetlname);
        etemail = (EditText) findViewById(R.id.cpetemail);

        refAuth = FirebaseAuth.getInstance();
        refDatabase = FirebaseDatabase.getInstance();

        storageref = storage.getReferenceFromUrl("gs://fir-test-ff77a.appspot.com/");

        DatabaseReference mrefcheckprofile = FirebaseDatabase.getInstance().getReference().child("users");

        mrefcheckprofile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserProfile tempprof = postSnapshot.getValue(UserProfile.class);
                    if(tempprof.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        hasProfile = true;
                        myuuid = tempprof.getUuid();
                        myname = tempprof.getFname();
                        break;
                    }
                }

                if(hasProfile) { // current user has a UserProfile in database

                    DatabaseReference mrefcheckprofile = FirebaseDatabase.getInstance().getReference().child("surveys");

                    mrefcheckprofile.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                if (myuuid.equals(postSnapshot.getKey())) {
                                    hasSurvey = true;
                                    break;
                                }
                            }

                            if (hasProfile && hasSurvey) {
                                Intent i = new Intent(getApplication(), newMessages.class);
                                finish();
                                startActivity(i);
                            } else if (hasProfile) {
                                Intent i = new Intent(getApplication(), FillSurvey.class);
                                finish();
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        etemail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        etemail.setEnabled(false);

        ibgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etfname.getText().toString().trim().length()<1) {
                    etfname.setError("Please enter the correct First name");
                } else if(etlname.getText().toString().trim().length()<1) {
                    etlname.setError("Please enter the correct Last name");
                } else {
                    final UserProfile profile = new UserProfile();

                    profile.setFname(etfname.getText().toString().trim());
                    profile.setLname(etlname.getText().toString().trim());
                    profile.setEmail(etemail.getText().toString().trim());
                    myname=etfname.getText().toString().trim();

                    final DatabaseReference pushid = refDatabase.getReference().child("users").push();
                    profile.setUuid(pushid.getKey());
                    myuuid=pushid.getKey();
                    profile.setImage(imgurl);

                    if(imgurl!=null) {
                        final StorageReference ImagesRef = storageref.child("profileImages/"+ UUID.randomUUID()+".jpg");

                        ImagesRef.putFile(Uri.parse(imgurl)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profile.setImage(ImagesRef.getPath());
                            pushid.setValue(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Profile created successfully",Toast.LENGTH_LONG).show();
                                }
                            });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        pushid.setValue(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Profile created successfully",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == 100) {
                    // Get the url from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // Set the image in ImageView
                        ibgallery.setImageURI(selectedImageUri);
                        imgurl = selectedImageUri.toString();
                    }
                }
            }
        } catch (Exception e) {
            Log.d("test",e.getMessage());
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
