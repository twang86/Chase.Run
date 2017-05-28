package run.quest.twang.questrun.quest;


import android.content.Context;
import android.view.View;

/**
 * Created by Tim on 5/19/2017.
 */

public abstract class Quest {
    public static final int TYPE_CHASE = 100;
    public static final int TYPE_CATCH = 101;
    public static final int TYPE_DURATION = 102;
    public static final int TYPE_DISTANCE = 103;

    public static final int MAX_QUESTS_ALLOWED = 6;

    public static final int IS_ONGOING = 10;
    public static final int IS_ONE_RUN = 5;
    public static final int IS_USER_MADE = -10;

    public static final int STATUS_FAILED = 2;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_ACTIVE = 0;
    public static final int STATUS_INACTIVE = -1;
    public static final int STATUS_FINISHED = -2;

    public static final int MONSTER_TOO_CLOSE = -5;
    public static final int MONSTER_PERSPECTIVE_MULTIPLIER = 10;

    private long mId;
    private long mTimeLimit;
    private double mWinValue;
    private int mStatus;
    private int mOngoing;
    private double mBackupWinValue;
    private int mType;



    public Quest(long id, int type, long timeLimitInMillis, double winValue, int status, int ongoing) {
        mId = id;
        mType = type;
        mTimeLimit = timeLimitInMillis;
        mWinValue = winValue;
        mOngoing = ongoing;
        if (ongoing == IS_ONGOING){
            mTimeLimit = Long.MAX_VALUE;
            mStatus = STATUS_ACTIVE;}
        else {
            mTimeLimit = timeLimitInMillis;
            mStatus = status;
            }
        mBackupWinValue = winValue;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        if (status >-3 && status < 3) {
            mStatus = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public long getTimeLimit() {
        return mTimeLimit;
    }

    public double getWinValue() {
        return mWinValue;
    }

    public void setWinValue(double newWinValue){
            mWinValue = newWinValue;
    }

    public int getOngoing() {
        return mOngoing;
    }

    public double getBackupWinValue() {
        return mBackupWinValue;
    }

    public void setBackupWinValue(double backupWinValue) {
        mBackupWinValue = backupWinValue;
    }

    public long getId() {
        return mId;
    }

    public int getType() {
        return mType;
    }

    public abstract View makeView(Context context);

    public abstract void updateView(View view);

    public abstract void updateSelf(long interval);

    public abstract void onRunStart(Context context);

    public abstract void onRunEnd();

    public abstract int checkStatus();

    public abstract String inGameMessage();

    public abstract String getDescription();

    public abstract void special(int specialType);

    //    public Quest(Context context, int layoutSource){
//        mView = LayoutInflater.from(context.getApplicationContext()).inflate(layoutSource, null);
//    }

}
