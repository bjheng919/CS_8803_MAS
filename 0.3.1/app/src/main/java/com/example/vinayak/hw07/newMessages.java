package com.example.vinayak.hw07;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;


public class newMessages extends AppCompatActivity implements View.OnClickListener {
    private TextView topBar;
    private TextView tabDeal;
    private TextView tabPoi;
    private TextView tabUser;
    private DrawerLayout mDlMain;
    private Button mBtnOpenLeftDrawer;
    private Toolbar toolbar;
    private FrameLayout ly_content;

    private OnlyRecommendation f1;
    private OnlyGroups f2;
    private OnlySetting f3;
    private FragmentManager fragmentManager;

    private boolean[] filterValues;
    FloatingActionButton fab;///////////////////fab

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        filterValues = new boolean[5];
        for (boolean filterValue : filterValues) { filterValue = false; }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView();
        tabDeal.performClick();

        /////////////fab below
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroupDialog();
            }
        });
        //////////////fab end

    }

    //UI组件初始化与事件绑定
    private void bindView() {
        tabDeal = (TextView)this.findViewById(R.id.txt_deal);
        tabPoi = (TextView)this.findViewById(R.id.txt_poi);
        tabUser = (TextView)this.findViewById(R.id.txt_user);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);
        mBtnOpenLeftDrawer = (Button) findViewById(R.id.btn_open_left_drawer);
        mDlMain = (DrawerLayout) findViewById(R.id.dl_main);
        mDlMain.setScrimColor(Color.TRANSPARENT);
        mBtnOpenLeftDrawer.setOnClickListener(this);
        tabDeal.setOnClickListener(this);
        tabUser.setOnClickListener(this);
        tabPoi.setOnClickListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);////////////////fab

    }

    //重置所有文本的选中状态
    public void selected(){
        tabDeal.setSelected(false);
        tabPoi.setSelected(false);
        tabUser.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }
        if(f3!=null){
            transaction.hide(f3);
        }

    }

    public void onCBClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.commitedCB:
                if (checked) {
                    filterValues[0] = true;
                } else {
                    filterValues[0] = false;
                }
                break;
            case R.id.nationCB:
                if (checked) {
                    filterValues[1] = true;
                } else {
                    filterValues[1] = false;
                }
                break;
            case R.id.genderCB:
                if (checked) {
                    filterValues[2] = true;
                } else {
                    filterValues[2] = false;
                }
                break;
            case R.id.lifestyleCB:
                if (checked) {
                    filterValues[3] = true;
                } else {
                    filterValues[3] = false;
                }
                break;
            case R.id.rmTypeCB:
                if (checked) {
                    filterValues[4] = true;
                } else {
                    filterValues[4] = false;
                }
                break;
        }
        tabDeal.performClick();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(v.getId()){

            case R.id.txt_deal:
                hideAllFragment(transaction);
                selected();
                tabDeal.setSelected(true);
                mBtnOpenLeftDrawer.setVisibility(View.VISIBLE);
                f1 = new OnlyRecommendation();
                f1.setFilterValues(filterValues);
                transaction.add(R.id.fragment_container,f1);
                fab.setVisibility(View.VISIBLE);
                break;

            case R.id.txt_poi:
                hideAllFragment(transaction);
                selected();
                tabPoi.setSelected(true);
                mBtnOpenLeftDrawer.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                if(f2==null){
                    f2 = new OnlyGroups();
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;

            case R.id.txt_user:
                hideAllFragment(transaction);
                selected();
                tabUser.setSelected(true);
                mBtnOpenLeftDrawer.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                if(f3==null){
                    f3 = new OnlySetting();
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;

            case R.id.btn_open_left_drawer:
                mDlMain.openDrawer(Gravity.LEFT);
                break;



        }

        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.signout) {
            FirebaseAuth.getInstance().signOut();

            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            finish();
            startActivity(i);
        } else if (id == R.id.survey) {
            Intent i = new Intent(getApplicationContext(),FillSurvey.class);
            finish();
            startActivity(i);
        } else if(id == R.id.editprofile) {
            Intent i = new Intent(getApplicationContext(),EditProfile.class);
            finish();
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    //////////////////show dialog to create group
    public void createGroupDialog() {
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
                final DatabaseReference mref0 = FirebaseDatabase.getInstance().getReference().child("surveys").child(CreateProfile.myuuid);
                mref0.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final UserSurvey currSurvey = dataSnapshot.getValue(UserSurvey.class);
                        final DatabaseReference pushid = FirebaseDatabase.getInstance().getReference().child("groups").push();
                        final String groupuuid = pushid.getKey();
                        GroupProfile groupProfile = new GroupProfile();
                        groupProfile.setCommitNum("0");
                        groupProfile.setTotalNum(currSurvey.getRmmtNum());
                        groupProfile.setCreatorUuid(CreateProfile.myuuid);
                        groupProfile.setGroupName(edt.getText().toString());
                        groupProfile.setUuid(groupuuid);
                        pushid.setValue(groupProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference().child("GroupList").child(CreateProfile.myuuid).child("groupUuidList");
                                mref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String currGroupUuidList = dataSnapshot.getValue().toString();
                                        mref1.setValue(currGroupUuidList+","+groupuuid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                final DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference().child("groupSurveys").child(groupuuid);
                                                mref1.setValue(currSurvey).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(),"Group Created",Toast.LENGTH_LONG).show();

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

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });



                //do something with edt.getText().toString();
//                final DatabaseReference pushid = FirebaseDatabase.getInstance().getReference().child("groups").push();
//                String groupuuid = pushid.getKey();
//                GroupProfile groupProfile = new GroupProfile();
//                groupProfile.setCommitNum("0");
//                groupProfile.setTotalNum();
//                groupProfile.setCreatorUuid(CreateProfile.myuuid);
//                groupProfile.setGroupName(edt.getText().toString());
//                groupProfile.setUuid(groupuuid);
//                pushid.setValue(groupProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        final DatabaseReference glref = FirebaseDatabase.getInstance().getReference().child("GroupList").child(CreateProfile.myuuid).child("groupUuidList");
//                        glref.setValue(edt.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(getApplicationContext(),"Group Created",Toast.LENGTH_LONG).show();
//
//                                Intent i = new Intent(getApplicationContext(),newMessages.class);
//                                finish();
//                                startActivity(i);
//                            }
//                        });
//
//                    }
//                });
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