package edu.vt.ece4564.finalproject.databridge;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class GetTask extends AsyncTask<String, String, String> {

	private ChatFragment chatFrag_;
	
	public GetTask(ChatFragment mainChat) {
		chatFrag_ = mainChat;
	}
	@Override
	protected String doInBackground(String... uri) {
		try {
			
			//if not GET, then this is not our job.
			if (uri[0] == "GET") {
							
					
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response;
				String responseString = null;
				response = httpclient.execute(new HttpGet(uri[1]+"?app"));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) 
				{
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} 
				else 
				{
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
				return responseString;
			} 
			else{
				return "error";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		//if there isn't a new message, or the new message is empty somehow,
		//do not add it to the chat fragment's messages.
		result = result.trim();
		if(result != null && !result.equals("")){
			chatFrag_.addNewMessage(new Message(result,false));
		}
	}

	
}

