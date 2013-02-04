package com.audiopong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;


public class DrawPong extends View {
	//private ShapeDrawable mDrawable;
	Paint paint = new Paint();
	int x, y, paddle_w, paddle_h, unit_w, unit_h, width, height;
	float ball_x, ball_y, paddle1_x, paddle2_x;
	float bottom;
	
	public DrawPong(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		
		height = metrics.heightPixels;
		width = metrics.widthPixels;
		unit_h = (int) height/200;
		unit_w = (int) width/100;
		
		paddle_h = (int) unit_h*5;
		paddle_w = (int) unit_w*30;
		
		bottom = height*(float)0.95;
		 
		 ball_x = paddle_w/2;
		 ball_y = paddle_h;
		 paddle1_x = 0;
		 paddle2_x = 0;
		
	}
	/*f
		mDrawable = new ShapeDrawable(new RectShape());
		mDrawable.getPaint().setColor(0xff74AC23);
		mDrawable.setBounds(x, y, x + width, y + height);
	}

	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);
	}

	 */

	@Override
	public void onDraw(Canvas canvas) {
		paint.setColor(Color.BLACK);
		canvas.drawRect(paddle1_x, bottom-paddle_h, paddle1_x + paddle_w, bottom, paint);
		paint.setColor(Color.CYAN);
		canvas.drawRect(paddle2_x, 0, paddle2_x + paddle_w, paddle_h, paint);
		paint.setColor(Color.RED);
		canvas.drawRect(ball_x, ball_y, ball_x+30, ball_y + 30, paint);
	}
	
	public void updateDraw(float ballX, float ballY, float pad1X, float pad2X) {
		ball_x = ballX;
		ball_y = ballY;
		paddle1_x = pad1X;
		paddle2_x = pad2X;
		
		
	}

}