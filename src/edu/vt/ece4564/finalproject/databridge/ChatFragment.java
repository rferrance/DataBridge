package edu.vt.ece4564.finalproject.databridge;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatFragment extends ListFragment {
	EditText writeMessage;
	Button sendMessage;
	TextView viewMessage;
	ArrayList<String> messageList; // List of messages
	ChatInterface chatInterface;
	ArrayList<Message> messages;
    AwesomeAdapter adapter;
    EditText text;
    static Random rand = new Random();        
    static String sender = "Server";


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
		//messageList = new ArrayList<String>();
		//writeMessage = (EditText) rootView.findViewById(R.id.editText1);
		sendMessage = (Button) rootView.findViewById(R.id.button1);
		//viewMessage = (TextView) rootView.findViewById(R.id.messageView); 
		
		text = (EditText) rootView.findViewById(R.id.text);
		messages = new ArrayList<Message>();
		adapter = new AwesomeAdapter(getActivity(), messages);
        setListAdapter(adapter);
		sendMessage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { /*
				// TODO do checking for blank and other fun jazz
				// Send the message using the interface
				String messageTyped = writeMessage.getText().toString();
				//check for empty string before doing anything
				if (!messageTyped.equals("")) 
				{
					chatInterface.sendMessage(messageTyped);
					messageList.add("Client: " + messageTyped + "\n");
					//viewMessage.append("Client: " + messageTyped + "\n");
					
					viewMessage.setText("");
					for (String s : messageList) {
						viewMessage.append(s);
						viewMessage.append("\n");
					}

				}
				writeMessage.setText("");*/
				sendMessage();
			}
		});
		return rootView;
	}

	/*
	 * Get a new message and add it to the chat array Call it from mainactivity
	 */
	public void addMessage(String newMessage) {
		messageList.add(newMessage); // Add the message to the messagelist
		viewMessage.setText("");
		for (String S : messageList) {
			viewMessage.append(S);
		}
		// TODO maybe something better idk I'm just doing this to debug
	}
	
	void addNewMessage(Message m)
    {
            messages.add(m);
            adapter.notifyDataSetChanged();
            getListView().setSelection(messages.size()-1);
    }
	
	public void sendMessage()
    {
            String newMessage = text.getText().toString().trim();
            if(newMessage.length() > 0)
            {
                    text.setText("");
                    addNewMessage(new Message(newMessage, true));
                    new SendMessage().execute();
            }
    }
	
	private class SendMessage extends AsyncTask<Void, String, String>
    {
        @Override
        protected String doInBackground(Void... params) {
        	try {
                    Thread.sleep(2000); //simulate a network call
            }catch (InterruptedException e) {
                    e.printStackTrace();
            }
            
            this.publishProgress(String.format("%s started writing", sender));
            try {
                    Thread.sleep(2000); //simulate a network call
            }catch (InterruptedException e) {
                    e.printStackTrace();
            }
            this.publishProgress(String.format("%s has entered text", sender));
            try {
                    Thread.sleep(3000);//simulate a network call
            }catch (InterruptedException e) {
                    e.printStackTrace();
            }
            
            return Utility.messages[rand.nextInt(Utility.messages.length-1)];
        }
        @Override
        public void onProgressUpdate(String... v) {
                
                if(messages.get(messages.size()-1).isStatusMessage)//check wether we have already added a status message
                {
                        messages.get(messages.size()-1).setMessage(v[0]); //update the status for that
                        adapter.notifyDataSetChanged();
                        getListView().setSelection(messages.size()-1);
                }
                else{
                        addNewMessage(new Message(true,v[0])); //add new message, if there is no existing status message
                }
        }
        @Override
        protected void onPostExecute(String text) {
                if(messages.get(messages.size()-1).isStatusMessage)//check if there is any status message, now remove it.
                {
                        messages.remove(messages.size()-1);
                }
                
                addNewMessage(new Message(text, false)); // add the orignal message from server.
        }
    }
}
