package edu.vt.ece4564.finalproject.databridge;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EmulatorFragment extends Fragment {
	
EmulatorInterface emulatorInterface;
Button startSensor;
Button stopSensor;
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
        startSensor = (Button) rootView.findViewById(R.id.button1);
        stopSensor = (Button) rootView.findViewById(R.id.button1);
        
        startSensor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// tell mainactivity to start sensors
				emulatorInterface.startSensor(); 
			}
		});
        
        stopSensor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// tell mainactivity to stop sensors
				emulatorInterface.stopSensor(); 
			}
		});
        
        return rootView;
    }
}
