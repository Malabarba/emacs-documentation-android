package com.malabarba.emacsdocumentation;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DocFragment extends Fragment {

	TextView view = null;
    Spanned text = null;
    
    public DocFragment() {
		// TODO Auto-generated constructor stub
        
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Uri uri = MainActivity.sharedUri;
            
        if (view == null) {
            view = (TextView)
                inflater.inflate(R.layout.doc_page, null, false);
            view.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        if (text == null) {
            setTextFromUri(view, uri);
                
            // if (!setTextFromUri(view, uri))
            //     App.toast("Doc page created\n" + uri);
            // else
            //     App.toast("NOPE");        
        }
        return view;
    }

    private boolean setTextFromUri(TextView view, Uri uri) {
        // InputStream in = new BufferedInputStream(urlConnection.getInputStream());
 
        try {
            URL url = new URL(uri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                String status = urlConnection.getHeaderField(null);

                if (!status.contains("200 OK")) {
                    App.toast("Failed to retrieve page. Status:\n"+status);
                    text = null;
                    view.setText("Failed");
                } else {
                    text = Html.fromHtml(App.streamToString(in).replace("</br>","<br />"));
                    view.setText(text);
                }
            } catch (Exception e) {
                App.toast(""+ e);
                App.e("Connection error.",e);
                return false;
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            App.toast(""+ e);
            App.e("Connection error.",e);
            return false;
        }
        
        return true;
    }
}
