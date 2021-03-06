package edu.vt.ece4564.finalproject.databridge;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class PostTask extends AsyncTask<String, String, String> {

	String URL = "";

	
	public PostTask(String url)
	{
		URL = url;
	}
	
	@Override
	protected String doInBackground(String... uri) {
		try {

			//if we aren't posing, not our job.
			if (uri[0] == "POST") {

				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(URL);

				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("newMessage", uri[1]));
				pairs.add(new BasicNameValuePair("sender", "client"));
				
				
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs);
				request.setEntity(entity);

				HttpResponse response = client.execute(request);
				String responseString = null;
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
				return responseString;
		

			} 
			//something went wrong.
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
		//We don't need to do anything with the response.
	}

}
