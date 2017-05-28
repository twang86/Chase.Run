package run.quest.twang.questrun.quest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import run.quest.twang.questrun.character.LonelyJogger;
import run.quest.twang.questrun.R;

/**
 * Created by Tim on 5/19/2017.
 */

public class QuestDuration extends Quest {
    private LonelyJogger sJogger;

    public QuestDuration(long id, double duration, int status, int ongoing) {
        super(id, Quest.TYPE_DURATION, Long.MAX_VALUE, duration, status, ongoing);
        sJogger = LonelyJogger.getInstance();
    }

    @Override
    public View makeView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.screen_quest_duration, null);
    }

    @Override
    public void updateView(View view) {
        TextView myText = (TextView) view.findViewById(R.id.screen_monster_distance);
        switch (getStatus()) {
            case STATUS_FAILED:
                myText.setText("Quest failed!");
                break;
            case STATUS_SUCCESS:
                myText.setText("Quest Complete!");
                break;
            default:
                myText.setText(inGameMessage());
        }

    }

    @Override
    public void updateSelf(long interval) {
        if (getStatus()== STATUS_ACTIVE){
            setStatus(checkStatus());
            if (checkStatus() == STATUS_SUCCESS && getOngoing()==Quest.IS_ONGOING){
                setWinValue(0);
            }
        }
    }

    @Override
    public void onRunStart(Context context) {

    }

    @Override
    public void onRunEnd() {
        if(getOngoing()==Quest.IS_ONGOING){
            setWinValue(getWinValue()-sJogger.getRunTime());
        }

    }

    @Override
    public int checkStatus() {
        if (sJogger.getRunTime()<getWinValue()){
            return STATUS_ACTIVE;
        } else {
            return STATUS_SUCCESS;
        }
    }

    @Override
    public String inGameMessage() {
        int minutesRemaining = (int) ((getWinValue() - sJogger.getRunTime())/60000);
        return "You have " + minutesRemaining + " minutes remaining.";
    }

    @Override
    public String getDescription() {
        String remaining = getOngoing()==Quest.IS_ONGOING ? " " + (int)getWinValue() + " remaining." : "";
        return "Run for " + (int)(getBackupWinValue()/60000) + " minutes." + remaining;
    }

    @Override
    public void special(int specialType) {
        //has no effect
    }
}
