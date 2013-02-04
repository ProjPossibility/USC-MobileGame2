package com.audiopong.entities;

public class Rectangle {

	private float x, y, width, height;

	public Rectangle(float x, float y, float width, float height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setBounds(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean intersects(Rectangle r) {
		return this.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	public boolean intersects(float x, float y, float width, float height) {
		float tw = this.width;
		float th = this.height;
		float rw = width;
		float rh = height;
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
			return false;
		}
		float tx = this.x;
		float ty = this.y;
		float rx = x;
		float ry = y;
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		
		// overflow || intersect
		return ((rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry));
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
