import java.net.*;
import java.io.*; 
public class Server 
{
	private Socket socket = null; 
	private ServerSocket server = null; 
	private DataInputStream in	 = null;
	public Server(int port) 
	{ 
		try
		{ 
			server = new ServerSocket(port); 
			System.out.println("Server started"); 
			System.out.println("Waiting for a client ..."); 
			socket = server.accept(); 
			System.out.println("Client accepted");
			in = new DataInputStream( new BufferedInputStream(socket.getInputStream())); 
			String line = ""; 
			while (!line.equals("Over")) 
			{ 
				try
				{ 
					line = in.readUTF();
					String s=convertToChars(line);
					System.out.println("\treceived:"+s);
					checkCorrectness(line);
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
		catch(Exception i) 
		{ 
			System.out.println(i); 
		} 
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
	String  convertToChars(String data) {
		String s="";
		for(int i=0;i<data.length();i+=8) {
			if(i+8<data.length()) {
				String str=data.substring(i,i+8);
				s+=binTodec(str);
			}
		}
		return s;
	}
	void checkCorrectness(String data) {
		int count=0;
		for(int i=0;i<data.length();i++) {
			if(data.charAt(i)=='1') {
				count++;
			}
		}
		if(count%2==0) {
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
