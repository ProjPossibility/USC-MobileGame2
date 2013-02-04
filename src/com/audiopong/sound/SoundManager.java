package com.audiopong.sound;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

import com.audiopong.R;

public class SoundManager implements OnLoadCompleteListener {

	Activity mainActivityReference;

	private static String TAG = "SoundManager";

	public static final int PING = 0; 
	public static final int WALL_HIT = 1; 
	public static final int PADDLE_HIT = 2; 
	public static final int PADDLE_MISS = 3;
	public static final int NOTE = 4;
	
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;

	public SoundManager(Activity activity) {
		mainActivityReference = activity;
		soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 100);
		soundPool.setOnLoadCompleteListener(this);
		soundPoolMap = new HashMap<Integer, Integer>();
		
		initAudioPongSounds();
	}
	
	public void initAudioPongSounds() {
		addSound(PING, R.raw.ping);
		addSound(WALL_HIT, R.raw.wall_hit);
		addSound(PADDLE_HIT, R.raw.paddle_hit);
		addSound(PADDLE_MISS, R.raw.paddle_miss);
		addSound(NOTE, R.raw.note);
	}

	public void addSound(int soundID, int resourceID) {
		Log.i(TAG, "Adding Continuous Sound");
		soundPoolMap.put(soundID, soundPool.load(mainActivityReference.getApplicationContext(), resourceID, 1));
	}

	/**
	 * 
	 * @param sound			defined in SoundManager
	 * @param volumeLeft	between 0 and 1
	 * @param volumeRight	between 0 and 1
	 * @param rate
	 * @return
	 */
	public int playSound(int sound, float volumeLeft, float volumeRight, float rate) {
		/* Updated: The next 4 lines calculate the current volume in a scale of 0.0 to 1.0 */
		AudioManager mgr = (AudioManager)mainActivityReference.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);    
		float volume = streamVolumeCurrent / streamVolumeMax;

		float volL = volumeLeft * volume;
		float volR = volumeRight * volume;

		/* Play the sound with the correct volume */
		return soundPool.play(soundPoolMap.get(sound), volL, volR, 1, 0, rate);
	}

	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		Log.i(TAG, "Loading of sampleID " + sampleId + " Complete");
	}
}


