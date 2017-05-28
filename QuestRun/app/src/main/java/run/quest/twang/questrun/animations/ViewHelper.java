package run.quest.twang.questrun.animations;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tim on 5/23/2017.
 */

public class ViewHelper {
    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String showSpeedUnits(boolean wantShort){
        return wantShort?"mph":"miles per hour";
    }

    public static String showTimeUnits(boolean wantShort){
        return wantShort?"min":"minuts";
    }

    public static String showDistanceUnits(double meters, boolean wantShort){
        double miles = meters / 1000 * 0.621371;
        if (miles<1) {
            return wantShort ? "ft" : "feet";
        }
        else {
            return wantShort ? "mi" : "miles";
        }
    }

    public static String showDistance(double meters, boolean showUnits){
        double miles = meters / 1000 * 0.621371;
        if (miles<1){
            double ft = meters *3.28084;
            return (int) ft + (showUnits?" feet":"");
        } else {

            String showMiles = String.format("%.2f", miles);
            return showMiles + (showUnits?" miles":"");
        }
    }

    public static String showAverageSpeed(double metersPerMillis, boolean showUnits){
        return showSpeed(metersPerMillis*1000, showUnits);
    }

    public static String showSpeed(double metersPerSecond, boolean showUnits){
        double kmPerHour = ((metersPerSecond * 3600)/1000);
        int milesPerHour = (int) (kmPerHour*0.621371);
        return milesPerHour + (showUnits?" mph":"");

    }

    public static String showMillis(long milliseconds, boolean showUnits){

        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(milliseconds);

        return minutes + (showUnits?" minutes":"");

    }
}
