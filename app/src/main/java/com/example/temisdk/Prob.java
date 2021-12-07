package com.example.temisdk;

import android.content.Context;
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
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Prob extends AppCompatActivity implements
        OnGoToLocationStatusChangedListener, OnDistanceToLocationChangedListener,
        Robot.ConversationViewAttachesListener
        //onCurrentPositionChangedListeners
       // OnDistanceToLocationChangedListener
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
    static final Float ON = 100F;
    static final Float OFF= -100F;
    public String id;
    public String loc;
    public Float dist;
    public List<String> map_list;
    public int index;
    public Map<String, Float> temi_list = new HashMap<>();
    public static Context context;
    boolean b =false;
    boolean c1 =false;
    boolean c2 =false;
    boolean c3 =false;

    final RoboTemi roboTemi = new RoboTemi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // my layout
        setContentView(R.layout.prob);
        context=this;
        prob_start = findViewById(R.id.prob_start);
        //list of workable Temi
        temi_list.put(TEMI1,ON);
        temi_list.put(TEMI2,ON);
        temi_list.put(TEMI3,ON);

        robot = Robot.getInstance();
        id=robot.getSerialNumber();
        map_list = robot.getLocations();

        if(id.equals(TEMI1))
            roboTemi.goTo("prob21");
        else if(id.equals(TEMI2))
            roboTemi.goTo("prob22");
        else if(id.equals(TEMI3))
            roboTemi.goTo("prob23");



        firebaseDatabase.getReference("action").addValueEventListener(new ValueEventListener() {
            //@SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object myText = snapshot.getValue();
                // o = Objects.requireNonNull(myText);
                if(myText!=null){
                    if ("point1".equals(myText.toString())) {
                        index = map_list.indexOf("point1");
                        b=true;
                    } else if ("point2".equals(myText.toString())) {
                        index = map_list.indexOf("point2");
                        b=true;

                    } else if ("point3".equals(myText.toString())) {
                        index = map_list.indexOf("point3");
                        b=true;

                    } else if ("point4".equals(myText.toString())) {
                        index = map_list.indexOf("point4");
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
                    Float dist = Float.parseFloat(snapshot.getValue().toString());
                    temi_list.put(TEMI1,dist);
                    Log.d("c1", String.valueOf(dist));
                    c1=true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Fail to read value.", error.toException());
            }
        });

        firebaseDatabase.getReference("distance2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Float dist = Float.parseFloat(snapshot.getValue().toString());
                    temi_list.put(TEMI2,dist);
                    Log.d("c2", String.valueOf(dist));
                    c2=true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Fail to read value.", error.toException());
            }
        });

        firebaseDatabase.getReference("distance3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Float dist = Float.parseFloat(snapshot.getValue().toString());
                    temi_list.put(TEMI3,dist);
                    Log.d("c3", String.valueOf(dist));
                    c3=true;
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
        if(b){
            b=false;
            loc = map_list.get(index);
            dist = map.get(loc);
            if(dist != null)
                temi_list.put(id,dist);

            // Send distance to firebase
            if(id.equals(TEMI1)&&temi_list.get(TEMI1)>0){
                databaseReference.child("distance1").setValue(dist);
            }
            else if(id.equals(TEMI2)&&temi_list.get(TEMI2)>0){
                databaseReference.child("distance2").setValue(dist);
            }
            else if(id.equals(TEMI3)&&temi_list.get(TEMI3)>0){
                databaseReference.child("distance3").setValue(dist);
            }


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // distance 1, 2, 3 are changed
            if(c1 && c2 && c3){
                boolean a=true;
                Map.Entry<String, Float> min = null;
                Set<Entry<String, Float>> entrySet = temi_list.entrySet();
                for (Entry<String, Float> entry : entrySet) {
                    if(entry.getValue()>0){
                        // for don't refer to null val
                        if(a){
                            min=entry;
                            a=false;
                        }
                        // find min val'
                        Log.d("getValue", "entryValue"+String.valueOf(entry.getValue())+" < minVal"+String.valueOf(min.getValue()));
                        if (entry.getValue() < min.getValue()) {
                            min = entry;
                            Log.d("min", String.valueOf(min.getValue()));
                            Log.d("minKey", String.valueOf(min.getKey()));
                        }
                    }
                }

 //               c1 =false;
 //               c2 =false;
 //               c3 =false;

                Log.d("equal?", String.valueOf(min.getKey().equals(id))+"     "+String.valueOf(min.getKey())+"          "+String.valueOf(id));
                if(min.getKey().equals(id)){
                    Log.d("min", "I'm in min.getkey == equal");
                    temi_list.put(id,OFF);

                    /*if(id.equals(TEMI1)){
                        databaseReference.child("distance1").setValue(OFF);
                    }
                    else if(id.equals(TEMI2)){
                        databaseReference.child("distance2").setValue(OFF);
                    }
                    else if(id.equals(TEMI3)){
                        databaseReference.child("distance3").setValue(OFF);
                    }*/
                    Intent intent = new Intent(getApplicationContext(),MainActivity3.class);
                    startActivity(intent);
                    //finish();
                }
                else{Log.d("alksjdla","Siibal");}
            }

        }
        //System.out.println(min);


    };



    // public void onCurrentPositionChangedListeners()

  //  @Override
   /* public void onDistanceToLocationChanged(@NonNull Map<String, Float> map) {
       List<String> locations;
        locations = robot.getLocations();


        dist=map.get(loc);
        System.out.println(dist);
        if (dist != null && dist <= 1) {
            switch (loc) {
                case "prob1":
                    roboTemi.goTo("prob2");
                    break;
                case "prob2":
                    roboTemi.goTo("prob3");
                    break;
                case "prob3":
                    roboTemi.goTo("prob4");
                    break;
                case "prob4":
                    roboTemi.goTo("prob1");
                    break;
            }
        }

    }*/
}