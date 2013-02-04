package com.audiopong.test;

import java.io.IOException;

import com.audiopong.networking.*;
//import com.moveableEntityData;

/**
 * copy this test into a java project with the audiopong.networking package to run networking tests
 * @author Ben
 *
 */
public class mainNetworkingTest {
	public static void main(String[] args) throws IOException 
	{
		//Networking.exchangeIPs();
		//String test = new moveableEntityData(1,2,3,4).toString();
		//System.out.println("Expected Value: "+test);
		//System.out.println("Expected Value: "+new moveableEntityData(test));
		Networking.setupConnection();
		System.out.println("My IP: "+Networking.getMyIP());
		System.out.println("Their IP: "+Networking.getTheirIP());
		
		//Networking.send(new moveableEntityData(1,2,3,4));
		System.out.println("Expected Value: "+new moveableEntityData(1,2,3,4));
		
		
		while(!Networking.checkForNewBallData())
		{
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Waiting for data...");
		}
		System.out.println("Actual Value: "+Networking.getNewBallData());
		
		Networking.send(new moveableEntityData(1,2,3,4));
		System.out.println("Sent data to partner");
		
	}
}
