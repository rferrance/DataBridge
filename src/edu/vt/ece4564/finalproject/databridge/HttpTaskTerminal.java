package edu.vt.ece4564.finalproject.databridge;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;

public class HttpTaskTerminal extends AsyncTask<String, Void, String> {
	private TerminalFragment termFrag_;
	
	public HttpTaskTerminal(TerminalFragment mainTerm) {
		termFrag_ = mainTerm;
	}
	
	@Override
	protected String doInBackground(String... params) {	
		try {		
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(params[0]);
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("cmd", params[1]));	
			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs);
			request.setEntity(entity);
			HttpResponse result = client.execute(request);
				
			InputStream in = result.getEntity().getContent();

			String line = "";

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			String response = sb.toString();
			//always close an InputStream when you are done
			in.close();		
			
			return response;		
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}		
	}
	
	@Override
	protected void onPostExecute(String result) {
		termFrag_.addMessage(result);
	}
}
