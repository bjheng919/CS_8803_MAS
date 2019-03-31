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

public class GroupSetting extends AppCompatActivity {

    Button btn_ChangeInfo;
    Button btn_ChangeSurvey;
    Button btn_Commit;
    Button btn_Delete;
    Button btn_Quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_setting);

        btn_ChangeInfo = (Button) this.findViewById(R.id.change_group_info);
        btn_ChangeSurvey = (Button) this.findViewById(R.id.change_group_info);
        btn_Commit = (Button) this.findViewById(R.id.group_commit);
        btn_Delete = (Button) this.findViewById(R.id.group_delete);
        btn_Quit = (Button) this.findViewById(R.id.group_quit);


    }

    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),newMessages.class);
        finish();
        startActivity(i);

        return;
    }
}
