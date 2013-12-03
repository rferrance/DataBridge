package edu.vt.ece4564.finalproject.databridge;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
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
	public static String server_;
	String url = "";
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
		//writeMessage = (EditText) rootView.findViewById(R.id.editText1);
		sendMessage = (Button) rootView.findViewById(R.id.button1);
		//viewMessage = (TextView) rootView.findViewById(R.id.messageView); 
		
		text = (EditText) rootView.findViewById(R.id.text);
		messages = new ArrayList<Message>();
		adapter = new AwesomeAdapter(getActivity(), messages);
        setListAdapter(adapter);
        url = getArguments().getString(server_);
		sendMessage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				/*
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
				writeMessage.setText("");
				*/
				sendMessage();
			}
		});
		handler_ = new Handler();
		runnable_ = new Runnable(){
			@Override
		    public void run(){
							
				GetTask gTask = new GetTask(ChatFragment.this);
				gTask.execute("GET",url);
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
		//viewMessage.setText("");
//		for (String S : messageList) {
//			viewMessage.append(S);
//		}
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
                    chatInterface.sendMessage(newMessage);
                    //new SendMessage().execute();
            }
    }
	
	/*private class SendMessage extends AsyncTask<Void, String, String>
    {
		GetTask gTask = new GetTask(ChatFragment.this);
        @Override
        protected String doInBackground(Void... params) {
        	
        	gTask.execute("GET",url);
            
            
//            this.publishProgress(String.format("%s started writing", sender));
//            try {
//                    Thread.sleep(500); //simulate a network call
//            }catch (InterruptedException e) {
//                    e.printStackTrace();
//            }
//            this.publishProgress(String.format("%s has entered text", sender));
//            try {
//                    Thread.sleep(500);//simulate a network call
//            }catch (InterruptedException e) {
//                    e.printStackTrace();
//            }
            
            //return Utility.messages[rand.nextInt(Utility.messages.length-1)];
        	 if(messages.get(messages.size()-1).isStatusMessage)//check if there is any status message, now remove it.
             {
                     messages.remove(messages.size()-1);
             }
            //return messages.get(messages.size()-1).getMessage();
        	 return "thinking....";
            
            
        }
//        @Override
//        public void onProgressUpdate(String... v) {
//                
//                if(messages.get(messages.size()-1).isStatusMessage)//check whether we have already added a status message
//                {
//                        messages.get(messages.size()-1).setMessage(v[0]); //update the status for that
//                        adapter.notifyDataSetChanged();
//                        getListView().setSelection(messages.size()-1);
//                }
//                else{
//                        addNewMessage(new Message(true,v[0])); //add new message, if there is no existing status message
//                }
//        }
        @Override
        protected void onPostExecute(String text) {
                if(messages.get(messages.size()-1).isStatusMessage)//check if there is any status message, now remove it.
                {
                        messages.remove(messages.size()-1);
                }
                
                //addNewMessage(new Message(text, false)); // add the original message from server.
        }
    }*/
	
	/*
	 * Change the server address
	 */
	public void changeAddress(String newAddress) {
		server_ = newAddress;
	}
	/*
	 * Overrides the onDestroy so that the sensors 
	 * can be turned off since they use a massive amount
	 * of battery power
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if(handler_ != null && runnable_ != null) {
			handler_.removeCallbacks(runnable_);
		}
		super.onDestroy();
	}
	@Override
	public void onDestroyView() {
		if(handler_ != null && runnable_ != null) {
			handler_.removeCallbacks(runnable_);
		}
		super.onDestroyView();
	}
}
