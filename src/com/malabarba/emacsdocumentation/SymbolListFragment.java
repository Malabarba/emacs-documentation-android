package com.malabarba.emacsdocumentation;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class SymbolListFragment extends ListFragment {
    static private final String baseUrl =
        "http://bruce-connor.github.io/emacs-online-documentation/";
    private int typeInt = -1;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	typeInt = getArguments().getInt(SectionsPagerAdapter.TYPE_NUMBER);

        try {	
            setListAdapter(SymbolDatabase.getAdapter(typeInt));
        } catch (Exception e) {
            App.d("Exception:",e);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    static private String symbolToUrl(String s, int type) {
        return baseUrl
            + SectionsPagerAdapter.getDir(type) + "/" 
            + (s.replace("%","%%")).replace("/","%_%");
    }
    
    @Override
    public void onStart() {
        super.onStart();
        setEmptyText(getString(R.string.empty_list));

        getListView().setOnItemClickListener(new OnItemClickListener() {       
                @Override
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                    App.i("Selected item "+position);
                    String name = ((TextView) v.findViewById(R.id.csi_name)).getText().toString();
                    goToDocumentationByName(name, typeInt);
                }});
    }

    // This is externalized so that it can be called from other places.
    // Might be better to put it somewhere else eventually.
    public static void goToDocumentationByName(String name,int type) {
        App.browseUrl(symbolToUrl(name, type), type);
    }
}
