package com.audiopong.sound;

public class Sound {
	public int soundID;
	public float volumeLeft;
	public float volumeRight;
	public float rate;
	
	public int resourceID;
	
	public Sound(int soundID, float volumeLeft, float volumeRight, float rate, int resourceID) {
		super();
		this.soundID = soundID;
		this.volumeLeft = volumeLeft;
		this.volumeRight = volumeRight;
		this.rate = rate;
		this.resourceID = resourceID;
	}
	
}
