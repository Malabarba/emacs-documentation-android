package com.malabarba.emacsdocumentation;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    static public final String TYPE_NUMBER =
        "com.malabarba.emacsdocumentation.SymbolListFragment.TYPE_NUMBER";
    
    // This enum allows us to easily change the order of tabs, without
    // having to change anything else in the code. It also automates
    // the process of getting the total number of tabs.
    static public enum Tabs {About, Functions, Variables};

    public static String getDir(int item) {
        switch (Tabs.values()[item]) {
        case Functions: return "Fun";
        case Variables: return "Var";
        case About: return "No dir in About page";
        default: return null;
        }
    }
    
    @Override
    public Fragment getItem(int item) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DummySectionFragment (defined as a static inner class
        // below) with the page number as its lone argument.
    
    	Fragment fragment = decideNewFragment(item);
    	Bundle args = new Bundle();
        args.putInt(TYPE_NUMBER, item);
        fragment.setArguments(args);
        // TODO (666504)
        return fragment;
    }

    private Fragment decideNewFragment(int item) {
    	switch (Tabs.values()[item]) {
        case Functions:
        case Variables:
            return new SymbolListFragment();
			
        case About:
            return new AboutFragment();
			
        default: return null;
        }
    }

    @Override
    public int getCount() {
        // Show as many tabs as we have defined.
        return Tabs.values().length;
    }

    @Override
    public CharSequence getPageTitle(int item) {
        Locale l = Locale.getDefault();
        switch (Tabs.values()[item]) {
        case Functions: return App.string(R.string.title_functions).toUpperCase(l);
        case Variables: return App.string(R.string.title_variables).toUpperCase(l);	
        case About: return App.string(R.string.title_about).toUpperCase(l);
        default: return null;
        }
    }
    
    public SectionsPagerAdapter(FragmentManager fm) {super(fm);}
}
