import java.net.*;
import java.util.ArrayList;
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
					if(line.length()==0)
						continue;
					System.out.println("\treceived:"+convertToChars(line));
					ArrayList<ArrayList<Integer>> datamatrix=constructMatrix(line);
					checkCorrectness(datamatrix);
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
	String convertToChars(String data) {
		String s="";
		for(int i=0;i<data.length();i+=8) {
			if(i+8<data.length()) {
				String str=data.substring(i,i+8);
				i++;
				s+=binTodec(str);
			}
		}
		s=s.substring(0,s.length()-1);
		return s;
	}
	ArrayList<ArrayList<Integer>> constructMatrix(String data) {
		ArrayList<ArrayList<Integer>> datamatrix=new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<data.length();i+=9) {
			String str=data.substring(i,i+9);
			ArrayList<Integer> temp=new ArrayList<Integer>();
			for(int l=0;l<str.length();l++) {
				int bit=Integer.parseInt(str.charAt(l)+"");
				temp.add(bit);
			}
			datamatrix.add(temp);
		}
		//printDataMatrix(datamatrix);
		return datamatrix;
	}
	static void printDataMatrix(ArrayList<ArrayList<Integer>> datamatrix) {
		for(int i=0;i<datamatrix.size();i++) {
			for(int j=0;j<datamatrix.get(i).size();j++) {
				System.out.print(datamatrix.get(i).get(j)+" ");
			}
			System.out.println();
		}
	}
	void  checkCorrectness(ArrayList<ArrayList<Integer>> datamatrix ) {
		ArrayList<Integer> rows=new ArrayList<Integer>();
		ArrayList<Integer> cols=new ArrayList<Integer>();
		int nr=datamatrix.size();
		int nc=datamatrix.get(0).size();
		for(int i=0;i<nr-1;i++) {
			int count=0;
			for(int j=0;j<nc;j++) {
				if(datamatrix.get(i).get(j)==1)
					count++;
			}
			if(count%2!=0)
				rows.add(i);
		}
		for(int i=0;i<nc;i++) {
			int count=0;
			for(int j=0;j<nr;j++) {
				if(datamatrix.get(j).get(i)==1)
					count++;
			}
			if(count%2!=0)
				cols.add(i);
		}
		if(rows.size()>0||cols.size()>0)
			System.out.println("wrong data received");
		else 
			System.out.println("correct data is received");
		if(rows.size()==1&&cols.size()==1) {
			System.out.println("Error is in "+(rows.get(0)+1)+"th character and "+(cols.get(0)+1)+"th bit");
		}
	}
	public static void main(String args[]) 
	{ 
		Server server = new Server(5000); 
	} 
} 
