package edu.vt.ece4564.finalproject.databridge;

import java.util.concurrent.SynchronousQueue;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EmulatorFragment extends Fragment {
	private boolean is_streaming_;

	private SynchronousQueue<byte[]> outgoing_data_;
	private UDPTask udp_task_;
	private SensorReader sensor_reader_;

	EmulatorInterface emulatorInterface;
	Button startSensor;
	Button leftClick;
	Button rightClick;

	/*
	 * This allows for the fragment to have an interface with mainactivity so
	 * that it can send chat messages
	 * 
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
		View rootView = inflater.inflate(R.layout.emulator_fragment, container,
				false);
		
		Activity a = getActivity();
        if(a != null)
        	a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		is_streaming_ = false;
		outgoing_data_ = new SynchronousQueue<byte[]>();

		startSensor = (ToggleButton) rootView.findViewById(R.id.toggleButton1);
		leftClick = (Button) rootView.findViewById(R.id.leftclick);
		rightClick = (Button) rootView.findViewById(R.id.rightclick);

		startSensor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (is_streaming_)
					stopStreaming();

				else if (!is_streaming_)
					startStreaming();
			}
		});

		leftClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.context, "Left Click",
						Toast.LENGTH_SHORT).show();
			}
		});

		rightClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.context, "Right Click",
						Toast.LENGTH_SHORT).show();
			}
		});

		return rootView;
	}

	@Override
	public void onDestroy() {
		if (sensor_reader_ != null)
			sensor_reader_.stopReadingSensors();
		if (udp_task_ != null)
			udp_task_.cancel(true);
		super.onDestroy();
	}

	// ========================================================================//
	// ============================Helper
	// Functions============================//
	// starts the timer and UDP task running
	private void startStreaming() {

		// requestSensorDataClear();

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.context);

		udp_task_ = new UDPTask(outgoing_data_, sharedPrefs.getString(
				"ip_preference", "NULL"), 2000);

		sensor_reader_ = new SensorReader(outgoing_data_,
				MainActivity.sensorManager);
		udp_task_.execute();
		sensor_reader_.start();
		is_streaming_ = true;
	}

	// stops the timer and UDP task
	private void stopStreaming() {
		udp_task_.cancel(true);
		sensor_reader_.stopReadingSensors();
		is_streaming_ = false;
	}
}
