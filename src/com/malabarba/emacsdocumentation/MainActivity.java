package com.malabarba.emacsdocumentation;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;

//import java.util.Locale;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
//import android.support.v4.view.ViewPager;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//
//import com.actionbarsherlock.app.ActionBar;
//import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;    

    /** The {@link ViewPager} that will host the section contents. */
    ViewPager mViewPager = null;
    /** The actionbar. */
    ActionBar actionBar = null;
    /** Search field in the actionbar. */
    EditText editSearch = null;
    MenuItem menuSearch = null;
    /** The text we receive from other apps. */
    String sharedText = "";
    /** The Database. */
    SymbolDatabase sd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.i("MainActivity Created");

        // Get preferences to find out which tab was selected
        // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SettingsActivity.createHiddenPreferences();

        initializeStructure();

        try {
            // This creates the actual DataBase! So it could take a while!
            sd = new SymbolDatabase(this);
        } catch (Exception e) {
            // App.dialog(getString(R.strings.cant_create_database));
            App.dialog("Bad exception creating Database!\n"+e);
            App.e("Couldn't create the database:",e);
        }
        App.d("Database created.");
        // try{	
        //     sd.getReadableDatabase();
        // } catch (Exception e) {
        //     // App.dialog(getString(R.strings.cant_create_database));
        //     App.dialog("Bad exception opening Database!\n"+e);
        //     App.e("Couldn't open the database:",e);
        // }
        // App.d("Database Opened.");

        // This is in case the first invocation is from the share menu
        onNewIntent(getIntent());
    }
    
    private boolean initializeStructure() {
        // Display the content xml
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        Integer selectedTab = SettingsManager.getInt("selected_tab");        
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                }});

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                             .setText(mSectionsPagerAdapter.getPageTitle(i))
                             .setTabListener(this),
                             (selectedTab == i));
        }

        return true;
    }
    
    @Override
    public void onPause() {
    	SettingsManager.put("selected_tab", actionBar.getSelectedNavigationIndex());
        super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.main, menu);

        // Set the right icon for the checkbox
        // (disabled for now, since it's not supported yet)
        menu.findItem(R.id.toggle_builtin).setVisible(false);
        if (SettingsManager.getBoolean("toggle_builtin"))
            menu.findItem(R.id.toggle_builtin).setIcon(R.drawable.btn_check_on);
        else
            menu.findItem(R.id.toggle_builtin).setIcon(R.drawable.btn_check_off);

        configureSearchView(menu);
        sd.updateMatches(sharedText);
        sharedText = "";
        
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean configureSearchView(Menu menu) {
        // Show the search menu item in menu.xml
        menuSearch = menu.findItem(R.id.menu_search);

        // Locate the EditText in menu.xml
        editSearch = (EditText) menuSearch.getActionView();

        // Capture Text in EditText
        editSearch.addTextChangedListener(textWatcher);

        // Cleanup the search when collapsed, and take focus when expanded
        menuSearch.setOnActionExpandListener(new OnActionExpandListener() {
                // Menu Action Collapse
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editSearch.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				
                    // Empty EditText to remove text filtering
                    editSearch.setText("");
                    editSearch.clearFocus();
                    return true;
                }

                // Menu Action Expand
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Focus on EditText
                    editSearch.requestFocus();
                    // Force the keyboard to show on EditText focus
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editSearch, 0);
                    return true;
                }
			
            });
        // And start expanded
        menuSearch.expandActionView();
        editSearch.setText(sharedText);
        
        return true;
    }

    @Override
    protected void onNewIntent(Intent shareIntent) {
        String action = shareIntent.getAction();
        String type = shareIntent.getType();
       App.d("Intent Received: " + shareIntent);
        if (Intent.ACTION_SEND.equals(action)
            && (type != null)
            && "text/plain".equals(type))
                handleSharedText(shareIntent);
    }

    private void handleSharedText(Intent intent) {
        String text = intent.getStringExtra(Intent.EXTRA_TEXT).trim();
//        App.toast("Got this text:\n"+text);
        Boolean ss = SettingsManager.getBoolean("share_speed_mode");
        App.d("Share_speed is "+ ss);
        
        if (sd == null) {
            App.e("sd is null!");
            return;
        } // else {
        // 	sd.getReadableDatabase();
        // }
        
        App.d("Database ready to query.");
        // Look for exact match if the preference says so.
        if (ss && sd.lookForExactMatch(text,this)) return;
       
        // TODO (150989)
        // Check if the menu is expanded
        App.d("No text found, filling the menu.");
        if ((menuSearch == null) || (editSearch == null)) {
            sharedText = text;
        } else {
            App.d("Seems it's already created, filling it now.");
            menuSearch.expandActionView();
            editSearch.setText(text);
            sd.updateMatches(text);
        }

        // // Locate the EditText in menu.xml
        // editSearch = (EditText) menuSearch.getActionView();
        // editSearch.setText
    }
    
    // EditText TextWatcher
    private TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                App.startTiming("Text received");
                sd.updateMatches(editSearch.getText().toString());
                App.finishTiming("View Updated");
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {} 
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {} 
	};

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.send_feedback:
            startActivity(App.emailIntent("bruce.connor.am@gmail.com",
                                          getString(R.string.feedback_email_subject),
                                          App.getSystemInformation() + getString(R.string.feedback_email_body)));
            break;
        case R.id.action_settings:
            startActivity(new Intent(this, SettingsActivity.class));
            break;

        case R.id.toggle_builtin:
            // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            if (SettingsManager.getBoolean("toggle_builtin")) {
                // This turns it off
                SettingsManager.put("toggle_builtin", false);
                item.setIcon(R.drawable.btn_check_off);
                item.setChecked(false);
                App.toast("Using only built-in packages.");
            } else {
                // This turns it on
                SettingsManager.put("toggle_builtin", true);
                item.setIcon(R.drawable.btn_check_on);
                item.setChecked(true);
                App.toast("Including ELPA packages.");
            }
            break;
        }

        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    } 

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
    	static public final String TYPE_NUMBER = "com.malabarba.emacsdocumentation.SymbolListFragment.TYPE_NUMBER";
        
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new SymbolListFragment();
            Bundle args = new Bundle();
            args.putInt(TYPE_NUMBER, position);
            fragment.setArguments(args);
            // TODO (666504)
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return SymbolDatabase.Type.values().length + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            if (position == getCount() - 1) return getString(R.string.title_random).toUpperCase(l);
            return SymbolDatabase.getTableName(position).toUpperCase(l);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.d("Closing the Database (activity destroyed).");
        sd.close();
    }

}
