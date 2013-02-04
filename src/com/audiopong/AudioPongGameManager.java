package com.audiopong;

import java.util.Random;

import com.audiopong.networking.Networking;

import android.util.Log;


public class AudioPongGameManager {
	
	Random rand = new Random();
	private boolean whichPlayer;
	private int winningPlayer; 
	private int collision; 
	
	boolean wasHit = false;
	
	Paddle paddle1;
	Paddle paddle2;
	PongBall ball;
	
	int Height = 100;
	int Width = 200;
	DrawPong drawObjs;
	
	public AudioPongGameManager(DrawPong drawPong)
	{
		drawObjs = drawPong;
		float velXBall = .00001f; //x velocity of ball
		//float velXBall = 0f; //x velocity of ball
		float velYBall = .00001f; //y velocity of ball
	
		
		
		paddle1 = new Paddle(0, 35, 5, 30, Height, Width);
		paddle2 = new Paddle(Width-5, 0, 5, Height, Height, Width); //set as a "wall" 
		ball = new PongBall(0, 0, 1, 1);
		
		whichPlayer = Networking.doIStart();
		//randomized ball placement (either in front of paddle1, or in front of paddle2))
		if(whichPlayer)
		{
			ball.setLocation(paddle1.getHeight(), 50);
			ball.setDY(velYBall);
			ball.setDX(velXBall);
		}
		else
		{
			ball.setLocation(200-paddle2.getHeight(),50);
			ball.setDY(velYBall);
			ball.setDX(-1*velXBall);
		}	
	}

	public int collisionDetect()
	{
		return (collision);
		//returns int, collision initialized to 0 
		//
	}
	public boolean selectPlayer()
	{
		return (whichPlayer);
	}
	
	public int whoWon()
	{
		return(winningPlayer);
	}
	
	public int update(int dt)
	{
		///MORE STUFF
		collision = 0; 
		wasHit = false;
		double vMagnitude;
		double kConst;
		double pHeight;
		
		//ball collides with left wall
		if(ball.getX()<= 0)
		{
			winningPlayer = 2; //player 2 wins
			ball.setX(10);
			ball.setDX(-1*ball.getDX());
			//Log.i("Game", "Player2 point");
			
			collision = 1;	
		}
		else if(ball.getX()>=200) //reaches the right wall
		{
			winningPlayer =1; //player 1 wins
			ball.setX(190);
			ball.setDX(-1*ball.getDX());
			//Log.i("Game", "Player1 point");
			collision =2;
		}
		else if(ball.getY()<=0) //hits top and bottom of the gameField
		{
			ball.setDY(-1f*ball.getDY());
			ball.setY(0);
			Log.i("Game" , "Hit top");
			collision= 3; 
		}
		else if (ball.getY() >= 100) {
			ball.setDY(-1*ball.getDY());
			ball.setY(100);
			Log.i("Game" , "Hit bottom");
			collision = 3;
		}
		else //if ball could collide with paddle
		{
			vMagnitude = Math.sqrt(Math.pow((double)ball.getDX(), 2)+Math.pow((double)ball.getDY(),2)); //velocity vector magnitude
			pHeight = 0.5*paddle1.getHeight(); //height of the paddle
			
			if(ball.intersects(paddle1))
			{
				//kConst = (Math.abs((ball.getDY()-paddle1.getDY())-0.5*pHeight))/pHeight; //constant value to be added 
				//middle intersection where kConst= 0 and edge intersection where kConst = 1
				ball.setDX(-ball.getDX());
				
				//ball.setDY((float)(kConst + 1)*(ball.getDY()));
				//ball.setDX((float)Math.sqrt(Math.pow((double)vMagnitude,2)-Math.pow((double)ball.getDY(),2)));
				Log.i("Game","Player1 hit");
				wasHit = true;		
				collision = 4;
			}
			if (ball.intersects(paddle2))
			{
				ball.setDX(-ball.getDX());
				//kConst = (Math.abs((ball.getDY()-paddle2.getDY())-0.5f*pHeight))/pHeight;
				//ball.setDY((float)(kConst + 1)*(ball.getDY()));
				//ball.setDX((float)Math.sqrt(Math.pow((double)vMagnitude,2)-Math.pow((double)ball.getDY(),2)));
				Log.i("Game", "Player2 hit");
				wasHit = true;
				collision = 5;
			}
			
		}
		
		ball.update(dt);
		paddle1.update(dt);
		paddle2.update(dt);
		drawObjs.updateDraw(ball.getX(), ball.getY(), paddle1.getY(), paddle2.getY());
		return collision;
		
	}

	public PongBall getBall() {
		return ball;
	}

	public Paddle getPaddle1() {
		return paddle1;
	}

	public Paddle getPaddle2() {
		return paddle2;
	}
}

			
			
			
	
		
		