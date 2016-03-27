package com.letv.android.wonderful.activity;

import android.app.WallpaperInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.letv.android.wonderful.R;
import com.letv.android.wonderful.Tags;
import com.letv.android.wonderful.adapter.VideoCategoriesAdapter;
import com.letv.android.wonderful.fragment.category.CollectedFragment;
import com.letv.android.wonderful.fragment.category.GameFragment;
import com.letv.android.wonderful.fragment.category.HistoryFragment;
import com.letv.android.wonderful.fragment.category.MusicFragment;
import com.letv.android.wonderful.fragment.category.NBAFragment;
import com.letv.android.wonderful.fragment.category.PictureFragment;
import com.letv.android.wonderful.fragment.category.SavedFragment;
import com.letv.android.wonderful.fragment.category.SoccerFragment;
import com.letv.android.wonderful.wallpaper.WonderfulWallpaperService;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = Tags.WONDERFUL_VIDEO;
    // private static final String APP_NAME = "Wonderful";

//    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.i(Tags.WONDERFUL_APP, "onCreate");
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // addSpinner(null);
        initToolBar();
        initDrawerLayout();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        changeFragment(0);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(Tags.WONDERFUL_APP, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        Log.i(Tags.WONDERFUL_APP, "onConfigurationChanged");
        final Locale locale = newConfig.locale;
        
        final String displayCountry = locale.getDisplayCountry();
        final String displayLanguage = locale.getDisplayLanguage();
        final String displayName = locale.getDisplayName();
        final String country = locale.getCountry();
        final String language = locale.getLanguage();
        
        Log.i(Tags.WONDERFUL_APP, "displayCountry = " + displayCountry);
        Log.i(Tags.WONDERFUL_APP, "displayLanguage = " + displayLanguage);
        Log.i(Tags.WONDERFUL_APP, "displayName = " + displayName);
        Log.i(Tags.WONDERFUL_APP, "country = " + country);
        Log.i(Tags.WONDERFUL_APP, "language = " + language);
        
        final int orientation = newConfig.orientation;
        Log.i(Tags.WONDERFUL_APP, "orientation = " + orientation);
    }

    /*
    public void addSpinner(View view) {
        final SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.content_type, android.R.layout.simple_spinner_dropdown_item);
        getSupportActionBar().setListNavigationCallbacks(spinnerAdapter, onNavigationListener);
    }
    */
    
    /*
    private static Intent getShareIntent() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        return intent;
    }
    */

    private Toolbar mToolbar;
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        final String nbaTitle = getResources().getStringArray(R.array.content_type)[0];
        mToolbar.setTitle(nbaTitle);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i(TAG, "toolbar menu item = " + item.getItemId());
                return false;
            }
        });
    }

    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawer;
    private void initDrawerLayout() {
        // set drawer listener
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (LinearLayout) findViewById(R.id.main_drawer);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.i(TAG, "onDrawerOpened");
                // update actions
                invalidateOptionsMenu();
            }
            
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.i(TAG, "onDrawerClosed");
                // update actions
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        initContentList();
        initSetAsWallpaper();
        initSettings();
    }

    private void initSetAsWallpaper() {
        final TextView setAsWallpaperButton = (TextView) findViewById(R.id.set_wallpaper_button);
        setAsWallpaperButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showAsLiveWallpaper();
            }
        });
    }
    
    // ================================================================================================
    private static final String LIVE_PICKER_PACKAGE_NAME = "com.android.wallpaper.livepicker";
    private static final String LIVE_PICKER_CLASS_NAME = "com.android.wallpaper.livepicker.LiveWallpaperPreview";
    private static final String EXTRA_LIVE_WALLPAPER_INTENT = "android.live_wallpaper.intent";

    private void showAsLiveWallpaper() {
        final WallpaperInfo wallpaperInfo = getLiveWallpaperInfo();
        if (wallpaperInfo != null) {
            // set live intent
            final Intent liveIntent = new Intent(WallpaperService.SERVICE_INTERFACE);
            liveIntent.setClassName(wallpaperInfo.getPackageName(), wallpaperInfo.getServiceName());
            // start system live picker activity
            final Intent pickerIntent = new Intent();
            final ComponentName pickerComponent = new ComponentName(LIVE_PICKER_PACKAGE_NAME, LIVE_PICKER_CLASS_NAME);
            pickerIntent.setComponent(pickerComponent);
            pickerIntent.putExtra(EXTRA_LIVE_WALLPAPER_INTENT, liveIntent);
            startActivity(pickerIntent);
        }
    }

    private WallpaperInfo getLiveWallpaperInfo() {
        // get wallpaperInfo
        final Intent wallpaperServiceIntent = new Intent(this, WonderfulWallpaperService.class);
        final ResolveInfo resolveInfo = getPackageManager().resolveService(wallpaperServiceIntent, PackageManager.GET_META_DATA);
        try {
            final WallpaperInfo wallpaperInfo = new WallpaperInfo(this, resolveInfo);
            return wallpaperInfo;
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // ================================================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
        Log.i(TAG, "onCreateOptionsMenu");
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        */
        /*
        final MenuItem shareItem = menu.findItem(R.id.action_share);
        final ActionProvider actionProvider = MenuItemCompat.getActionProvider(shareItem);
        Log.i(TAG, "actionProvider = " + actionProvider);
        final ShareActionProvider shareActionProvider = (ShareActionProvider) actionProvider;
        Log.i(TAG, "shareActionProvider = " + shareActionProvider);
        shareActionProvider.setShareIntent(getShareIntent());
        */
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*
        Log.i(TAG, "onPrepareOptionsMenu");
        final boolean isDrawerVisible = mDrawerLayout.isDrawerVisible(mDrawer);
        final MenuItem searchMenuItem = menu.findItem(R.id.action_volume);
        searchMenuItem.setVisible(!isDrawerVisible);
        */
        return super.onPrepareOptionsMenu(menu);
    }
    
    private void initSettings() {
        // init settings
        final TextView settingsButton = (TextView) findViewById(R.id.settings_bt);
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toSettings();
            }
        });
    }

    private ListView mContentList;
    private void initContentList() {
        // init drawer list
        mContentList = (ListView) findViewById(R.id.left_drawer_list);
        final String[] categories = getResources().getStringArray(R.array.content_type);
        /*
        final ArrayAdapter<String> contentAdapter = new ArrayAdapter<String>(this, R.layout.item_drawer, categories);
        mContentList.setAdapter(contentAdapter);
        */
        final VideoCategoriesAdapter adapter = new VideoCategoriesAdapter(categories);
        mContentList.setAdapter(adapter);
        mContentList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Object object = mContentList.getItemAtPosition(position);
                Log.i(TAG, "item at position " + position + " = " + object);
                if (object != null) {
                    final String title = (String) object;
                    selectItem(position);
                    // setTitle(title);
                }
            }
        });
    }
    ActionBarDrawerToggle actionBarDrawerToggle;
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onPostCreate");
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected");
        
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        switch (item.getItemId()) {
            case R.id.action_search:
                Log.i(TAG, "action search selected");
                
                break;
                
                /*
            case R.id.action_share:
                Log.i(TAG, "action share selected");
                
                break;
                */

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void toSettings() {
        Log.i(TAG, "toSettings");
        SettingsActivity.showSettings(this);
    }

    private void selectItem(int position) {
        Log.i(TAG, "selectItem position = " + position);
        // highlight selected item, update title, close drawer
        mContentList.setItemChecked(position, true);
        final String title = getResources().getStringArray(R.array.content_type)[position];
        setTitle(title);
        mDrawerLayout.closeDrawer(mDrawer);
        // replace with new fragment
        changeFragment(position);
    }

    private void changeFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            // nba
            case 0:
                fragment = NBAFragment.newInstance();
                break;
            // soccer
            case 1:
                fragment = SoccerFragment.newInstance();
                break;
            // music
            case 2:
                fragment = MusicFragment.newInstance();
                break;
            // picture
            case 3:
                fragment = PictureFragment.newInstance();
                break;
            // game
            case 4:
                fragment = GameFragment.newInstance();
                break;
            // history
            case 5:
                fragment = HistoryFragment.newInstance();
                break;
            // collected
            case 6:
                fragment = CollectedFragment.newInstance();
                break;
            // saved
            case 7:
                fragment = SavedFragment.newInstance();
                break;
        }
        if (fragment != null) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_container, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        getSupportActionBar().setTitle(title);
    }
}
