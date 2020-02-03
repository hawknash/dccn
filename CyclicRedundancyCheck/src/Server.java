// A Java program for a Server 
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;

import java.io.*; 

public class Server 
{ 
	//initialize socket and input stream 
	private Socket		 socket = null; 
	private ServerSocket server = null; 
	private DataInputStream in	 = null; 

	// constructor with port 
	private int crc;
	public Server(int port) 
	{ 
		// starts server and waits for a connection 
		crc=0;
		try
		{ 
			server = new ServerSocket(port); 
			System.out.println("Server started"); 

			System.out.println("Waiting for a client ..."); 

			socket = server.accept(); 
			System.out.println("Client accepted"); 

			// takes input from the client socket 
			in = new DataInputStream( 
				new BufferedInputStream(socket.getInputStream())); 

			String line = ""; 

			// reads message from client until "Over" is sent 
			while (!line.equals("Over")) 
			{ 
				try
				{ 
					line = in.readUTF();
					String strs[]=line.split(" ");
					crc=Integer.parseInt(strs[1]);
					int noc=noCRCBits(crc);
					int len=strs[0].length();
					String rawstr=strs[0].substring(0,len-noc+1);
					String str=convertToChars(rawstr);
					System.out.println("\treceived:"+str);
					checkCorrectness(strs[0]);
				} 
				catch(IOException i) 
				{ 
					System.out.println(i); 
				} 
			} 
			System.out.println("Closing connection"); 

			// close connection 
			socket.close(); 
			in.close(); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	}
	int noCRCBits(int n) {
		int count=0;
		while(n!=0) {
			count++;
			n/=2;
		}
		return count;
	}
	String binTodec(String data) {
		int sum=0;
		for(int i=data.length()-1;i>=0;i--) {
			if(data.charAt(i)=='1') {
				sum+=Math.pow(2, data.length()-i-1);
			}
		}
		return String.valueOf(Character.toChars(sum));
	}
	String convertToChars(String data) {
		String s="";
		for(int i=0;i<data.length();i+=8) {
			if(i+8<=data.length()) {
				String str=data.substring(i,i+8);
				s+=binTodec(str);
			}
		}
		return s;
	}
	void checkCorrectness(String data) {
		ArrayList<Integer> d=new ArrayList<Integer>();
		for(int i=0;i<data.length();i++) {
			d.add(Integer.parseInt(data.charAt(i)+""));
		}
		ArrayList<Integer> crcbits=new ArrayList<Integer>();
		while(crc!=0) {
			crcbits.add(crc%2);
			crc/=2;
		}
		Collections.reverse(crcbits);
		int ds=d.size();
		for(int i=0;i<d.size();i++) {
			if(d.get(i)==0) {
				continue;
			}
			int j;
			for(j=0;j<crcbits.size();j++) {
				if(i+j>=ds)
					break;
				if(d.get(i+j)==crcbits.get(j))
					d.set(i+j, 0);
				else
					d.set(i+j, 1);
			}
			if(i+j>=ds)
				break;	
		}
		int c=0;
		for(int i=0;i<d.size();i++) {
			if(d.get(i)==1)
				c++;
		}
		if(c==0) {
			System.out.println("correct data is received");
		}
		else {
			System.out.println("wrong data is received");
		}
	}

	public static void main(String args[]) 
	{ 
		Server server = new Server(5000); 
	} 
} 
