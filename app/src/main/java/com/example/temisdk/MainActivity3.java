package com.example.temisdk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.temisdk.temi.RoboTemi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;


public class MainActivity3 extends AppCompatActivity implements
        OnGoToLocationStatusChangedListener {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    Button gotoPosition1;
    Button gotoPosition2;
    Button gotoPosition3;
    Button buttonFollow;
    Button buttonBack;
    TextView actionText;
    Robot robot;
    static final String TEMI1 = "00120485035";
    static final String TEMI2 = "00120474994";
    static final String TEMI3 = "00120485020";


    final RoboTemi roboTemi = new RoboTemi();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // my layout
        setContentView(R.layout.activity_my);

        gotoPosition1 = findViewById(R.id.goto_position1);
        gotoPosition2 = findViewById(R.id.goto_position2);
        gotoPosition3 = findViewById(R.id.goto_position3);
        buttonFollow = findViewById(R.id.buttonFollow);
        buttonBack = findViewById(R.id.buttonBack);
        actionText= (TextView)findViewById(R.id.action1);



        robot = Robot.getInstance();
        firebaseDatabase.getReference("action").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object myText = snapshot.getValue();

                if(myText!=null){
                    if ("point1".equals(myText.toString())) {
                        roboTemi.goTo("point1");
                    } else if ("point2".equals(myText.toString())) {
                        roboTemi.goTo("point2");

                    } else if ("point3".equals(myText.toString())) {
                        roboTemi.goTo("point3");

                    } else if ("point4".equals(myText.toString())) {
                        roboTemi.goTo("point4");
                    }
                    else if("start".equals(myText.toString())){
                        setContentView(R.layout.prob);
                        finish();
                        Intent intent = new Intent(getApplicationContext(),Prob.class);
                        startActivity(intent);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Fail to read value.", error.toException());
            }
        });

        gotoPosition1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roboTemi.goTo("point1");
            }
        });

        gotoPosition2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roboTemi.goTo("point2");
            }
        });

        gotoPosition3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roboTemi.goTo("point3");
            }
        });

        buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonFollow.getText().equals("따라가기")) {
                    roboTemi.followMe();
                    buttonFollow.setText("중지");
                } else if (buttonFollow.getText().equals("중지")) {
                    roboTemi.stopMovement();
                    buttonFollow.setText("따라가기");
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Listener 객체추가, this 추가
        robot.addOnGoToLocationStatusChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        robot.removeOnGoToLocationStatusChangedListener(this);
    }


    @Override
    public void onGoToLocationStatusChanged(@NonNull String s, @NonNull String s1, int i, @NonNull String s2) {

        if(s1.equals(OnGoToLocationStatusChangedListener.COMPLETE)) {
            roboTemi.speak(s+"에 도착했습니다");
            actionText.setText(s);
            databaseReference.child("action").setValue("start");
        }
    }


    }

