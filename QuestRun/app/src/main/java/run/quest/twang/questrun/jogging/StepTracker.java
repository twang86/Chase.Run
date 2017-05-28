package run.quest.twang.questrun.jogging;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Tim on 5/22/2017.
 */

public class StepTracker implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mStepDetectorSensor;
    private int mSteps;

    public StepTracker(Context context) {
        mSteps = 0;
        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    public void startTrack(){
        mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopTrack(){
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    public int getSteps() {
        return mSteps;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType()==sensor.TYPE_STEP_DETECTOR) {
            mSteps++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
