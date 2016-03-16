package com.letv.android.wonderful;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.PowerManager;
import android.util.Log;

import com.letv.android.wonderful.activity.TestActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class OnScreenShakeListener implements SensorEventListener {
    private static final String TAG = TestActivity.TAG;
    private Context mContext;
    private long mLastShakeTime;

    public OnScreenShakeListener(Context context) {
        mContext = context;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        Log.i(TAG, "onSensorChanged");
        final int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            // 1 check acceleration
            final double acceleration = computeAcceleration(event.values);
//            Log.i(TAG, "acceleration = " + acceleration);
            final boolean isAShake = isShaking(acceleration);
//            Log.i(TAG, "isAShake = " + isAShake);
            if (isAShake) {
                // 2 check shake interval
                final long currentTime = System.currentTimeMillis();
                if (currentTime - mLastShakeTime > 1 * 1000) {
                    mLastShakeTime = currentTime;
                    // 3 is lock screen
//                    final boolean isLockScreen = isLockScreen(mContext);
//                    Log.i(TAG, "isLockScreen = " + isLockScreen);
//                    if (isLockScreen) {
                        onLockScreenShake();
//                    }
                } else {
                }
            }
        }
    }
    

    private static double computeAcceleration(float[] values) {
        final double acceleration = Math.sqrt(Math.pow(values[0], 2) + Math.pow(values[1], 2) + Math.pow(values[2], 2));
        return acceleration;
    }

    public static int SHAKE_THRESHOLD = 18;
    
    private static boolean isShaking(double acceleration) {
        final boolean isShaking = acceleration > SHAKE_THRESHOLD;
        return isShaking;
    }

    /*
    private static boolean isTargetScreen(String availableScreen, String screen) {
        if (screen.equals(availableScreen)) {
            return true;
        }
        return false;
    }

    private static int getScreenValue(String screen) {
        if (screen.equals(LOCK_SCREEN)) {
            return PreferenceUtil.LOCK_SCREEN;
        } else if (screen.equals(HOME_SCREEN)) {
            return PreferenceUtil.HOME_SCREEN;
        }
        return PreferenceUtil.OTHER_SCREEN;
    }
    */

    public static final String LOCK_SCREEN = "lockscreen";
    public static final String HOME_SCREEN = "homescreen";
    public static final String OTHER_SCREEN = "otherscreen";

    private static String getCurrentScreen(Context context) {
        final boolean isLockScreen = isLockScreen(context);
        if (isLockScreen) {
            return LOCK_SCREEN;
        }
        final boolean isHomeScreen = isHomeScreen(context);
        if (isHomeScreen) {
            return HOME_SCREEN;
        }
        return OTHER_SCREEN;
    }

    // TODO optimize
    private static boolean isHomeScreen(Context context) {
        final ArrayList<String> packageNames = getHomeApps(context);
        final String packageName = getTopApp(context);
        if (packageNames.contains(packageName)) {
            return true;
        }
        return false;
    }

    private static String getTopApp(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // TODO
        final List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
        final RunningTaskInfo taskInfo = taskInfos.get(0);
        final ComponentName componentName = taskInfo.topActivity;
        final String packageName = componentName.getPackageName();
        return packageName;
    }

    private static ArrayList<String> getHomeApps(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        final ArrayList<String> packageNames = new ArrayList<String>();
        for (ResolveInfo info : infos) {
            final String packageName = info.activityInfo.packageName;
            packageNames.add(packageName);
        }
        return packageNames;
    }

    public static boolean isLockScreen(Context context) {
        if (context != null) {
            // interactive
            final PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            final boolean isInteractive = powerManager.isInteractive();
//            Log.i(Tags.LOCK_WALLPAPER_STATE, "isInteractive = " + isInteractive);
            // keyguard
            final KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            final boolean inKeyguardRestrictedInputMode = keyguardManager.inKeyguardRestrictedInputMode();
            final boolean isKeyguardLocked = keyguardManager.isKeyguardLocked();
//            final boolean isKeyguardShowing = keyguardManager.isKeyguardShowing();
//            Log.i(Tags.LOCK_WALLPAPER_STATE, "inKeyguardRestrictedInputMode = " + inKeyguardRestrictedInputMode);
//            Log.i(Tags.LOCK_WALLPAPER_STATE, "isKeyguardLocked = " + isKeyguardLocked);
//            Log.i(Tags.LOCK_WALLPAPER_STATE, "isKeyguardShowing = " + isKeyguardShowing);
//            if (isInteractive && inKeyguardRestrictedInputMode && isKeyguardLocked && isKeyguardShowing) {
            if (isInteractive && inKeyguardRestrictedInputMode && isKeyguardLocked) {
                return true;
            }
        }
        return false;
    }

    public abstract void onLockScreenShake();
}
