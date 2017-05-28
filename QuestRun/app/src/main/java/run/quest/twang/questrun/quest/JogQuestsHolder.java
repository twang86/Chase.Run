package run.quest.twang.questrun.quest;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import run.quest.twang.questrun.monsters.Monster;
import run.quest.twang.questrun.monsters.MonsterTypes;

/**
 * Created by Tim on 5/19/2017.
 */

public class JogQuestsHolder {
    private List<Quest> mQuests;
    private List<QuestChase> mUserQuests;
    private static JogQuestsHolder sInstance;

    private JogQuestsHolder(){
        mQuests = new ArrayList<>();
        mUserQuests = new ArrayList<>();
    }

    public static JogQuestsHolder getInstance(){
        if (sInstance == null){
            sInstance = new JogQuestsHolder();
        }
        return sInstance;
    }

    public List<QuestChase> getUserQuests() {
        return mUserQuests;
    }

    public void addToUserQuest(QuestChase quest){
        mUserQuests.add(quest);
    }

    public void clearUserQuests(){
        mUserQuests.clear();
    }

    public void addToMainQuestList(Quest quest){
        mQuests.add(quest);
    }

    public List<Quest> getQuests() {
        return mQuests;
    }

    public void replaceAllQuests(List<Quest> newQuestList){
        removeAllQuests();
        mQuests = newQuestList;
    }

    public void removeAllQuests(){
        mQuests.clear();
    }

    public void startAllQuests(Context context){
        for (int i = 0; i < mQuests.size(); i++) {
            mQuests.get(i).onRunStart(context);
        }
        for (int i = 0; i < mUserQuests.size(); i++) {
            mUserQuests.get(i).onRunStart(context);
        }
    }

    public void resetAllQuests(){
        for (int i = 0; i <mQuests.size(); i++) {
            mQuests.get(i).onRunEnd();
        }

        for (int i = 0; i <mUserQuests.size() ; i++) {
            mUserQuests.get(i).onRunEnd();
        }
    }

    public void updateAllQuests(long interval){
        for (int i = 0; i <mQuests.size(); i++) {
            mQuests.get(i).updateSelf(interval);
        }

        for (int i = 0; i <mUserQuests.size() ; i++) {
            mUserQuests.get(i).updateSelf(interval);
        }
    }

    public void generateNewRandomQuests(int amount){
        removeAllQuests();
        if (amount<=Quest.MAX_QUESTS_ALLOWED){
            for (int i = 0; i < amount; i++) {
                switch(i){
                    case 0:
                        mQuests.add(makeChaseQuest());
                        break;
                    case 1:
                        mQuests.add(makeDurationQuest());
                        break;
                    case 2:
                        mQuests.add(makeDistanceQuest());
                        break;
                    default:
                        if (Math.random()>.5){
                            mQuests.add(makeDistanceQuest());
                        }else{
                            mQuests.add(makeDurationQuest());
                        }
                }
            }
        }
    }

    public QuestChase makeChaseQuest(){
        MonsterTypes[] allMonsterTypes = MonsterTypes.values();
        int rand = (int) Math.floor(Math.random()*3);
        MonsterTypes randType = allMonsterTypes[rand];
        //randType = MonsterTypes.ZOMBIE;
        return new QuestChase(System.nanoTime(), new Monster(randType, randType.getSpeed()*Quest.MONSTER_TOO_CLOSE), (long) randType.getStamina(), randType.getHealth(), Quest.STATUS_INACTIVE, Quest.IS_ONE_RUN);
    }

    public QuestDuration makeDurationQuest(){
        long randTime = (long) (600000 + (Math.random()*600000));
        return new QuestDuration(System.nanoTime(),
                randTime,
                //1,
                Quest.STATUS_ACTIVE, Quest.IS_ONE_RUN);
    }

    public QuestDistance makeDistanceQuest(){
        double randDist = 500+ (Math.random()*500);
        return new QuestDistance(System.nanoTime(), Long.MAX_VALUE,
                randDist,
                //1,
                Quest.STATUS_ACTIVE, Quest.IS_ONE_RUN);
    }
}
