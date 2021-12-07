package com.example.temisdk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.temisdk.temi.RoboTemi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

public class MainActivity extends AppCompatActivity implements

        OnRobotReadyListener,
        View.OnClickListener {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button prob;
    Robot robot;

    final RoboTemi roboTemi = new RoboTemi();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        prob = findViewById(R.id.prob);
        robot = Robot.getInstance();

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        prob.setOnClickListener(this);


        firebaseDatabase.getReference("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object myText = snapshot.getValue();
                if (myText.toString().equals("start")) {
                    Intent intent = new Intent(getApplicationContext(), Prob.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Fail to read value.", error.toException());
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        robot.addOnRobotReadyListener(this);


    }

    @Override
    protected void onStop() {
        super.onStop();
        robot.removeOnRobotReadyListener(this);

    }


    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Class exampleContext = null;
        switch (view.getId()) {
            case R.id.button1:
                exampleContext = MainActivity1.class;
                break;
            case R.id.button2:
                exampleContext = MainActivity2.class;
                break;
            case R.id.button3:
                exampleContext = MainActivity3.class;
                break;
            case R.id.button4:
                exampleContext = MainActivity4.class;
                break;
            case R.id.prob:
                exampleContext = Prob.class;

        }
        Intent intent = new Intent(getApplicationContext(),exampleContext);
        startActivity(intent);
    }


    }


