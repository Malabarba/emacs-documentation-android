package com.malabarba.emacsdocumentation;

import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;


public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        // if (key.equals("after_done_package")) {
        //     App.i("after_done_package changed to: "+sp.getString("after_done_package", "oops"));
        // }
       
        if (key.equals("share_speed_mode")) updateShareSpeedMode();
        if (key.equals("toggle_builtin")) updateToggleBuiltin();
    }
    
    private void updateShareSpeedMode() {
        try {
            @SuppressWarnings("deprecation")
                Preference pref = findPreference("share_speed_mode");
            // Set summary to be the user-description for the selected value
            if (SettingsManager.getBoolean("share_speed_mode",false)) {
                App.d("share_speed_mode => true.");
                pref.setSummary(getString(R.string.share_speed_mode_summ_yes, ""));
            } else {
                App.d("share_speed_mode => false.");
                pref.setSummary(getString(R.string.share_speed_mode_summ_no, ""));
            }

        } catch (Exception e) {
            App.e( "Exception in updateShareSpeedMode.",e);
        }
    }
    
    private void updateToggleBuiltin() {
        try {			
            @SuppressWarnings("deprecation")
                Preference pref = findPreference("toggle_builtin");
            // Set summary to be the user-description for the selected value
            if (SettingsManager.getBoolean("toggle_builtin",false)) {
                App.d("toggle_builtin => true.");
                pref.setSummary(getString(R.string.toggle_builtin_summ_yes, ""));
            } else {
                App.d("toggle_builtin => false.");
                pref.setSummary(getString(R.string.toggle_builtin_summ_no, ""));
            }

        } catch (Exception e) {
            App.e( "Exception in updateToggleBuiltin.",e);
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
        // Load the legacy preferences headers
        oldPreferences();
        updateToggleBuiltin();
        updateShareSpeedMode();
    }

    public static void createHiddenPreferences() {
        SettingsManager.putIfAbsent("selected_tab", 0, false);
        SettingsManager.putIfAbsent("page_zoom", 16f, false);
        SettingsManager.putIfAbsent("first_time", true, false);
        SettingsManager.putIfAbsent("share_speed_mode", true, false);
        // SettingsManager.putIfAbsent("initial_tab", 0, false);
        SettingsManager.putIfAbsent("toggle_builtin", true, false);
        SettingsManager.commit();    	    	
    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private void newPreferences() {
//    	// TODO Auto-generated method stub
//    	// Display the fragment as the main content.
//    	getFragmentManager().beginTransaction()
//    	.replace(android.R.id.content, new SettingsFragment())
//    	.commit();
//    }
    
    @SuppressWarnings("deprecation")
    private void oldPreferences() {addPreferencesFromResource(R.xml.preferences);}

    @SuppressWarnings("deprecation")
	@Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
