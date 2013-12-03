package edu.vt.ece4564.finalproject.databridge;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatFragment extends ListFragment {

	Button sendMessage;
	TextView viewMessage;
	ArrayList<String> messageList; // List of messages (strings)
	ChatInterface chatInterface; // interface to MainActivity
	ArrayList<Message> messages; // Messages instead of Strings to use chat
									// bubbles
	AwesomeAdapter adapter;
	EditText text; // Text box user types message in
	static String sender = "Server";
	public static String server_; // Used for server ip
	String url = ""; // Also used for server ip
	private Runnable runnable_;
	private Handler handler_;

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
			chatInterface = (ChatInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ServerFragmentInterface.");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.chat_fragment, container,
				false);
		messageList = new ArrayList<String>();
		sendMessage = (Button) rootView.findViewById(R.id.button1);
		text = (EditText) rootView.findViewById(R.id.text);
		messages = new ArrayList<Message>();
		adapter = new AwesomeAdapter(getActivity(), messages);
		setListAdapter(adapter);
		url = getArguments().getString(server_);

		// When the "send" button is clicked. send the message.
		sendMessage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				sendMessage();
			}
		});

		// The following code checks for new messages on the server. It checks
		// once a second.
		handler_ = new Handler();
		runnable_ = new Runnable() {
			@Override
			public void run() {

				GetTask gTask = new GetTask(ChatFragment.this);
				gTask.execute("GET", url);
				// sets the runnable to run again in "delay_" milliseconds
				handler_.postDelayed(this, 1000);
			}
		};
		runnable_.run();
		return rootView;
	}

	/*
	 * Get a new message and add it to the chat array Call it from mainactivity
	 */
	public void addMessage(String newMessage) {
		messageList.add(newMessage); // Add the message to the messagelist
	}

	void addNewMessage(Message m) {
		messages.add(m);
		adapter.notifyDataSetChanged();
		getListView().setSelection(messages.size() - 1);
	}

	public void sendMessage() {
		String newMessage = text.getText().toString().trim();
		if (newMessage.length() > 0) {
			text.setText("");
			addNewMessage(new Message(newMessage, true));
			chatInterface.sendMessage(newMessage);
		}
	}

	/*
	 * Change the server address
	 */
	public void changeAddress(String newAddress) {
		server_ = newAddress;
	}

	/*
	 * Overrides the onDestroy method so that when we close the runnable does
	 * not continue polling the server which uses up battery power
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if (handler_ != null && runnable_ != null) {
			handler_.removeCallbacks(runnable_);
		}
		super.onDestroy();
	}

	/*
	 * Overrides the onDestroyViw method so that when we switch fragments the
	 * runnable does not continue polling the server which uses up battery power
	 */
	@Override
	public void onDestroyView() {
		if (handler_ != null && runnable_ != null) {
			handler_.removeCallbacks(runnable_);
		}
		super.onDestroyView();
	}
}
