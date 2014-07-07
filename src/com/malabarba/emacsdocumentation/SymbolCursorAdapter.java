package com.malabarba.emacsdocumentation;

import com.malabarba.util.App;
//import com.malabarba.util.HugeSQLiteCursor;

import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;

//import com.malabarba.util.HugeSQLiteCursor;

public class SymbolCursorAdapter extends SimpleCursorAdapter {
//    private String table;
//    private String[] columns;
//    private String query;
//    private String limit;
//    private int numericLimit;
	
    public SymbolCursorAdapter(Context context, int layout, String[] from, int[] to// , 
                               // String table, String[] columns, String query, String limit
                               ) {
        super(context, layout, null, from, to, 0);

        // this.table = table;
        // this.columns = columns;
        // this.query = query;
        // this.limit = limit;
        // if (limit == null) numericLimit = 0;
        // else numericLimit = Integer.parseInt(limit);
        
	
        // TODO Auto-generated constructor stub
		
    }

    // public void changeCursor(String[] searchText, SQLiteDatabase db){
    //     // count = queryCount(searchText, db);
    //     App.stepTiming("Got count");
        
    //     // Cursor c = db.queryWithFactory(new HugeCursor.Factory(count), false,
    //     //                                table, columns, query, searchText,
    //     //                                null, null, null, limit);
    //     App.stepTiming("Got cursor");
        
    //     super.changeCursor(c);
    //     App.stepTiming("Changed Cursor");
    // }

        
}
