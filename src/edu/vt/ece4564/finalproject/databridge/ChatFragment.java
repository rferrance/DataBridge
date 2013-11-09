package edu.vt.ece4564.finalproject.databridge;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatFragment extends Fragment {
EditText writeMessage;
Button sendMessage;
TextView viewMessage;
ArrayList<String> messageList; // List of messages
ChatInterface chatInterface;

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
	        chatInterface = (ChatInterface) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString()
	                + " must implement ServerFragmentInterface.");
	    }
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_fragment, container, false);
        messageList = new ArrayList<String>();
        writeMessage = (EditText)rootView.findViewById(R.id.editText1);
		sendMessage = (Button) rootView.findViewById(R.id.button1);
		viewMessage = (TextView) rootView.findViewById(R.id.textView2); // Textview for all messages
		
		sendMessage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO do checking for blank and other fun jazz
				// Send the message using the interface
				chatInterface.sendMessage(writeMessage.getText().toString()); 
			}
		});
        return rootView;
    }
	
	/*
	 * Get a new message and add it to the chat array
	 * Call it from mainactivity
	 */
	public void addMessage(String newMessage) {
		messageList.add(newMessage); // Add the message to the messagelist
		// TODO do something to update the textview
	}
}
