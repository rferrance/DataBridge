package edu.vt.ece4564.finalproject.databridge;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class EmulatorFragment extends Fragment {
	
EmulatorInterface emulatorInterface;
Button startSensor;
Button leftClick;
Button rightClick;
	/*
	 * This allows for the fragment to have an interface with mainactivity
	 * so that it can send chat messages
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	
	    // This makes sure that the container activity has implemented
	    // the callback interface. If not, it throws an exception
	    try {
	        emulatorInterface = (EmulatorInterface) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString()
	                + " must implement ServerFragmentInterface.");
	    }
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.emulator_fragment, container, false);
        
        startSensor = (ToggleButton) rootView.findViewById(R.id.toggleButton1);
        leftClick = (Button) rootView.findViewById(R.id.leftclick);
        rightClick = (Button) rootView.findViewById(R.id.rightclick);
        
        startSensor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// tell mainactivity to start sensors
				emulatorInterface.startSensor(); 
			}
		});
        
        
        leftClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO something to do with clicking
			}
		});
        
        rightClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO something to do with clicking 
			}
		});
        
        return rootView;
    }
}
