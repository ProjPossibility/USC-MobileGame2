package com.audiopong;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.widget.Button;

import com.audiopong.networking.Networking;
import com.audiopong.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class AudioPongActivity extends Activity implements SensorEventListener{

	DrawPong mDrawPong = null;
	float yAccel;
	AudioPongClient myClient;
	SensorManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_pong);
		manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		if(!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)){
			Log.e("SENSOR", "Error, could not register sensor listener");

		}
		
		Log.i("Create", "I created a game");

		//textView.setText("We are running!");
		//setContentView(textView);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		mDrawPong = new DrawPong(this);
		mDrawPong.setBackgroundColor(Color.WHITE);
	//	setContentView(mDrawPong);
	
		myClient = new AudioPongClient(mDrawPong, this);
		
			

		final Button button = (Button) findViewById(R.id.startbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            
            	Thread t = new Thread(new Runnable() {
            		public void run() {
            			Log.i("CREATED", "LOOK I DID IT");
            			Networking.exchangeIPs();
            			myClient.createGame();
            		}
            	});
            	t.start();
            	//myClient.createGame();
            	// Perform action on click
            }
        });
    
		


	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		//getting the linear y acceleration from sensor
		//if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			yAccel = (float) event.values[1];
		//}
		//yAccel =  (float)event.sensor.values[1]; 
		//Log.i("Activity acc", "STUPID " + event.values[1]);
		
		try{
		myClient.getGameManager().paddle1.setYAccel(yAccel);
		}
		catch (Exception e) {
			//Log.e("ERROR SENSOR CALLBACK", "STOP IT");
		}
	}
	
}
