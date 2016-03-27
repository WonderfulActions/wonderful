package com.letv.android.wonderful.util;

import android.util.Log;

import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.application.WonderfulApplication;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by michael on 16-3-22.
 */
public class LockVideoUtil {

    public static void setLockVideoPath(String path) {
        if (path != null) {
            final ArrayList<String> paths = new ArrayList<String>();
            paths.add(path);
            setLockVideoPaths(paths);
        }
    }

    public static void setLockVideoPaths(ArrayList<String> paths) {
        if (paths != null && paths.size() > 0) {
            // covert ArrayList to JsonArray
            // persistent JsonArray to file
            try {
                final JSONArray array = new JSONArray();
                for (int i = 0; i < paths.size(); i++) {
                    array.put(i, paths.get(i));
                }
                writeLockVideoPaths(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getLockVideoPaths() {
        final JSONArray array = readLockVideoPaths();
        final ArrayList<String> paths = new ArrayList<String>();
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    final String path = (String) array.get(i);
                    if (path != null && path.length() > 0) {
                        Log.i(Tags.WALLPAPER_ENGINE, "get lock video paths path = " + path);
                        paths.add(path);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return paths;
    }

    private synchronized static JSONArray readLockVideoPaths() {
        final File settingFile = generateLockVideoSetting();
        if (settingFile != null && settingFile.exists() && settingFile.isFile()) {
            try {
                final int length = (int) settingFile.length();
                Log.i(Tags.WALLPAPER_ENGINE, "lockVideoPaths json length = " + length);
                final byte[] bytes = new byte[length];
                final FileInputStream inputStream = new FileInputStream(settingFile);
                inputStream.read(bytes);
                inputStream.close();
                final String jsonArray = new String(bytes);
                if (jsonArray != null && jsonArray.length() > 0) {
                    final JSONArray array = new JSONArray(jsonArray);
                    return array;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }





        return null;
    }

    private synchronized static void writeLockVideoPaths(JSONArray array) {
        // check setting file
        // persistent paths
        if (array != null && array.length() > 0) {
            final File settingFile = generateLockVideoSetting();
            final String








                    pathsJson = array.toString();
            Log.i(Tags.WALLPAPER_ENGINE, "pathsJson = " + pathsJson);
            if (settingFile != null && settingFile.exists() && settingFile.isFile()) {
                try {
                    FileOutputStream outputStream = new FileOutputStream(settingFile);
                    outputStream.write(pathsJson.getBytes());
                    outputStream.flush();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static File generateLockVideoSetting() {
        final String settingsDirPath = WonderfulApplication.mCacheDir.getAbsolutePath() + File.separator + "settings";
        final File settingsDirFile = new File(settingsDirPath);
        // check setting dir
        if (settingsDirFile != null && settingsDirFile.exists() && settingsDirFile.isDirectory()) {
            // do nothing
        } else {
            settingsDirFile.mkdirs();
        }

        final String lockVideoSettingPath = settingsDirPath + File.separator + "lockVideoPaths.settings";
        Log.i(Tags.WALLPAPER_ENGINE, "generateLockVideoPath lockVideoSettingPath = " + lockVideoSettingPath);
        // check lockVideo setting file
        final File lockVideoSettingFile = new File(lockVideoSettingPath);
        if (lockVideoSettingFile != null && lockVideoSettingFile.exists() && lockVideoSettingFile.isFile()) {
            // do nothing
        } else {
            try {
                lockVideoSettingFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lockVideoSettingFile;
    }




}
