package com.malabarba.emacsdocumentation;

import com.malabarba.util.App;
import java.util.ArrayList;
import java.util.Locale;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    static public final String TYPE_NUMBER =
        "com.malabarba.emacsdocumentation.SymbolListFragment.TYPE_NUMBER";

    private ArrayList<Fragment> tabs = new ArrayList<Fragment>();

    // This enum allows us to easily change the order of tabs, without
    // having to change anything else in the code. It also automates
    // the process of getting the total number of tabs.
    static public enum Tabs {About, Functions, Variables};
    int docPageCount = 0;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        for (int i = 0; i < Tabs.values().length; ++i) {
            tabs.add(decideNewFragment(i));
        }
    }

    public static String getDir(int item) {
        switch (Tabs.values()[item]) {
        case Functions: return "Fun";
        case Variables: return "Var";
        case About: return "No dir in About page";
        default: return null;
        }
    }

    public boolean tabIsDocPage(int item) {
        return item >= Tabs.values().length;
    }

    public void addDocPage(String uri, int cause) {
        Fragment fragment = new DocFragment();
        // fragment.setRetainInstance(true);
        Bundle args = new Bundle();

        args.putString(DocFragment.URI, uri);
        args.putInt(App.EXTRA_INT, cause);
        fragment.setArguments(args);

        tabs.add(fragment);
    }

    @Override
    public Fragment getItem(int item) {
        return tabs.get(item);
    }

    private Fragment decideNewFragment (int item) {
        switch (Tabs.values()[item]) {
        case Functions:
        case Variables:
            Fragment fragment = new SymbolListFragment();
            // fragment.setRetainInstance(true);
            Bundle args = new Bundle();
            args.putInt(TYPE_NUMBER, item);
            fragment.setArguments(args);
            return fragment;

        case About:
            Fragment af = new AboutFragment();
            // af.setRetainInstance(true);
            return af;
        default:
            App.d("Strange fragment requested from sectionspageradapter");
            return null;
        }
    }

    @Override
    public int getCount() {return tabs.size();}

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

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    public void removeTab(int i) {
        tabs.remove(i);
        notifyDataSetChanged();
    }

    // Which tab opened tab i?
    // -1 means external.
    public int docPageCause(int i) {
        return tabIsDocPage(i) ? ((DocFragment) tabs.get(i)).cause : -1;
    }

    public void detach() {
        App.d("Detaching "+this);
        for (int i = 0; i < Tabs.values().length; ++i)
            tabs.set(i, null);
    }
    public void recycle() {
        App.d("Recycling "+this);
        for (int i = 0; i < Tabs.values().length; ++i)
            tabs.set(i, decideNewFragment(i));
    }
}
