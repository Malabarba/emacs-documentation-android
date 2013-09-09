package com.malabarba.emacsdocumentation;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class SymbolTypeDialog extends DialogFragment {
    String fun;
    String var;

    static void createAndShow(FragmentManager fm, String fun, String var) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("symbol_type_dialog");
        
        if (prev != null) ft.remove(prev);
        
        ft.addToBackStack(null);

        // Create and show the dialog.
        (new SymbolTypeDialog(fun,var)).show(ft, "symbol_type_dialog");
    }
    
    public SymbolTypeDialog(String fun, String var) {
        super();
        this.var = var;
        this.fun = fun;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(fun + R.string.symbol_is_both)
            .setPositiveButton(R.string.function, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SymbolListFragment.goToDocumentationByName(fun, 0);
                    }})
            .setNegativeButton(R.string.variable, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SymbolListFragment.goToDocumentationByName(var, 1);
                    }});
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
