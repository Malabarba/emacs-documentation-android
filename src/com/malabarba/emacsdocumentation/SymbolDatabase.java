package com.malabarba.emacsdocumentation;

import com.malabarba.util.App;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import com.malabarba.hugesqlitecursor.HugeSQLiteCursor;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import android.support.v4.app.FragmentManager;

/** @author Artur Malabarba */
public class SymbolDatabase extends SQLiteAssetHelper {

//    public static final String TABLE_NAME= "Symbols";
    public static final String TABLE_FUNCTIONS = "Functions";
    public static final String TABLE_VARIABLES = "Variables";
    public static final String TABLE_FACES = "Faces";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_EXT = "External";
    public static final String[] COLUMN_FIELDS= {COLUMN_ID, COLUMN_NAME};//, COLUMN_TYPE};

    private static final String DB_NAME = "symbols.db";
    private static final Integer DB_VERSION = 1;

    private final String stepSize = "50";
    
    private String[] searchText = new String[] {""}; 

    private SQLiteDatabase db;
    private static SymbolCursorAdapter[] adapters;
    // private Context context;
    
    /** @param context */
    public SymbolDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // this.context = context;
        
        setForcedUpgradeVersion(DB_VERSION);

        createAdapters(context);
    }

    public void updateMatches(String t) {
        if (!searchText[0].equals("%"+t+"%")) {
            
            App.stepTiming("Start update");
            searchText[0] = "%"+t+"%";
		
            db = getReadableDatabase();
            App.stepTiming("Received DB");

            adapters[SectionsPagerAdapter.Tabs.Functions.ordinal()].
                changeCursor(new HugeSQLiteCursor(db, stepSize,
                                                  TABLE_FUNCTIONS, COLUMN_FIELDS,
                                                  COLUMN_NAME + " like ?", searchText));
            adapters[SectionsPagerAdapter.Tabs.Variables.ordinal()].
                changeCursor(new HugeSQLiteCursor(db, stepSize,
                                                          TABLE_VARIABLES, COLUMN_FIELDS,
                                                          COLUMN_NAME + " like ?", searchText));
            
            App.stepTiming("Changed Cursors");
        }
    }
    
    public static CursorAdapter getAdapter(int i) {return adapters[i];}
    
    public void createAdapters(Context context) {
        if (adapters == null) {
            adapters = new SymbolCursorAdapter[] {
                // new SymbolCursorAdapter(context, R.layout.cursor_simple_item,
                //                         new String[] {SymbolDatabase.COLUMN_NAME},
                //                         new int[] {R.id.csi_name})
                null,
                new SymbolCursorAdapter(context, R.layout.cursor_simple_item,
                                        new String[] {SymbolDatabase.COLUMN_NAME},
                                        new int[] {R.id.csi_name}),
                new SymbolCursorAdapter(context, R.layout.cursor_simple_item,
                                        new String[] {SymbolDatabase.COLUMN_NAME},
                                        new int[] {R.id.csi_name}),
            };
        }
    }
    
    // static public String getTableName(int i) {
    //     if (null == Type.values()) App.e("Type.values() is null!");
    //     if (i >= Type.values().length) App.e("The value of i ("+i+") was beyond the number of types:\n+"+Type.values());
        
    //     switch (Type.values()[i]) {
    //         // case All:      return App.string(R.string.title_all);
    //     case Function: return App.string(R.string.title_functions);
    //     case Variable: return App.string(R.string.title_variables);
    //     // case About: return App.string(R.string.title_about);
    //     }
    //     return null;
    // }

    // The context is necessary in case we need to query about Fun vs
    // Var. Pass null to avoid querying.
    public Boolean lookForExactMatch(String name, FragmentManager fm) {
        String[] nameArray = new String[] {name};
        
        db = getReadableDatabase();
        
        App.i("Querying Functions");
        Cursor res = db.query(TABLE_FUNCTIONS, COLUMN_FIELDS, COLUMN_NAME + " = ?",
                              nameArray, null,null,null,null);
        String fun = null;
        App.i("Checking if we found something.");
        if (res.getCount() > 0){
            App.i("Got Something!!");
            res.moveToFirst();
            fun = res.getString(1);
        }
        App.i("Closing");
        res.close();

        App.i("Querying Variables");
        res = db.query(TABLE_VARIABLES, COLUMN_FIELDS, COLUMN_NAME + " = ?",
                       nameArray, null,null,null,null);
        String var = null;
        if (res.getCount() > 0){
            App.i("Got Something!!");
            res.moveToFirst();
            var = res.getString(1);
        }
        App.i("Closing");
        res.close();

        if (fun != null) {
            // if (var != null)
            //     SymbolTypeDialog.createAndShow(fm, fun,var);
            // else
            SymbolListFragment.goToDocumentationByName(fun, SectionsPagerAdapter.Tabs.Functions.ordinal(), true);
            // Either way, return true cause it worked
            return true;
        } else if (var != null) {
            SymbolListFragment.goToDocumentationByName(var, SectionsPagerAdapter.Tabs.Variables.ordinal(), true);
            return true;
        }
        
        return false;
    }
    // static private int typeInt = -1;
    static public enum Type {Function, Variable};//, All};
    // static public final String[] sectionNames = {"All", "Functions", "Variables"};
}
