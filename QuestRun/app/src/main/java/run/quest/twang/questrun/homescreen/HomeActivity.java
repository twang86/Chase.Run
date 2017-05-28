package run.quest.twang.questrun.homescreen;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import run.quest.twang.questrun.R;
import run.quest.twang.questrun.character.CharacterActivity;
import run.quest.twang.questrun.databases.DBHelper;
import run.quest.twang.questrun.monsterselector.MonsterSelectionActivity;
import run.quest.twang.questrun.quest.JogQuestsHolder;
import run.quest.twang.questrun.quest.Quest;
import run.quest.twang.questrun.character.LonelyJogger;
import run.quest.twang.questrun.jogging.JoggingActivity;
import run.quest.twang.questrun.quest.QuestChase;

public class HomeActivity extends AppCompatActivity {
    List<TextView> mQuestDisplayArray;
    LinearLayout mQuestHolder;
    LinearLayout mMonsterCage;
    JogQuestsHolder sQuests;
    LonelyJogger sJogger;
    DBHelper sData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sData = DBHelper.getInstance(this);
        sJogger = LonelyJogger.getInstance();
        if (sJogger.isRunning()) {
            startRun();
        }
        mQuestHolder = (LinearLayout) findViewById(R.id.home_quest_item_holder);
        mMonsterCage = (LinearLayout) findViewById(R.id.home_monster_cage);
        sQuests = JogQuestsHolder.getInstance();
        mQuestDisplayArray = new ArrayList<>();

        findViewById(R.id.home_quest_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout questContainer = (LinearLayout) findViewById(R.id.home_quest_container);
                ImageView picView = (ImageView) v.findViewById(R.id.home_quest_button_pic);
                TextView textView = (TextView) v.findViewById(R.id.home_quest_button_text);
                if (questContainer.getVisibility() == View.GONE) {
                    picView.setImageResource(R.drawable.ic_close_black);
                    textView.setVisibility(View.GONE);
                    questContainer.setVisibility(View.VISIBLE);
                } else {
                    picView.setImageResource(R.drawable.ic_exclaim);
                    textView.setVisibility(View.VISIBLE);
                    questContainer.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.home_pick_monster_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MonsterSelectionActivity.class));
            }
        });

        findViewById(R.id.home_run_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRun();
            }
        });

        findViewById(R.id.home_character_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CharacterActivity.class));
            }
        });
    }

    public void startRun() {
        Intent intent = new Intent(HomeActivity.this, JoggingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQuestDisplayArray.clear();
        mQuestHolder.removeAllViews();
        mMonsterCage.removeAllViews();

        for (int i = 0; i < sQuests.getQuests().size(); i++) {
            LayoutInflater inflater = getLayoutInflater();
            View questContainer = inflater.inflate(R.layout.view_holder_quest_selector_holder, null);
            mQuestDisplayArray.add((TextView) questContainer.findViewById(R.id.quest_holder_text));
            mQuestHolder.addView(questContainer);
        }
        for (int i = 0; i < mQuestDisplayArray.size(); i++) {
            switch (sQuests.getQuests().get(i).getStatus()) {
                case Quest.STATUS_ACTIVE:
                    if (sQuests.getQuests().get(i).getOngoing() == Quest.IS_ONGOING) {
                        mQuestDisplayArray.get(i).setText(sQuests.getQuests().get(i).getDescription() + " (In Progress)");
                    } else {
                        mQuestDisplayArray.get(i).setText(sQuests.getQuests().get(i).getDescription());
                    }
                    break;
                case Quest.STATUS_FINISHED:
                    mQuestDisplayArray.get(i).setText(sQuests.getQuests().get(i).getDescription() + " (Completed)");
                    mQuestDisplayArray.get(i).setTextColor(ContextCompat.getColor(this, R.color.green));
                    break;
                default:
                    mQuestDisplayArray.get(i).setText(sQuests.getQuests().get(i).getDescription());
                    break;
            }
        }
        for (int i = 0; i < sQuests.getUserQuests().size(); i++) {
            Quest current = sQuests.getUserQuests().get(i);
            if (current instanceof QuestChase) {
                ImageView currentMonsterPic = new ImageView(this);
                currentMonsterPic.setImageResource(((QuestChase) current).getMonster().getType().getAnimation());
                mMonsterCage.addView(currentMonsterPic);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sData.resetQuestTable();

        for (int i = 0; i < sQuests.getQuests().size(); i++) {
            sData.addQuestToDb(sQuests.getQuests().get(i));
        }

        sJogger.saveToSharedPref(this);
    }
}
