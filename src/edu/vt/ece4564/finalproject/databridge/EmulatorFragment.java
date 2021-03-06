package edu.vt.ece4564.finalproject.databridge;

import java.util.concurrent.SynchronousQueue;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

/*
 * Fragment for the mouse emulator allows the user to move the mouse
 * on the server by tilting the phone.
 */
public class EmulatorFragment extends Fragment {
	private boolean is_streaming_;

	private SynchronousQueue<byte[]> outgoing_data_;
	private UDPTask udp_task_;
	private SensorReader sensor_reader_;

	Button startSensor;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.emulator_fragment, container,
				false);
		

		Activity a = getActivity();
		// Disables rotation of the phone since it would mess up the UI and data
		if (a != null) {
			a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		is_streaming_ = false;
		outgoing_data_ = new SynchronousQueue<byte[]>();

		startSensor = (ToggleButton) rootView.findViewById(R.id.toggleButton1);

		startSensor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (is_streaming_)
					stopStreaming();

				else if (!is_streaming_)
					startStreaming();
			}
		});
		
		
		
		/*
		 * Set the on touch listener so that we can do left and right
		 * clicks by touching the corresponding UI area on the mouse
		 * Used the idea from 
		 * http://blahti.wordpress.com/2012/06/26/images-with-clickable-areas/
		 */
		rootView.setOnTouchListener(new OnTouchListener() {
            /*
        	 * Method to determine if click occurs
        	 */
        	@Override
        	public boolean onTouch(View v, MotionEvent ev) {
        		final int action = ev.getAction();
        		final int evX = (int) ev.getX();
        		final int evY = (int) ev.getY();
        		switch (action) {
        		case MotionEvent.ACTION_DOWN:
        			break;
        		case MotionEvent.ACTION_UP:
        			// On the UP, we do the click action.
        			// The hidden image (image_areas) has three different hotspots on
        			// it.
        			// The colors are red, blue, and yellow.
        			// Use image_areas to determine which region the user touched.
        			// (2)
        			int touchColor = getHotspotColor(R.id.image_areas, evX, evY);
        			// Compare the touchColor to the expected values.
        			// Switch to a different image, depending on what color was touched.
        			// Note that we use a Color Tool object to test whether the
        			// observed color is close enough to the real color to
        			// count as a match. We do this because colors on the screen do
        			// not match the map exactly because of scaling and
        			// varying pixel density.
        			ColorTool ct = new ColorTool();
        			int tolerance = 25;
        			// nextImage = R.drawable.p2_ship_default;
        			// (3)
        			if (ct.closeMatch(Color.RED, touchColor, tolerance)) {
        				// Do the action associated with the RED region
        				new NetworkClickTask().executeOnExecutor(
        						AsyncTask.THREAD_POOL_EXECUTOR, "left");
        				
        			} else if (ct.closeMatch(Color.GREEN, touchColor, tolerance)) {
        				// Do the action associated with the RED region
        				new NetworkClickTask().executeOnExecutor(
        						AsyncTask.THREAD_POOL_EXECUTOR, "right");
        				
        			}else {
        				// Color was not red or green
        			}
        			break;
        		} // end switch
        		return true;
        	}
        });

		return rootView;
	}

	/*
	 * Overrides the onDestroy so that the sensors 
	 * can be turned off since they use a massive amount
	 * of battery power
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy(){
		if(sensor_reader_ != null){
			sensor_reader_.stopReadingSensors();
		}
		if(udp_task_ != null){
			udp_task_.cancel(true);
		}
		super.onDestroy();
	}
	@Override
	public void onDestroyView(){
		if (sensor_reader_ != null){
			sensor_reader_.stopReadingSensors();
		}
		if (udp_task_ != null){
			udp_task_.cancel(true);
		}
		super.onDestroyView();
	}
	
	

	// ========================================================================//
	// ============================Helper Functions============================//
	// starts the timer and UDP task running
	private void startStreaming(){
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.context);

		udp_task_ = new UDPTask(outgoing_data_, sharedPrefs.getString(
				"ip_preference", "NULL"), Integer.parseInt(sharedPrefs.getString(
				"udp_port_preference", "NULL")));

		sensor_reader_ = new SensorReader(outgoing_data_,
				MainActivity.sensorManager);
		udp_task_.execute();
		sensor_reader_.start();
		is_streaming_ = true;
	}

	// stops the timer and UDP task
	private void stopStreaming() {
		udp_task_.cancel(true);
		sensor_reader_.stopReadingSensors();
		is_streaming_ = false;
	}
	
	/*
	 * Get the color of the pixel in the overlay
	 * Code used from 
	 * http://blahti.wordpress.com/2012/06/26/images-with-clickable-areas/
	 */
	public int getHotspotColor(int hotspotId, int x, int y) {
		ImageView img = (ImageView) getView().findViewById(hotspotId);
		img.setDrawingCacheEnabled(true);
		Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
		img.setDrawingCacheEnabled(false);
		return hotspots.getPixel(x, y);
	}
	
	/*
	 * Tool to help determine overlay color
	 * Code used from
	 * http://blahti.wordpress.com/2012/06/26/images-with-clickable-areas/
	 */
	public class ColorTool {
		
		public boolean closeMatch(int color1, int color2, int tolerance) {
			if ((int) Math.abs(Color.red(color1) - Color.red(color2)) > tolerance)
				return false;
			if ((int) Math.abs(Color.green(color1) - Color.green(color2)) > tolerance)
				return false;
			if ((int) Math.abs(Color.blue(color1) - Color.blue(color2)) > tolerance)
				return false;
			return true;
		} // end match
	}
}

class NetworkClickTask extends AsyncTask<String, Void, Boolean>
{
	protected Boolean doInBackground(String... params){
		return sendQueryString(params[0]);
	}

	protected void onPostExecute(boolean result){
		if(!result)
			Toast.makeText(MainActivity.context, "Click Failed",
					Toast.LENGTH_SHORT).show();
	}
	
	private boolean sendQueryString(String url){
		HttpGet 		request;
		HttpClient 		client = new DefaultHttpClient();
		
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.context);
	    
	    request = new HttpGet("http://" + sharedPrefs.getString(
				"ip_preference", "NULL") + ":" + sharedPrefs.getString(
				"port_preference", "NULL") + "/dataupdateterminal?req_click_" + url);
	    
		try{
			client.execute(request);
			return true;
		}catch(Exception e){
			Log.e("!!!", "NetworkTask: " + e.toString());
		}
		return false;
	}
}
