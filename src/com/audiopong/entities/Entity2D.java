package com.audiopong.entities;

public interface Entity2D {
	public void update(int delta);
	public void setLocation(float x, float y);
	public void setX(float x);
	public void setY(float y);
	public float getX();
	public float getY();
	public void setWidth(float w);
	public void setHeight(float h);
	public float getWidth();
	public float getHeight();
	public boolean intersects(Entity2D e);
}
