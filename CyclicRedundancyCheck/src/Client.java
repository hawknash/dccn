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

	// constructor to put ip address and port 
	private static int crc;
	public Client(String address, int port) 
	{ 
		// establish a connection 
		crc=0;
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
		
		while (!line.equals("Over")) 
		{ 
			try
			{ 
				System.out.print("enter the data: ");
				line = input.readLine();
				System.out.print("enter CRC generating number: ");
				crc=Integer.parseInt(input.readLine());
				String d=prepare(line,crc);
				System.out.println("\tsending: "+line);
				d=generateSingleBitError(d)+" "+crc;
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
	String generateSingleBitError(String s) {
		int l=s.length();
		Random r=new Random();
		int bit=r.nextInt(2);
		int pos=r.nextInt(l-1);
		System.out.println("generated bit:"+bit+"  pos:"+pos+" actual bit:"+s.charAt(pos));
		System.out.println("before:"+s);
		s=s.substring(0,pos)+bit+s.substring(pos+1,l);
		System.out.println("after:"+s);
		return s;
	}
	static String prepare(String data,int crc) {
		ArrayList<Integer> d=new ArrayList<>();
		ArrayList<Integer> crcbits=new ArrayList<Integer>();
		while(crc!=0) {
			crcbits.add(crc%2);
			crc/=2;
		}
		Collections.reverse(crcbits);
		int ncb=crcbits.size()-1;
		for(int i=0;i<data.length();i++) {
			d.addAll(toBinary(data.charAt(i)));
		}
		ArrayList<Integer> sd=new ArrayList<Integer>();
		for(int i=0;i<d.size();i++) {
			sd.add(d.get(i));
		}
		for(int i=0;i<ncb;i++) {
			d.add(0);
		}
		String stemp="00000000";
		int t=0;
		int ds=d.size();
		for(int i=0;i<ds-ncb+1;i++) {
			if(d.get(i)==0) {
				continue;
			}
			int j;
			stemp="";
			for(j=0;j<crcbits.size();j++) {
				if(i+j>=ds)
					break;
				if(d.get(i+j)==crcbits.get(j))
					d.set(i+j, 0);
				else
					d.set(i+j, 1);
				stemp+=d.get(i+j)+"";
			}
			t=0;
			if(i+j>=ds)
				break;
		}
		for(int i=ds-ncb;i<ds;i++) {
			sd.add(d.get(i));
		}
		stemp="";
		for(int i=0;i<sd.size();i++) {
			stemp+=sd.get(i)+"";
		}
		return stemp;
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
	
	public static ArrayList<Integer> send(int data){
        ArrayList<Integer> bindata=toBinary(data);
        int count=0;
        Collections.reverse(bindata);
        for(int i=0;i<bindata.size();i++){
                if(bindata.get(i)==1)
                        count++;
        }
        if(count%2==0)
                bindata.add(0);
        else
                bindata.add(1);
        return bindata;
}
	public static void main(String args[]) 
	{ 
		Client client = new Client("127.0.0.1", 5000); 
	} 
} 
