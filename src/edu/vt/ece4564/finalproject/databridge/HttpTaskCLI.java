package org.vt.ece4564.CourseNotifier;

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

public class HttpTask extends AsyncTask<String, Void, String> {
	private MainActivity mainActivity_;
	
	public HttpTask(MainActivity mainA) {
		mainActivity_ = mainA;
	}
	
	@Override
	protected String doInBackground(String... params) {	
		try {		
			//HttpClient client = new DefaultHttpClient();
			//HttpGet request = new HttpGet(params[0]);
			//HttpResponse result = client.execute(request);
			
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest");
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("TERMYEAR", params[0]));
			pairs.add(new BasicNameValuePair("CORE_CODE", "AR%"));
			pairs.add(new BasicNameValuePair("SUBJ_CODE", "%"));	
			pairs.add(new BasicNameValuePair("CRN", params[1]));
			pairs.add(new BasicNameValuePair("open_only", params[2]));	
			
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
			
			String out = "";
			
			if(response.contains("NO SECTIONS FOUND FOR THIS INQUIRY.") == true) out = "NO SECTIONS FOUND FOR THIS CRN.";
			else if (response.contains("There was a problem with your request:") == true) out = "THERE WAS A PROBLEM WITH YOUR REQUEST."; 
			//next two only used when "signed in"
			else if(response.contains("style=background-color:#d3d3d3") == true) out = "CLASS SECTION FULL";
			else if (response.contains("<B class=green_msg>") == true){
				int loc = response.indexOf("<B class=green_msg>");
				String temp = response.substring((loc+19), (loc+20));
				
				out = temp + " SEATS AVAILABLE!";
			}else out = " SEATS AVAILABLE!";
			
			return out;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}		
	}
	
	@Override
	protected void onPostExecute(String result) {
		mainActivity_.last(result);
	}
}
