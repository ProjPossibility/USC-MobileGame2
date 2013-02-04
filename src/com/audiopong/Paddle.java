package com.audiopong;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.audiopong.entities.AbstractMoveableEntity2D;

public class Paddle extends AbstractMoveableEntity2D{

	float y_accel = 0;
	int gameWidth, gameHeight;
	


	public Paddle(float x, float y, float width, float height, int game_h, int game_w) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
		gameWidth = game_w;
		gameHeight = game_h;
		
	}

	@Override
	public void update(int delta) {
		// TODO Auto-generated method stub
		//Log.i("yAccel: ", "yAccel" + y_accel);
		if(y_accel > 0) { //the phone is moving to the left (screen towards you)
			this.setDY(y_accel/100000f);
			//Log.i("moving: ", "move left?");
		}
		else if(y_accel < 0) { //phone is moving to the right (screen towards you)
			this.setDY(y_accel/100000f);
			//Log.i("moving: ", "move right?");
		}
		
//		if((this.y+height) >= gameHeight) {
//			this.y = gameHeight;
//			this.setDY(0);
//		}
//		else if(y <= 0) {
//			y = 0;
//			this.setDY(0);
//		}
		super.update(delta);
	}
	
	public void setYAccel(float accel) {
		y_accel = accel;
	}

	
}
