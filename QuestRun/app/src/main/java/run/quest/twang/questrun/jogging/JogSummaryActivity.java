package run.quest.twang.questrun.jogging;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import run.quest.twang.questrun.R;
import run.quest.twang.questrun.animations.ViewHelper;
import run.quest.twang.questrun.character.LonelyJogger;
import run.quest.twang.questrun.databases.DBHelper;
import run.quest.twang.questrun.quest.JogQuestsHolder;
import run.quest.twang.questrun.quest.Quest;
import run.quest.twang.questrun.quest.QuestChase;

public class JogSummaryActivity extends AppCompatActivity {
    private JogQuestsHolder sQuests;
    private LonelyJogger sJogger;
    private DBHelper sData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_summary);

        sQuests = JogQuestsHolder.getInstance();
        sJogger = LonelyJogger.getInstance();
        sData = DBHelper.getInstance(this);

        TextView speedText = (TextView) findViewById(R.id.summary_speed_display);
        TextView speedUnits = (TextView) findViewById(R.id.summary_speed_units_display);
        TextView distanceText = (TextView) findViewById(R.id.summary_distance_display);
        TextView distanceUnits = (TextView) findViewById(R.id.summary_distance_units_display);
        TextView timeText = (TextView) findViewById(R.id.summary_time_display);
        TextView timeUnits = (TextView) findViewById(R.id.summary_time_units_display);
        TextView stepText = (TextView) findViewById(R.id.summary_steps_display);

        speedText.setText(ViewHelper.showSpeed(sJogger.getSpeed(), false));
        speedUnits.setText(ViewHelper.showSpeedUnits(true));

        distanceText.setText(ViewHelper.showDistance(sJogger.getDistance(), false));
        distanceUnits.setText(ViewHelper.showDistanceUnits(sJogger.getDistance(), true));

        timeText.setText(ViewHelper.showMillis(sJogger.getRunTime(), false));
        timeUnits.setText(ViewHelper.showTimeUnits(true));

        stepText.setText(String.valueOf(sJogger.getStepsTaken()));

        sJogger.storeRun();
        sJogger.saveToSharedPref(this);
        sJogger.stopRun();

        List<Quest> successQuestsList = new ArrayList<>();

        //a quadratic loop that checks if any user created quests match
        //any inactive, generated quests. All generated chase quests are inactive
        for (int i = 0; i < sQuests.getUserQuests().size(); i++) {
            QuestChase currentChase = sQuests.getUserQuests().get(i);
            if(currentChase.getStatus()== Quest.STATUS_SUCCESS){
                //if this quest is successful, check if it matches any inactive quests that can
                //use it
                boolean foundOne = false;//checks if we found a match
                for (int j = 0; j < sQuests.getQuests().size(); j++) {
                    Quest currentRegular = sQuests.getQuests().get(j);
                    if (currentChase.equals(currentRegular)&&currentRegular.getStatus()!=Quest.STATUS_SUCCESS) {
                        System.out.println("matched user quests");
                        //found one, lets set that one's status to success
                        //we shouldn't add this quest to successful quest list because
                        //it will be added when we check the main quest list
                        currentRegular.setStatus(Quest.STATUS_SUCCESS);
                        foundOne = true;
                        break;
                    }
                }
                if (!foundOne){//if we didn't, add to success quest list to show later
                    System.out.println("adding user quests");
                    successQuestsList.add(currentChase);
                }
            }
        }

        // ok done with user quests. let's clear these quests
        sQuests.clearUserQuests();

        //new variable to track how many main quests we finished

        int totalFinished = 0;

        //loops through the quest array and check the status of all the quests
        for (int i = 0; i < sQuests.getQuests().size(); i++) {
            Quest current = sQuests.getQuests().get(i);

            //This quest ended in success
            if (current.getStatus() == Quest.STATUS_SUCCESS) {
               //add to total
                totalFinished++;
                //set this quest to finished so it won't ever run
                current.setStatus(Quest.STATUS_FINISHED);
                //add to the list
                successQuestsList.add(current);
            }else if (current.getStatus() == Quest.STATUS_FAILED) {
                //if quest failed, usually doesn't happen unless this is a timed quest or chase quest
                current.setStatus(Quest.STATUS_ACTIVE);
                //no worries, we set it back to active so people can try it again
            }else if (current.getStatus() == Quest.STATUS_FINISHED){
                totalFinished++;
            }
        }



        if(totalFinished >= sQuests.getQuests().size()){
            //this means all our quests are finished, we are ready to level up and get new quests
            sJogger.levelUp(1);
            sJogger.saveToSharedPref(this);
            sQuests.generateNewRandomQuests(4);
            AlertDialog.Builder builder = new AlertDialog.Builder(JogSummaryActivity.this);
            builder.setTitle("Level up!")
                    .setMessage("You are now at level "+ sJogger.getLevel())
                    .setPositiveButton("Yay!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        LinearLayout successQuestHolder = (LinearLayout) findViewById(R.id.summary_quest_holder);
        successQuestHolder.removeAllViews();

        for (int i = 0; i < successQuestsList.size(); i++) {
            LayoutInflater inflater = getLayoutInflater();
            View questContainer = inflater.inflate(R.layout.view_holder_quest_selector_holder, null);
            ((TextView)questContainer.findViewById(R.id.quest_holder_text)).setText(
                    successQuestsList.get(i).getDescription()
            );
            successQuestHolder.addView(questContainer);
        }

        sData.resetQuestTable();

        for (int i = 0; i < sQuests.getQuests().size(); i++) {
                sData.addQuestToDb(sQuests.getQuests().get(i));
        }

        findViewById(R.id.summary_ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
