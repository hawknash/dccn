import java.net.*;
import java.util.*;
import java.io.*; 
public class Client 
{ 
	private Socket socket = null; 
	private DataInputStream input = null; 
	private DataOutputStream out = null;
	public Client(String address, int port) 
	{
		try
		{ 
			socket = new Socket(address, port); 
			System.out.println("Connected");
			input = new DataInputStream(System.in);
			out = new DataOutputStream(socket.getOutputStream()); 
		} 
		catch(Exception u) 
		{ 
			System.out.println(u); 
		} 
		String line = ""; 
		System.out.println("enter the strings \"Over\" to end:");
		while (!line.equals("Over")) 
		{ 
			try
			{ 
				line = input.readLine();
				String d=prepare(line);
				System.out.println("\tsending:"+line);
				d=generateSingleBitError(d);
				out.writeUTF(d); 
			} 
			catch(IOException i) 
			{ 
				System.out.println(i); 
			} 
		} 
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
		s=s.substring(0,pos-1)+bit+s.substring(pos,l);
		System.out.println("after:"+s);
		return s;
	}
	static String prepare(String data) {
		ArrayList<Integer> d=new ArrayList<>();
		for(int i=0;i<data.length();i++) {
			d.addAll(toBinary(data.charAt(i)));
		}
		int count=0;
		String str="";
		for(int i=0;i<d.size();i++){
			str+=d.get(i)+"";
            if(d.get(i)==1){
                 count++;
            }
        }
		if(count%2==0) {
            d.add(0);
            str+="0";
		}
		else {
		    d.add(1);
		    str+="1";
		}
		return str;
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
	public static void main(String args[]) 
	{ 
		Client client = new Client("127.0.0.1", 5000); 
	} 
} 
