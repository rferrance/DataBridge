package edu.vt.ece4564.finalproject.databridge;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		Intent intent = new Intent(this, ConnectionActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, 1);
        
        
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
				// Allows sending of arguements to the fragment, I'll keep it for now
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
			} else if(position == 1) {
				fragment = new TerminalFragment();
				Bundle args = new Bundle();
				args.putString(TerminalFragment.server_, "http://172.31.171.139:8080");
				fragment.setArguments(args);
			} else if (position == 2) {
				fragment = new ChatFragment();
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
			if(resultCode == RESULT_OK){      
				serverAddress = data.getStringExtra("result");
				Toast.makeText(getApplicationContext(), serverAddress, Toast.LENGTH_LONG).show();
				//ChatFragment frag = (ChatFragment) mSectionsPagerAdapter.getItem(2);
				//frag.addMessage(serverAddress);
				// TODO connect to server
				
				Toast.makeText(getApplicationContext(), "Connected to: " + serverAddress, Toast.LENGTH_LONG).show();
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

}
