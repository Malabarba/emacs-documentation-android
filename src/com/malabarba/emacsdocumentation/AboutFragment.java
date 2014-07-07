package com.malabarba.emacsdocumentation;

import com.malabarba.util.App;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {

	public AboutFragment() {
		// TODO Auto-generated constructor stub
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView view = (TextView) inflater.inflate(R.layout.fragment_about, container, false);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }
}
