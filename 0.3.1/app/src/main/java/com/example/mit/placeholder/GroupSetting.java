package com.example.mit.placeholder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GroupSetting extends AppCompatActivity {

    Button btn_ChangeInfo;
    Button btn_ChangeSurvey;
    Button btn_Commit;
    Button btn_Delete;
    Button btn_Quit;
    TextView tvMembers;
    GroupProfile currGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_setting);

        btn_ChangeInfo = (Button) this.findViewById(R.id.change_group_info);
        btn_ChangeSurvey = (Button) this.findViewById(R.id.change_group_info);
        btn_Commit = (Button) this.findViewById(R.id.group_commit);
        btn_Delete = (Button) this.findViewById(R.id.group_delete);
        btn_Quit = (Button) this.findViewById(R.id.group_quit);
        tvMembers = (TextView) this.findViewById(R.id.listmembers);
        currGroup = (GroupProfile) getIntent().getExtras().getSerializable("chatwith");
        StringBuilder sb = new StringBuilder();
        List<String> memlist = currGroup.getMembers();
        if(memlist != null){
            boolean first = true;
            for(String mem:memlist){
                if(!first) sb.append(", ");
                sb.append(mem);
                first = false;
            }
            tvMembers.setText(sb.toString());
        }


        btn_ChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNameDialog();
            }
        });

        btn_Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitGroup();
            }
        });


    }

    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),GroupChat.class);
        i.putExtra("chatwith",currGroup);
        finish();
        startActivity(i);
    }


    public void quitGroup(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("You sure you want to quit this group?");

        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //quit the group here(modify grouplist and groups)
                final DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference().child("GroupList").child(CreateProfile.myuuid);
                mref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List currGroupUuidList = (List<String>)dataSnapshot.getValue();
                        currGroupUuidList.remove(currGroup.getUuid());
                        mref1.setValue(currGroupUuidList).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final DatabaseReference mref2 = FirebaseDatabase.getInstance().getReference().child("groups").child(currGroup.getUuid());
                                currGroup.getMembers().remove(CreateProfile.myname);
                                mref2.setValue(currGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Quit success",Toast.LENGTH_LONG).show();
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
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    public void changeNameDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.alertedt);
        edt.setHint("Enter the new group name here");
        dialogBuilder.setTitle("Rename This Group");

        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                currGroup.setGroupName(edt.getText().toString());
                final DatabaseReference mref0 = FirebaseDatabase.getInstance().getReference().child("groups").child(currGroup.getUuid());
                mref0.setValue(currGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Rename success",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
