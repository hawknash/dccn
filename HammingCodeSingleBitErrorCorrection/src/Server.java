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
	int m,r;
	public Server(int port) 
	{ 
		// starts server and waits for a connection 
		m=0;	
		r=0;
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
					m=Integer.parseInt(strs[1]);
					r=Integer.parseInt(strs[2]);
					String s=removeExtraBits(strs[0], m, r);
					String realstr=convertToChars(s);
					System.out.println("\treceived:"+realstr);
					String rs=strs[0];
					int errpos=checkCorrectness(rs);
					if(errpos==-1) {
						System.out.println("correct data is received");
					}
					else {
						System.out.println("wrong data is received");
						String str="";
						if(strs[0].charAt(errpos)=='1')
							str=strs[0].substring(0,errpos)+"0"+strs[0].substring(errpos+1,strs[0].length());
						else
							str=strs[0].substring(0,errpos)+"1"+strs[0].substring(errpos+1,strs[0].length());
						
						s=removeExtraBits(str, m, r);
						realstr=convertToChars(s);
						System.out.println("\tcorrect data is:"+realstr);
					}
				} 
				catch(IOException i) 
				{ 
					System.out.println(i); 
				} 
			} 
			System.out.println("Closing connection");
			socket.close(); 
			in.close(); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	}
	String removeExtraBits(String s,int m,int r) {
		for(int i=r-1;i>=0;i--) {
			int pos=(int)Math.pow(2, i);
			s=s.substring(0,pos-1)+s.substring(pos,s.length());
		}
		s=s.substring(0,m);
		return s;
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
	int checkCorrectness(String str) {
		int tl=(int)Math.pow(2, r);
		int arr[]=new int[tl];
		for(int i=0;i<tl;i++) {
			arr[i]=i+1;
		}
		ArrayList<Integer> errs=new ArrayList<Integer>();
		for(int i=0;i<r;i++) {
			int count=0;
			for(int j=0;j<tl;j++) {
				if(arr[j]%2==1) {
					if(str.charAt(j)=='1') {
						count++;
					}
				}
				arr[j]/=2;
			}
			if(count%2==0) {
				errs.add(0);
			}
			else {
				errs.add(1);
			}
		}
		Collections.reverse(errs);
		int pos=0;
		for(int i=0;i<errs.size();i++) {
			pos=pos*2+errs.get(i);
		}
		if(pos==0) {
			return -1;
		}
		else {
			Collections.reverse(errs);
			System.out.println("error position(bit):"+(pos-1));
			return pos-1;
		}
	}
	public static void main(String args[]) 
	{ 
		Server server = new Server(5000); 
	} 
} 
