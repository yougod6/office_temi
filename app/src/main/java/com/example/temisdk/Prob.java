package com.example.temisdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.temisdk.temi.RoboTemi;
import com.example.temisdk.temi.TemiList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prob extends AppCompatActivity implements
        OnGoToLocationStatusChangedListener, OnDistanceToLocationChangedListener,
        Robot.ConversationViewAttachesListener
{
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    Button prob_start;
    Button prob_restart;
    TextView prob_stop_txt;
    Robot robot;
    static final String TEMI1 = "00120485035";
    static final String TEMI2 = "00120474994";
    static final String TEMI3 = "00120485020";
    static final Float WORKING = 1000F;
    static final Float WORKABLE = 10F;
    public String id;
    public String loc = "starting";
    public String Dist1;
    public Float dist;
    public List<String> map_list;
    public int index;
    public Map<String, Float> temi_list = new HashMap<>();
    public Float minVal;
    boolean b =false;
    boolean c1 = false;
    boolean c2 = false;
    boolean c3 = false;

    public TemiList temiList = new TemiList();
    final RoboTemi roboTemi = new RoboTemi();
    // for sharing variables with other classes
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // my layout
        setContentView(R.layout.prob);
        context=this;
        prob_start = findViewById(R.id.prob_start);
        //list of workable Temi
        temi_list.put(TEMI1,WORKABLE);
        temi_list.put(TEMI2,WORKABLE);
        temi_list.put(TEMI3,WORKABLE);

        robot = Robot.getInstance();
        id=robot.getSerialNumber();
        map_list = robot.getLocations();

        if(id.equals(TEMI1))
            roboTemi.goTo("prob11");
        else if(id.equals(TEMI2))
            roboTemi.goTo("prob31");
        else if(id.equals(TEMI3))
            roboTemi.goTo("prob31");


        firebaseDatabase.getReference("location").addValueEventListener(new ValueEventListener() {
            //@SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object myText = snapshot.getValue();
                // o = Objects.requireNonNull(myText);
                if(myText!=null){
                    if ("point1".equals(myText.toString())) {
                        index = map_list.indexOf("point1");
                        loc="point1";
                        b=true;
                    } else if ("point2".equals(myText.toString())) {
                        index = map_list.indexOf("point2");
                        loc="point2";
                        b=true;

                    } else if ("point3".equals(myText.toString())) {
                        index = map_list.indexOf("point3");
                        loc="point3";
                        b=true;

                    } else if ("point4".equals(myText.toString())) {
                        index = map_list.indexOf("point4");
                        loc="point4";
                        b=true;
                    }
                    else if("stop".equals(myText.toString())){
                        roboTemi.stopMovement();
                        finish();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Fail to read value.", error.toException());
            }
        });

        firebaseDatabase.getReference("distance1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(temi_list.get(id)<WORKING){
                    Float distance = Float.parseFloat(snapshot.getValue().toString());
                    temiList.setDistacne1(distance);
                    c1=true;
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Fail to read value.", error.toException());
            }
        });

        firebaseDatabase.getReference("distance2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(temi_list.get(id)<WORKING){
                    Float distance = Float.parseFloat(snapshot.getValue().toString());
                    temiList.setDistacne2(distance);
                    c2=true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Fail to read value.", error.toException());
            }
        });

        firebaseDatabase.getReference("distance3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(temi_list.get(id)<WORKING){
                    Float distance = Float.parseFloat(snapshot.getValue().toString());
                    temiList.setDistacne3(distance);
                    c3=true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Fail to read value.", error.toException());
            }
        });

        temiList=writeTemiList(loc,WORKABLE,WORKABLE,WORKABLE);

    }
    private TemiList writeTemiList(String location, Float distance1, Float distance2, Float distance3) {
        // TemiList 생성
        TemiList temiList = new TemiList(location, distance1, distance2,distance3);

        databaseReference.setValue(temiList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(Prob.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(Prob.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    return temiList;
    }

    private void readTemiList(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(TemiList.class) != null){
                    temiList = dataSnapshot.getValue(TemiList.class);

                    Log.w("FireBaseData", "getData" + temiList.toString());
                } else {
                    Toast.makeText(Prob.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Listener 객체추가, this 추가
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addConversationViewAttachesListenerListener(this);
        robot.addOnDistanceToLocationChangedListener(this);
        // robot.addOnCurrentPositionChangedListener(this);
        // robot.addOnDistanceToLocationChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        robot.removeOnGoToLocationStatusChangedListener(this);
        robot.removeConversationViewAttachesListenerListener(this);
        robot.removeOnDistanceToLocationChangedListener(this);
        // robot.removeOnCurrentPositionChangedListener(this);
        // robot.removeOnDistanceToLocationChangedListener(this);
    }

    // Patrol area
    @Override
    public void onGoToLocationStatusChanged(@NonNull String s, @NonNull String s1, int i, @NonNull String s2) {
        if(s1.equals(OnGoToLocationStatusChangedListener.COMPLETE)) {
            switch (s) {
                //Temi1 prob
                case "prob11":
                    roboTemi.goTo("prob12");
                    break;
                case "prob12":
                    roboTemi.goTo("prob13");
                    break;
                case "prob13":
                    roboTemi.goTo("prob14");
                    break;
                case "prob14":
                    roboTemi.goTo("prob11");
                    break;
                //Temi2 prob
                case "prob21":
                    roboTemi.goTo("prob22");
                    break;
                case "prob22":
                    roboTemi.goTo("prob23");
                    break;
                case "prob23":
                    roboTemi.goTo("prob24");
                    break;
                case "prob24":
                    roboTemi.goTo("prob21");
                    break;
                // Temi3 prob
                case "prob31":
                    roboTemi.goTo("prob32");
                    break;
                case "prob32":
                    roboTemi.goTo("prob33");
                    break;
                case "prob33":
                    roboTemi.goTo("prob34");
                    break;
                case "prob34":
                    roboTemi.goTo("prob31");
                    break;
             /* case "point1":
                  Intent intent = new Intent(this,MainActivity3.class);
                  startActivity(intent);*/
            }
        }
        else if(s1.equals(OnGoToLocationStatusChangedListener.ABORT))
        {
            setContentView(R.layout.prob_stop);
            prob_restart = findViewById(R.id.prob_restart);
            prob_stop_txt = findViewById(R.id.prob_stop_txt);
            prob_stop_txt.setText(id);
            prob_restart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.prob);
                    roboTemi.goTo(s);
                }
            });
        }
    }

    public void onConversationAttaches(boolean b) {

        // 대화가 시작되었을 경우
        // goTo 멈추기 -> default ?
        // 다시 순찰을 하려면 ?
        //  사람이 버튼을 누르게 할까 ?
    }


    @Override

    public void onDistanceToLocationChanged(@NonNull Map<String, Float> map) {
        /*Log.d("Dist1", String.valueOf(databaseReference.child("distance1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Dist1=task.getResult().getValue().toString();
                }
            }
        })));

         */

        //action is changed and temi is workable
        if(b && temi_list.get(id)!=WORKING) {
            b = false;
            loc = map_list.get(index);
            dist = map.get(loc);
            if (dist != null) {
                temi_list.put(id, dist);
                if (id.equals(TEMI1)) {
                    databaseReference.child("distance1").setValue(dist);
                } else if (id.equals(TEMI2)) {
                    databaseReference.child("distance2").setValue(dist);
                } else if (id.equals(TEMI3)) {
                    databaseReference.child("distance3").setValue(dist);
                }
            }

            Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {

                    public void run() {
                        Float Dist1 = temiList.getDistance1();
                        Float Dist2 = temiList.getDistance2();
                        Float Dist3 = temiList.getDistance3();
                        temi_list.put(TEMI1,Dist1);
                        temi_list.put(TEMI2,Dist2);
                        temi_list.put(TEMI3,Dist3);
                        Log.d("Dist1", String.valueOf(Dist1));
                        Log.d("Dist2", String.valueOf(Dist2));
                        Log.d("Dist3", String.valueOf(Dist3));
                        minVal = Collections.min(temi_list.values());
                        Log.d("equal?", String.valueOf(minVal.equals(temi_list.get(id)) + "     " + String.valueOf(minVal) + "          " + String.valueOf(id)));
                        if (minVal.equals(temi_list.get(id))) {
                            Log.d("min", "I'm in min.getkey == equal");
                            //start MainActivity3
                            temiList.setLocation(loc);
                            Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                            startActivity(intent);
                        }
                        else {
                            Log.d("alksjdla", "Siibal");
                        }// 시간 지난 후 실행할 코딩
                    }
                    }, 4000); // 3초후


                // distance 1, 2, 3 all changes
                //if(c1 && c2 && c3){

               /*
                Float dist1 = Float.parseFloat(Dist1.toString());
                Float dist2 = Float.parseFloat(Dist2.toString());
                Float dist3 = Float.parseFloat(Dist3.toString());
                */


                /*
                temi_list.put(TEMI1,dist1);
                temi_list.put(TEMI2,dist2);
                temi_list.put(TEMI3,dist3);
                c1 = false;
                c2 = false;
                c3 = false;
                Log.d("TEMI 1", String.valueOf(temi_list.get(TEMI1)));
                Log.d("TEMI 2", String.valueOf(temi_list.get(TEMI2)));
                Log.d("TEMI 3", String.valueOf(temi_list.get(TEMI3)));

                //find key of min val
                minVal = Collections.min(temi_list.values());

                Log.d("equal?", String.valueOf(minVal.equals(temi_list.get(id)) + "     " + String.valueOf(minVal) + "          " + String.valueOf(id)));
                if (minVal.equals(temi_list.get(id))) {
                    Log.d("min", "I'm in min.getkey == equal");
                    //start MainActivity3
                    Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                    startActivity(intent);
                }
                else {
                    Log.d("alksjdla", "Siibal");
                }

                */
            }





           // }
        } // end of onDistanceToLocationChanged

    };
