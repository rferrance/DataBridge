package edu.vt.ece4564.finalproject.databridge;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity 
	implements ChatInterface, TerminalInterface, EmulatorInterface {
		
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	String serverAddress;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	// Fragments to be used.
	EmulatorFragment emulFragment;
	TerminalFragment termFragment;
	ChatFragment chatFragment;
	
	// sensor manager for the EmulatorFragment
	public static SensorManager sensorManager;
	public static Context context;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//addPreferencesFromResource(R.layout.fragmented_preferences);
		PreferenceManager.setDefaultValues(this, R.layout.fragmented_preferences, false);
		
		emulFragment = new EmulatorFragment();
		termFragment = new TerminalFragment();
		chatFragment = new ChatFragment();
		
		context = getApplicationContext();
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			
			if(position == 0) {
				fragment = new EmulatorFragment();
				// fragment = emulFragment;
				// Allows sending of arguements to the fragment, I'll keep it for now
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
			} else if(position == 1) {
				fragment = new TerminalFragment();
				//fragment = termFragment;
				Bundle args = new Bundle();
				if(MainActivity.context != null) { 
		        	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.context); 
		        	Log.d("!!!", sharedPrefs.getString("ip_preference", "NULL")); 
		        	Log.d("!!!", sharedPrefs.getString("port_preference", "NULL")); 
		        	serverAddress = sharedPrefs.getString( "ip_preference", "") + ":" + sharedPrefs.getString("port_preference", ""); 
		        } else { 
		        	Log.d("!!!", "context is null"); 
		        }
				args.putString(TerminalFragment.server_, "http://" + serverAddress + "/cli");
				fragment.setArguments(args);
			} else if (position == 2) {
				fragment = new ChatFragment();
				//fragment = chatFragment;
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "EMULATE";
			case 1:
				return "TERMINAL";
			case 2:
				return "CHAT";
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	/*
	 * Send a message
	 * @see edu.vt.ece4564.finalproject.databridge.ChatInterface#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String aMessage) {
		// TODO Auto-generated method stub
		
		PostTask pTask = new PostTask("http://10.0.0.5:8080/chat");
		pTask.execute("POST",aMessage);
	}
	
	/*
	 * Receive a message
	 * @see edu.vt.ece4564.finalproject.databridge.ChatInterface#receiveMessage(java.lang.String)
	 */
	@Override
	public void receiveMessage() {
		// TODO Auto-generated method stub
		
		GetTask gTask = new GetTask();
		gTask.execute("GET");
	}
	/*
	 * Send a command
	 * @see edu.vt.ece4564.finalproject.databridge.TerminalInterface#sendCommand(java.lang.String)
	 */
	@Override
	public void sendCommand(String aMessage) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Activates when connection activity finishes
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if(resultCode == RESULT_OK) {
				if((data.getStringExtra("type") != null) &&
						(data.getStringExtra("type").equals("connect"))) {
					serverAddress = data.getStringExtra("result");
					Toast.makeText(getApplicationContext(), serverAddress, Toast.LENGTH_LONG).show();
					//ChatFragment frag = (ChatFragment) mSectionsPagerAdapter.getItem(2);
					//frag.addMessage(serverAddress);
					// TODO connect to server
					
					Toast.makeText(getApplicationContext(), "Connected to: " + serverAddress, Toast.LENGTH_LONG).show();
				} else if((data.getStringExtra("type") != null) &&
						(data.getStringExtra("type").equals("settings"))) {
					// TODO maybe something
				}
			}
		    if (resultCode == RESULT_CANCELED) {    
		    	//Write your code if there's no result
		    	//TODO anything really
		    }
		}
	}

	@Override
	public void startSensor() {
		// TODO Auto-generated method stub
		// Start sensor capture here
	}

	@Override
	public void stopSensor() {
		// TODO Auto-generated method stub
		// Stop sensor capture here
	}
	
	@Override public boolean onOptionsItemSelected(MenuItem item) {
	// the settings option takes the user to the settings page 
		if(item.getItemId() == R.id.action_settings) { 
			Intent intent = new Intent(this, SamplePreferenceActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        startActivityForResult(intent, 1);
			return true; 
		} else {
			return super.onOptionsItemSelected(item); 
		}
	}
	
	// When key is pressed
	// From http://stackoverflow.com/questions/6937782/android-how-to-put-a-dialog-box-after-pressing-a-back-button
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        exitByBackKey();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	 
	// Exit the program by pressing back key
	// From http://stackoverflow.com/questions/6937782/android-how-to-put-a-dialog-box-after-pressing-a-back-button
	protected void exitByBackKey() {
	    AlertDialog alertbox = new AlertDialog.Builder(this)
	    	.setMessage("Do you want to exit the application?")
	    	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	
	        // do something when the button is clicked
	        public void onClick(DialogInterface arg0, int arg1) {
	            finish();
	            //close();
	        }
	    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
	        // do something when the button is clicked
	        public void onClick(DialogInterface arg0, int arg1) {
	        }
		    }).show();
		}
}
