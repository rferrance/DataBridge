package edu.vt.ece4564.finalproject.databridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This class provides an activity for connecting to the server
 * It pops up when the activity starts
 */
public class ConnectionActivity extends Activity {
	private Button confirmAddress;
	private EditText newAddress;
	private EditText newPort;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection_activity);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		
		newAddress = (EditText)findViewById(R.id.editText1);
        newPort = (EditText)findViewById(R.id.editText2);
		confirmAddress = (Button) findViewById(R.id.button1);
		
		confirmAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if((!newAddress.getText().toString().equals(""))&&
				(!newPort.getText().toString().equals(""))) {
					String result = newAddress.getText().toString().trim()
							+ ":" + newPort.getText().toString().trim();
					Toast.makeText(getApplicationContext(), "Server address set.", Toast.LENGTH_LONG).show();
					Intent returnIntent = new Intent();
					returnIntent.putExtra("type","connect");
					returnIntent.putExtra("result",result);
					setResult(RESULT_OK,returnIntent);     
					finish();
				} else if (newAddress.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "Please enter an address.", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "Please enter a port Address.", Toast.LENGTH_LONG).show();
				}
			}
		});

	}
}
