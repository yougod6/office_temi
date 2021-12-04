package com.example.temisdk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.temisdk.temi.CustomTtsListener;
import com.example.temisdk.temi.RoboTemi;
import com.example.temisdk.temi.RoboTemiListeners;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import org.jetbrains.annotations.NotNull;

public class Main1Activity extends AppCompatActivity implements
        Robot.TtsListener,
        Robot.ConversationViewAttachesListener,
        OnRobotReadyListener {

    TextView tv1;
    RoboTemiListeners listeners;
    Robot robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.textView1);

        // Listener 객체추가, this 추가
        robot = Robot.getInstance();
        robot.addTtsListener(this);
        robot.addConversationViewAttachesListenerListener(this);
        // Listener 재사용
        robot.addTtsListener(new CustomTtsListener());
        robot.speak(TtsRequest.create("Hello Temi!",true));



        /* Listener 몰아넣기 예시
        RoboTemiListeners listeners = new RoboTemiListeners();
        listeners.init();
        */

        RoboTemi roboTemi = new RoboTemi();//조종 관련 코드 정리 클래스 인스턴스 생성
        listeners = new RoboTemiListeners();
        listeners.init();//리스너 추가 예시


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


    public void openExample(View v){
        listeners.stop();// 예제 리스너 추가를 위해서 Main activity의 적용된 리스너 모두 제거
        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
        //intent.putExtra("data", "data");
        startActivity(intent);
        //startActivityForResult(intent,0);

    }

    @Override
    public void onTtsStatusChanged(@NotNull TtsRequest ttsRequest) {
        tv1.setText("테미가 말하고 있습니다.");//View 간섭 예제
        Log.i("Temi","Tts Status changed! "+ttsRequest.getStatus()+" - Original");
    }

    @Override
    public void onConversationAttaches(boolean b) {
        tv1.setText("테미가 대화중입니다.");//View 간섭 예제2
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

}
