package run.quest.twang.questrun.character;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tim on 5/18/2017.
 */

public class LonelyJogger {
    public static final String USER_LOCAL_SAVE = "run.quest.twang.questrun.localsave";
    public static final String SAVE_NAME = "what's yo name bro";
    public static final String SAVE_LEVEL = "it's over nine thousaaaaaand!";
    public static final String SAVE_HEIGHT = "tom cruise";
    public static final String SAVE_SPEED_MULTIPLIER = "super saiyan yet?";
    public static final String SAVE_TOTAL_RUN_TIME = "hyperbolic time chamber";
    public static final String SAVE_TOTAL_DISTANCE = "around the world in 80 days";
    public static final String SAVE_TOTAL_GAME_DISTANCE = "discover any new planets?";
    public static final String SAVE_AVERAGE_SPEED = "sonic the hedgehog";
    public static final String SAVE_TOTAL_STEPS = "And I will walk five hundred miles and i will...";

    public static final double AVERAGE_STRIDE_LENGTH_HEIGHT_RATIO = .7;
    public static final float DEFAULT_HEIGHT = 1.8f;

    public static final int REQUEST_LOCATION = 10;
    public static final String RUN_CAST = "Hey! Listen!!";
    public static final long RUN_INTERVAL = 40;
    public static final long LOCATION_INTERVAL = 1000;
    public static final long LOCATION_INTERVAL_FAST = 500;

    private static LonelyJogger sInstance;

    private String mName;
    private int mLevel;
    private float mHeight;
    private float mSpeedMultiplier;
    private long mTotalRunTime;
    private long mTotalDistance;
    private long mTotalGameDistance;
    private float mAverageSpeed;
    private long mTotalStepsTaken;

    //running event variables
    private boolean mIsRunning;
    private double mDistance;
    private double mSpeed;
    private long mRunTime;
    private int mStepsTaken;

    private LonelyJogger(){
        setDefault();
        resetRun();
    }

    public void setDefault(){
        mName = "Lonely Jogger";
        mLevel = 1;
        mHeight = DEFAULT_HEIGHT;
        mSpeedMultiplier = 1;
        mTotalDistance = 0;
        mTotalGameDistance = 0;
        mTotalRunTime = 0;
        mAverageSpeed = 0;
        mTotalStepsTaken = 0;
    }

    public static LonelyJogger getInstance() {
        if (sInstance == null){
            sInstance = new LonelyJogger();
        }
        return sInstance;
    }

    public void setValuesFromSharedPref(Context context){
        SharedPreferences userSave = context.getApplicationContext().getSharedPreferences(USER_LOCAL_SAVE, context.MODE_PRIVATE);
        mName = userSave.getString(SAVE_NAME, "Lonely Jogger");
        mLevel = userSave.getInt(SAVE_LEVEL, 1);
        mHeight = userSave.getFloat(SAVE_HEIGHT, DEFAULT_HEIGHT);
        mSpeedMultiplier = userSave.getFloat(SAVE_SPEED_MULTIPLIER, 1f);
        mTotalDistance = userSave.getLong(SAVE_TOTAL_DISTANCE, 0);
        mTotalGameDistance = userSave.getLong(SAVE_TOTAL_GAME_DISTANCE, 0);
        mTotalRunTime = userSave.getLong(SAVE_TOTAL_RUN_TIME, 0);
        mAverageSpeed = userSave.getFloat(SAVE_AVERAGE_SPEED, 0f);
        mTotalStepsTaken = userSave.getLong(SAVE_TOTAL_STEPS, 0);
    }

    public void saveToSharedPref(Context context){
        SharedPreferences userSave = context.getApplicationContext().getSharedPreferences(USER_LOCAL_SAVE, context.MODE_PRIVATE);
        SharedPreferences.Editor saveEditor = userSave.edit();
        saveEditor.putString(SAVE_NAME, mName);
        saveEditor.putInt(SAVE_LEVEL, mLevel);
        saveEditor.putFloat(SAVE_HEIGHT, mHeight);
        saveEditor.putFloat(SAVE_SPEED_MULTIPLIER, mSpeedMultiplier);
        saveEditor.putLong(SAVE_TOTAL_DISTANCE, mTotalDistance);
        saveEditor.putLong(SAVE_TOTAL_GAME_DISTANCE, mTotalGameDistance);
        saveEditor.putLong(SAVE_TOTAL_RUN_TIME, mTotalRunTime);
        saveEditor.putFloat(SAVE_AVERAGE_SPEED, mAverageSpeed);
        saveEditor.putLong(SAVE_TOTAL_STEPS, mTotalStepsTaken);
        saveEditor.commit();
    }

    public void doRun(long interval){
        mRunTime+=interval;
        setDistance(mStepsTaken*mHeight*AVERAGE_STRIDE_LENGTH_HEIGHT_RATIO);
    }

    public void doRunOnStep(long interval, int steps){
        mRunTime+=interval;
        setStepsTaken(steps);
        setDistance(mStepsTaken*mHeight*AVERAGE_STRIDE_LENGTH_HEIGHT_RATIO);
    }

    public float setSpeedMultiplierByLevel(int level){
        return (float) (1+ (.05*(level-1)));
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    public void setRunning(boolean running) {
        mIsRunning = running;
    }

    public void addDistance(double distance){
        mDistance += distance;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public double getSpeed() {
        if (mRunTime == 0){
            mRunTime = 1;
        }//no divide by zero error here!
        mSpeed = mDistance/mRunTime; //this is measured in meters per milliseconds, not very useful, lets change it to meters per second
        mSpeed*=1000;
        return mSpeed;
    }

    public long getRunTime() {
        return mRunTime;
    }

    public void setRunTime(long runTime) {
        mRunTime = runTime;
    }

    public int getStepsTaken() {
        return mStepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        mStepsTaken = stepsTaken;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getLevel() {
        return mLevel;
    }

    public long getTotalRunTime() {
        return mTotalRunTime;
    }

    public void setTotalRunTime(long totalRunTime) {
        mTotalRunTime = totalRunTime;
    }

    public long getTotalDistance() {
        return mTotalDistance;
    }

    public void setTotalDistance(long totalDistance) {
        mTotalDistance = totalDistance;
    }

    public float getAverageSpeed() {
        return mAverageSpeed;
    }

    public void setAverageSpeed(float averageSpeed) {
        mAverageSpeed = averageSpeed;
    }

    public double getHeight() {
        return mHeight;
    }

    public void setHeight(float height) {
        mHeight = height;
    }

    public float getSpeedMultiplier() {
        return mSpeedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        mSpeedMultiplier = speedMultiplier;
    }

    public long getTotalStepsTaken() {
        return mTotalStepsTaken;
    }

    public void setTotalStepsTaken(int totalStepsTaken) {
        mTotalStepsTaken = totalStepsTaken;
    }

    public void startRun(){
        mIsRunning = true;
    }

    public void levelUp(int levels){
        mLevel += levels;
        mSpeedMultiplier += levels*.1;
    }

    public void stopRun(){
        mIsRunning = false;
        resetRun();
    }

    public void storeRun(){
        mTotalDistance += mDistance;
        mTotalGameDistance += mDistance*mSpeedMultiplier;
        mTotalStepsTaken += mStepsTaken;
        mTotalRunTime += mRunTime;
        mAverageSpeed = (float)mTotalDistance/mTotalRunTime;
    }

    private void resetRun(){
        mIsRunning = false;
        mDistance = 0;
        mSpeed = 0;
        mRunTime = 0;
        mStepsTaken = 0;
    }

}
