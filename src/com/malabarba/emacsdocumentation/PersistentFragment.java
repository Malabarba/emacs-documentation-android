package com.malabarba.emacsdocumentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

class PersistentFragment extends Fragment {
    final public static String TAG =
        "com.malabarba.emacsdocumentation.PersistentFragment";

    public SectionsPagerAdapter sectionPager = null;
    
    static public PersistentFragment get(FragmentManager fm) {
        PersistentFragment pf = (PersistentFragment)
            fm.findFragmentByTag(PersistentFragment.TAG);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (pf == null) {
            pf = new PersistentFragment();
            fm.beginTransaction().add(pf, TAG).commit();
            pf.sectionPager = new SectionsPagerAdapter(fm);
        }

        return pf;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);
    }

}
