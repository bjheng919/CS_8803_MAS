package com.example.mit.placeholder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TimePicker;

import android.widget.TextView;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.widget.DatePicker;

import java.util.Calendar;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FillSurvey extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener{
    UserSurvey survey = new UserSurvey();

    Spinner etNation;
    ArrayAdapter<String> adapter1;
    RadioGroup rgRmType;
    RadioGroup rgRmmtNum;

    Spinner etGender;
    ArrayAdapter<String> adapter2;

    EditText etRentLow;
    EditText etRentHigh;
    EditText preProperty;
    EditText etTimeFrom;
    EditText etTimeTo;
    EditText etDiscription;

    private Context context;
    public static int year, month, day;
    public static int disturb_hour, disturb_minute;
    private StringBuffer date, time;


    RadioGroup rgSameNation;
    RadioGroup rgSameGender;
    RadioGroup rgRoommateOne;
//    RadioGroup rgRoommateThree;
//    RadioGroup rgRoommateFive;
    RadioGroup rgSmoke;
    RadioGroup rgMindSmoke;
    RadioGroup rgPet;
    RadioGroup rgMindPet;
    RadioGroup rgUseRoom;
    RadioGroup rgCook;
    RadioGroup rgOtherCook;


    Button btnBack1;
    Button btnBack2;
    Button btnBack3;
    Button btnBack4;
    Button btnBack5;
    Button btnBack6;
    Button btnBack7;
    Button btnNext1;
    Button btnNext2;
    Button btnNext3;
    Button btnNext4;
    Button btnNext5;
    Button btnNext6;
    Button btnFinish;

    private TextView startDate, startTime;
    private TextView endDate, endTime;

    private LinearLayout sDate, sTime;
    private LinearLayout eDate, eTime;

    GroupProfile currGroup;

    FirebaseAuth refAuth;
    FirebaseDatabase refDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate1();
    }

    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Update Cancelled", Toast.LENGTH_LONG).show();
        finish();
    }

    protected void onCreate1() {
        setContentView(R.layout.activity_basic_info_one);

        final String[] Nation = {"Chinese", "American", "Japanese", "Indian", "Korean"};
        final String str;

        etNation = (Spinner) findViewById(R.id.etNation);
        adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,Nation);
        //设置下拉列表风格
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etNation.setAdapter(adapter1);
        etNation.setVisibility(View.VISIBLE);//设置默认显示

        rgSameNation = (RadioGroup) findViewById(R.id.surveySameNation);
        btnBack1 = (Button) findViewById(R.id.surveyback);
        btnNext1 = (Button) findViewById(R.id.surveynext);

        refAuth = FirebaseAuth.getInstance();
        refDatabase = FirebaseDatabase.getInstance();

        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final String nation = (String) etNation.getSelectedItem();
                    survey.setNation(nation);
                    String prefNation = getSelectedRadioButtonText(rgSameNation);
                    if (prefNation.equals("From the same country")) {
                        survey.setPrefNation("Yes");
                    } else if (prefNation.equals("From the different countries")) {
                        survey.setPrefNation("No");
                    } else {
                        survey.setPrefNation("Don't mind");
                    }

                    onCreate2();
                }

        });

        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Update Cancelled",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    protected void onCreate2() {
        setContentView(R.layout.activity_basic_info_two);
        final String[] genderArray = {"Male", "Female", "Others"};
        final String gender;

        etGender = (Spinner) findViewById(R.id.etGender);
        adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,genderArray);
        //设置下拉列表风格
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etGender.setAdapter(adapter2);
        etGender.setVisibility(View.VISIBLE);//设置默认显示
        gender = (String) etGender.getSelectedItem();

        rgSameGender = (RadioGroup) findViewById(R.id.surveySameGender);
        btnBack2 = (Button) findViewById(R.id.surveyback);
        btnNext2 = (Button) findViewById(R.id.surveynext);

        refAuth = FirebaseAuth.getInstance();
        refDatabase = FirebaseDatabase.getInstance();

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                survey.setGender(gender);
                String prefGender = getSelectedRadioButtonText(rgSameGender);
                if (prefGender.equals("I don't mind")) {
                    survey.setPrefGender("Don't mind");
                } else {
                    survey.setPrefGender(prefGender);
                }

                onCreate3();
            }
        });

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreate1();
            }
        });
    }

    protected void onCreate3() {
        setContentView(R.layout.activity_room_choice_one);
        context = this;
        date = new StringBuffer();
        time = new StringBuffer();
        initView();
        initDateTime();

        rgRmType = (RadioGroup) findViewById(R.id.sroomtype);
        etRentLow = (EditText) findViewById(R.id.surveyRentLow);
        etRentHigh = (EditText) findViewById(R.id.surveyRentHigh);
        btnBack3 = (Button) findViewById(R.id.surveyback);
        btnNext3 = (Button) findViewById(R.id.surveynext);

        refAuth = FirebaseAuth.getInstance();
        refDatabase = FirebaseDatabase.getInstance();

        btnNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRentLow.getText().toString().trim().length() < 1) {
                    etRentLow.setError("Please enter the correct minimum rent");
                } else if (etRentHigh.getText().toString().trim().length() < 1) {
                    etRentHigh.setError("Please enter the correct maximum rent");
                } else {
                    String rmType = getSelectedRadioButtonText(rgRmType);
                    if (rmType.equals("I don't mind")) {
                        survey.setRmType("Don't mind");
                    } else {
                        survey.setRmType(rmType);
                    }

                    survey.setLsStartTime(startDate.getText().toString());
                    survey.setLsEndTime(endDate.getText().toString());
                    survey.setRentLow(etRentLow.getText().toString().trim());
                    survey.setRentHigh(etRentHigh.getText().toString().trim());

                    onCreate4();
                }
            }

        });

        btnBack3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onCreate2();
        }
        });
    }

    private String getSelectedRadioButtonText(RadioGroup rg) {
        return ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
    }

    private void initView() {
        sDate = (LinearLayout) findViewById(R.id.start_date);
        startDate = (TextView) findViewById(R.id.surveyLsStartTime);
        eDate = (LinearLayout) findViewById(R.id.end_date);
        endDate = (TextView) findViewById(R.id.surveyLsEndTime);
        sDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        startDate.setText(date.append(String.valueOf(year)).append(".").append(String.valueOf(month)).append(".").append(day));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

                dialog.setTitle("Set date");
                dialog.setView(dialogView);
                dialog.show();

                //初始化日期监听事件
                datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        FillSurvey.year = year;
                        FillSurvey.month = monthOfYear+1;
                        FillSurvey.day = dayOfMonth;
                    }
                }) ;
            }
        });
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        endDate.setText(date.append(String.valueOf(year)).append(".").append(String.valueOf(month)).append(".").append(day));
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker2 = (DatePicker) dialogView2.findViewById(R.id.datePicker);

                dialog2.setTitle("Set date");
                dialog2.setView(dialogView2);
                dialog2.show();
                //初始化日期监听事件
                datePicker2.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        FillSurvey.year = year;
                        FillSurvey.month = monthOfYear+1;
                        FillSurvey.day = dayOfMonth;
                    }
                });
            }
        });
    }

    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.surveyLsStartTime:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        startDate.setText(date.append(String.valueOf(year)).append("Year").append(String.valueOf(month)).append("Month").append(day).append("Day"));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

                dialog.setTitle("Set date");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(year, month, day, this);
                break;
            case R.id.surveyLsEndTime:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        endDate.setText(date.append(String.valueOf(year)).append("Year").append(String.valueOf(month)).append("Month").append(day).append("Day"));
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(context, R.layout.dialog_date, null);
                final DatePicker datePicker2 = (DatePicker) dialogView2.findViewById(R.id.datePicker);

                dialog2.setTitle("Set date");
                dialog2.setView(dialogView2);
                dialog2.show();
                //初始化日期监听事件
                datePicker2.init(year, month, day, this);
                break;
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    protected void onCreate4() {
        setContentView(R.layout.activity_room_choice_two);

        rgRoommateOne = (RadioGroup) findViewById(R.id.surveyroommateone);
        preProperty = (EditText) findViewById(R.id.property);

        btnBack4 = (Button) findViewById(R.id.surveyback);
        btnNext4 = (Button) findViewById(R.id.surveynext);

        refAuth = FirebaseAuth.getInstance();
        refDatabase = FirebaseDatabase.getInstance();

        btnNext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                survey.setRmmtNum(getSelectedRadioButtonText(rgRoommateOne));
                onCreate5();
            }
        });

        btnBack4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onCreate3();
            }
        });
    }

    protected void onCreate5() {
        setContentView(R.layout.activity_life_style_one);

        rgSmoke = (RadioGroup) findViewById(R.id.surveysmoke);
        rgMindSmoke = (RadioGroup) findViewById(R.id.surveyothersmoke);
        rgPet = (RadioGroup) findViewById(R.id.surveypet);
        rgMindPet = (RadioGroup) findViewById(R.id.othersurveypet);

        btnBack5 = (Button) findViewById(R.id.surveyback);
        btnNext5 = (Button) findViewById(R.id.surveynext);

        refAuth = FirebaseAuth.getInstance();
        refDatabase = FirebaseDatabase.getInstance();

        btnNext5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                survey.setSmoke(getSelectedRadioButtonText(rgSmoke));
                survey.setMindSmoke(getSelectedRadioButtonText(rgMindSmoke));
                survey.setPet(getSelectedRadioButtonText(rgPet));
                survey.setMindPet(getSelectedRadioButtonText(rgMindPet));

                onCreate6();
            }
        });

        btnBack5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreate4();
            }
        });
    }

    protected void onCreate6() {
        setContentView(R.layout.activity_life_style_two);

        rgUseRoom = (RadioGroup) findViewById(R.id.surveyuseroom);
        rgCook = (RadioGroup) findViewById(R.id.surveycook);
        rgOtherCook = (RadioGroup) findViewById(R.id.othercook);

        btnBack6 = (Button) findViewById(R.id.surveyback);
        btnNext6 = (Button) findViewById(R.id.surveynext);

        refAuth = FirebaseAuth.getInstance();
        refDatabase = FirebaseDatabase.getInstance();

        btnNext6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                UserSurvey survey = new UserSurvey();

                survey.setUseRoom(getSelectedRadioButtonText(rgUseRoom));
                survey.setCook(getSelectedRadioButtonText(rgCook));
                survey.setOtherCook(getSelectedRadioButtonText(rgOtherCook));

                onCreate7();
            }
        });

        btnBack6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreate5();
            }
        });
    }

    protected void onCreate7() {
        setContentView(R.layout.activity_life_style_three);
        context = this;
        time = new StringBuffer();
        initView2();
        initDateTime2();

        etDiscription = (EditText) findViewById(R.id.resdiscription);

        btnBack7 = (Button) findViewById(R.id.surveyback);
        btnFinish = (Button) findViewById(R.id.surveynext);

        refAuth = FirebaseAuth.getInstance();
        refDatabase = FirebaseDatabase.getInstance();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                UserSurvey survey = new UserSurvey();

                survey.setTimeFrom(startTime.getText().toString());
                survey.setTimeTo(endTime.getText().toString());
                survey.setDiscription(etDiscription.getText().toString());
                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                if(getIntent().getExtras() != null)
                    currGroup = (GroupProfile) getIntent().getExtras().getSerializable("chatwith");

                final DatabaseReference pushid;
                if(currGroup == null)
                    pushid = refDatabase.getReference().child("surveys").child(CreateProfile.myuuid);
                else
                    pushid = refDatabase.getReference().child("groupSurveys").child(currGroup.getUuid());

                pushid.setValue(survey).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Survey created successfully", Toast.LENGTH_LONG).show();
                    }
                });
                Intent i;
                if(currGroup == null)
                    i = new Intent(getApplicationContext(), newMessages.class);
                else{
                    i = new Intent(getApplicationContext(), GroupSetting.class);
                    i.putExtra("chatwith",currGroup);
                }
                finish();
                startActivity(i);
            }
        });

        btnBack7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreate6();
            }
        });
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.disturb_hour = hourOfDay;
        this.disturb_minute = minute;
    }

    private void initView2() {

        sTime = (LinearLayout) findViewById(R.id.start_time);
        startTime = (TextView) findViewById(R.id.timeFrom);
        eTime = (LinearLayout) findViewById(R.id.end_time);
        endTime = (TextView) findViewById(R.id.timeTo);

        sTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (time.length() > 0) { //清除上次记录的日期
                            time.delete(0, time.length());
                        }
                        startTime.setText(time.append(String.valueOf(disturb_hour)).append(":").append(String.valueOf(disturb_minute)));
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
                TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
                timePicker.setCurrentHour(disturb_hour);
                timePicker.setCurrentMinute(disturb_minute);
                timePicker.setIs24HourView(true); //设置24小时制
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        FillSurvey.disturb_hour = hourOfDay;
                        FillSurvey.disturb_minute = minute;
                    }
                });
                dialog2.setTitle("Set time");
                dialog2.setView(dialogView2);
                dialog2.show();
            }
        });
        eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (time.length() > 0) { //清除上次记录的日期
                            time.delete(0, time.length());
                        }
                        endTime.setText(time.append(String.valueOf(disturb_hour)).append(":").append(String.valueOf(disturb_minute)));
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
                TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
                timePicker.setCurrentHour(disturb_hour);
                timePicker.setCurrentMinute(disturb_minute);
                timePicker.setIs24HourView(true); //设置24小时制
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        FillSurvey.disturb_hour = hourOfDay;
                        FillSurvey.disturb_minute = minute;
                    }
                });
                dialog2.setTitle("Set time");
                dialog2.setView(dialogView2);
                dialog2.show();
            }
        });
    }

    /**
     * 获取当前的日期和时间
     */
    private void initDateTime2() {
        Calendar calendar = Calendar.getInstance();

        disturb_hour = calendar.get(Calendar.HOUR);
        disturb_minute = calendar.get(Calendar.MINUTE);

    }





}
