package com.example.vinayak.hw07;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileDetails extends AppCompatActivity {

    GroupProfile groupProfile;
    UserSurvey groupSurvey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        TextView tvGroupName = (TextView) findViewById(R.id.profDetailTVGroupName);
        TextView tvNation =(TextView) findViewById(R.id.profDetailTVNation);
        TextView tvSameNation =(TextView) findViewById(R.id.profDetailTVSameNation);
        TextView tvRmType =(TextView) findViewById(R.id.profDetailTVRmType);
        TextView tvLsStartTime =(TextView) findViewById(R.id.profDetailTVLsStartTime);
        TextView tvLsEndTime =(TextView) findViewById(R.id.profDetailTVLsEndTime);
        TextView tvRentLow =(TextView) findViewById(R.id.profDetailTVRentLow);
        TextView tvRentHigh =(TextView) findViewById(R.id.profDetailTVRentHigh);
        TextView tvCook =(TextView) findViewById(R.id.profDetailTVCook);
        TextView tvParty =(TextView) findViewById(R.id.profDetailTVParty);
        TextView tvSmoke =(TextView) findViewById(R.id.profDetailTVSmoke);
        TextView tvPet =(TextView) findViewById(R.id.profDetailTVPet);

        groupProfile = new GroupProfile();
        groupProfile = (GroupProfile) getIntent().getExtras().getSerializable("groupProfile");

        groupSurvey = new UserSurvey();
        groupSurvey = (UserSurvey) getIntent().getExtras().getSerializable("groupSurvey");

        tvGroupName.setText(groupProfile.getGroupName());
        tvNation.setText(groupSurvey.getNation());
        tvSameNation.setText(groupSurvey.getSameNation());
        tvRmType.setText(groupSurvey.getRmType());
        tvLsStartTime.setText(groupSurvey.getLsStartTime());
        tvLsEndTime.setText(groupSurvey.getLsEndTime());
        tvRentLow.setText(groupSurvey.getRentLow());
        tvRentHigh.setText(groupSurvey.getRentHigh());
        tvCook.setText(groupSurvey.getCook());
        tvParty.setText(groupSurvey.getParty());
        tvSmoke.setText(groupSurvey.getSmoke());
        tvPet.setText(groupSurvey.getPet());

        /* Original code for setting the picture in user profile page, but now there is no picture for group profile
        if(groupProfile.getImage() != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://fir-test-ff77a.appspot.com/");
            StorageReference storageref = storageReference.child(groupProfile.getImage());

            storageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Picasso.with(getApplicationContext()).load(uri).into(ivimage);
                }
            });
        } else {
            ivimage.setImageResource(R.mipmap.noimage);
        }
        */

        findViewById(R.id.profDetailBtnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),newMessages.class);
                finish();
                startActivity(i);
            }
        });

        Button btnJoinGroup = (Button) findViewById(R.id.profDetailBtnJoinGroup);
        /*
        btnmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),MyChat.class);
                i.putExtra("chatwith",usertemp);
                finish();
                startActivity(i);

            }
        });
        */
    }
}
