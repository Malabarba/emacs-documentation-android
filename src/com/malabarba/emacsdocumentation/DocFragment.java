package com.malabarba.emacsdocumentation;

import com.malabarba.util.App;
import com.malabarba.util.SettingsManager;
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
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.TextView;

public class DocFragment extends Fragment {
    static public String URI = "com.malabarba.emacsdocumentation.DocFragment.URI";
    public WebView view = null;
    public Spanned text = null;
    public String url = null;
    public int cause = -2;

    public DocFragment() {}

    public void incScale(int f) {
        if (view == null) {
            App.e("Docs WebView is null!: "+this);
            view = (WebView) getView();
            App.e("Docs WebView set to: "+view);
        } else {
            final WebSettings ws = view.getSettings();
            int fontSize = f + ws.getDefaultFontSize();
            ws.setDefaultFixedFontSize(fontSize);
            ws.setDefaultFontSize(fontSize);

            SettingsManager.put("page_font_size", fontSize);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.i("Received args:\n"+ getArguments());
        url = getArguments().getString(URI);
        cause = getArguments().getInt(App.EXTRA_INT, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            // Inflate the webview
            view = (WebView)
                inflater.inflate(R.layout.doc_page, null, false);
            App.i(""+this+" Inflated view: "+view);

            // Load the url
            view.loadUrl(url);

            // Set the fontSize
            final WebSettings ws = view.getSettings();
            final int fontSize = SettingsManager.getInt("page_font_size",16);
            ws.setDefaultFixedFontSize(fontSize);
            ws.setDefaultFontSize(fontSize);
        } else {
            ViewGroup vg = (ViewGroup) view.getParent();
            App.i("getParent on a DocFragment view. Returned: "+vg);

            if (vg != null) vg.removeView(view);
            App.i("ViewGroup detached. Our view is now: "+view);
        }

        return view;
    }

    // private boolean setTextFromUri(TextView view, String uri) {
    //     try {
    //         URL u = new URL(uri);
    //         HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();

    //         try {
    //             InputStream in = new BufferedInputStream(urlConnection.getInputStream());

    //             String status = urlConnection.getHeaderField(null);

    //             if (!status.contains("200 OK")) {
    //                 App.toast("Failed to retrieve page. Status:\n"+status);
    //                 text = null;
    //                 view.setText("Failed");
    //             } else {
    //                 text = Html.fromHtml(App.streamToString(in)
    //                                      .replace("</br>","<br />")
    //                                      .replace("<tr>","<br /><tr>"));
    //                 view.setText(text);
    //             }
    //         } catch (Exception e) {
    //             App.toast(""+ e);
    //             App.e("Connection error.",e);
    //             return false;
    //         } finally {
    //             urlConnection.disconnect();
    //         }
    //     } catch (Exception e) {
    //         App.toast(""+ e);
    //         App.e("Connection error.",e);
    //         return false;
    //     }

    //     return true;
    // }
}
