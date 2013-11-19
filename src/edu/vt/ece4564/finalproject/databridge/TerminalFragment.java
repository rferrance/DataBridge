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

public class TerminalFragment extends Fragment {
	EditText writeCommand;
	Button sendCommand;
	TextView viewCommands;
	ArrayList<String> commandList; // List of messages
	TerminalInterface terminalInterface;
	
	public String cmd;
	public static String server_ = "";
	
	// runnable to call async task to send cmd to server!
	Runnable post = new Runnable() {
		@Override
		public void run() {
			try{
				final HttpTaskTerminal newtask = new HttpTaskTerminal(TerminalFragment.this);
				newtask.execute(server_ + "/cli", "/c dir");
			}
			catch(Exception e) {
				e.printStackTrace();
				viewCommands.setText(viewCommands.getText().toString() + e.getMessage() + "\n");
			}
		}
	};
	
	private Thread t1 = new Thread(post, "post");	
	
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
	        terminalInterface = (TerminalInterface) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString()
	                + " must implement ServerFragmentInterface.");
	    }
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.terminal_fragment, container, false);
        commandList = new ArrayList<String>();
        writeCommand = (EditText)rootView.findViewById(R.id.editText1);
		sendCommand = (Button) rootView.findViewById(R.id.button1);
		viewCommands = (TextView) rootView.findViewById(R.id.messageView); // Textview for all messages
		
		sendCommand.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO do checking for blank and other fun jazz
				// Send the command using the interface
				try {
					cmd = writeCommand.getText().toString();
					//terminalInterface.sendCommand(cmd); 
					viewCommands.setText(viewCommands.getText().toString() + cmd + "\n");
					writeCommand.setText("");
						
					if((t1.getState() == Thread.State.NEW)){
						t1.start();
					}else{
						t1.run();
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					viewCommands.setText(viewCommands.getText().toString() + e.getMessage() + "\n");
				}
			}
		});
        return rootView;
    }
	
	/*
	 * Get a new message and add it to the chat array
	 * Call it from mainactivity
	 */
	public void addMessage(String newCommand) {
		viewCommands.setText(viewCommands.getText().toString() + newCommand +  "\n");
		commandList.add(newCommand); // Add the message to the messagelist
		// TODO do something to update the textview
	}
}
