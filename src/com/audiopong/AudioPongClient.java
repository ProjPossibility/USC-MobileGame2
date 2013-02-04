package com.audiopong;

import java.util.Date;

import android.app.Activity;
import android.util.Log;

import com.audiopong.networking.Networking;
import com.audiopong.networking.moveableEntityData;
import com.audiopong.sound.SoundManager;

public class AudioPongClient {

	public static final String TAG = "AudioPong Client";

	long currentTime;
	long prevTime;
	long soundTime = 0;
	moveableEntityData messageData;
	moveableEntityData ballData;
	long dt = 0;
	DrawPong myDraw;
	SoundManager soundManager;
	AudioPongGameManager newGame;
	Date date = new Date();
	int frameSkip = 0;

	public AudioPongClient(DrawPong draw, Activity activity) {
		myDraw = draw;
		soundManager = new SoundManager(activity);
		currentTime = System.nanoTime();
		prevTime = System.nanoTime();
	}

	public void createGame() {

		newGame = new AudioPongGameManager(myDraw);
		boolean stillPlaying = true;
		boolean hit = false;

		while(stillPlaying) {
			frameSkip++;
			if(frameSkip >= 4000) {
				frameSkip = 0;
			}
			if(frameSkip % 30 == 0) {
				currentTime = System.nanoTime();
				dt = Long.valueOf(currentTime - prevTime) / 1000;
				prevTime = currentTime;

				//Log.i(TAG, "dt: " + dt);
				//MESSAGE STUFF
				if (Networking.checkForNewBallData()) {
					ballData = Networking.getNewBallData();
					//ballData = new moveableEntityData(0, 0, 3, 3);
					if(ballData.x == -1 && ballData.y == -1) {
						stillPlaying = false;
						break;
					}
					newGame.ball.setX(ballData.x);
					newGame.ball.setY(ballData.y);
					newGame.ball.setDX(ballData.vx);
					newGame.ball.setDY(ballData.vy);
				}
				//CHECK FOR COLLISION
				hit = false;
				int collision = 0;
				hit = ((collision = newGame.update((int)dt)) != 0);

				if (hit) {
					if(collision == 4) {
						Log.i("HIT", "hit ball");
						messageData = new moveableEntityData(200-newGame.ball.getX(), newGame.ball.getY(), -newGame.ball.getDX(), newGame.ball.getDY());
						Networking.send(messageData);
						soundManager.playSound(SoundManager.PADDLE_HIT, 1, 1, 1);
					}
					else if(collision == 5) {
						soundManager.playSound(SoundManager.NOTE, 1, 1, 1);						
					}
					else if(collision == 1) {
						messageData = new moveableEntityData(-1, -1, -1, -1);
						Networking.send(messageData);
						soundManager.playSound(SoundManager.PADDLE_MISS, 1, 1, 1);
						stillPlaying = false;					
					}
					hit = false;
				}

				myDraw.updateDraw(newGame.ball.getX(), newGame.ball.getY(), newGame.paddle1.getY(), newGame.paddle2.getY());

				computeSound((int)dt);
			}
		}
	}


	private void computeSound(int dt) {
		boolean playPing = false;
		soundTime += dt;

		if(soundTime >= 250000) {
			playPing = true;
		}
		//Log.i("SOUND", "sound time: " + soundTime);

		float paddleYMid = newGame.getPaddle1().getY() + (newGame.getPaddle1().getHeight() / 2f);
		float ballY = newGame.getBall().getY() + (newGame.getBall().getHeight() / 2f);
		float ballLVol = 1f;
		float ballRVol = 1f;
		float rate = 1f;

		//Math with the sounds and things and stuff...
		float d = ballY - paddleYMid;
		float deltaSound = Math.abs(d/100f);

		if(d > 0) {
			ballLVol = 1 - (2f*deltaSound);
			ballRVol = 1 - deltaSound;
		}
		else {
			ballLVol = 1 - deltaSound;			
			ballRVol = 1 - (2f*deltaSound);
		}


		float h = newGame.getBall().getX();
		float deltaSH = h/200f;

		//ballLVol -= .125f * deltaSH;
		//ballRVol -= .125f * deltaSH;

		rate = 1.25f - (deltaSH/2f);


		if(playPing == true) {
			Log.i("SOUND", "values: " + h + " " + rate + " " + ballLVol + " " + ballRVol);
			soundManager.playSound(SoundManager.PING, ballLVol, ballRVol, rate);
			soundTime = 0;
			playPing = false;
			//Log.i("SOUND", "Playing ping...");
		}
	}

	public AudioPongGameManager getGameManager() {
		return newGame;
	}	
}