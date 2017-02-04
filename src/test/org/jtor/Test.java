package test.org.jtor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.jtor.OnionProxyManager;

public class Test {

	public static void main(String[] args) {
		try {
			OnionProxyManager.start(); //Spawn the Tor process
			System.out.println("Started...");
			Thread.sleep(5000); //Give it some time to create a circuit
			System.out.println("Connecting...");
			Socket socket = OnionProxyManager.openSSLSocket("jsonip.com", 443); //Open an SSLSocket
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Writing...");
			bw.write("GET / HTTP/1.1\r\n");
			bw.write("User-agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n");
			bw.write("Host: jsonip.com\r\n");
			bw.write("Connection: close\r\n");
			bw.write("\r\n");
			bw.flush();
			
			System.out.println("Reading...");
			String line;
			while((line = br.readLine()) != null){
				System.out.println(line);
			}
			System.out.println("Cleaning up...");
			bw.close();
			br.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			OnionProxyManager.stop(); //Make sure to destroy the Tor process!!!
		}
	}
}
