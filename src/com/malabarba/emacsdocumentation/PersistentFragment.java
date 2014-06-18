package com.malabarba.emacsdocumentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class PersistentFragment extends Fragment {
    final public static String TAG =
        "com.malabarba.emacsdocumentation.PersistentFragment";

    public SectionsPagerAdapter sectionPager = null;

    public PersistentFragment() {super();}
    
    static public PersistentFragment get(FragmentManager fm) {
        PersistentFragment pf = (PersistentFragment)
            fm.findFragmentByTag(PersistentFragment.TAG);

        App.d("Received  PersistentFragment: " + pf);
        
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (pf == null) {
            pf = new PersistentFragment();
            fm.beginTransaction().add(pf, TAG).commit();
            pf.sectionPager = new SectionsPagerAdapter(fm);
            App.d("Had to create PersistentFragment: " + pf);
        } else if (pf.sectionPager == null) {
            pf.sectionPager = new SectionsPagerAdapter(fm);
            App.e("Something went wrong! The sectionPager in persistentFragment is null!");
        } else
            pf.sectionPager.recycle();

        return pf;
    }

    @Override
    public String toString() {
        return ""+"PersistentFragment("+sectionPager+")";
    }

    @Override
    public void onDetach() {
        sectionPager.detach();
        super.onDetach();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
