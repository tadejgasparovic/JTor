package org.jtor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class OnionProxyManager {
	
	/**
	 * Tor Process
	 * **/
	private static Process tor_process;
	
	/**
	 * Proxy
	 * **/
	private static Proxy proxy;
	
	/**
	 * Spawns the Tor proxy process.
	 * @throws IOException 
	 * **/
	public static void start() throws IOException{
		tor_process = Runtime.getRuntime().exec("./Tor/tor.exe");
		proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9050));
	}
	
	/**
	 * Returns the proxy object.
	 * @return The proxy object.
	 * **/
	public static Proxy getProxy(){
		return proxy;
	}
	
	/**
	 * Opens a plain-text socket connection over the Tor network. Avoids DNS leaking.
	 * @param address The destination address.
	 * @param port The destination port.
	 * @return The opened socket ready for read / write operations.
	 * **/
	public static Socket openSocket(String address, int port) throws IOException{
		Socket socket = new Socket(proxy);
		InetSocketAddress addr = InetSocketAddress.createUnresolved(address, port);
		socket.connect(addr);
		return socket;
	}
	
	/**
	 * Opens a secure SSL socket connection over the Tor network. Avoids DNS leaking.
	 * @param address The destination address.
	 * @param port The destination port.
	 * @return The opened socket ready for read / write operations.
	 * **/
	public static SSLSocket openSSLSocket(String address, int port) throws IOException{
		Socket socket = new Socket(proxy);
		InetSocketAddress addr = InetSocketAddress.createUnresolved(address, port);
		socket.connect(addr);
		
		SSLSocket ssl_socket = (SSLSocket) ((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(socket, "127.0.0.1", 9050, true);
		
		return ssl_socket;
	}
	
	/**
	 * Is the Tor proxy process alive.
	 * @return TRUE if the process is alive, else FALSE.
	 * **/
	public static boolean isProcessAlive(){
		return tor_process.isAlive();
	}
	
	/**
	 * Stops the Tor proxy process.
	 * **/
	public static void stop(){
		tor_process.destroy();
	}
}
