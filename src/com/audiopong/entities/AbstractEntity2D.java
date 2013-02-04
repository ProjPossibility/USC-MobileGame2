package com.audiopong.entities;


public abstract class AbstractEntity2D implements Entity2D {

	protected float x;
	protected float y;
	protected float width;
	protected float height;
	protected Rectangle hitbox;
	
	public AbstractEntity2D(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		hitbox = new Rectangle(x, y, width, height);
	}

	@Override
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public void setWidth(float w) {
		width = w;
	}

	@Override
	public void setHeight(float h) {
		height = h;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public boolean intersects(Entity2D e) {
		hitbox.setBounds(x, y, width, height);
		return hitbox.intersects(e.getX(), e.getY(), e.getWidth(), e.getHeight());
	}

}
