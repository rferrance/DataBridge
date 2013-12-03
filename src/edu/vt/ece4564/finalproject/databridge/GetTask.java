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
import android.widget.EditText;

public class GetTask extends AsyncTask<String, String, String> {

	String httpString = "try again";
	
	private ChatFragment chatFrag_;
	
	public GetTask(ChatFragment mainChat) {
		chatFrag_ = mainChat;
	}
	@Override
	protected String doInBackground(String... uri) {
		try {
			// HttpClient client = new DefaultHttpClient();
			// HttpGet request = new HttpGet(params[0]);
			// HttpResponse result = client.execute(request);

			if (uri[0] == "GET") 
			{				
					
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response;
				String responseString = null;
				response = httpclient.execute(new HttpGet(uri[1]));
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
			else
				return "error";

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		result = result.trim();
		if(result != null && !result.equals("")){
			chatFrag_.addNewMessage(new Message(result,false));
		}
		httpString = result;
		// Do anything with response..
	}

	public String getHttpString() {
		return httpString;
	}
}

