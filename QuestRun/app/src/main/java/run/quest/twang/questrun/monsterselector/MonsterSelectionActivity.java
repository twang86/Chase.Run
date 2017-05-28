package run.quest.twang.questrun.monsterselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import run.quest.twang.questrun.R;
import run.quest.twang.questrun.monsters.Monster;
import run.quest.twang.questrun.monsters.MonsterTypes;
import run.quest.twang.questrun.quest.JogQuestsHolder;
import run.quest.twang.questrun.quest.Quest;
import run.quest.twang.questrun.quest.QuestChase;

public class MonsterSelectionActivity extends AppCompatActivity implements MonsterRecyclerAdapter.MonsterSelectorCallback{

    private MonsterQuestRecyclerAdapter mQuestAdapter;
    private JogQuestsHolder sQuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_selection);

        sQuests = JogQuestsHolder.getInstance();

        RecyclerView monsterRecycler = (RecyclerView) findViewById(R.id.monster_selector_recycler);
        MonsterRecyclerAdapter adapter = new MonsterRecyclerAdapter(MonsterTypes.values(), this);
        monsterRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        monsterRecycler.setAdapter(adapter);


        RecyclerView questRecycler = (RecyclerView) findViewById(R.id.monster_quest_recycler);
        questRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mQuestAdapter = new MonsterQuestRecyclerAdapter(sQuests.getUserQuests());
        questRecycler.setAdapter(mQuestAdapter);

        findViewById(R.id.monster_ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void typeSelected(MonsterTypes type) {
        JogQuestsHolder sQuests = JogQuestsHolder.getInstance();
        if (sQuests.getUserQuests().size()<5){
            sQuests.addToUserQuest(
                    new QuestChase(System.currentTimeMillis(),
                            new Monster(type, type.getSpeed()*Quest.MONSTER_TOO_CLOSE),
                            (long) type.getStamina(),
                            type.getHealth(),
                            Quest.STATUS_ACTIVE,
                            Quest.IS_USER_MADE));

            mQuestAdapter.notifyItemInserted(sQuests.getUserQuests().size()-1);

        } else {
            Toast.makeText(this, "Too many monsters!!", Toast.LENGTH_SHORT).show();
        }
    }
}
