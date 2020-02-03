// A Java program for a Client 
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.io.*; 

public class Client 
{ 
	// initialize socket and input output streams 
	private Socket socket		 = null; 
	private DataInputStream input = null; 
	private DataOutputStream out	 = null; 
	static private int m,r;
	// constructor to put ip address and port 
	public Client(String address, int port) 
	{ 
		// establish a connection 
		try
		{ 
			socket = new Socket(address, port); 
			System.out.println("Connected"); 

			// takes input from terminal 
			input = new DataInputStream(System.in); 

			// sends output to the socket 
			out = new DataOutputStream(socket.getOutputStream()); 
		} 
		catch(UnknownHostException u) 
		{ 
			System.out.println(u); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		}

		// string to read message from input 
		String line = ""; 

		// keep reading until "Over" is input
		System.out.println("enter a string:");
		while (!line.equals("Over")) 
		{ 
			try
			{ 
				line = input.readLine();
				String d=prepare(line);
				System.out.println("\tsending:"+line);
				d=generateSingleBitError(d);
				out.writeUTF(d.toString()); 
			} 
			catch(IOException i) 
			{ 
				System.out.println(i); 
			} 
		} 
		// close the connection 
		try
		{ 
			input.close(); 
			out.close(); 
			socket.close(); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	}
	static String prepare(String data) {
		ArrayList<Integer> d=new ArrayList<>();
		for(int i=0;i<data.length();i++) {
			d.addAll(toBinary(data.charAt(i)));
		}
		m=d.size();
		r=getHammingBit(m);
		String sent_data=prepareData(d, m, r);	
		return sent_data+" "+m+" "+r;
	}
	public static ArrayList<Integer> toBinary(int data){
		int temp=data;
        ArrayList<Integer> bindata=new ArrayList<>();
        while(temp!=0){
                bindata.add(temp%2);
                temp/=2;
        }
        while(bindata.size()!=8) {
        	bindata.add(0);
        }
        Collections.reverse(bindata);
        return bindata;
	}
	public static int getHammingBit(int m){
		int r=0;
		while(Math.pow(2,r)<(m+r+1)){
			r++;
		}
		return r;
	}
	static ArrayList<Integer> getBinaryEqui(int d){
		int temp=d;
		ArrayList<Integer> bindata=new ArrayList<Integer>();
		while(temp!=0){
			bindata.add(temp%2);
			temp/=2;
		}
		Collections.reverse(bindata);
		return bindata;
	}
	static String prepareData(ArrayList<Integer> act_data,int m,int r) {
		for(int i=0;i<r;i++) {
			int pos=(int)Math.pow(2, i)-1;
			act_data.add(pos,0);
		}
		int tl=(int)Math.pow(2, r);
		while(tl!=act_data.size()) {
			act_data.add(0);
		}
		int arr[]=new int[tl];
		for(int i=0;i<tl;i++) {
			arr[i]=i+1;
		}
		String s="";
		for(int i=0;i<act_data.size();i++) {
			s+=act_data.get(i)+"";
		}
		for(int i=0;i<r;i++) {
			int count=0;
			for(int j=0;j<tl;j++) {
				if(arr[j]%2==1) {
					if(act_data.get(j)==1)
						count++;
				}	
				arr[j]/=2;
			}
			int pos=(int)Math.pow(2, i)-1;
			if(count%2==1) {
				act_data.set(pos, 1);
			}
		}
		s="";
		for(int i=0;i<act_data.size();i++) {
			s+=act_data.get(i)+"";
		}
		return s;
	}
	String generateSingleBitError(String st) {
		String strs[]=st.split(" ");
		String s=strs[0];
		int l=s.length();
		Random r=new Random();
		int bit=r.nextInt(2);
		int pos=r.nextInt(m+this.r-1);
		System.out.println("generated bit:"+bit+"  pos:"+pos+" actual bit:"+s.charAt(pos));
		System.out.println("before:"+s);
		s=s.substring(0,pos)+bit+s.substring(pos+1,l);
		System.out.println("after:"+s);
		st=s+" "+strs[1]+" "+strs[2];
		return st;
	}
	public static void main(String args[]) 
	{ 
		Client client = new Client("127.0.0.1", 5000); 
	} 
} 
