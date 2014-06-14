package com.malabarba.emacsdocumentation;

import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DocFragment extends Fragment {

	public DocFragment() {
		// TODO Auto-generated constructor stub
        
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Uri uri = MainActivity.sharedUri;
        TextView view = (TextView)
            inflater.inflate(R.layout.doc_page, container, false);
        view.setMovementMethod(LinkMovementMethod.getInstance());

        if (setTextFromUri(view, uri))
            App.toast("Doc page created\n" + uri);
        else
            App.toast("NOPE");
        
        return view;
    }

    private boolean setTextFromUri(TextView view, Uri uri) {
        // InputStream in = new BufferedInputStream(urlConnection.getInputStream());
 
        try {
        	   
            URL url = new URL(uri.toString());
            URLConnection urlConnection = url.openConnection();

            App.toast(""+ urlConnection.getHeaderField(null));
            
            view.setText((String) urlConnection.getContent());
        } catch (Exception e) {
            App.toast(""+ e);
            App.e("Connection error.",e);
        }
        finally {}
        //        finally {in.close();}
        
        
        return true;
    }
}
