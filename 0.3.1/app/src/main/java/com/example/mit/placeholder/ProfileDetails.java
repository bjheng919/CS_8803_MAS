package com.example.mit.placeholder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileDetails extends AppCompatActivity {

    GroupProfile groupProfile;
    UserSurvey groupSurvey;
    GroupProfile currGroup;


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
        currGroup = (GroupProfile) getIntent().getExtras().getSerializable("groupProfile");

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

        Button btnJoinGroup = (Button) findViewById(R.id.profDetailBtnJoinGroup);

        btnJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroup();
            }
        });
    }

    public void joinGroup(){

        final DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference().child("GroupList").child(CreateProfile.myuuid);
        mref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List currGroupUuidList = (List<String>)dataSnapshot.getValue();
                if(currGroupUuidList == null) currGroupUuidList = new ArrayList<String>();
                currGroupUuidList.add(currGroup.getUuid());
                mref1.setValue(currGroupUuidList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        final DatabaseReference mref2 = FirebaseDatabase.getInstance().getReference().child("groups").child(currGroup.getUuid());
                        List<String> memlist = currGroup.getMembers();
                        if(memlist == null)
                            memlist = new ArrayList<String>();
                        memlist.add(CreateProfile.myname);
                        currGroup.setMembers(memlist);
                        mref2.setValue(currGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Join success",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(),newMessages.class);
                                finish();
                                startActivity(i);
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
