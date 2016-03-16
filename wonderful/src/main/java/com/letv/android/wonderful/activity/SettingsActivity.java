package com.letv.android.wonderful.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.letv.android.wonderful.R;

public class SettingsActivity extends PreferenceActivity {
    
    public static void showSettings(Context context) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
