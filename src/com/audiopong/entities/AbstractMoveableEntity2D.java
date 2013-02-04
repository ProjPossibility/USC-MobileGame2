package com.audiopong.entities;

import android.util.Log;

public abstract class AbstractMoveableEntity2D extends AbstractEntity2D implements MoveableEntity2D {

	protected float dx, dy;
	
	public AbstractMoveableEntity2D(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.dx = 0;
		this.dy = 0;
	}

	@Override
	public void update(int delta) {
		//Log.i("BALL", "updating ball: " + delta + " " + this.x + " " + this.y + " "+ this.dx + " " + this.dy);
		this.x += delta * dx;
		this.y += delta * dy;
	}

	@Override
	public void setDX(float dx) {
		this.dx = dx;
	}

	@Override
	public void setDY(float dy) {
		this.dy = dy;
	}

	@Override
	public float getDX() {
		return dx;
	}

	@Override
	public float getDY() {
		return dy;
	}

}
