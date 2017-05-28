package run.quest.twang.questrun.jogging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;

import run.quest.twang.questrun.R;
import run.quest.twang.questrun.character.LonelyJogger;
import run.quest.twang.questrun.quest.JogQuestsHolder;

/**
 * Created by Tim on 5/18/2017.
 */

public class JogService extends Service {
    private Thread mJogThread;
    private LonelyJogger sJogger;
    private JogQuestsHolder sQuests;
    private long mLastTime;
    private long mCurrentTime;
    private StepTracker mStepTracker;
    private NotificationManager mNotificationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this, JoggingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(this);
        noteBuilder.setContentIntent(pIntent);
        noteBuilder.setSmallIcon(R.drawable.ic_runner);
        noteBuilder.setContentTitle("Quest.Run is active");
        noteBuilder.setContentText("Quest.Run is tracking your run");
        Notification serviceNote= noteBuilder.build();
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(123, serviceNote);
        startForeground (123, serviceNote);

        sJogger = LonelyJogger.getInstance();
        sQuests = JogQuestsHolder.getInstance();

        mStepTracker = new StepTracker(this);

        sQuests.startAllQuests(this);

        mJogThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (sJogger.isRunning()) {

                    //checks how long it's been since this last ran
                    mCurrentTime = System.currentTimeMillis();
                    long timeElapsed = mCurrentTime - mLastTime;
                    mLastTime = mCurrentTime;

                    //update jogger positions and distance
                    //this doRun method uses the step  tracker because it's more accurate than location
                    sJogger.doRunOnStep(timeElapsed, mStepTracker.getSteps());

                    //do work for all the quests
                    //actually, only the monster quests really need to do any work, I should probably fix that in the future
                    sQuests.updateAllQuests(timeElapsed);

                    //broadcasts to any activity that is listening to update their views or whatever
                    //because we just updated ourselves
                    Intent broadcastIntent = new Intent(LonelyJogger.RUN_CAST);
                    broadcastIntent.putExtra("Message", "QuestUpdated");
                    LocalBroadcastManager.getInstance(JogService.this).sendBroadcast(broadcastIntent);

                    //waits for a set amount of time before doing work again. I don't even know
                    //if this is that memory efficient, but I don't like to have things run
                    //every millisecond. And it doesn't seem worth it either.
                    try {
                        Thread.sleep(LonelyJogger.RUN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //if the service dies while thread is waiting, stop running as well and shut down the thread.
                        stopSelf();
                    }
                }

                //when this thread is finished, stop the app as well.
                stopSelf();

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!sJogger.isRunning()) {
            sJogger.startRun();
            mLastTime = System.currentTimeMillis();
            mCurrentTime = System.currentTimeMillis();
            mStepTracker.startTrack();
            if (!mJogThread.isAlive()) {
                mJogThread.start();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service dead!");
        mNotificationManager.cancel(123);
        mStepTracker.stopTrack();
        sJogger.setRunning(false);
        sQuests.resetAllQuests();
    }
}
