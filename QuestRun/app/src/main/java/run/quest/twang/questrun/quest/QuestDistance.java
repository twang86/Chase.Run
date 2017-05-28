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

public class QuestDistance extends Quest {
    private LonelyJogger sJogger;

    public QuestDistance(long id, long timeLimitInMillis, double winValue, int status, int ongoing) {
        super(id, Quest.TYPE_DISTANCE, timeLimitInMillis, winValue, status, ongoing);
        sJogger = LonelyJogger.getInstance();
    }

    @Override
    public View makeView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.screen_quest_distance, null);
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
            System.out.println("ummm");
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
        if(getOngoing() == IS_ONGOING){
            setWinValue(getWinValue()-sJogger.getDistance());
        }

    }

    @Override
    public int checkStatus() {
        if (sJogger.getRunTime()<getTimeLimit()){
            if (sJogger.getDistance()>=getWinValue()){
                return STATUS_SUCCESS;
            }
            return STATUS_ACTIVE;
        } else {
            if (sJogger.getDistance()<getWinValue()){
                return STATUS_FAILED;
            }
            return STATUS_SUCCESS;
        }
    }

    @Override
    public String inGameMessage() {
        double distanceRemaining = getWinValue()-sJogger.getDistance();
        String timeRemaining = getTimeLimit()<Long.MAX_VALUE ? " in the next " + (int)((getTimeLimit()-sJogger.getRunTime())/60000) + " minutes." : ".";
        return "You need to run " + (int)distanceRemaining + " more meters" + timeRemaining;
    }

    @Override
    public String getDescription() {
        String timeLimit = getTimeLimit()<Long.MAX_VALUE ? " in " + (int)(getTimeLimit()/60000)+ " minutes." : ".";
        String remaining = getOngoing()==Quest.IS_ONGOING ? " " + (int)getWinValue() + " remaining." : "";
        return "Run " + (int)getBackupWinValue() + " meters" + timeLimit + remaining;
    }

    @Override
    public void special(int specialType) {

    }
}
