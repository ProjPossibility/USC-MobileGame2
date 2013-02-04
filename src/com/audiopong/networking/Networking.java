package com.audiopong.networking;

//import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;


/**
 * Handles all networking capabilities need by the game
 * @author Ben White (benjamtw)
 */
@SuppressLint("DefaultLocale")
public class Networking {
public static String theirIP = null;
public static String myIP = null;
static Socket sockID = null;
static ServerSocket sSockID = null;
static BufferedReader inBuf = null;
static PrintWriter outBuf = null;
static moveableEntityData data = null;
public static boolean socketFlag = false;
static boolean socketReceived = false;

/**
 * Communicates with the server to exchange IP address with the opponent
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public static void exchangeIPs()
{
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	StrictMode.setThreadPolicy(policy);
	
	//new Thread(new NetworkingConnectionSupport()).run();
	
	try {
		Log.i("Networking", "Beginning handshake");
		InetAddress serverAddr = InetAddress.getByName("nunki.usc.edu");
	sockID = new Socket(serverAddr, 22061);
	outBuf = new PrintWriter(sockID.getOutputStream(),true);
	inBuf = new BufferedReader(new InputStreamReader(sockID.getInputStream()));
	if(Networking.myIP == null)
	{
		myIP = "blarg";//Networking.getCurrentIP();
	}
	System.out.println("Sending: '"+Networking.myIP+"' to server");
    outBuf.println(Networking.myIP);
    System.out.println("Preparing to receive from server...");
    Networking.theirIP = inBuf.readLine();//need to read to clear input buffer, but value is bad so over ride next line
    Networking.theirIP = InetAddress.getByName("nunki.usc.edu").getHostAddress().toString();
    Log.i("Networking", "Handshake completed");
    if(inBuf.ready())
    {
    	Networking.socketFlag=true;
    }
    
    //outputBuf.close();
	//inputBuf.close();
	//socketID.close();
    } 
	catch (UnknownHostException e) 
    {
        System.err.println("Don't know about host: nunki.usc.edu.");
        System.exit(1);
    } 
	catch (IOException e) 
    {
        System.err.println("Couldn't get I/O for "
                           + "the connection to: nunki.usc.edu.");
        e.printStackTrace();
        System.exit(1);
    }
}

public static boolean doIStart()
{
	if(socketFlag)
	{
		try {
			String str = inBuf.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	else
		return false;
}
/*public static String getLocalIpAddress()
{
	try {
        List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        if(interfaces == null)
        {
        	System.out.println("found no interfaces!");
        }
        for (NetworkInterface intf : interfaces) {
            List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
            for (InetAddress addr : addrs) {
                if (!addr.isLoopbackAddress()) 
                {
                    String sAddr = addr.getHostAddress();//.toUpperCase();
                    return sAddr;
                   
                }
            }
        }
    } catch (Exception ex) { } // for now eat exceptions
    return "";
}*/
public static void socketHeard(moveableEntityData dat)
{
	data = dat;
	socketReceived = true;
}

/**
 * discovers my IP address
 * @throws UnknownHostException
 * @deprecated
 */
public static void findMyIP() throws UnknownHostException
{
	InetAddress ownIP=InetAddress.getLocalHost();
	myIP = ownIP.getHostAddress();
}

/**
 * returns the value of myIP. Note: myIP is not initialized by default, call findMyIP() first!
 * @return returns my IP address
 * {@link Networking#findMyIP()}
 */
public static String getMyIP()
{
	return myIP;
}

/**
 *  Returns the current value for opponents IP address. Note: theirIP is not initialized! Call exchangeIPs before calling this function!
 * @return returns the stored value for the opponents IP address
 * {@link Networking#exchangeIPs()}
 */
public static String getTheirIP()
{
	return theirIP;
}
/**
 * Makes all the function calls to handshake and create a connection to another phone.
 * @link {@link Networking#exchangeIPs()}
 * @link {@link Networking#initializeConnection()}
 */
public static void setupConnection()
{
	exchangeIPs();
	//initializeConnection();
}
/**
 * creates a connection with the other phone using the IP address discovered using exchangeIPs()
 * {@link Networking#exchangeIPs()}
 * @deprecated
 */
public static void initializeConnection()
{
	try
	{
	if(socketFlag)
	{
		System.out.println("Preparing to sleep");
		Thread.sleep(500);
		System.out.println("slept, creating socket...");
		sockID = new Socket(theirIP, 22061);
		sockID.setTcpNoDelay(true);
	
		outBuf = new PrintWriter(sockID.getOutputStream(),true);
		inBuf = new BufferedReader(new InputStreamReader(sockID.getInputStream()));
		System.out.println("socket created");

	}
	else
	{
		System.out.println("did not sleep, creating socket");

		sSockID = new ServerSocket(22061);
		System.out.println("preparing to accept");

		sockID=sSockID.accept();
		System.out.println("accepted");


		outBuf = new PrintWriter(sockID.getOutputStream(),true);
		inBuf = new BufferedReader(new InputStreamReader(sockID.getInputStream()));
	}
	//new Thread(new NetworkListener(inBuf)).start();
	
	}
	catch (UnknownHostException e) 
    {
        System.err.println("Don't know about host: "+theirIP);
        System.exit(1);
    } 
	catch (IOException e) 
    {
        System.err.println("Couldn't get I/O for "
                           + "the connection to: "+theirIP);
        e.printStackTrace();
        System.exit(1);
    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
/**
 * shuts down all streams and sockets created for networking
 */
public static void terminateConnection()
{
	try
	{
		outBuf.close();
		inBuf.close();
		sockID.close();
		sSockID.close();
		Log.i("Networking", "Connection closed");
	}
	catch (IOException e) {
		e.printStackTrace();
	}
}

/**
 * sends newBallData to the paired phone via TCP/IP
 * @param newBallData data to be sent
 */
public static void send(moveableEntityData newBallData)
{
	Log.i("Networking", "Sending data");
	outBuf.println(newBallData.toString());
}

/**
 * checks to see if new data is available for the ball
 * @return true if new data, otherwise false
 */
public static boolean  checkForNewBallData()
{	
	try
	{
		return inBuf.ready();
	}
	catch (IOException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
	//return socketReceived;
}

/**
 * returns the new data from a paddle hit by other phone
 * @return data returned
 */
public static moveableEntityData getNewBallData()
{
	String dat = null;
		try
		{
			Log.i("Networking", "Reading data");
			dat = inBuf.readLine();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//socketReceived = false;
		return new moveableEntityData(dat);
}

}//class Networking
