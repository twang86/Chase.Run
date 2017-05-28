package run.quest.twang.questrun.loadingscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import run.quest.twang.questrun.R;
import run.quest.twang.questrun.databases.DBHelper;
import run.quest.twang.questrun.homescreen.HomeActivity;
import run.quest.twang.questrun.character.LonelyJogger;
import run.quest.twang.questrun.quest.JogQuestsHolder;

public class LoadingActivity extends AppCompatActivity {
    DBHelper sData;
    LonelyJogger sJogger;
    JogQuestsHolder sQuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        sData = DBHelper.getInstance(this);
        sQuests = JogQuestsHolder.getInstance();
        sJogger = LonelyJogger.getInstance();
        sJogger.setValuesFromSharedPref(this);

        if(sData.isQuestTableEmpty()) {
            sQuests.generateNewRandomQuests(3);
            for (int i = 0; i < sQuests.getQuests().size(); i++) {
                sData.addQuestToDb(sQuests.getQuests().get(i));
            }
        } else {
            sQuests.replaceAllQuests(sData.getAllQuests());
        }

        //sQuests.generateNewRandomQuests(3);

        Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
