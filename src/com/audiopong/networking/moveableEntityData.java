package com.audiopong.networking;

//import java.io.IOException;
import java.io.Serializable;

public class moveableEntityData implements Serializable{
	private static final long serialVersionUID = 571698596989163268L;
	public float x,y,vx,vy;
public moveableEntityData(float X, float Y, float VX, float VY)
{
	x = X;
	y = Y;
	vx = VX;
	vy = VY;
}
public moveableEntityData(String data)
{
	String[] dat;
	//String tar;
	dat = data.split(" ");
	x = Float.parseFloat(dat[0]);
	y = Float.parseFloat(dat[1]);
	vx = Float.parseFloat(dat[2]);
	vy = Float.parseFloat(dat[3]);
}
public String toString()
{
	String target = new String();
	target = Float.toString(x) +" "+ Float.toString(y) +" "+ Float.toString(vx) +" "+ Float.toString(vy);
	return target;
}
}
