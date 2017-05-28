package run.quest.twang.questrun.quest;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import run.quest.twang.questrun.animations.ViewHelper;
import run.quest.twang.questrun.character.LonelyJogger;
import run.quest.twang.questrun.R;
import run.quest.twang.questrun.monsters.Monster;

/**
 * Created by Tim on 5/19/2017.
 */

public class QuestChase extends Quest {

    private Monster mMonster;
    private LonelyJogger sJogger;
    private MediaPlayer mMediaPlayer;

    public QuestChase(long id, Monster monster, long timeLimitInMillis, double winValue, int status, int ongoing) {
        super(id, Quest.TYPE_CHASE, timeLimitInMillis, winValue, status, ongoing);
        mMonster = monster;
        sJogger = LonelyJogger.getInstance();
        mMediaPlayer = null;
    }

    private void playWarning() {
        if (mMediaPlayer == null) {
            return;
        }
        double distanceFromMe = sJogger.getDistance() - mMonster.getPosition();
        if (distanceFromMe < 0) {
            distanceFromMe = 0;
        }
        double farBound = Math.abs(mMonster.getSpeed()*Quest.MONSTER_TOO_CLOSE);
        float volume = (float) (1 * (1 - (distanceFromMe/farBound)));
        if (volume<0){ volume=0;}
        if (volume > .05 && getStatus() == Quest.STATUS_ACTIVE) {
            mMediaPlayer.setVolume(volume, volume);
            mMediaPlayer.setLooping(true);

            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }

        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }

    }


    @Override
    public View makeView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.screen_quest_chase, null);
    }

    @Override
    public void updateView(View view) {
        FrameLayout viewWindow = (FrameLayout) view.findViewById(R.id.chase_layout_root);
        if (viewWindow != null) {
            ImageView monsterPic = (ImageView) view.findViewById(R.id.monster_image);
            TextView nameText = (TextView) view.findViewById(R.id.screen_monster_name);
            TextView statusText = (TextView) view.findViewById(R.id.screen_monster_distance);
            nameText.setText(mMonster.getName());
            switch (getStatus()) {
                case STATUS_FAILED:
                    monsterPic.setVisibility(View.GONE);
                    statusText.setText("Caught!");
                    break;
                case STATUS_SUCCESS:
                    monsterPic.setVisibility(View.GONE);
                    statusText.setText("Escaped!");
                    break;
                default:
                    double distanceFromMe = sJogger.getDistance() - mMonster.getPosition();
                    if (distanceFromMe < 0) {
                        distanceFromMe = 0;
                    }
                    if (distanceFromMe > Math.abs(mMonster.getType().getSpeed()*Quest.MONSTER_TOO_CLOSE*2)){
                        statusText.setTextColor(ContextCompat.getColor(view.getContext(), R.color.green));
                    } else if (distanceFromMe > Math.abs(mMonster.getType().getSpeed()*Quest.MONSTER_TOO_CLOSE)){
                        statusText.setTextColor(ContextCompat.getColor(view.getContext(), R.color.black));
                    } else {
                        statusText.setTextColor(ContextCompat.getColor(view.getContext(), R.color.red));
                    }

                    statusText.setText(ViewHelper.showDistance(distanceFromMe, true));
                    int width = viewWindow.getWidth();
                    int height = viewWindow.getHeight();
                    int imageSize = width > height ? height : width;

                    double monsterPerspective = (1 / (distanceFromMe+1))* mMonster.getType().getSize();
                    if (monsterPerspective>.01) {


                        monsterPic.setBackgroundResource(mMonster.getType().getAnimation());
                        AnimationDrawable myAnim = (AnimationDrawable) monsterPic.getBackground();
                        myAnim.start();

                        monsterPic.getLayoutParams().width = (int) (imageSize * monsterPerspective);
                        monsterPic.getLayoutParams().height = (int) (imageSize * monsterPerspective);
                    }
                    break;
            }
        }
    }

    @Override
    public void updateSelf(long interval) {
        if (getStatus() == STATUS_ACTIVE) {
            double timeMultiplier = (double) interval / 1000;
            double currentSpeed = mMonster.getSpeed() * (timeMultiplier);
            mMonster.move(currentSpeed);
            mMonster.getTired(timeMultiplier);
            setStatus(checkStatus());
            try {
                playWarning();
            } catch (IllegalStateException e){
                e.printStackTrace();
                System.out.println("well shit...");
            }
        }
    }

    @Override
    public void onRunStart(Context context) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(context.getApplicationContext(), mMonster.getType().getSound());
        }
    }

    @Override
    public void onRunEnd() {
        if (getStatus() == Quest.STATUS_ACTIVE) {
            setStatus(Quest.STATUS_FAILED);
        }
        mMonster.setPosition(mMonster.getType().getSpeed() * -5);
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public int checkStatus() {
        if (sJogger.getRunTime() < getTimeLimit()) {//checks if there is still time left in the quest
            if (mMonster.getPosition() >= sJogger.getDistance()) {
                return STATUS_FAILED;
            }
            return STATUS_ACTIVE;
        } else { //if time has run out
            if (mMonster.getHealth() > getWinValue()) {
                //checks if monster has more health than the win condition allows
                //(if this isn't a kill quest, win value is set as the monster's starting health, so this code will never run)
                return STATUS_FAILED;
            }
            return STATUS_SUCCESS;
        }
    }

    @Override
    public String inGameMessage() {
        long monsterFromYou = Math.round(sJogger.getDistance() - mMonster.getPosition());
        String firstMessage = "The " + mMonster.getName() + " is " + monsterFromYou + " meters away from you!!";
        String secondMessage = " It has " + mMonster.getHealth() + " health.";
        return firstMessage + (getWinValue() >= mMonster.getHealth() ? "" : secondMessage);
    }

    @Override
    public String getDescription() {
        return (getWinValue() >= mMonster.getHealth() ? " Outrun " : " Kill ") + "a " + mMonster.getName();
    }

    @Override
    public void special(int specialType) {
        System.out.println("Special activated!!");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuestChase){
            return (((QuestChase) obj).getMonster().getType()== mMonster.getType() && ((QuestChase) obj).getWinValue() == getWinValue());
        } else {
            return false;
        }
    }

    public Monster getMonster() {
        return mMonster;
    }
}
