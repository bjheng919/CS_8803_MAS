package com.example.vinayak.hw07;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
        for(int i=0; i<memlist.size(); i++){
            if(i!=0) sb.append(", ");
            sb.append(memlist.get(i));
        }
        tvMembers.setText(sb.toString());

        btn_ChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNameDialog();
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



    //////////////////show dialog to create group
    public void createNameDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.alertedt);
        edt.setHint("Enter your group name here");
        dialogBuilder.setTitle("Create My Group");
        dialogBuilder.setMessage("You will create a group whose profile is based on your survey.");

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
