package run.quest.twang.questrun.jogging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import run.quest.twang.questrun.R;
import run.quest.twang.questrun.animations.ViewHelper;
import run.quest.twang.questrun.character.LonelyJogger;
import run.quest.twang.questrun.quest.JogQuestsHolder;
import run.quest.twang.questrun.quest.Quest;

public class JoggingActivity extends AppCompatActivity {
    JogQuestRecyclerViewAdapter mAdapter;
    BroadcastReceiver mReceiver;
    JogQuestsHolder sQuests;
    List<Quest> mVisibleQuests;
    LonelyJogger sJogger;
    List<View> mQuestViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogging);

        sJogger = LonelyJogger.getInstance();
        sQuests = JogQuestsHolder.getInstance();
        mVisibleQuests = new ArrayList<>();
        mQuestViews = new ArrayList<>();

        for (int i = 0; i < sQuests.getUserQuests().size(); i++) {
            mVisibleQuests.add(sQuests.getUserQuests().get(i));
            mQuestViews.add(sQuests.getUserQuests().get(i).makeView(this));
        }

        mAdapter = new JogQuestRecyclerViewAdapter(mQuestViews);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.jogging_screen_recycler);
        LinearLayoutManager linearLayoutMan = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutMan);
        recyclerView.setAdapter(mAdapter);

//        SnapHelper helper = new LinearSnapHelper();
//        helper.attachToRecyclerView(recyclerView);

        Intent serviceIntent = new Intent(this, JogService.class);
        startService(serviceIntent);

        findViewById(R.id.jogging_stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRun();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                for (int i = 0; i < mVisibleQuests.size(); i++) {
                    mVisibleQuests.get(i).updateView(mQuestViews.get(i));
                }
                TextView speedText = (TextView) findViewById(R.id.jogging_speed_display);
                TextView speedUnits = (TextView) findViewById(R.id.jogging_speed_units_display);
                TextView distanceText = (TextView) findViewById(R.id.jogging_distance_display);
                TextView distanceUnits = (TextView) findViewById(R.id.jogging_distance_units_display);
                TextView timeText = (TextView) findViewById(R.id.jogging_time_display);
                TextView timeUnits = (TextView) findViewById(R.id.jogging_time_units_display);

                speedText.setText(ViewHelper.showSpeed(sJogger.getSpeed(), false));
                speedUnits.setText(ViewHelper.showSpeedUnits(true));

                distanceText.setText(ViewHelper.showDistance(sJogger.getDistance(), false));
                distanceUnits.setText(ViewHelper.showDistanceUnits(sJogger.getDistance(), true));

                timeText.setText(ViewHelper.showMillis(sJogger.getRunTime(), false));
                timeUnits.setText(ViewHelper.showTimeUnits(true));

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(LonelyJogger.RUN_CAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        stopRun();
    }

    public void stopRun(){

        AlertDialog.Builder builder = new AlertDialog.Builder(JoggingActivity.this);
        builder.setTitle("Stop Run")
                .setMessage("Really stop running?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopService(new Intent(JoggingActivity.this, JogService.class));
                        startActivity(new Intent(JoggingActivity.this, JogSummaryActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
